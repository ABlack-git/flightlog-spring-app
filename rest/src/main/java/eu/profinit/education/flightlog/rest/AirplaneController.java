package eu.profinit.education.flightlog.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.profinit.education.flightlog.service.AirplaneService;
import eu.profinit.education.flightlog.to.AirplaneTo;

@RestController
public class AirplaneController {
    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping("/airplane")
    public List<AirplaneTo> getClubAirplanes() {
        return airplaneService.getClubAirplanes();
    }
}
