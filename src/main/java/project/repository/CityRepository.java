package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.entity.City;

public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {
    City findByCity(String city);

}
