/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author markus
 */
public class SpielgeldDAO {
    @PersistenceContext
    protected EntityManager entityManager;
    
    public void setEntityManager(EntityManager manager){
        this.entityManager = manager;
    }
    
    /**
     *
     * @param ID
     * @return
     */
    public Spielgeld getSpielgeld(String ID) {
        return entityManager.find(Spielgeld.class, ID);
    }
    
  
    public void deleteSpielgeld (String ID){
        entityManager.remove(ID);
    }
    

    public void mergeSpielgeld(Spielgeld spielgeld){
        entityManager.merge(spielgeld);
    }
    
     public void persistSpielgeld(Spielgeld spielgeld){
        entityManager.persist(spielgeld);
    }
}