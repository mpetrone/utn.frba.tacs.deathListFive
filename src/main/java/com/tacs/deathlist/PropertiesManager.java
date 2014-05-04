package com.tacs.deathlist;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesManager {

    private Properties properties;

    public PropertiesManager() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/com/tacs/deathlist/properties/deathlist.properties"));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar las propiedades", e);
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}

