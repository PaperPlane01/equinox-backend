package aphelion.service.impl;

import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.LoginUsernameIsAlreadyInUseException;
import aphelion.exception.NoMatchFoundForGivenUsernameAndPasswordException;
import aphelion.exception.UserNotFoundException;
import aphelion.mapper.UserToCurrentUserDTOMapper;
import aphelion.mapper.UserToCurrentUserFullProfileDTOMapper;
import aphelion.mapper.UserToUserDTOMapper;
import aphelion.model.domain.AuthType;
import aphelion.model.domain.Authority;
import aphelion.model.domain.PersonalInformation;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateStandardUserDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.CurrentUserFullProfileDTO;
import aphelion.model.dto.UpdateUserDTO;
import aphelion.model.dto.UserDTO;
import aphelion.repository.AuthorityRepository;
import aphelion.repository.UserRepository;
import aphelion.security.AuthenticationFacade;
import aphelion.service.UserService;
import aphelion.util.ColorUtils;
import aphelion.util.SortingDirectionUtils;
import com.google.api.services.oauth2.model.Userinfoplus;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                    .roles(Collections.singletonList(authority))
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

    @Override
    public User registerGoogleUser(Userinfoplus userinfoplus) {
        Authority authority = authorityRepository.findByName("ROLE_USER");
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        User user = User.builder()
                .displayedName(userinfoplus.getName())
                .googleId(userinfoplus.getId())
                .authType(AuthType.GOOGLE)
                .avatarUri(userinfoplus.getPicture())
                .roles(authorities)
                .enabled(true)
                .locked(false)
                .letterAvatarColor(ColorUtils.getRandomColor())
                .personalInformation(PersonalInformation
                        .builder()
                        .email(userinfoplus.getEmail())
                        .build())
                .build();
        user = save(user);
        user.setGeneratedUsername(user.getDisplayedName() + "-" + user.getId() + "-" + UUID.randomUUID());
        return save(user);
    }
}