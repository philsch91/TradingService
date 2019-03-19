/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.ui.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author A4938
 */
public class TextBox {
    private String title="";
    private TextBoxInputType type = TextBoxInputType.TEXT;
    public TextBox(String title){
        this.title=title;
    }
    
    public TextBox(String title, TextBoxInputType type){
        this.title=title;
        this.type=type;
    }
    
    public String show(){
        System.out.print(this.title + ": ");
        String input="";
        input=this.readLine();
        if(this.type==TextBoxInputType.NUMBER){
            try{
                int i=Integer.parseInt(input);
                input=Integer.toString(i);
            }catch(NumberFormatException ne){
                System.out.println("Input must be a number");
                input = this.show();
            }
        }
        return input;
    }
    
    private String readLine(){
        String value = "\0";
        BufferedReader breader = new BufferedReader(new InputStreamReader(System.in));
        try{
            value = breader.readLine();
        }catch(IOException e){
            System.out.println(e.getMessage());
            return "";
        }
        return value.trim();
    }
}
