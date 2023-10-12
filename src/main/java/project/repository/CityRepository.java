package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entity.City;

public interface CityRepository extends JpaRepository<City, Long> {

}
