package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.AdditiveType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveTypeMapper {
    AdditiveTypeMapper ADDITIVE_TYPE_MAPPER = Mappers.getMapper(AdditiveTypeMapper.class);
    List<AdditiveTypeNameDTO> additiveTypeToDTO(List<AdditiveType> additiveTypes);
    AdditiveTypeNameDTO additiveTypeToAdditiveTypeNameDTO(AdditiveType additiveType);
}
