package aphelion.service.impl;

import aphelion.annotation.NotifySubscribers;
import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.GlobalBlockingNotFoundException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.CreateGlobalBlockingDTOToGlobalBlockingMapper;
import aphelion.mapper.GlobalBlockingToGlobalBlockingDTOMapper;
import aphelion.model.domain.GlobalBlocking;
import aphelion.model.domain.NotificationType;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateGlobalBlockingDTO;
import aphelion.model.dto.GlobalBlockingDTO;
import aphelion.model.dto.UpdateGlobalBlockingDTO;
import aphelion.repository.GlobalBlockingRepository;
import aphelion.repository.UserRepository;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import aphelion.service.GlobalBlockingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GlobalBlockingServiceImpl implements GlobalBlockingService {
    private final GlobalBlockingRepository globalBlockingRepository;
    private final UserRepository userRepository;
    private final GlobalBlockingToGlobalBlockingDTOMapper globalBlockingToGlobalBlockingDTOMapper;
    private final CreateGlobalBlockingDTOToGlobalBlockingMapper
            createGlobalBlockingDTOToGlobalBlockingMapper;

    @NotifySubscribers(type = NotificationType.GLOBAL_BLOCKING)
    @Override
    public GlobalBlockingDTO save(CreateGlobalBlockingDTO createGlobalBlockingDTO) {
        GlobalBlocking globalBlocking = createGlobalBlockingDTOToGlobalBlockingMapper
                .map(createGlobalBlockingDTO);
        globalBlocking = globalBlockingRepository.save(globalBlocking);
        return globalBlockingToGlobalBlockingDTOMapper.map(globalBlocking);
    }

    @Override
    public GlobalBlockingDTO update(Long id, UpdateGlobalBlockingDTO updateGlobalBlockingDTO) {
        GlobalBlocking globalBlocking = findGlobalBlockingById(id);
        globalBlocking.setReason(updateGlobalBlockingDTO.getReason());
        globalBlocking.setEndDate(updateGlobalBlockingDTO.getEndDate());
        globalBlocking = globalBlockingRepository.save(globalBlocking);
        return globalBlockingToGlobalBlockingDTOMapper.map(globalBlocking);
    }

    @Override
    public void delete(Long id) {
        GlobalBlocking globalBlocking = findGlobalBlockingById(id);
        globalBlockingRepository.delete(globalBlocking);
    }

    @Override
    public GlobalBlockingDTO findById(Long id) {
        GlobalBlocking globalBlocking = findGlobalBlockingById(id);
        return globalBlockingToGlobalBlockingDTOMapper.map(globalBlocking);
    }

    private GlobalBlocking findGlobalBlockingById(Long id) {
        return globalBlockingRepository.findById(id)
                .orElseThrow(() -> new GlobalBlockingNotFoundException("Global blocking" +
                        " with given id " + id + " could not be found."));
    }

    @Override
    @ValidatePaginationParameters
    public List<GlobalBlockingDTO> findAllByBlockedUser(Long userId,
                                                 @Page int page,
                                                 @PageSize(max = 100) int pageSize,
                                                 @SortingDirection String sortingDirection,
                                                 @SortBy(allowed = {"id", "startDate", "endDate"}) String sortBy) {
        User user = findUserById(userId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return globalBlockingRepository.findByBlockedUser(user, pageRequest)
                .stream()
                .map(globalBlockingToGlobalBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<GlobalBlockingDTO> findNotEndedByBlockedUser(Long userId) {
        User user = findUserById(userId);
        return findNotEndedByBlockedUser(user)
                .stream()
                .map(globalBlockingToGlobalBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<GlobalBlockingDTO> findAllCreatedByUser(Long userId,
                                                        @Page int page,
                                                        @PageSize(max = 100) int pageSize,
                                                        @SortingDirection String sortingDirection,
                                                        @SortBy(allowed = {
                                                                "id", "startDate", "endDate"
                                                        }) String sortBy) {
        User user = findUserById(userId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return globalBlockingRepository.findByBlockedBy(user, pageRequest)
                .stream()
                .map(globalBlockingToGlobalBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<GlobalBlockingDTO> findNotEndedAndCreatedByUser(Long userId) {
        User user = findUserById(userId);
        return globalBlockingRepository.findAllByBlockedByAndEndDateGreaterThan(user, Date.from(Instant.now()))
                .stream()
                .map(globalBlockingToGlobalBlockingDTOMapper::map)
                .collect(Collectors.toList());
    }

    private List<GlobalBlocking> findNotEndedByBlockedUser(User user) {
        return globalBlockingRepository
                .findAllByBlockedUserAndEndDateGreaterThan(user, Date.from(Instant.now()));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + userId + " could not be found."));
    }

    @Override
    public boolean isUserBlockedGlobally(Long userId) {
        return !findNotEndedByBlockedUser(userId).isEmpty();
    }

    @Override
    public boolean isUserBlockedGlobally(User user) {
        return !findNotEndedByBlockedUser(user).isEmpty();
    }
}
