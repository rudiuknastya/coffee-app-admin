package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Category;
import project.model.CategoryNameDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper CATEGORY_MAPPER = Mappers.getMapper(CategoryMapper.class);
    List<CategoryNameDTO> categoryListToCategoryNameDTOList(List<Category> categories);
    CategoryNameDTO categoryToCategoryNameDTO(Category category);

}
