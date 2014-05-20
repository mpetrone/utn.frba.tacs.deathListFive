package com.tacs.deathlist;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.core.Application;
import java.io.FileInputStream;
import java.util.Properties;

public class DeathListTest extends JerseyTest {
    
    private static final String RESOURCES_PATH = "/src/main/resources/com/tacs/deathlist/";
    
    @Override
    protected Application configure() {
        // Para loguear con mas detalle
        //enable(TestProperties.LOG_TRAFFIC);
        //enable(TestProperties.DUMP_ENTITY);
        return new ApplicationConfiguration();
    }
    
    @Override
    protected void configureClient(ClientConfig config) {
        config.loadFrom(new ApplicationConfiguration());
    }
    
    @Override
    public TestContainerFactory getTestContainerFactory() {
        configureLog4j();
        return new InMemoryTestContainerFactory();
    }
    
    /**
     * Configura log4j en el sistema, lo necesita para el logueo de los tests.
     */
    private void configureLog4j()  {
        String log4JPropertyFile = System.getProperty("user.dir")
                + RESOURCES_PATH + "properties/log4j.properties";
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(log4JPropertyFile));
        } catch (Exception e) {
            System.out.append("Problema al configurar el log4j.properties");
            e.printStackTrace();
        }
        PropertyConfigurator.configure(p);
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

}
