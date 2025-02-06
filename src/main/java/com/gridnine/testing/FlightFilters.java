package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

public class FlightFilters {

    private FlightFilters() {}

    public static Predicate<Flight> isDepartureAfterNow() {
        return flight -> {
            LocalDateTime departure = flight.getSegments().get(0).getDepartureDate();
            return !departure.isBefore(LocalDateTime.now());
        };
    }

    public static Predicate<Flight> isArrivalNotBeforeDeparture() {
        return flight -> flight.getSegments().stream()
                .noneMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
    }

    public static Predicate<Flight> totalGroundTimeNotExceedTwoHours() {
        return flight -> {
            if (flight.getSegments().size() <= 1) {
                return true;
            }
            Duration totalGroundTime = Duration.ZERO;
            for (int i = 1; i < flight.getSegments().size(); i++) {
                Duration groundTime = Duration.between(
                        flight.getSegments().get(i - 1).getArrivalDate(),
                        flight.getSegments().get(i).getDepartureDate());
                totalGroundTime = totalGroundTime.plus(groundTime);
            }
            return !totalGroundTime.minus(Duration.ofHours(2)).isPositive();
        };
    }
}