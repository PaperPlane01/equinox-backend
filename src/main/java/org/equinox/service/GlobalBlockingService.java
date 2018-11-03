package org.equinox.service;

import org.equinox.model.domain.User;
import org.equinox.model.dto.CreateGlobalBlockingDTO;
import org.equinox.model.dto.GlobalBlockingDTO;
import org.equinox.model.dto.UpdateGlobalBlockingDTO;

import java.util.List;

public interface GlobalBlockingService {
    GlobalBlockingDTO save(CreateGlobalBlockingDTO createGlobalBlockingDTO);
    GlobalBlockingDTO update(Long id, UpdateGlobalBlockingDTO updateGlobalBlockingDTO);
    void delete(Long id);
    GlobalBlockingDTO findById(Long id);
    List<GlobalBlockingDTO> findAllByUser(Long userId, int page, int pageSize, String sortingDirection, String sortBy);
    List<GlobalBlockingDTO> findNonExpiredByUser(Long userId);
    boolean isUserBlockedGlobally(Long userId);
    boolean isUserBlockedGlobally(User user);
}
