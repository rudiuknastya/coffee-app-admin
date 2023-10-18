package project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.entity.City;

import java.util.List;

public interface CityService {
    Page<City> getCities(Pageable pageable, String name);
    City getCityByName(String name);
}
