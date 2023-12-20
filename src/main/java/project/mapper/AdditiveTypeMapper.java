package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import project.model.additiveModel.AdditiveRequest;
import project.model.additiveTypeModel.AdditiveTypeNameDTO;
import project.entity.AdditiveType;
import project.model.additiveTypeModel.AdditiveTypeRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveTypeMapper {
    AdditiveTypeMapper ADDITIVE_TYPE_MAPPER = Mappers.getMapper(AdditiveTypeMapper.class);
    List<AdditiveTypeNameDTO> additiveTypeToDTO(List<AdditiveType> additiveTypes);
    AdditiveTypeNameDTO additiveTypeToAdditiveTypeNameDTO(AdditiveType additiveType);
    @Mapping(ignore = true, target = "id")
    @Mapping(target = "deleted", expression = "java(false)")
    AdditiveType additiveTypeRequestToadditiveType(AdditiveTypeRequest additiveTypeRequest);
    @Mapping(ignore = true, target = "id")
    void setAdditiveTypeRequest(@MappingTarget AdditiveType additiveType, AdditiveTypeRequest additiveTypeRequest);
}
