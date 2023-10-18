package project.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.entity.City;
import project.entity.Location;
import project.service.CityService;
import project.service.LocationService;

import java.util.List;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final CityService cityService;

    public LocationController(LocationService locationService, CityService cityService) {
        this.locationService = locationService;
        this.cityService = cityService;
    }
    private int pageSize = 1;
    @GetMapping("/admin/locations")
    public String locations(Model model){
        model.addAttribute("pageNum", 7);
        return "location/locations";
    }
    @GetMapping("/admin/getLocations")
    public @ResponseBody Page<Location> getAllLocations(@RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        return locationService.getLocationsByPage(pageable);
    }
    @GetMapping("/admin/getCitiesForLocations")
    public @ResponseBody Page<City> getCitiesForLocations(@RequestParam(value = "search", required = false)String city, @RequestParam("page")int page){
        Pageable pageable = PageRequest.of(page-1,pageSize);
        return cityService.getCities(pageable, city);
    }
    @GetMapping("/admin/getCityForLocation")
    public @ResponseBody City getCityForLocation(@RequestParam(value = "city", required = false)String city){
        return cityService.getCityByName(city);
    }
    @GetMapping("/admin/deleteLocation")
    public @ResponseBody String deleteLocation(@RequestParam("id") Long id){
        Location location = locationService.getLocationById(id);
        location.setDeleted(true);
        locationService.saveLocation(location);
        return "success";
    }
    @GetMapping("/admin/searchLocation")
    public @ResponseBody Page<Location> searchLocation(@RequestParam("page")int page, @RequestParam(name="address", required = false) String address, @RequestParam(name="city", required = false) String city){
        Pageable pageable = PageRequest.of(page,pageSize);
        return locationService.getLocationsByAddressAndCity(address, city, pageable);
    }

    @GetMapping("/admin/locations/new")
    public String createLocation(Model model){
        String l = "new";
        model.addAttribute("location", new Location());
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        return "location/location_page";
    }
    @PostMapping("/admin/locations/new")
    public String saveLocation(@Valid @ModelAttribute("location") Location location, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            String l = "new";
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 7);
            return "location/location_page";
        }
        location.setDeleted(false);
        locationService.saveLocation(location);
        return "redirect:/admin/locations";
    }

    @GetMapping("/admin/locations/edit/{id}")
    public String editLocation(@PathVariable Long id, Model model){
        String l = "edit/"+id;
        model.addAttribute("location", locationService.getLocationById(id));
        model.addAttribute("link", l);
        model.addAttribute("pageNum", 7);
        return "location/location_page";
    }
    @PostMapping("/admin/locations/edit/{id}")
    public String updateLocation(@PathVariable Long id, @Valid @ModelAttribute("location") Location location, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            String l = "edit/"+id;
            model.addAttribute("link", l);
            model.addAttribute("pageNum", 7);
            return "location/location_page";
        }
        Location locationInDB = locationService.getLocationById(id);
        locationInDB.setCity(location.getCity());
        locationInDB.setAddress(location.getAddress());
        locationInDB.setCoordinates(location.getCoordinates());
        locationInDB.setPhoneNumber(location.getPhoneNumber());
        locationInDB.setWorkingHours(location.getWorkingHours());
        locationService.saveLocation(locationInDB);
        return "redirect:/admin/locations";
    }
}
