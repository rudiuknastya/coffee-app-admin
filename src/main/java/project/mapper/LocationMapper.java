package project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import project.entity.Location;
import project.model.locationModel.LocationRequest;
import project.model.locationModel.SaveLocationRequest;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationMapper LOCATION_MAPPER = Mappers.getMapper(LocationMapper.class);
    @Mapping(target = "deleted", expression = "java(false)")
    Location saveLocationRequestToLocation(SaveLocationRequest saveLocationRequest);
    @Mapping(ignore = true, target = "id")
    void setLocationRequest(@MappingTarget Location location, LocationRequest locationRequest);
}
