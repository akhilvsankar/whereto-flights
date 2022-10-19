package com.fc.flights.service;

import com.fc.flights.model.AirshoppingRequest;
import com.fc.flights.model.AirshoppingResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirshoppingService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    private static final Integer DISTANCE_AIRPORTS = 20;

    private static final String URL = "https://gist.githubusercontent.com/bgdavidx/132a9e3b9c70897bc07cfa5ca25747be/raw/8dbbe1db38087fad4a8c8ade48e741d6fad8c872/gistfile1.txt";

    public List<AirshoppingResponse> getFlightsWithCriteria(AirshoppingRequest airshoppingRequest)
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type","text/plain;charset=utf=8");
        String resp = restTemplate.getForObject(URL,String.class);
        List<AirshoppingResponse> response = null;
        ObjectMapper obj = new ObjectMapper();
        obj.configure(SerializationFeature.WRAP_ROOT_VALUE,true);
        try {
             response = obj.readValue(resp, new TypeReference<>() {
             });
             return filterFlightsWithCriteria(response,airshoppingRequest);
        } catch (JsonProcessingException e) {
           return null;
        }

    }

    private List<AirshoppingResponse> filterFlightsWithCriteria(List<AirshoppingResponse> response,AirshoppingRequest request) {
        DateTimeFormatter dtf =DateTimeFormatter.ofPattern(DATE_PATTERN);
        ZonedDateTime arrivalDate = getParsedTime(request.getArrivalDate());
        ZonedDateTime depDate = getParsedTime(request.getDepartureDate());

        List<AirshoppingResponse> filteredResp = response.stream().filter(fl->getParsedTime(fl.getArrivalTime()).isBefore(arrivalDate) &&
                getParsedTime(fl.getDepartureTime()).isAfter(depDate) &&
                getTimeDuration(getParsedTime(fl.getArrivalTime()),getParsedTime(fl.getDepartureTime()))<=request.getHours()).collect(Collectors.toList());

      List<AirshoppingResponse> filtered = filteredResp.stream().peek(fl -> {
          long timeDur = getTimeDuration(getParsedTime(fl.getArrivalTime()), getParsedTime(fl.getDepartureTime()));
          boolean isPreferredCarrier = request.getCarrier().equalsIgnoreCase(fl.getCarrier());
          double carrierPref = isPreferredCarrier ? 0.9D : 1.0D;
          double sortKey = timeDur * carrierPref + DISTANCE_AIRPORTS;
          fl.setSortKey(sortKey);
      }).sorted(Comparator.comparing(AirshoppingResponse::getSortKey)).collect(Collectors.toList());

        return  filtered;

    }

    private ZonedDateTime getParsedTime(String date)
    {
        DateTimeFormatter dtf =DateTimeFormatter.ofPattern(DATE_PATTERN);
        return ZonedDateTime.parse(date,dtf);
    }

    private long getTimeDuration(ZonedDateTime arrivalDate, ZonedDateTime depDate)
    {
       return arrivalDate.getHour()-depDate.getHour();
    }
}
