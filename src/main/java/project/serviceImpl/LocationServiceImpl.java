package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Location;
import project.repository.LocationRepository;
import project.service.LocationService;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.LocationSpecification.*;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<Location> getLocationsByPage(Pageable pageable) {
        logger.info("getLocationsByPage() - Finding all locations for page "+ pageable.getPageNumber());
        Page<Location> locations = locationRepository.findAll(byDeleted(), pageable);
        logger.info("getLocationsByPage() - All locations were found");
        return locations;
    }

    @Override
    public Page<Location> getLocationsByAddressAndCity(String address, String city, Pageable pageable) {
        logger.info("getLocationsByAddress() - Finding all locations for address "+ address+ " and city "+ city);
        Page<Location> locations;
        if((city != null && !city.equals("")) &&  (address == null || address.equals(""))) {
            locations = locationRepository.findAll(where(byCity(city).and(byDeleted())),pageable);
        } else if((address != null && !address.equals(""))  && (city == null || city.equals(""))){
            locations = locationRepository.findAll(where(byAddressLike(address).and(byDeleted())),pageable);
        } else if((address != null && !address.equals("")) && (city != null && !city.equals(""))) {
            locations = locationRepository.findAll(where(byCity(city).and(byAddressLike(address).and(byDeleted()))),pageable);
        } else {
            locations = locationRepository.findAll(byDeleted(),pageable);
        }
        logger.info("getLocationsByAddress() - All locations were found");
        return locations;
    }

    @Override
    public Location saveLocation(Location location) {
        logger.info("saveLocation() - Saving location");
        Location location1 = locationRepository.save(location);
        logger.info("saveLocation() - Location was saved");
        return location1;
    }

    @Override
    public Location getLocationById(Long id) {
        logger.info("getLocationById() - Finding location with id "+id);
        Location location = locationRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("getLocationById() - Location was found");
        return location;
    }
}
