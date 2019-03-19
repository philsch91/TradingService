/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.dsfinance.bank.ui.console;

import com.fostw.dsfinance.bank.common.exceptions.InvalidRemoteMethodParameterException;
import com.fostw.dsfinance.bank.common.exceptions.PermissionDeniedException;
import com.fostw.dsfinance.bank.common.interfaces.IBank;
import com.fostw.dsfinance.bank.common.interfaces.IUser;
import com.fostw.dsfinance.bank.common.models.Share;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.froihofer.dsfinance.bank.client.BankClient;
import com.fostw.dsfinance.bank.common.interfaces.IShare;

/**
 *
 * @author A4938
 */
public class ConsoleUI {    
    
    private IBank bank = null;
    
    public ConsoleUI(IBank bank){
        this.bank=bank;
    }
    
    public IBank getBank() {
        return bank;
    }

    public void setBank(IBank bank) {
        this.bank = bank;
    }
    
    public void show(){
        //TODO: create menu depending on user type
        Menu menu = new Menu();
        String title = "Bank Client Main Menu";
        String username = bank.getLoginName();
        title=title + System.lineSeparator() + "User: " + username;
        menu.setTitle(title);
        menu.insert("Search Stock");
        menu.insert("Buy Stock");
        menu.insert("Sell Stock");
        menu.insert("Show Depot");
        if(bank.checkEmployee()){
            menu.insert("Search Customer");
            menu.insert("Create Customer");
            menu.insert("Show Bank Volume");
        }
        menu.insert("Exit");
        int option;
        while((option=menu.show())!=menu.getEntries().size()){
            if(option==1){
                this.searchShareSubMenu();
            }else if(option==2){
                if(bank.checkEmployee()){
                    this.buyShareEmployeeSubMenu();
                }else{
                    this.buyShareSubMenu();
                }
            }else if(option==3){
                if(this.bank.checkEmployee()){
                    this.sellShareEmployeeSubMenu();
                }else{
                    this.sellShareSubMenu();
                }
            }else if(option==4){
                if(bank.checkEmployee()){
                    this.showDepotEmployee();
                }else{
                    this.showDepotCustomer();
                }
            }else if(option==5){
                this.searchCustomerSubMenu();
            }else if(option==6){
                this.newCustomerSubMenu();
            }else if(option==7){
                this.showBankVolume();
            }
        }
        
    }
    
    private void newCustomerSubMenu(){
        System.out.println("New Customer:");
        //String s = new TextBox("ID", TextBoxInputType.NUMBER).show();
        String username = new TextBox("Username").show();
        
        IUser user=null;
        try {
            user = bank.searchCustomer(username);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server error");
            return;
        }
        
        if(user!=null){
            System.out.println("Username exists already");
            //TODO: separate function and return early
            return;
        }
        
        String password = new TextBox("Password").show();
        
        String firstname = new TextBox("Firstname").show();
        String lastname = new TextBox("Lastname").show();
        String address = new TextBox("Address").show();
        
        Long userId;
        try {
            userId=this.bank.newCustomer(username, password, firstname, lastname, address);
        } catch (PermissionDeniedException pex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, pex);
            System.out.println(pex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server error");
            return;
        }
        System.out.println("User ID for " + firstname + " "+ lastname + ": " + Long.toString(userId));
    }
    
    private void searchCustomerSubMenu(){
        String firstname = new TextBox("Firstname").show();
        String lastname = new TextBox("Lastname").show();
        List<IUser> customers = null;
        try {
            customers=this.bank.searchCustomer(firstname, lastname);
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customers == null){
            System.out.println("no user found");
            return;
        }
        List<Object> users = new ArrayList<>();
        for(IUser customer : customers){
            users.add((Object)customer);
        }
        ObjectListView listView = new ObjectListView();
        listView.setItems(users);
        listView.show();
    }
    
    private void searchShareSubMenu(){
        String name="";
        //company or symbol name
        name = new TextBox("Share name").show();
        if(name.equals("")){
            System.out.println("share name must not be empty");
            return;
        }
        List<Share> shares = new ArrayList<>();
        try {
            shares = this.bank.searchShare(name);
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        for(Share share : shares){
            System.out.println(share.toString());
        }*/
        ObjectListView listView = new ObjectListView();
        for(Share s : shares){
            listView.addItem(s);
        }
        listView.show();
    }
    
    private void buyShareEmployeeSubMenu(){
        System.out.println("Buy Stock");
        String firstname = new TextBox("Firstname").show();
        String lastname = new TextBox("Lastname").show();
        List<IUser> customers = null;
        try {
            customers=this.bank.searchCustomer(firstname, lastname);
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customers == null){
            System.out.println("no user found");
            return;
        }
        List<Object> users = new ArrayList<>();
        for(IUser customer : customers){
            users.add((Object)customer);
        }
                
        SelectionMenu userSelection = new SelectionMenu();
        userSelection.setOptions(users);
        IUser user = (IUser)userSelection.show();
        System.out.println(user.toString());
        
        //TODO: call this.buyShare(user);
                
        String searchText = new TextBox("Search Share").show();
        List<Share> shareList = new ArrayList<>();
        try {
            shareList = this.bank.searchShare(searchText);
        }catch (InvalidRemoteMethodParameterException iex) {
            //Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, iex);
            System.out.println(iex.getMessage());
            return;
        }catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        SelectionMenu shareMenu = new SelectionMenu();
        for(Share s : shareList){
            shareMenu.addOption(s);
        }
        Share share = (Share)shareMenu.show();
                
        String cnt = new TextBox("Count", TextBoxInputType.NUMBER).show();
        int shareCount = Integer.parseInt(cnt);
                
        try {
            this.bank.buyShares(user.getId(),share.getSymbol(),shareCount);
        } catch (SecurityException secex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, secex);
            System.out.println(secex.getMessage());
            return;
        } catch (InvalidRemoteMethodParameterException iex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, iex);
            System.out.println(iex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("general error occured");
        }
        BigDecimal bankMoney;
        try {
            bankMoney = this.bank.getSpielgeld();
        } catch (PermissionDeniedException ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        System.out.println("Bank Money: " + bankMoney.toString());
    }
    
    private void buyShareSubMenu(){
        System.out.println("Buy Stock");
        IUser user = this.bank.getSessionUser();
        this.buyShare(user);
    }
    
    private void buyShare(IUser user){
        String searchText = new TextBox("Search Share").show();
        List<Share> shareList = new ArrayList<>();
        try {
            shareList = this.bank.searchShare(searchText);
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        SelectionMenu shareMenu = new SelectionMenu();
        for(Share s : shareList){
            shareMenu.addOption(s);
        }
        Share share = (Share)shareMenu.show();
                
        String cnt = new TextBox("Count", TextBoxInputType.NUMBER).show();
        int shareCount = Integer.parseInt(cnt);
                
        try {
            this.bank.buyShares(user.getId(),share.getSymbol(),shareCount);
        } catch (SecurityException secex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, secex);
            System.out.println(secex.getMessage());
            return;
        } catch (InvalidRemoteMethodParameterException iex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, iex);
            System.out.println(iex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void sellShareEmployeeSubMenu(){
        System.out.println("Sell Stock");
        String firstname = new TextBox("Firstname").show();
        String lastname = new TextBox("Lastname").show();
        List<IUser> customers = null;
        try {
            customers=this.bank.searchCustomer(firstname, lastname);
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(customers == null){
            System.out.println("no user found");
            return;
        }
        List<Object> users = new ArrayList<>();
        for(IUser customer : customers){
            users.add((Object)customer);
        }
                
        SelectionMenu userSelection = new SelectionMenu();
        userSelection.setOptions(users);
        IUser user = (IUser)userSelection.show();
        System.out.println(user.toString());
        //TODO: call this.sellShare(user);
        //String searchText = new TextBox("Search Share").show();
        List<IShare> shareList = new ArrayList<>();
        try {
            //shareList = this.bank.searchShare(searchText);
            shareList = this.bank.getDepot(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SelectionMenu shareMenu = new SelectionMenu();
        for(IShare s : shareList){
            shareMenu.addOption(s);
        }
        Share share = (Share)shareMenu.show();
        
        System.out.println("Stock to sell:" + share.toString());
                
        String cnt = new TextBox("Count", TextBoxInputType.NUMBER).show();
        int shareCount = Integer.parseInt(cnt);
        
        if(shareCount<0 || shareCount>share.getQuantity()){
            System.out.println("invalid share count");
            return;
        }
        
        try {
            this.bank.sellShares(user.getId(), share.getSymbol(), shareCount);
        } catch (SecurityException secex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, secex);
            System.out.println(secex.getMessage());
            return;
        } catch (InvalidRemoteMethodParameterException iex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, iex);
            System.out.println(iex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            shareList = this.bank.getDepot(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(IShare s : shareList){
            if(s.getSymbol().equals(share.getSymbol())){
                System.out.println("new share count for " + s.getSymbol() + ": " + s.getQuantity());
            }
        }
    }
    
    private void sellShareSubMenu(){
        System.out.println("Sell Stock");
        IUser user = this.bank.getSessionUser();
        this.sellShare(user);
    }
    
    private void sellShare(IUser user){
        List<IShare> shareList = new ArrayList<>();
        try {
            //shareList = this.bank.searchShare(searchText);
            shareList = this.bank.getDepot(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        SelectionMenu shareMenu = new SelectionMenu();
        for(IShare s : shareList){
            shareMenu.addOption(s);
        }
        Share share = (Share)shareMenu.show();
        
        System.out.println("Stock to sell:" + share.toString());
                
        String cnt = new TextBox("Count", TextBoxInputType.NUMBER).show();
        int shareCount = Integer.parseInt(cnt);
        
        if(shareCount<0 || shareCount>share.getQuantity()){
            System.out.println("invalid share count");
            return;
        }
        
        try {
            this.bank.sellShares(user.getId(), share.getSymbol(), shareCount);
        } catch (SecurityException secex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, secex);
            System.out.println(secex.getMessage());
            return;
        } catch (InvalidRemoteMethodParameterException iex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, iex);
            System.out.println(iex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            shareList = this.bank.getDepot(user.getId());
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(IShare s : shareList){
            if(s.getSymbol().equals(share.getSymbol())){
                System.out.println("new share count for " + s.getSymbol() + ": " + s.getQuantity());
            }
        }
    }
    
    private void showDepotCustomer(){
        System.out.println("Show Depot");
        //String username = this.bank.getLoginName();
        IUser user = this.bank.getSessionUser();
        //TODO: calls this.showDepot()
        List<IShare> shareList = new ArrayList<>();
        try {
            shareList=this.bank.getDepot(user.getId());
        } catch (SecurityException secex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, secex);
            System.out.println(secex.getMessage());
            return;
        } catch (Exception ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Share> displayedShares = new ArrayList<>();
        
        for(IShare s : shareList){
            Share share = new Share();
            share.setId(s.getId());
            share.setSymbol(s.getSymbol());
            share.setPrice(s.getPrice());
            share.setQuantity(s.getQuantity());
            
            List<Share> shareDetailList=null;
            try {
                shareDetailList = this.bank.searchShare(s.getSymbol());
            } catch (Exception ex) {
                Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(shareDetailList!=null && shareDetailList.size()==1){
                share.setCompany(shareDetailList.get(0).getCompany());
                share.setCurrentPrice(shareDetailList.get(0).getPrice());
            }
            displayedShares.add(share);
        }
        
        //SelectionMenu listView = new SelectionMenu();
        ObjectListView listView = new ObjectListView();
        for(Share s : displayedShares){
            listView.addItem(s);
        }
        listView.show();
        
        double sum=0;
        int i=0;
        for(Share s : displayedShares){
            Share currentShare = displayedShares.get(i);
            Double douPricePerCompany=currentShare.getPrice()*currentShare.getQuantity();
            BigDecimal decPricePerCompany=BigDecimal.valueOf(douPricePerCompany);
            System.out.println("Value for Shares of " + currentShare.getCompany() + ": " +decPricePerCompany.toString());
            sum=s.getPrice()*s.getQuantity();
            i++;
        }
        System.out.println("Depot Value: " + Double.toString(sum));
    }
    
    private void showDepotEmployee(){
        System.out.println("Show Depot");
        String firstname = new TextBox("Firstname").show();
        String lastname = new TextBox("Lastname").show();
        List<IUser> customers = null;
        try {
            customers=this.bank.searchCustomer(firstname, lastname);
        } catch (Exception ex) {
            //Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return;
        }
        if(customers == null){
            System.out.println("no user found");
            return;
        }
        List<Object> users = new ArrayList<>();
        for(IUser customer : customers){
            users.add((Object)customer);
        }
                
        SelectionMenu userSelection = new SelectionMenu();
        userSelection.setOptions(users);
        IUser user = (IUser)userSelection.show();
        System.out.println(user.toString());
        
        this.showDepot(user);
    }
    
    private void showDepot(IUser user){
        List<IShare> shareList = new ArrayList<>();
        try {
            shareList=this.bank.getDepot(user.getId());
        } catch (Exception ex) {
            //Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            return;
        }
        
        List<Share> displayedShares = new ArrayList<>();
        
        for(IShare s : shareList){
            Share share = new Share();
            share.setId(s.getId());
            share.setSymbol(s.getSymbol());
            share.setPrice(s.getPrice());
            share.setQuantity(s.getQuantity());
            
            //search and add current price and company name for saved shares in Depot
            List<Share> shareDetailList=null;
            try {
                shareDetailList = this.bank.searchShare(s.getSymbol());
            } catch (Exception ex) {
                //Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                return;
            }
            if(shareDetailList!=null && shareDetailList.size()==1){
                share.setCompany(shareDetailList.get(0).getCompany());
                share.setCurrentPrice(shareDetailList.get(0).getPrice());
            }
            displayedShares.add(share);
        }
        
        //SelectionMenu listView = new SelectionMenu();
        ObjectListView listView = new ObjectListView();
        for(Share s : displayedShares){
            listView.addItem(s);
        }
        listView.show();
        
        double sum=0;
        int i=0;
        for(Share s : displayedShares){
            Share currentShare = displayedShares.get(i);
            Double douPricePerCompany=currentShare.getPrice()*currentShare.getQuantity();
            BigDecimal decPricePerCompany=BigDecimal.valueOf(douPricePerCompany);
            System.out.println("Value for Shares of " + currentShare.getCompany() + ": " +decPricePerCompany.toString());
            sum=s.getPrice()*s.getQuantity();
            i++;
        }
        System.out.println("Depot Value: " + Double.toString(sum));
    }
    
    private void showBankVolume(){
        //TODO: BankService Call
        BigDecimal spielgeld;
        try {
            spielgeld = bank.getSpielgeld();
        } catch (PermissionDeniedException ex) {
            Logger.getLogger(ConsoleUI.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        System.out.println("Bank Volume: " + spielgeld +" $");
    }
    
}
