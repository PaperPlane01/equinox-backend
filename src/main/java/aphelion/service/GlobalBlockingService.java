package aphelion.service;

import aphelion.model.domain.User;
import aphelion.model.dto.CreateGlobalBlockingDTO;
import aphelion.model.dto.GlobalBlockingDTO;
import aphelion.model.dto.UpdateGlobalBlockingDTO;

import java.util.List;

public interface GlobalBlockingService {
    GlobalBlockingDTO save(CreateGlobalBlockingDTO createGlobalBlockingDTO);
    GlobalBlockingDTO update(Long id, UpdateGlobalBlockingDTO updateGlobalBlockingDTO);
    void delete(Long id);
    GlobalBlockingDTO findById(Long id);
    List<GlobalBlockingDTO> saveMultiple(List<CreateGlobalBlockingDTO> createGlobalBlockingDTOList);
    List<GlobalBlockingDTO> findAll(int page, int pageSize, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findAllNotEnded(int page, int pageSize, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findAllByBlockedUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findNotEndedByBlockedUser(Long userId);
    List<GlobalBlockingDTO> findAllCreatedByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findNotEndedAndCreatedByUser(Long userId);
    List<GlobalBlockingDTO> findNotEndedAndBlockedUserUsernameContains(String username, int page, int pageSie, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findAllByBlockedUserUsernameContains(String username, int page, int pageSize, String sortingDirection, String sortBy);
    boolean isUserBlockedGlobally(Long userId);
    boolean isUserBlockedGlobally(User user);
}
