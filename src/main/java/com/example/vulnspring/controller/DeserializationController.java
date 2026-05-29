package com.example.vulnspring.controller;


import com.example.vulnspring.util.DeserializationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InvalidClassException;

@RestController
@RequestMapping("/api/deserialize")
public class DeserializationController {

    @PostMapping(value = "/vulnerable", consumes = "text/plain")
    public ResponseEntity<String> deserializeVulnerable(@RequestBody String base64Payload)
            throws IOException, ClassNotFoundException {
        byte[] bytes = DeserializationUtils.decodeBase64(base64Payload);
        Object obj   = DeserializationUtils.readUnsafe(bytes);
        return ResponseEntity.ok(obj.toString());
    }


    @PostMapping(value = "/secure", consumes = "text/plain")
    public ResponseEntity<String> deserializeSecure(@RequestBody String base64Payload) {
        try {
            byte[] bytes = DeserializationUtils.decodeBase64(base64Payload);
            Object obj   = DeserializationUtils.readFiltered(bytes);
            return ResponseEntity.ok(obj.toString());
        } catch (InvalidClassException ice) {
            String msg = "[SECURITY] Deserialization blocked: " + ice.getMessage();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
        } catch (IllegalArgumentException ex) {
            // invalid Base64
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid payload: " + ex.getMessage());
        } catch (IOException | ClassNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid payload: " + ex.getMessage());
        }
    }

}
