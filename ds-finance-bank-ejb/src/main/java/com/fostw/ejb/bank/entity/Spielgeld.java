
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *
 * @author markus
 */
@Entity
@Table(name = "Spielgeld")
public class Spielgeld implements Serializable{

   @Id
   private String id = "1";
   
   @Column(name = "MONEY")
   private BigDecimal money;
   
   public Spielgeld(){
   }
   
   public Spielgeld(BigDecimal money){
       this.money = money;
   }
   
   public BigDecimal getMoney(){
       return this.money;
   }
   
   public void setMoney(BigDecimal money){
       this.money = money;
   }

   /*@Override
    public String getId() {
        return id;
    }*/

    /*@Override
    public void setId(String id) {
        //this.id = id;
    }*/
    
}

  
