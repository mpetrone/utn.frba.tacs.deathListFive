package com.tacs.deathlist;

/**
 * 
 * Interface para todos los builders que ayuden a construir objectos en los tests.
 * 
 * @author matias.petrone
 *
 */
public interface Builder<E> {
    
    E build();
}
