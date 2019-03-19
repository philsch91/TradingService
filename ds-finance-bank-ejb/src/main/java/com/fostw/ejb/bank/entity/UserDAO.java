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
public abstract class UserDAO implements IUserDAO{
    @PersistenceContext
    protected EntityManager entityManager;
    //refer to
    //https://docs.spring.io/spring/docs/3.0.x/spring-framework-reference/html/orm.html#orm-jpa-straight
    
    @Override
    public Object getUser(String ID){
        return entityManager.find(Object.class, ID);
    }
    
    @Override
    public void deleteUser(Object user){
        entityManager.remove(user);
    }
    
    @Override
    public void persistUser(Object user){
        entityManager.merge(user);
    }
}
