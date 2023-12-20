package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import project.entity.AdditiveType;
import project.model.additiveModel.AdditiveDTO;
import project.entity.Additive;
import project.model.additiveModel.AdditiveOrderResponse;
import project.model.additiveModel.AdditiveOrderSelect;
import project.model.additiveModel.AdditiveRequest;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdditiveMapper {
    AdditiveMapper ADDITIVE_MAPPER = Mappers.getMapper(AdditiveMapper.class);
    @Mapping(target="additiveTypeName", source="additiveType.name")
    List<AdditiveDTO> additiveListToDTOList(List<Additive> additives);
    @Mapping(target = "additiveType", source = "setAdditiveType")
    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "deleted")
    @Mapping(target = "name", source = "additiveRequest.name")
    @Mapping(target = "status", source = "additiveRequest.status")
    void setAdditiveRequest(@MappingTarget Additive additive, AdditiveRequest additiveRequest, AdditiveType setAdditiveType);
    @Mapping(target="additiveTypeName", source="additiveType.name")
    AdditiveDTO additiveToAdditiveDTO(Additive additive);
    @Mapping(target="additiveTypeId", source="additiveType.id")
    AdditiveRequest additiveTOAdditiveRequest(Additive additive);
    @Mapping(target="additiveTypeId", source="additiveType.id")
    @Mapping(target="additiveTypeName", source="additiveType.name")
    AdditiveOrderResponse additiveToAdditiveOrderResponse(Additive additive);
    List<AdditiveOrderSelect> additiveListToAdditiveOrderSelectList(List<Additive> additives);
    @Mapping(ignore = true, target = "id")
    @Mapping(target = "deleted", expression = "java(false)")
    Additive additiveRequestToAdditive(AdditiveRequest additiveRequest);
}
