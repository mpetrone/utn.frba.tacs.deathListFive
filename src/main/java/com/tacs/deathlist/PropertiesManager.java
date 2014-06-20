package com.tacs.deathlist;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * Esta clase permite definir en archivos de texto determinadas
 * propiedades o parámetros de la aplicación y/o de los frameworks
 * utilizados. Se utiliza para el App Id y el App Secret de Facebook,
 * y para detalles del framework de logs.
 *
 */

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

