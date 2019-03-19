/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fostw.ejb.bank;

import com.fostw.dsfinance.bank.common.exceptions.InvalidRemoteMethodParameterException;
import com.fostw.dsfinance.bank.common.exceptions.PermissionDeniedException;
import com.fostw.dsfinance.bank.common.interfaces.*;
import com.fostw.dsfinance.bank.common.models.Share;
import com.fostw.dsfinance.bank.common.models.User;
import com.fostw.ejb.Log;
import com.fostw.ejb.bank.entity.Customer;
import com.fostw.ejb.bank.entity.CustomerDAO;
import com.fostw.ejb.bank.entity.Depot;
import com.fostw.ejb.bank.entity.DepotDAO;
import com.fostw.ejb.bank.entity.Employee;
import com.fostw.ejb.bank.entity.EmployeeDAO;
import com.fostw.ejb.bank.entity.Spielgeld;
import com.fostw.ejb.bank.entity.SpielgeldDAO;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.ws.BindingProvider;
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;
import net.froihofer.dsfinance.ws.trading.TradingWSException_Exception;
import net.froihofer.dsfinance.ws.trading.TradingWebService;
import net.froihofer.dsfinance.ws.trading.TradingWebServiceService;
import net.froihofer.util.jboss.WildflyAuthDBHelper;

/**
 *
 * @author A4938
 */
@Stateless(name="BankService")
@PermitAll
public class Bank implements IBank {
    @Resource
    SessionContext context;
    TradingWebService webservice;
    
    @Inject
    SpielgeldDAO spielgeldDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    DepotDAO depotDAO;
    @Inject
    EmployeeDAO employeeDAO;
    
    @PostConstruct
    private void initBank(){
        this.initSpielgeld();
        this.createFirstEmployee();
        this.initTradingService();
    }
    
    private void initSpielgeld(){
        System.out.println("initSpielgeld");
        /*
        Log log = Log.Open("C:\\logs\\bank.log");
        log.write("initSpielgeld");
        */
        
        Spielgeld sg = null;
        if((sg=spielgeldDAO.getSpielgeld("1"))==null){
            System.out.println("set initial Spielgeld value");
            Spielgeld spielgeld = new Spielgeld();
            spielgeld.setMoney(BigDecimal.valueOf(1000000000));
            spielgeldDAO.persistSpielgeld(spielgeld);
            sg=spielgeldDAO.getSpielgeld("1");
        }
        System.out.println("Bank Spielgeld: " + sg.getMoney().toString());
        
    }
    
    private void createFirstEmployee(){
        System.out.println("createFirstEmployee");
        Employee e = new Employee();
        e.setId(Long.getLong("1"));
        e.setUserName("manager");
        e.setFirstName("manager");
        e.setLastName("manager");
        Employee foundEmployee=this.employeeDAO.getEmployee(e.getUserName());
        if(foundEmployee==null){
            System.out.println("persist employee with username " + e.getUserName());
            this.employeeDAO.persistEmployee(e);
        }
        
        WildflyAuthDBHelper authHelper = new WildflyAuthDBHelper(new File("C:\\Program Files\\NetBeans 8.2\\wildfly-10.1.0.Final"));
        try {
            authHelper.addUser("manager", "manager", new String[0]);
        } catch (IOException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initTradingService(){
        TradingWebServiceService tradingWebService=null;
        try {
            tradingWebService = new TradingWebServiceService(new URL("http://edu.dedisys.org/ds-finance/ws/TradingService?wsdl"));
            this.webservice = (TradingWebService)tradingWebService.getTradingWebServicePort();
        } catch (MalformedURLException mex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, mex);
        } catch (NullPointerException nex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, nex);
        }
        
        BindingProvider bindingProvider = (BindingProvider) this.webservice;
        bindingProvider.getRequestContext()
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://edu.dedisys.org/ds-finance/ws/TradingService");
        bindingProvider.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "ittb19bb_06");
        bindingProvider.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "teeD8shu");
    }
    
    @PermitAll
    @Override
    public Long newCustomer(String username, String password, String firstname, String lastname, String address) throws PermissionDeniedException,Exception{
        if(!this.checkEmployee()){
            System.out.println("only employees are allowed to create a new customer");
            //return null;
            throw new PermissionDeniedException("only employees are allowed to create a new customer");
        }
        //TODO: check if username already exists
        Customer customer = new Customer();
        customer.setUserName(username);
        customer.setFirstName(firstname);
        customer.setLastName(lastname);
        customer.setAddress(address);
        this.customerDAO.persistCustomer(customer);
        
        WildflyAuthDBHelper authHelper = new WildflyAuthDBHelper(new File("C:\\Program Files\\NetBeans 8.2\\wildfly-10.1.0.Final"));
        try {
            authHelper.addUser(username, password, new String[0]);
        } catch (IOException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        customer = this.customerDAO.getCustomer(username);
        
        return customer.getId();
    }
    
    @PermitAll
    @Override
    public IUser searchCustomer(String userName){
        Customer customer = this.customerDAO.getCustomer(userName);
        if(customer==null){
            return null;
        }
        User user = new User();
        user.setId(customer.getId());
        user.setUserName(customer.getUserName());
        user.setFirstName(user.getFirstName());
        user.setLastName(customer.getLastName());
        user.setAddress(customer.getAddress());
        
        return user;
    }
    
    @PermitAll
    @Override
    public IUser searchCustomer(Long userId){
        Customer customer = this.customerDAO.getCustomer(userId);
        if(customer==null){
            return null;
        }
        User user = new User();
        user.setId(customer.getId());
        user.setUserName(customer.getUserName());
        user.setFirstName(user.getFirstName());
        user.setLastName(customer.getLastName());
        user.setAddress(customer.getAddress());
        
        return user;
    }
    
    @PermitAll
    @Override
    public List<IUser> searchCustomer(String firstname, String lastname) throws Exception{
        List<Customer> customerList = this.customerDAO.findCustomer(firstname, lastname);
        if(customerList == null){
            return null;
        }
        List<IUser> customers = new ArrayList<>(customerList.size());
        for(Customer c : customerList){
            IUser user = new User();
            user.setId(c.getId());
            user.setUserName(c.getUserName());
            user.setFirstName(c.getFirstName());
            user.setLastName(c.getLastName());
            //user.setAddress(c.getAddress());
            customers.add(user);
        }
        return customers;
    }
    
    @PermitAll
    @Override
    public Boolean buyShares(Long userId, String symbol, int shareCount) throws SecurityException,InvalidRemoteMethodParameterException,Exception{
        Customer customer = this.customerDAO.getCustomer(userId);
        if(customer==null){
            System.out.println("no user for provided user id found");
            return false;
            //throw new Exception("no user for provided user id found");
        }
        
        if(!this.checkEmployee() && !customer.getUserName().equals(this.getLoginName())){
            //TODO: throw Exception
            System.out.println("user id does not match context user");
            //return false;
            throw new SecurityException("user id does not match context user");
        }
        
        if(shareCount<=0){
            System.out.println("count of shares must be greater than 0");
            //return false;
            throw new InvalidRemoteMethodParameterException("count of shares must be greater than 0");
        }
        
        List<PublicStockQuote> psqList  = new ArrayList<>();    //could be set to null
        //psqList = this.webservice.findStockQuotesByCompanyName(symbol);
        List<String> symbolList = new ArrayList<>();
        symbolList.add(symbol);
        psqList = this.webservice.getStockQuotes(symbolList);
        if(psqList.isEmpty()){
            System.out.println("no shares for provided symbol name found");
            return false;
        }
        
        String lastSymbol = psqList.get(0).getSymbol();
        for(PublicStockQuote psq : psqList){
            System.out.println(psq.getCompanyName()+ " " +psq.getSymbol());
            if(!psq.getSymbol().equals(lastSymbol)){
                System.out.println("no unique symbol provided");
                return false;
            }
            lastSymbol=psq.getSymbol();
        }
        
        //Double price = 0.0;
        //Double price = psqList.get(0).getLastTradePrice().doubleValue();
        
        BigDecimal pricePerShare;
        try{
            pricePerShare = this.webservice.buy(symbol, shareCount);
        }catch(TradingWSException_Exception tex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, tex);
            return false;
        }
        
        Depot depot = new Depot();
        depot.setSymbol(symbol);
        depot.setQuantity(shareCount);
        depot.setPrice(pricePerShare.doubleValue());
        
        Depot persDepot = this.depotDAO.getDepotUserSymbol(userId, symbol);
        if(persDepot!=null){
            int shareSum = persDepot.getQuantity()+shareCount;
            Double updatedPrice = ((persDepot.getPrice()*persDepot.getQuantity())+(pricePerShare.doubleValue())*shareCount)/shareSum;
            persDepot.setQuantity(shareSum);
            persDepot.setPrice(updatedPrice);
            depot=persDepot;
            //this.depotDAO.persist(persDepot);
            //return true;
        }
        
        System.out.println("Customer ID: " + customer.getId());
        
        customer.addDepot(depot);
        this.customerDAO.persistCustomer(customer);
        
        depot.setCustomer(customer);
        this.depotDAO.persist(depot);
        
        //BigDecimal tradingVolume = BigDecimal.valueOf(pricePerShare.doubleValue()*shareCount);
        BigDecimal tradingVolume = pricePerShare.multiply(new BigDecimal(shareCount));
        this.decreaseBankVolume(tradingVolume);
        
        return true;
    }
    
    @Override
    public Boolean sellShares(Long userId, String symbol, int shareCount) throws SecurityException,InvalidRemoteMethodParameterException,Exception{
        Customer customer = this.customerDAO.getCustomer(userId);
        if(customer==null){
            System.out.println("no user for provided user id found");
            return false;
        }
        
        if(!this.checkEmployee() && !customer.getUserName().equals(this.getLoginName())){
            //TODO: throw Exception
            System.out.println("user id does not match context user");
            //return false;
            throw new SecurityException("user id does not match context user");
        }
        
        if(shareCount<=0){
            System.out.println("count of shares must be greater than 0");
            //return false;
            throw new InvalidRemoteMethodParameterException("count of shares must be greater than 0");
        }
        
        List<PublicStockQuote> psqList  = new ArrayList<>();    //could be set to null
        //psqList = this.webservice.findStockQuotesByCompanyName(symbol);
        List<String> symbolList = new ArrayList<>();
        symbolList.add(symbol);
        psqList = this.webservice.getStockQuotes(symbolList);
        if(psqList.isEmpty()){
            System.out.println("no shares for provided symbol name found");
            return false;
        }
        
        String lastSymbol = psqList.get(0).getSymbol();
        for(PublicStockQuote psq : psqList){
            System.out.println(psq.getCompanyName()+ " " +psq.getSymbol());
            if(!psq.getSymbol().equals(lastSymbol)){
                System.out.println("no unique symbol provided");
                return false;
            }
            lastSymbol=psq.getSymbol();
        }
        
        if(psqList.size()>1){
            System.out.println("no unique symbol provided");
            return false;
        }
        
        //TODO: Change to Depot
        Depot depot = this.depotDAO.getDepotUserSymbol(userId, symbol);
        //List<Depot> depotList = this.depotDAO.getDepotUserSymbol(userId, symbol);
        if(depot==null){
            System.out.println("no " + symbol + " shares for user with id " + userId + " found");
            return false;
        }
        
        /*
        int shareSum=0;
        for(Depot d : depotList){
            shareSum=shareSum+d.getQuantity();
        }*/
        
        if(depot.getQuantity()<shareCount){
        //if(shareSum<shareCount){
            System.out.println("sum of shares owned by user with id " +userId + " is less than the count provided");
            return false;
        }
        
        BigDecimal pricePerShare;
        try{
            pricePerShare = this.webservice.sell(symbol, shareCount);
        }catch(TradingWSException_Exception tex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, tex);
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, tex.getMessage());
            return true;
        }
        
        int shareSum = depot.getQuantity()-shareCount;
        //BigDecimal tradingVolume = BigDecimal.valueOf(pricePerShare.doubleValue()*shareCount);
        BigDecimal tradingVolume = pricePerShare.multiply(new BigDecimal(shareCount));
        
        if(shareSum == 0){
            customer.removeDepot(depot);
            this.customerDAO.persistCustomer(customer);
            this.depotDAO.deleteDepot(depot);
            //update Spielgeld
            this.increaseBankVolume(tradingVolume);
            return true;
        }
        
        Double updatedPrice = ((depot.getPrice()*depot.getQuantity())-(pricePerShare.doubleValue())*shareCount)/shareSum;
        depot.setQuantity(shareSum);
        depot.setPrice(updatedPrice);
        
        //TODO: could be removed
        customer.addDepot(depot);
        this.customerDAO.persistCustomer(customer);
        
        depot.setCustomer(customer);
        this.depotDAO.persist(depot);
        
        this.increaseBankVolume(tradingVolume);
        
        return true;
    }
    
    @Override
    public List<Share> searchShare(String symbolName) throws InvalidRemoteMethodParameterException,Exception{
        List<Share> shareList = null;
        List<PublicStockQuote> psqList = null;
        if(symbolName.equals("")){
            throw new InvalidRemoteMethodParameterException("provided symbolname must not be empty");
        }
        //TODO: replace empty string with list of all available shares
        try{
            psqList = this.webservice.findStockQuotesByCompanyName(symbolName);
        }catch(TradingWSException_Exception tex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, tex);
        }catch(Exception ex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(Integer.toString(psqList.size()));
        //----
        ArrayList<String> symbolList = new ArrayList<>(1);
        symbolList.add(symbolName);
        List<PublicStockQuote> symbolPsqList = null;
        try{
            symbolPsqList = this.webservice.getStockQuotes(symbolList);
        }catch(TradingWSException_Exception ex){
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(symbolPsqList != null && symbolPsqList.size()>0){
            if(psqList==null){
                //no shares for company name found
                psqList = new ArrayList<>();
            }
            psqList.addAll(symbolPsqList);
        }
        //----
        //init new share list and return to client
        shareList = new ArrayList<>(psqList.size());
        for(PublicStockQuote psq : psqList){
            if(psq.getSymbol() != null && psq.getLastTradePrice() != null && psq.getFloatShares() != null){
                //Share=DTO for Client
                Share share = new Share();
                share.setCompany(psq.getCompanyName());
                share.setSymbol(psq.getSymbol());
                share.setPrice(psq.getLastTradePrice().doubleValue());
                share.setQuantity(psq.getFloatShares().intValue());
                
                shareList.add(share);
            }
        }
        return shareList;
    }
    
    @Override
    public List<IShare> getDepot(Long userId) throws Exception{
        Customer customer = this.customerDAO.getCustomer(userId);
        if(!this.checkEmployee() && !customer.getUserName().equals(this.getLoginName())){
            System.out.println("user id does not match context user");
            //return false;
            throw new SecurityException("user id does not match context user");
        }
        List<Depot> depotList = this.depotDAO.getUserDepot(userId);
        if(depotList == null){
            return null;
        }
        List<IShare> shareList = new ArrayList<>();
        for(Depot d : depotList){
            IShare share = new Share();
            share.setId(d.getId());
            share.setSymbol(d.getSymbol());
            share.setPrice(d.getPrice());
            share.setQuantity(d.getQuantity());
            shareList.add(share);
        }
        return shareList;
    }
    
    @Override
    public String getLoginName(){
        return context.getCallerPrincipal().getName();
    }
    
    @Override
    public IUser getSessionUser(){
        //return context.getCallerPrincipal().getName();
        String sessionUserName=this.context.getCallerPrincipal().getName();
        
        IUser entity;
        IUser user = new User();
        //could use this.checkEmployee
        entity = this.customerDAO.getCustomer(sessionUserName);
        if(entity==null){
            entity = this.employeeDAO.getEmployee(sessionUserName);
        }
        
        user.setId(entity.getId());
        user.setUserName(entity.getUserName());
        user.setFirstName(entity.getFirstName());
        user.setLastName(entity.getLastName());
        //user.setAddress(entity.getAddress);
        return user;
    }

    @Override
    public Boolean checkEmployee(){
        String username = context.getCallerPrincipal().getName();
        Employee employee = this.employeeDAO.getEmployee(username);
        if(employee==null){
            return false;
        }
        return true;
    }
    
    @Override
    public BigDecimal getSpielgeld() throws PermissionDeniedException{
        if(!this.checkEmployee()){
            //TODO: throw Exception
            System.out.println("user id does not match context user");
            throw new PermissionDeniedException();
            //return null;
        }
        Spielgeld spielgeld = spielgeldDAO.getSpielgeld("1");
        return spielgeld.getMoney();
    }
    
    private BigDecimal increaseBankVolume(BigDecimal value){
        Spielgeld spielgeld = this.spielgeldDAO.getSpielgeld("1");
        BigDecimal bankMoney = spielgeld.getMoney();
        bankMoney = bankMoney.add(value);
        spielgeld.setMoney(bankMoney);
        this.spielgeldDAO.persistSpielgeld(spielgeld);
        return bankMoney;
    }
    
    private BigDecimal decreaseBankVolume(BigDecimal value){
        Spielgeld spielgeld = this.spielgeldDAO.getSpielgeld("1");
        BigDecimal bankMoney = spielgeld.getMoney();
        bankMoney = bankMoney.subtract(value);
        spielgeld.setMoney(bankMoney);
        this.spielgeldDAO.persistSpielgeld(spielgeld);
        return bankMoney;
    }
    
}
