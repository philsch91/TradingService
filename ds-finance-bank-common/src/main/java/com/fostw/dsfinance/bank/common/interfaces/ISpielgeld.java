/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.common.interfaces;

import java.math.BigInteger;

/**
 *
 * @author markus
 */
public interface ISpielgeld {
   public BigInteger getMoney(); 
   public void setMoney(BigInteger money);
   //public String getId();
   //public void setId(String id);
}