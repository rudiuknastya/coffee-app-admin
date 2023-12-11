package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import project.entity.Location;
import project.model.locationModel.SaveLocationRequest;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper LOCATION_MAPPER = Mappers.getMapper(LocationMapper.class);
    Location saveLocationRequestToLocation(SaveLocationRequest saveLocationRequest);
}
