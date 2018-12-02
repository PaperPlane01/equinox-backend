package aphelion.service.impl;

import aphelion.annotation.Page;
import aphelion.annotation.PageSize;
import aphelion.annotation.SortBy;
import aphelion.annotation.SortingDirection;
import aphelion.annotation.ValidatePaginationParameters;
import aphelion.exception.BlogPostNotFoundException;
import aphelion.exception.BlogPostReportNotFoundException;
import aphelion.mapper.BlogPostReportToBlogPostReportDTOMapper;
import aphelion.mapper.CreateBlogPostReportDTOToBlogPostReportMapper;
import aphelion.model.domain.BlogPost;
import aphelion.model.domain.BlogPostReport;
import aphelion.model.domain.ReportStatus;
import aphelion.model.dto.BlogPostReportDTO;
import aphelion.model.dto.CreateBlogPostReportDTO;
import aphelion.model.dto.UpdateBlogPostReportDTO;
import aphelion.repository.BlogPostReportRepository;
import aphelion.repository.BlogPostRepository;
import aphelion.util.SortingDirectionUtils;
import lombok.RequiredArgsConstructor;
import aphelion.service.BlogPostReportService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BlogPostReportServiceImpl implements BlogPostReportService {
    private final BlogPostReportRepository blogPostReportRepository;
    private final BlogPostRepository blogPostRepository;
    private final BlogPostReportToBlogPostReportDTOMapper blogPostReportToBlogPostReportDTOMapper;
    private final CreateBlogPostReportDTOToBlogPostReportMapper createBlogPostReportDTOToBlogPostReportMapper;

    @Override
    public BlogPostReportDTO save(CreateBlogPostReportDTO createBlogPostReportDTO) {
        BlogPostReport blogPostReport = createBlogPostReportDTOToBlogPostReportMapper.map(createBlogPostReportDTO);
        blogPostReport = blogPostReportRepository.save(blogPostReport);
        return blogPostReportToBlogPostReportDTOMapper.map(blogPostReport);
    }

    @Override
    public BlogPostReportDTO update(Long id, UpdateBlogPostReportDTO updateBlogPostReportDTO) {
        BlogPostReport blogPostReport = findBlogPostReportById(id);
        blogPostReport.setStatus(updateBlogPostReportDTO.getStatus());
        blogPostReport = blogPostReportRepository.save(blogPostReport);
        return blogPostReportToBlogPostReportDTOMapper.map(blogPostReport);
    }

    private BlogPostReport findBlogPostReportById(Long id) {
        return blogPostReportRepository.findById(id)
                .orElseThrow(() -> new BlogPostReportNotFoundException("Blog post report with given id + " + id + " could not be found."));
    }

    private BlogPost findBlogPostById(Long blogPostId) {
        return blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with given id " + blogPostId + "could not be found"));
    }

    @Override
    public void delete(Long id) {
        BlogPostReport blogPostReport = findBlogPostReportById(id);
        blogPostReportRepository.delete(blogPostReport);
    }

    @Override
    public BlogPostReportDTO findById(Long id) {
        BlogPostReport blogPostReport = findBlogPostReportById(id);
        return blogPostReportToBlogPostReportDTOMapper.map(blogPostReport);
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostReportDTO> findAll(@Page int page,
                                           @PageSize(max = 150) int pageSize,
                                           @SortingDirection String sortingDirection,
                                           @SortBy(allowed = {
                                                   "id", "status"
                                           }) String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostReportRepository.findBy(pageRequest)
                .stream()
                .map(blogPostReportToBlogPostReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostReportDTO> findByBlogPost(Long blogPostId,
                                                  @Page int page,
                                                  @PageSize(max = 150) int pageSize,
                                                  @SortingDirection String sortingDirection,
                                                  @SortBy(allowed = {"id", "status"}) String sortBy) {
        BlogPost blogPost = findBlogPostById(blogPostId);
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostReportRepository.findByBlogPost(blogPost, pageRequest)
                .stream()
                .map(blogPostReportToBlogPostReportDTOMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @ValidatePaginationParameters
    public List<BlogPostReportDTO> findByStatus(ReportStatus reportStatus,
                                                @Page int page,
                                                @PageSize(max = 150) int pageSize,
                                                @SortingDirection String sortingDirection,
                                                @SortBy(allowed = "id") String sortBy) {
        Sort.Direction direction = SortingDirectionUtils.convertFromString(sortingDirection);
        PageRequest pageRequest = PageRequest.of(page, pageSize, direction, sortBy);
        return blogPostReportRepository.findByStatus(reportStatus, pageRequest)
                .stream()
                .map(blogPostReportToBlogPostReportDTOMapper::map)
                .collect(Collectors.toList());
    }
}