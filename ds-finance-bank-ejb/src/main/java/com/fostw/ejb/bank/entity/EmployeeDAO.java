/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;

import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author markus
 */
public class EmployeeDAO extends UserDAO{
    @Override
    public Employee getUser(String ID) {
        return entityManager.find(Employee.class, ID);
    }
    
    public Employee getEmployee(String username){
        System.out.println("get Employee " + username);
        Query query = this.entityManager.createQuery("select e from Employee e "
                        + "where e.userName = :pUsername",Employee.class);
        query.setParameter("pUsername", username);
        List<Employee> elist = query.getResultList();
        if(elist.isEmpty()){
            System.out.println("no user found");
            return null;
        }
        if(elist.size()>1){
            System.out.println("multiple users with same username found");
            return null;
        }
        for(Employee e : elist){
            if(e.getUserName().equals(username)){
                return e;
            }
        }
        return null;
    }
    
    public void persistEmployee(Employee employee){
        this.entityManager.merge(employee);
    }
    
    public void deleteEmployee(Employee employee){
        this.entityManager.remove(employee);
    }
}
