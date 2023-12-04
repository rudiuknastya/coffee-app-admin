package project.serviceImpl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.entity.City;
import project.repository.CityRepository;
import project.service.CityService;
import static project.specifications.CitySpecification.*;
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
    private Logger logger = LogManager.getLogger("serviceLogger");
    @Override
    public Page<City> getCities(Pageable pageable, String city) {
        logger.info("getCities() - Finding cities for page "+pageable.getPageNumber()+ " and search "+city);
        Page<City> cities;
        if(city == null){
            cities = cityRepository.findAll(pageable);
        } else {
            cities = cityRepository.findAll(byCityLike(city), pageable);
        }
        logger.info("getCities() - Cities were found");
        return cities;
    }

    @Override
    public City getCityByName(String name) {
        logger.info("getCityByName() - Finding city by name "+name);
        City city = cityRepository.findByCity(name);
        logger.info("getCityByName() - City was found");
        return city;
    }
}
