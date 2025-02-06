package com.gridnine.testing;

import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        // Filter 1: departing in the past
        Predicate<Flight> departureFilter = FlightFilters.isDepartureAfterNow();
        List<Flight> filteredDeparture = flights.stream()
                .filter(departureFilter)
                .toList();
        System.out.println("Flight departing in the past filtered out. Flight 3 must be not in list");
        filteredDeparture.forEach(System.out::println);
        System.out.println("=======================================");

        // Filter 2: segments with departs before arrives
        Predicate<Flight> arrivalFilter = FlightFilters.isArrivalNotBeforeDeparture();
        List<Flight> filteredArrival = flights.stream()
                .filter(arrivalFilter)
                .toList();
        System.out.println("Flights with segments where the arrival occurs before the departure are filtered out." +
                "Flight 4 must be not in list");
        filteredArrival.forEach(System.out::println);
        System.out.println("=======================================");

        // Filter 3: more than two hours ground time
        Predicate<Flight> groundTimeFilter = FlightFilters.totalGroundTimeNotExceedTwoHours();
        List<Flight> filteredGroundTime = flights.stream()
                .filter(groundTimeFilter)
                .toList();
        System.out.println("Flights with more than two hours ground time. Flights 5,6 must be not in list");
        filteredGroundTime.forEach(System.out::println);
    }
}