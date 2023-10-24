package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.Location;

import java.util.List;

public interface LocationService {
    Page<Location> getLocationsByPage(Pageable pageable);
    Page<Location> getLocationsByAddressAndCity(String address, String city, Pageable pageable);
    Location saveLocation(Location location);
    Location getLocationById(Long id);
    Location getLocationByPhoneNumber(String number);
}
