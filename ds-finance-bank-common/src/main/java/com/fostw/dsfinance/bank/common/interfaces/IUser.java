/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.common.interfaces;

/**
 *
 * @author markus
 */
public interface IUser {
    public Long getId();
    public void setId(Long id);
    public String getUserName();
    public void setUserName(String username);
    public String getFirstName();
    public void setFirstName(String firstname);
    public String getLastName();
    public void setLastName(String lastname);
}
