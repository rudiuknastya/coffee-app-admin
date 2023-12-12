package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import project.entity.Admin;
import project.entity.City;
import project.entity.Location;
import project.model.locationModel.LocationRequest;
import project.model.locationModel.SaveLocationRequest;
import project.service.AdminService;
import project.service.CityService;
import project.service.LocationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final CityService cityService;
    private final AdminService adminService;

    public LocationController(LocationService locationService, CityService cityService, AdminService adminService) {
        this.locationService = locationService;
        this.cityService = cityService;
        this.adminService = adminService;
    }

    private int pageSize = 5;
    @GetMapping("/locations")
    public String locations(Model model){
        model.addAttribute("pageNum", 7);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "location/locations";
    }
    @GetMapping("/getLocations")
    public @ResponseBody Page<Location> getAllLocations(@RequestParam("page")int page,@RequestParam("size")int size){
        Pageable pageable = PageRequest.of(page,size);
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
    public @ResponseBody Page<Location> searchLocation(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam(name="address", required = false) String address, @RequestParam(name="city", required = false) String city){
        Pageable pageable = PageRequest.of(page,size);
        return locationService.getLocationsByAddressAndCity(address, city, pageable);
    }

    @GetMapping("/locations/new")
    public String createLocation(Model model){
        String l = "saveLocation";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "location/location_page";
    }
    @PostMapping("/locations/saveLocation")
    public @ResponseBody ResponseEntity<?> saveLocation(@Valid @ModelAttribute("saveLocation") SaveLocationRequest location){
        locationService.createAndSaveLocation(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/locations/edit/{id}")
    public String editLocation(@PathVariable Long id, Model model){
        String l = "editLocation";
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String email = userDetails.getUsername();
        Optional<Admin> admin = adminService.getAdminByEmail(email);
        model.addAttribute("image",admin.get().getImage());
        return "location/location_page";
    }
    @GetMapping("/locations/edit/getLocation/{id}")
    public @ResponseBody Location getLocation(@PathVariable Long id){
        return locationService.getLocationById(id);
    }
    @PostMapping("/locations/edit/editLocation")
    public @ResponseBody ResponseEntity<?> updateLocation(@Valid @ModelAttribute("editLocation") LocationRequest location){
        locationService.updateLocation(location);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/getLocationCount")
    public @ResponseBody Long getLocationCount(){
        return locationService.getLocationCount();
    }
}
