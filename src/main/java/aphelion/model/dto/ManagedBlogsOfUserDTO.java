package aphelion.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagedBlogsOfUserDTO {
    private List<BlogMinifiedDTO> ownedBlogs;
    private List<ManagedBlogWithBlogDTO> managedBlogs;
}
