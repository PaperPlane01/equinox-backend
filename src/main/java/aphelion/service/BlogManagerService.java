package aphelion.service;

import aphelion.model.dto.CreateBlogManagerDTO;
import aphelion.model.dto.ManagedBlogDTO;
import aphelion.model.dto.ManagedBlogWithBlogDTO;
import aphelion.model.dto.ManagedBlogWithUserDTO;
import aphelion.model.dto.UpdateBlogManagerDTO;

import java.util.List;

public interface BlogManagerService {
    ManagedBlogDTO save(CreateBlogManagerDTO createBlogManagerDTO);
    ManagedBlogDTO update(Long id, UpdateBlogManagerDTO updateBlogManagerDTO);
    void delete(Long id);
    ManagedBlogDTO findById(Long id);
    List<ManagedBlogWithBlogDTO> findByUser(Long id, int page, int pageSize, String sortingDirection, String sortBy);
    List<ManagedBlogWithUserDTO> findByBlog(Long id, int page, int pageSize, String sortingDirection, String sortBy);
    List<ManagedBlogDTO> findByBlogAndUser(Long blogId, Long userId);
}
