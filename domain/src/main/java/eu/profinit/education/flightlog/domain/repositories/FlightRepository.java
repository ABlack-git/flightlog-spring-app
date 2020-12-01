package eu.profinit.education.flightlog.domain.repositories;

import eu.profinit.education.flightlog.domain.entities.Flight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Flight.Type flightType);


    List<Flight> findAllByLandingTimeIsNullOrderByTakeoffTimeAscIdAsc();
    // Lety by se měly řadit od nejstarších a v případě shody podle ID tak, aby vlečná byla před kluzákem, který táhne
    // Výsledek si můžete ověřit v testu k této tříde v modulu service

    // TODO 8.1: Vytvorte metodu pro nacteni vlecnych letu pro vytvoreni dvojice letu na obrazovce Report
    List<Flight> findAllOrderBy();
}

