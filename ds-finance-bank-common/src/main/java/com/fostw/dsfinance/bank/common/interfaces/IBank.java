/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.common.interfaces;

import com.fostw.dsfinance.bank.common.exceptions.InvalidRemoteMethodParameterException;
import com.fostw.dsfinance.bank.common.exceptions.PermissionDeniedException;
import com.fostw.dsfinance.bank.common.models.Share;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author A4938
 */
@Remote
public interface IBank {
    public Long newCustomer(String userName,String password, String firstName, String lastName, String address) throws PermissionDeniedException,Exception;
    public IUser searchCustomer(String username);
    public IUser searchCustomer(Long userId);
    public List<IUser> searchCustomer(String firstname, String lastname) throws Exception;
    public Boolean buyShares(Long userId, String symbol, int shareCount) throws SecurityException,InvalidRemoteMethodParameterException,Exception;
    public Boolean sellShares(Long userId, String symbol, int shareCount) throws SecurityException,InvalidRemoteMethodParameterException,Exception;
    public List<Share> searchShare(String symbolName) throws InvalidRemoteMethodParameterException,Exception;
    public List<IShare> getDepot(Long userId) throws SecurityException,Exception;
    public String getLoginName();
    public IUser getSessionUser();
    public Boolean checkEmployee();
    public BigDecimal getSpielgeld() throws PermissionDeniedException;
}
