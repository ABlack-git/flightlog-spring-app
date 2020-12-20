package eu.profinit.education.flightlog.domain.repositories;

import eu.profinit.education.flightlog.domain.entities.Flight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Flight.Type flightType);


    List<Flight> findAllByLandingTimeIsNullOrderByTakeoffTimeAscIdAsc();

    List<Flight> findAllOrderBy();
}

