package org.equinox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.var;
import org.equinox.exception.UserNotFoundException;
import org.equinox.annotation.Page;
import org.equinox.annotation.PageSize;
import org.equinox.annotation.SortBy;
import org.equinox.annotation.SortingDirection;
import org.equinox.annotation.ValidatePaginationParameters;
import org.equinox.exception.LoginUsernameIsAlreadyInUseException;
import org.equinox.exception.NoMatchFoundForGivenUsernameAndPasswordException;
import org.equinox.mapper.UserToCurrentUserDTOMapper;
import org.equinox.mapper.UserToCurrentUserFullProfileDTOMapper;
import org.equinox.mapper.UserToUserDTOMapper;
import org.equinox.model.domain.AuthType;
import org.equinox.model.domain.PersonalInformation;
import org.equinox.model.domain.User;
import org.equinox.model.dto.CreateStandardUserDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.CurrentUserFullProfileDTO;
import org.equinox.model.dto.UpdateUserDTO;
import org.equinox.model.dto.UserDTO;
import org.equinox.repository.AuthorityRepository;
import org.equinox.repository.UserRepository;
import org.equinox.security.AuthenticationFacade;
import org.equinox.service.UserService;
import org.equinox.util.ColorUtils;
import org.equinox.util.SortingDirectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserToCurrentUserDTOMapper userToCurrentUserDTOMapper;
    private final UserToUserDTOMapper userToUserDTOMapper;
    private final UserToCurrentUserFullProfileDTOMapper
            userToCurrentUserFullProfileDTOMapper;
    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO findById(Long id) {
        User user = findUserById(id);
        return userToUserDTOMapper.map(user);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with given id "
                        + id + " could not be found."));
    }

    @Override
    public UserDTO findByLoginUsername(String loginUsername) {
        User user = userRepository.findByLoginUsername(loginUsername)
                .orElseThrow(NoMatchFoundForGivenUsernameAndPasswordException::new);
        return userToUserDTOMapper.map(user);
    }

    @Override
    @ValidatePaginationParameters
    public List<UserDTO> findByDisplayedNameContains(String line,
                                                  @Page int page,
                                                  @PageSize(max = 50) int pageSize,
                                                  @SortingDirection String sortingDirection,
                                                  @SortBy(allowed = "id") String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);

        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return userRepository.findByDisplayedNameContains(line, pageRequest)
                .stream()
                .map(userToUserDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO saveStandardUser(CreateStandardUserDTO createStandardUserDTO) {
        if (!userRepository.findByLoginUsername(createStandardUserDTO.getLoginUsername()).isPresent()) {
            var authority = authorityRepository.findByName("ROLE_USER");
            var displayedUserName = createStandardUserDTO.getDisplayedUsername() == null
                    || createStandardUserDTO.getDisplayedUsername().isEmpty()
                    ? createStandardUserDTO.getLoginUsername()
                    : createStandardUserDTO.getDisplayedUsername();

            var user = User.builder()
                    .loginUsername(createStandardUserDTO.getLoginUsername())
                    .displayedName(displayedUserName)
                    .password(passwordEncoder.encode(createStandardUserDTO.getPassword()))
                    .roles(Collections.singleton(authority))
                    .authType(AuthType.USERNAME_AND_PASSWORD)
                    .enabled(true)
                    .locked(false)
                    .letterAvatarColor(ColorUtils.getRandomColor())
                    .build();
            return userToUserDTOMapper.map(saveStandardUser(user));
        } else {
            throw new LoginUsernameIsAlreadyInUseException();
        }
    }

    private User saveStandardUser(User user) {
        user = save(user);
        user.setGeneratedUsername(user.getLoginUsername() + "-" + user.getId() + "-" + UUID.randomUUID());
        return save(user);
    }

    @Override
    public UserDTO update(Long id, UpdateUserDTO updateUserDTO) {
        User user = findUserById(id);
        user.setDisplayedName(updateUserDTO.getDisplayedName());
        user.setAvatarUri(updateUserDTO.getAvatarUri());
        user.getPersonalInformation().setEmail(updateUserDTO.getEmail());
        user.getPersonalInformation().setBirthDate(updateUserDTO.getBirthDate());
        user.getPersonalInformation().setBio(updateUserDTO.getBio());
        user = userRepository.save(user);
        return userToUserDTOMapper.map(user);
    }

    @Override
    public void delete(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Override
    public CurrentUserDTO getCurrentUser() {
        if (authenticationFacade.isUserAuthenticated()) {
            return userToCurrentUserDTOMapper.map(authenticationFacade.getCurrentUser());
        } else {
            return null;
        }
    }

    @Override
    public synchronized CurrentUserFullProfileDTO getCurrentUserFullProfile() {
        return userToCurrentUserFullProfileDTOMapper.map(authenticationFacade.getCurrentUser());
    }

    @Override
    public CurrentUserFullProfileDTO updateCurrentUser(UpdateUserDTO updateUserDTO) {
        User currentUser = authenticationFacade.getCurrentUser();

        currentUser.setAvatarUri(updateUserDTO.getAvatarUri());
        currentUser.setDisplayedName(updateUserDTO.getDisplayedName());

        if (currentUser.getPersonalInformation() == null) {
            currentUser.setPersonalInformation(new PersonalInformation());
        }

        currentUser.getPersonalInformation().setBio(updateUserDTO.getBio());
        currentUser.getPersonalInformation().setBirthDate(updateUserDTO.getBirthDate());
        currentUser.getPersonalInformation().setEmail(updateUserDTO.getEmail());

        return userToCurrentUserFullProfileDTOMapper.map(userRepository.save(currentUser));
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByGeneratedUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("No user has been found with such username " + s);
        } else {
            return user;
        }
    }

    @Override
    public UserDetails loadUserByLoginUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLoginUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user has been found with such username " + username));
    }

    @Override
    public void assertThatUsernameIsNotInUse(String username) throws LoginUsernameIsAlreadyInUseException {
        Optional<User> user = userRepository.findByLoginUsername(username);

        if (user.isPresent()) {
            throw new LoginUsernameIsAlreadyInUseException("This username " + username + " is already in use.");
        }
    }
}