package com.fc.flights.model;

import lombok.Data;

import java.util.Date;

@Data
public class AirshoppingRequest {

    private String departureDate;
    private String arrivalDate;
    private long hours;
    private String carrier;

}
