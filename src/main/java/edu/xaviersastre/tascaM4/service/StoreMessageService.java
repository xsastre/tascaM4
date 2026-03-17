package edu.xaviersastre.tascaM4.service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class StoreMessageService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE_CET = ZoneId.of("CET");

    /**
     * Guarda el missatge (xifrat o en clar) al fitxer data.txt amb data i hora (CET).
     *
     * @param message el text a guardar
     */
    public void saveToFile(String message) {
        String timestamp = ZonedDateTime.now(ZONE_CET).format(formatter);
        String lineToSave = "[" + timestamp + " CET] " + message + System.lineSeparator();
        
        try {
            Files.write(
                    Paths.get("data.txt"),
                    lineToSave.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (java.io.IOException e) {
            System.err.println("Error en guardar a data.txt: " + e.getMessage());
        }
    }
}
