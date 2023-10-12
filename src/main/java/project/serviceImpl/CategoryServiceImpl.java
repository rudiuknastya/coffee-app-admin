package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dto.CategoryDTO;
import project.dto.CategoryNameDTO;
import project.entity.Category;
import project.repository.CategoryRepository;
import project.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<CategoryDTO> getAllCategories(Pageable pageable) {
        logger.info("getAllCategories() - Finding all categories for page "+ pageable.getPageNumber());
        Page<CategoryDTO> categoryDTOS = categoryRepository.findCategories(pageable);
        logger.info("getAllCategories() - All categories were found");
        return categoryDTOS;
    }

    @Override
    public Category saveCategory(Category category) {
        logger.info("saveCategory() - Saving category");
        Category category1 = categoryRepository.save(category);
        logger.info("saveCategory() - Category was saved");
        return category1;
    }

    @Override
    public Category getCategoryById(Long id) {
        logger.info("getCategoryById() - Finding category by id "+id);
        Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getCategoryById() - Category was found");
        return category;
    }

    @Override
    public Page<CategoryDTO> searchCategories(String name, Pageable pageable) {
        logger.info("searchCategories() - Finding categories by name like "+name);
        String n = "%"+name.toUpperCase()+"%";
        Page<CategoryDTO> categoryDTOS = categoryRepository.searchCategories(n,pageable);
        logger.info("searchCategories() - Categories were found");
        return categoryDTOS;
    }

    @Override
    public List<CategoryNameDTO> getCategoriesName() {
        logger.info("getCategoriesName() - Finding categories name");
        List<CategoryNameDTO> categoryNameDTOS = categoryRepository.findCategoriesName();
        logger.info("getCategoriesName() - Categories name were found");
        return categoryNameDTOS;
    }
}
