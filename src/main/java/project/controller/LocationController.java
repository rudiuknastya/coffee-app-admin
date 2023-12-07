package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.City;
import project.entity.Location;
import project.service.CityService;
import project.service.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final CityService cityService;

    public LocationController(LocationService locationService, CityService cityService) {
        this.locationService = locationService;
        this.cityService = cityService;
    }
    private int pageSize = 1;
    @GetMapping("/locations")
    public String locations(Model model){
        model.addAttribute("pageNum", 7);
        return "location/locations";
    }
    @GetMapping("/getLocations")
    public @ResponseBody Page<Location> getAllLocations(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return locationService.getLocationsByPage(pageable);
    }
    @GetMapping("/getCitiesForLocations")
    public @ResponseBody Page<City> getCitiesForLocations(@RequestParam(value = "search", required = false)String city, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return cityService.getCities(pageable, city);
    }
    @GetMapping("/getCityForLocation")
    public @ResponseBody City getCityForLocation(@RequestParam(value = "city", required = false)String city){
        return cityService.getCityByName(city);
    }
    @GetMapping("/deleteLocation")
    public @ResponseBody ResponseEntity deleteLocation(@RequestParam("id") Long id){
        Location location = locationService.getLocationById(id);
        location.setDeleted(true);
        locationService.saveLocation(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/searchLocation")
    public @ResponseBody Page<Location> searchLocation(@RequestParam("page")int page, @RequestParam(name="address", required = false) String address, @RequestParam(name="city", required = false) String city){
        Pageable pageable = PageRequest.of(page,pageSize);
        return locationService.getLocationsByAddressAndCity(address, city, pageable);
    }

    @GetMapping("/locations/new")
    public String createLocation(Model model){
        String l = "saveLocation";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        return "location/location_page";
    }
    @PostMapping("/locations/saveLocation")
    public @ResponseBody List<FieldError> saveLocation(@Valid @ModelAttribute("location") Location location, BindingResult bindingResult, Model model){
        Optional<Location> location1 = locationService.getLocationByPhoneNumber(location.getPhoneNumber());
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(location1.isPresent() && location1.get().getId() != location.getId()){
                FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(location1.isPresent() && location1.get().getId() != location.getId()){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        location.setDeleted(false);
        locationService.saveLocation(location);
        return null;
    }

    @GetMapping("/locations/edit/{id}")
    public String editLocation(@PathVariable Long id, Model model){
        String l = "editLocation";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        return "location/location_page";
    }
    @GetMapping("/locations/edit/getLocation/{id}")
    public @ResponseBody Location getLocation(@PathVariable Long id){
        return locationService.getLocationById(id);
    }
    @PostMapping("/locations/edit/editLocation")
    public @ResponseBody List<FieldError> updateLocation(@Valid @ModelAttribute("location") Location location, BindingResult bindingResult){
        Optional<Location> location1 = locationService.getLocationByPhoneNumber(location.getPhoneNumber());
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = new ArrayList<>(bindingResult.getFieldErrors());
            if(location1.isPresent() && location1.get().getId() != location.getId()){
                FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
                fieldErrors.add(fieldError);
            }
            return fieldErrors;
        }
        if(location1.isPresent() && location1.get().getId() != location.getId()){
            List<FieldError> fieldErrors = new ArrayList<>(1);
            FieldError fieldError = new FieldError("Number exist","phoneNumber","Такий номер телефону вже існує");
            fieldErrors.add(fieldError);
            return fieldErrors;
        }
        locationService.updateLocation(location);
        return null;
    }
    @GetMapping("/getLocationCount")
    public @ResponseBody Long getLocationCount(){
        return locationService.getLocationCount();
    }
}
