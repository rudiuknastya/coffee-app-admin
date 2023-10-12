package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import project.entity.City;
import project.repository.CityRepository;
import project.service.CityService;

import java.util.List;
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public List<City> getAllCities() {
        logger.info("getAllCities() - Finding all cities");
        List<City> cities = cityRepository.findAll();
        logger.info("getAllCities() - All cities were found");
        return cities;
    }
}
