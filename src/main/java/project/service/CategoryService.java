package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.dto.CategoryDTO;
import project.dto.CategoryNameDTO;
import project.entity.Category;

import java.util.List;

public interface CategoryService {
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    Category saveCategory(Category category);
    Category getCategoryById(Long id);
    Page<CategoryDTO> searchCategories(String name, Pageable pageable);
    List<CategoryNameDTO> getCategoriesName();
}
