package aphelion.service;

import aphelion.exception.LoginUsernameIsAlreadyInUseException;
import aphelion.model.domain.User;
import aphelion.model.dto.CreateStandardUserDTO;
import aphelion.model.dto.CurrentUserDTO;
import aphelion.model.dto.CurrentUserFullProfileDTO;
import aphelion.model.dto.UpdateUserDTO;
import aphelion.model.dto.UserDTO;
import com.google.api.services.oauth2.model.Userinfoplus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDTO findById(Long id);
    UserDTO findByLoginUsername(String loginUsername);
    List<UserDTO> findByDisplayedNameContains(String line, int page, int pageSize, String sortingDirection, String sortBy);
    UserDTO saveStandardUser(CreateStandardUserDTO createStandardUserDTO);
    UserDTO update(Long id, UpdateUserDTO updateUserDTO);
    void delete(Long id);
    CurrentUserDTO getCurrentUser();
    CurrentUserFullProfileDTO getCurrentUserFullProfile();
    CurrentUserFullProfileDTO updateCurrentUser(UpdateUserDTO updateUserDTO);
    @Override
    UserDetails loadUserByUsername(String s) throws UsernameNotFoundException;
    UserDetails loadUserByLoginUsername(String username) throws UsernameNotFoundException;
    void assertThatUsernameIsNotInUse(String username) throws LoginUsernameIsAlreadyInUseException;
    User registerGoogleUser(Userinfoplus userinfoplus);
}