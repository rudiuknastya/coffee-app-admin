package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Location;
import project.model.locationModel.LocationRequest;
import project.model.locationModel.SaveLocationRequest;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Page<Location> getLocationsByPage(Pageable pageable);
    Page<Location> getLocationsByAddressAndCity(String address, String city, Pageable pageable);
    Location saveLocation(Location location);
    Location getLocationById(Long id);
    Optional<Location> getLocationByPhoneNumber(String number);
    Long getLocationCount();
    void createAndSaveLocation(SaveLocationRequest saveLocationRequest);
    void updateLocation(LocationRequest locationRequest);
}
