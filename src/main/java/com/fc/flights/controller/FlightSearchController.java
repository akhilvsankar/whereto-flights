package com.fc.flights.controller;

import com.fc.flights.model.AirshoppingRequest;
import com.fc.flights.model.AirshoppingResponse;
import com.fc.flights.service.AirshoppingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FlightSearchController {

    @Autowired
    private AirshoppingService airshoppingService;


    @PostMapping("/flights")
    public List<AirshoppingResponse> getFlightsWithCriteria(@RequestBody AirshoppingRequest airshoppingRequest)
    {
       return airshoppingService.getFlightsWithCriteria(airshoppingRequest);
    }
}
