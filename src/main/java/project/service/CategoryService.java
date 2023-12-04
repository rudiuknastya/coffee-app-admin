package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.model.CategoryDTO;
import project.model.CategoryNameDTO;
import project.entity.Category;

import java.util.List;

public interface CategoryService {
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    Category saveCategory(Category category);
    Category getCategoryById(Long id);
    Page<CategoryDTO> searchCategories(String name, Pageable pageable);
    Page<CategoryNameDTO> getCategoriesName(Pageable pageable, String name);
    CategoryNameDTO getCategoryNameDTOById(Long id);
    void updateCategory(Category category);
    void deleteCategory(Long id);
}
