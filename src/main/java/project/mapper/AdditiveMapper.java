package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import project.dto.AdditiveDTO;
import project.entity.Additive;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveMapper {
    AdditiveMapper ADDITIVE_MAPPER = Mappers.getMapper(AdditiveMapper.class);
    @Mapping(target="additiveTypeName", source="additiveType.name")
    List<AdditiveDTO> additiveListToDTO(List<Additive> additives);

    @Mapping(target="additiveTypeName", source="additiveType.name")
    AdditiveDTO additiveToDTO(Additive additive);

}
