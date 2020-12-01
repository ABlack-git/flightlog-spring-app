package eu.profinit.education.flightlog.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import eu.profinit.education.flightlog.domain.entities.Flight;
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

    public CsvExportServiceImpl(FlightRepository flightRepository, @Value("${csv.export.flight.fileName}") String fileName) {
        this.flightRepository = flightRepository;
        this.fileName = fileName;
    }

    @Override
    public FileExportTo getAllFlightsAsCsv() {
        List<Flight> flights = flightRepository.findAll();
        byte[] fileContent;
        try (var output = new ByteArrayOutputStream();
             var printer = new CSVPrinter(new OutputStreamWriter(output, StandardCharsets.UTF_8), CSVFormat.DEFAULT)) {
            // TODO: Finish implementation
            for (var flight : flights) {
                printer.printRecord();
            }
            printer.flush();
            fileContent = output.toByteArray();
        } catch (IOException e) {
            throw new FlightLogException("Error during flights CSV export", e);
        }
        return new FileExportTo(fileName, new MediaType("text", "csv"), fileContent);
    }

}
