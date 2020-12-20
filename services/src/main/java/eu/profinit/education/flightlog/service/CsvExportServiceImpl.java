package eu.profinit.education.flightlog.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import eu.profinit.education.flightlog.domain.entities.Flight;
import eu.profinit.education.flightlog.domain.entities.Person;
import eu.profinit.education.flightlog.domain.repositories.FlightRepository;
import eu.profinit.education.flightlog.exceptions.FlightLogException;
import eu.profinit.education.flightlog.to.FileExportTo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class CsvExportServiceImpl implements CsvExportService {

    private final FlightRepository flightRepository;

    private final String fileName;

    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";

    public CsvExportServiceImpl(FlightRepository flightRepository, @Value("${csv.export.flight.fileName}") String fileName) {
        this.flightRepository = flightRepository;
        this.fileName = fileName;
    }

    @Override
    public FileExportTo getAllFlightsAsCsv() {
        List<Flight> flights = flightRepository.findAllByFlightTypeOrderByTakeoffTimeDescIdAsc(Flight.Type.TOWPLANE);
        byte[] fileContent;
        final String[] HEADERS = {"flightId", "flightType", "airplaneImmatriculation", "crew", "task", "takeoffTime", "landingTime"};
        try (var output = new ByteArrayOutputStream();
             var printer = new CSVPrinter(new OutputStreamWriter(output, StandardCharsets.UTF_8),
                 CSVFormat.DEFAULT.withHeader(HEADERS).withRecordSeparator('\n'))) {

            for (var flight : flights) {
                var towPlaneFlight = getCsvRecord(flight.getId().getId(), flight);
                printer.printRecord(towPlaneFlight);
                if (flight.getGliderFlight() != null) {
                    var gliderPlaneFlight = getCsvRecord(flight.getId().getId(), flight.getGliderFlight());
                    printer.printRecord(gliderPlaneFlight);
                }
            }
            printer.flush();
            fileContent = output.toByteArray();
        } catch (IOException e) {
            throw new FlightLogException("Error during flights CSV export", e);
        }
        return new FileExportTo(fileName, new MediaType("text", "csv"), fileContent);
    }

    private List<String> getCsvRecord(Long flightId, Flight flight) {
        List<String> values = new ArrayList<>();
        values.add(flightId.toString());
        values.add(flight.getFlightType().name());
        values.add(flight.getAirplane().getSafeImmatriculation());
        values.add(getCrew(flight));
        values.add(flight.getTask() == null ? "" : flight.getTask().getValue());
        values.add(formatDateTime(flight.getTakeoffTime()));
        values.add(flight.getLandingTime() == null ? "" : formatDateTime(flight.getLandingTime()));
        return values;
    }

    private String getCrew(Flight flight) {
        StringBuilder sb = new StringBuilder();
        sb.append(personToString(flight.getPilot()));
        if (flight.getCopilot() != null) {
            sb.append(", ");
            sb.append(personToString(flight.getCopilot()));
        }
        return sb.toString();
    }

    private String personToString(Person person) {
        return (person.getFullName() + " " + person.getFullAddress()).trim();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return formatter.format(dateTime);
    }

}
