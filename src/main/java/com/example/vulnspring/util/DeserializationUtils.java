package com.example.vulnspring.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.util.Base64;

public final class DeserializationUtils {
    public static final String SAFE_FILTER_PATTERN =
            "maxdepth=10;maxarray=500;maxrefs=100;maxbytes=50000;"
                    + "com.example.vulnspring.model.UserProfile;"
                    + "java.lang.Number;"
                    + "java.lang.Long;"
                    + "java.lang.String;"
                    + "!*";
    private static final ObjectInputFilter SAFE_FILTER =
            ObjectInputFilter.Config.createFilter(SAFE_FILTER_PATTERN);

    private DeserializationUtils() { }


    public static byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64.trim());
    }


    public static Object readUnsafe(byte[] bytes)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }


    public static Object readFiltered(byte[] bytes)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois =
                     new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            ois.setObjectInputFilter(SAFE_FILTER);
            return ois.readObject();
        }
    }
}
