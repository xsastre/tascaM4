package edu.xaviersastre.tascaM4.security;

import edu.xaviersastre.tascaM4.service.StoreMessageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NoEncryptionService {

    @Autowired
    private StoreMessageService storeMessageService;

    public String noencrypt(String plaintext) {
        // Guardar a data.txt per verificar que el contingut NO es xifra
        storeMessageService.saveToFile(plaintext);

        return plaintext;
    }
}
