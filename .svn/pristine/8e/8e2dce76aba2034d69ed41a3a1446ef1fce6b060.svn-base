/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.ui.console;

import com.fostw.dsfinance.bank.Credential;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author A4938
 */
public class LoginForm {
        
    
    public Credential show(){
        BufferedReader inputBuffReader = new BufferedReader(new InputStreamReader(System.in));
        String username="\0";
        
        System.out.print("Username: ");
        try{
            username=inputBuffReader.readLine();
        }catch(IOException ioe){
            System.out.println("Failure: "+ioe.toString()+"\n"+"Please restart the application with CTRL+C");
        }
        
        if(!this.validateLoginInput(username)){
            return null;
        }
        
        String password="\0";
        
        System.out.print("Password: ");
        try{
            password=inputBuffReader.readLine();
        }catch(IOException ioe){
            System.out.println("Failure: "+ioe.toString()+"\n"+"Please restart the application with CTRL+C");
        }
        
        if(!this.validateLoginInput(password)){
            return null;
        }
        
        Credential cred = new Credential();
        cred.setUsername(username);
        cred.setPassword(password);
        
        return cred;
    }
    
    private String readInput(String inputName){
        BufferedReader inputBuffReader = new BufferedReader(new InputStreamReader(System.in));
        String val="\0";
        while(true){
            System.out.println(inputName + ": ");
            try{
                val=inputBuffReader.readLine();
            }catch(IOException ioe){
                System.out.println("Failure: "+ioe.toString()+"\n"+"Please restart the application with CTRL+C");        
            }
            if(this.validateLoginInput(val)){
               return val; 
            }
            System.out.println("invalid input");
        }
    }
    
    private boolean validateLoginInput(String input){
        input=input.trim();
        if(input.length()==0){
            return false;
        }
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(input);
        
        return !matcher.find();
    }
    
}
