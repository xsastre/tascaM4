package edu.xaviersastre.tascaM4.service;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class StoreMessageService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId ZONE_CET = ZoneId.of("CET");
    private static final Path DATA_FILE = Paths.get("data.txt");

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
                    DATA_FILE,
                    lineToSave.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (java.io.IOException e) {
            System.err.println("Error en guardar a data.txt: " + e.getMessage());
        }
    }

    public Optional<String> readLastMessage() {
        try {
            if (!Files.exists(DATA_FILE)) {
                return Optional.empty();
            }

            List<String> lines = Files.readAllLines(DATA_FILE);
            for (int i = lines.size() - 1; i >= 0; i--) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }

                int closingBracket = line.indexOf("] ");
                if (line.startsWith("[") && closingBracket > 0) {
                    return Optional.of(line.substring(closingBracket + 2));
                }
                return Optional.of(line);
            }
            return Optional.empty();
        } catch (java.io.IOException e) {
            return Optional.empty();
        }
    }
}
