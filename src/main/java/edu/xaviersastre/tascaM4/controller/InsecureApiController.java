package edu.xaviersastre.tascaM4.controller;

import java.util.Map;

import edu.xaviersastre.tascaM4.security.NoEncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/aes/insecure_api")
public class InsecureApiController {

    @Autowired
    private NoEncryptionService noEncryptionService;

    private String storedData = ""; // Simulació de persistència (sense xifrat)

    @PostMapping
    public ResponseEntity<Map<String, String>> saveData(@RequestBody Map<String, String> payload) throws Exception {
        try {
            String text = payload.get("text");
            String notencrypted = noEncryptionService.noencrypt(text);
            storedData = notencrypted;
            return ResponseEntity.ok(Map.of("message", "Texto almacenado en claro"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getData() throws Exception {
        try {
            if (storedData.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "No hay mensajes"));
            }
            return ResponseEntity.ok(Map.of("plaintext", storedData));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
