package project.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.Location;
import project.mapper.LocationMapper;
import project.model.locationModel.LocationRequest;
import project.model.locationModel.SaveLocationRequest;
import project.repository.LocationRepository;
import project.service.LocationService;
import project.specifications.LocationSpecification;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;
import static project.specifications.LocationSpecification.*;

@Service
public class LocationServiceImpl implements LocationService {
    private Logger logger = LogManager.getLogger("serviceLogger");
    private final LocationRepository locationRepository;
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }
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
        Location location = locationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Location was not found by id "+id));
        logger.info("getLocationById() - Location was found");
        return location;
    }

    @Override
    public Optional<Location> getLocationByPhoneNumber(String number) {
        logger.info("getLocationByPhoneNumber() - Finding location by phone number "+number);
        Optional<Location> location = locationRepository.findByPhoneNumber(number);
        logger.info("getLocationByPhoneNumber() - Location was found");
        return location;
    }

    @Override
    public Long getLocationCount() {
        logger.info("getLocationCount() - Finding locations count");
        Long count = locationRepository.findLocationCount();
        logger.info("getLocationCount() - Locations count was found");
        return count;
    }

    @Override
    public void updateLocation(LocationRequest locationRequest) {
        logger.info("updateLocation() - Updating location");
        Location locationInDB = locationRepository.findById(locationRequest.getId()).orElseThrow(()-> new EntityNotFoundException("Location was not found by id "+locationRequest.getId()));
        LocationMapper.LOCATION_MAPPER.setLocationRequest(locationInDB,locationRequest);
        locationRepository.save(locationInDB);
        logger.info("updateLocation() - Location was updated");
    }

    @Override
    public void createAndSaveLocation(SaveLocationRequest saveLocationRequest) {
        logger.info("createAndSaveLocation() - Creating and saving location");
        Location location = LocationMapper.LOCATION_MAPPER.saveLocationRequestToLocation(saveLocationRequest);
        locationRepository.save(location);
        logger.info("createAndSaveLocation() - Location was created and saved");
    }
}
