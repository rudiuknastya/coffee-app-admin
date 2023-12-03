package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
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

    @Mapping(target="additiveTypeName", source="additiveType.name")
    AdditiveDTO additiveToAdditiveDTO(Additive additive);
    @Mapping(target="additiveTypeId", source="additiveType.id")
    AdditiveRequest additiveTOAdditiveRequest(Additive additive);
    @Mapping(target="additiveTypeId", source="additiveType.id")
    @Mapping(target="additiveTypeName", source="additiveType.name")
    AdditiveOrderResponse additiveToAdditiveOrderResponse(Additive additive);
    List<AdditiveOrderSelect> additiveListToAdditiveOrderSelectList(List<Additive> additives);
    @Named("adminListToAdminDTOList")
    static Additive additiveRequestToAdditive(AdditiveRequest additiveRequest){
        if(additiveRequest == null){
            return null;
        }
        Additive additive = new Additive();
        additive.setDeleted(false);
        additive.setName(additiveRequest.getName());
        additive.setPrice(additiveRequest.getPrice());
        additive.setStatus(additiveRequest.getStatus());
        return additive;
    }
}
