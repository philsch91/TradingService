/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.common.exceptions;

/**
 *
 * @author A4938
 */
public class PermissionDeniedException extends Exception{
    public PermissionDeniedException(){
        super("permission denied");
    }
    
    public PermissionDeniedException(String message){
        super(message);
    }
}
