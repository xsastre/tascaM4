package edu.xaviersastre.tascaM4.controller;

import edu.xaviersastre.tascaM4.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/aes/secure_api")
public class SecureApiController {

    @Autowired
    private EncryptionService encryptionService;

    private String storedData = ""; // Simulació de persistència

    @PostMapping
    public ResponseEntity<Map<String, String>> saveData(@RequestBody Map<String, String> payload) {
        try {
            String text = payload.get("text");
            // Xifrar abans de guardar (Com a secure_api.php)
            storedData = encryptionService.encrypt(text);
            return ResponseEntity.ok(Map.of("message", "Texto cifrado y almacenado"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getData() {
        try {
            if (storedData.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "No hay mensajes"));
            }
            // Desxifrar abans de retornar
            String decrypted = encryptionService.decrypt(storedData);
            return ResponseEntity.ok(Map.of("plaintext", decrypted));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Desencriptación fallida"));
        }
    }
}
