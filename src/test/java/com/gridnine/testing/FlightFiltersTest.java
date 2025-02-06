package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FlightFiltersTest {

    List<Flight> flights;

    @BeforeEach
    void setUp() {
        flights = FlightBuilder.createFlights();
    }

    @Test
    public void isDepartureAfterNow() {
        List<Flight> expected = flights.stream()
                .filter(FlightFilters.isDepartureAfterNow())
                .toList();

        expected.forEach(flight -> {
            LocalDateTime departure = flight.getSegments().get(0).getDepartureDate();
            assertFalse(departure.isBefore(LocalDateTime.now()),
                    "Flight departing in the past: " + flight);
        });
    }

    @Test
    void isArrivalNotBeforeDeparture() {
        List<Flight> expected = flights.stream()
                .filter(FlightFilters.isArrivalNotBeforeDeparture())
                .toList();

        expected.forEach(flight -> {
            flight.getSegments().forEach(segment ->
                    assertFalse(segment.getArrivalDate().isBefore(segment.getDepartureDate()),
                            "Flight that departs before it arrives: " + flight)
            );
        });
    }

    @Test
    public void isTotalGroundTimeNotExceedTwoHours() {
        List<Flight> expected = flights.stream()
                .filter(FlightFilters.totalGroundTimeNotExceedTwoHours())
                .toList();

        expected.forEach(flight -> {
            if (flight.getSegments().size() > 1) {
                Duration totalGroundTime = Duration.ZERO;
                for (int i = 1; i < flight.getSegments().size(); i++) {
                    Duration groundTime = Duration.between(
                            flight.getSegments().get(i - 1).getArrivalDate(),
                            flight.getSegments().get(i).getDepartureDate()
                    );
                    totalGroundTime = totalGroundTime.plus(groundTime);
                }
                assertFalse(totalGroundTime.minus(Duration.ofHours(2)).isPositive(),
                        "Flight with more than two hours ground time: " + flight);
            }
        });
    }
}