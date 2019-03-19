/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.ui.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A4938
 */
public class Menu {
    
    private String title="";
    private ArrayList<String> entries = new ArrayList<String>();
    
    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the entries
     */
    public ArrayList<String> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(ArrayList<String> entries) {
        this.entries = entries;
    }
    
    public Menu(){}
    
    public int insert(String text){
        getEntries().add(text);
        return getEntries().size();
    }
    
    public int remove(int index){
        getEntries().remove(index);
        return getEntries().size();
    }
    
    public int show(){
        System.out.println("\n"+this.getTitle());
        for(int i=0;i<this.getTitle().length();i++){
            System.out.print("-");
        }
        System.out.println("\n");
        
        int j=1;
        for(String s : getEntries()){
            System.out.println(Integer.toString(j)+")\t"+s);
            j++;
        }
        System.out.println("\n");
        BufferedReader inputBuffReader = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String val="\0";
            System.out.print("> ");
            
            try{
                val=inputBuffReader.readLine();
            }catch(IOException ioe){
                System.out.println("Failure: "+ioe.toString()+"\n"+
                        "Please restart the application with CTRL+C");
            }
            if(val.length()>0){
                int inputNumber=Character.getNumericValue(val.charAt(0));
                //if(inputNumber==-1 && Character.toLowerCase(val.charAt(0))=='q'){return 0;}
                for(int k=0;k<getEntries().size();k++){
                    if((k+1)==inputNumber){
                        return inputNumber;
                    }
                }
            }
            System.out.println("Invalid input");
        }
    }
}
