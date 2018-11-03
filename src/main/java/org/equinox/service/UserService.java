package org.equinox.service;

import org.equinox.exception.LoginUsernameIsAlreadyInUseException;
import org.equinox.model.dto.CreateStandardUserDTO;
import org.equinox.model.dto.CurrentUserDTO;
import org.equinox.model.dto.CurrentUserFullProfileDTO;
import org.equinox.model.dto.UpdateUserDTO;
import org.equinox.model.dto.UserDTO;
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
}