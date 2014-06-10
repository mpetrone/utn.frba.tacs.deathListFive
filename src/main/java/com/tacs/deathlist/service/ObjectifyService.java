package com.tacs.deathlist.service;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.Work;
import com.tacs.deathlist.domain.Lista;
import com.tacs.deathlist.domain.Usuario;
import com.tacs.deathlist.domain.exception.CustomInternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ObjectifyService {

    public static final String GENERIC_ERROR_MESSAGE = "Tenemos un problema interno, sepa disculpar";

    public static final Logger LOGGER = LoggerFactory.getLogger(ObjectifyService.class);

    private ObjectifyFactory objectifyFactory;

    public ObjectifyService() {
        this.objectifyFactory = new ObjectifyFactory();
        objectifyFactory.register(Usuario.class);
        objectifyFactory.register(Lista.class);
    }

    private Objectify getGaeService() {
        return objectifyFactory.begin();
    }

    public void save(Object entity) {
        try {
            getGaeService().save().entity(entity).now();
        }
        catch (RuntimeException re){
            LOGGER.error("Hubo un problema cuando se quizo guardar una entidad", re);
            throw new CustomInternalServerErrorException(GENERIC_ERROR_MESSAGE);
        }
    }

    public Object load(String id, Class clazz){
        Object entity = null;
        try{
            entity = getGaeService().load().type(clazz).id(id).now();
        }catch (RuntimeException re){
            LOGGER.error("Hubo un problema cuando se quizo cargar una entidad", re);
            throw new CustomInternalServerErrorException(GENERIC_ERROR_MESSAGE);
        }
        return entity;
    }

    public void delete(String id, Class clazz){
        try{
            getGaeService().delete().type(clazz).id(id).now();
        }catch (RuntimeException re){
            LOGGER.error("Hubo un problema cuando se quizo eliminar una entidad", re);
            throw new CustomInternalServerErrorException(GENERIC_ERROR_MESSAGE);
        }
    }

    public void transaction(Work work){
        try{
            getGaeService().transactNew(3, work);
        }catch (RuntimeException re){
            LOGGER.error("Hubo un problema cuando se quizo ejecutar una transaccion", re);
            throw new CustomInternalServerErrorException(GENERIC_ERROR_MESSAGE);
        }
    }
}