/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;

/**
 *
 * @author markus
 */

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.persistence.Query;


public class DepotDAO implements IDepotDAO{
    @PersistenceContext
    private EntityManager entityManager;
       
    @Override
    public Depot getDepot(String ID) {
        return this.entityManager.find(Depot.class, ID);
    }
    
    @Override
    public void persist(Depot depot){
        this.entityManager.persist(depot);
    }
    
    @Override
    public Depot merge(Depot depot){
        return this.entityManager.merge(depot);
    }
    
    /**
     *
     * @param depot
     */
    @Override
    public void deleteDepot(Depot depot){
        this.entityManager.remove(depot);
    }
    
    /**
     *
     * @param id
     * @param symbol
     * @return
     */
    public Depot getDepotUserSymbol(Long id,String symbol) {
        //List<Depot> depotList = null;
        Depot depot = null;
        try {
            //Depot depot = entityManager.createQuery("SELECT a FROM Depot a WHERE a.KundenID.id = " + id + " AND a.Symbol = :symbol", Depot.class).setParameter("symbol", symbol).getSingleResult();
            Query query = this.entityManager.createQuery("SELECT a FROM Depot a WHERE a.customer.id = :pUserId AND a.symbol = :symbol",Depot.class);
            query.setParameter("pUserId", id);
            query.setParameter("symbol", symbol);
            //depotList = query.getResultList();
            depot = (Depot)query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return depotList;
        return depot;
   }
   //Find customer's shares -> returns List with Shares
   
   public List<Depot> getUserDepot(Long id){
       return this.entityManager.createQuery("SELECT a FROM Depot a Where a.customer.id =:pUserId", Depot.class)
               .setParameter("pUserId", id).getResultList();
   }
    
    
}
    