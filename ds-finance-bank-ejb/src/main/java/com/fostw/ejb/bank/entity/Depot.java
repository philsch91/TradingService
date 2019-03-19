/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.fostw.dsfinance.bank.common.interfaces.IShare;

/**
 *
 * @author markus
 */
@Entity
@Table(name = "Depot")
public class Depot implements Serializable, IShare {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="DepotID")
    private Long id;
   
    @Column(name="Symbol")
    private String symbol;
 
    @Column(name="Quantity")
    private int quantity;
   
    @Column(name="Price")
    private Double price;
   
    @ManyToOne
    @JoinColumn(name="USERID")
    private Customer customer;
   
    public Depot(){
    }
   
    public Depot(String symbol, int quantity, Double price, Customer customer){
        this.quantity = quantity; 
        this.symbol = symbol; 
        this.price = price;
        this.customer = customer;
    }
    
    @Override
    public Long getId(){
        return this.id;
    }
    
    @Override
    public void setId(Long id){
        this.id=id;
    }
   
    public Customer getCustomer(){
        return this.customer;
    }
        
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     *
     * @return
     */
    @Override
    public int getQuantity(){
        return this.quantity;
    }
        
    /**
     *
     * @param quantity
     */
    @Override
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
        
    @Override
    public String getSymbol(){
        return this.symbol;
    }
        
    @Override
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    @Override
    public Double getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }
    
}

