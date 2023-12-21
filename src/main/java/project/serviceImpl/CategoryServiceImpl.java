package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Product;
import project.mapper.CategoryMapper;
import project.model.categoryModel.CategoryDTO;
import project.model.categoryModel.CategoryNameDTO;
import project.entity.Category;
import project.repository.CategoryRepository;
import project.repository.ProductRepository;
import project.service.CategoryService;
import static project.specifications.CategorySpecification.*;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Category was not found by id "+id));
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
    public Page<CategoryNameDTO> getCategoriesName(Pageable pageable, String name) {
        logger.info("getCategoriesName() - Finding categories name for page "+ pageable.getPageNumber()+" and search "+name);
        Page<Category> categories;
        if(name != null){
            categories = categoryRepository.findAll(byDeleted().and(byNameLike(name)),pageable);
        } else {
            categories = categoryRepository.findAll(byDeleted(),pageable);
        }
        List<CategoryNameDTO> categoryNameDTOS = CategoryMapper.CATEGORY_MAPPER.categoryListToCategoryNameDTOList(categories.getContent());
        Page<CategoryNameDTO> categoryNameDTOPage = new PageImpl<>(categoryNameDTOS, pageable, categories.getTotalElements());
        logger.info("getCategoriesName() - Categories name were found");
        return categoryNameDTOPage;
    }

    @Override
    public CategoryNameDTO getCategoryNameDTOById(Long id) {
        logger.info("getCategoryNameDTOById() - Finding category for category name dto by id "+id);
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Category was not found by id "+id));
        CategoryNameDTO categoryNameDTO = CategoryMapper.CATEGORY_MAPPER.categoryToCategoryNameDTO(category);
        logger.info("getCategoryNameDTOById() - Category name was found");
        return categoryNameDTO;
    }

    @Override
    public void updateCategory(Category category) {
        logger.info("updateCategory() - Updating category");
        Category categoryInDb = categoryRepository.findById(category.getId()).orElseThrow(()-> new EntityNotFoundException("Category was not found by id "+category.getId()));
        categoryInDb.setName(category.getName());
        categoryInDb.setStatus(category.getStatus());
        categoryRepository.save(categoryInDb);
        logger.info("updateCategory() - Category was updated");
    }

    @Override
    public void deleteCategory(Long id) {
        logger.info("deleteCategory() - Deleting category");
        Category category = categoryRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Category was not found by id "+id));
        category.setDeleted(true);
        List<Product> products = productRepository.findProductsForCategory(id);
        for(Product product: products){
            product.setDeleted(true);
            productRepository.save(product);
        }
        categoryRepository.save(category);
        logger.info("deleteCategory() - Category was deleted");
    }
}
