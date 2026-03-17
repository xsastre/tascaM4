package edu.xaviersastre.tascaM4.security;

import edu.xaviersastre.tascaM4.service.StoreMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionService {

    @Autowired
    private StoreMessageService storeMessageService;

    @Value("${app.security.aes-key}")
    private String hexKey;

    private SecretKey getKey() {
        // Convertir hex string a byte array per crear la SecretKey
        byte[] keyBytes = hexStringToByteArray(hexKey);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // IV de 12 bytes per GCM (recomanat)
        new SecureRandom().nextBytes(iv);

        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, getKey(), spec);

        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Empaquetar: IV (12) + Ciphertext (variable)
        // Nota: Java gestiona el Tag d'autenticació internament dins del ciphertext
        byte[] combined = new byte[iv.length + ciphertext.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);

        String encryptedBase64 = Base64.getEncoder().encodeToString(combined);

        // Guardar a data.txt per verificar que el contingut es xifra
        storeMessageService.saveToFile(encryptedBase64);

        return encryptedBase64;
    }

    public String decrypt(String combinedB64) throws Exception {
        byte[] combined = Base64.getDecoder().decode(combinedB64);
        byte[] iv = java.util.Arrays.copyOfRange(combined, 0, 12);
        byte[] ciphertext = java.util.Arrays.copyOfRange(combined, 12, combined.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec);

        return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
    }

    // Utilitat per passar de Hex a Bytes
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
