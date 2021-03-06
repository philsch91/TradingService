package net.froihofer.dsfinance.bank.client;

import com.fostw.dsfinance.bank.Credential;
import com.fostw.dsfinance.bank.ui.console.LoginForm;
import com.fostw.dsfinance.bank.common.interfaces.*;
import com.fostw.dsfinance.bank.ui.console.ConsoleUI;
import com.fostw.dsfinance.bank.ui.console.Menu;
import com.fostw.dsfinance.bank.ui.console.TextBox;
import com.fostw.dsfinance.bank.ui.console.TextBoxInputType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import net.froihofer.util.AuthCallbackHandler;
import net.froihofer.util.JBoss7JndiLookupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for starting the bank client.
 *
 */
public class BankClient {
  private static Logger log = LoggerFactory.getLogger(BankClient.class);
  
  public static void main(String[] args) {
      
    LoginForm loginForm = new LoginForm();
    Credential cred = loginForm.show();
    if(cred==null){
        System.out.println("wrong username or password");
        return;
    }
      
    //AuthCallbackHandler.setUsername("customer");
    //AuthCallbackHandler.setPassword("customerpass");
    AuthCallbackHandler.setUsername(cred.getUsername());
    AuthCallbackHandler.setPassword(cred.getPassword());
    
    Properties props = new Properties();
    props.put(Context.SECURITY_PRINCIPAL,AuthCallbackHandler.getUsername());
    props.put(Context.SECURITY_CREDENTIALS,AuthCallbackHandler.getPassword());
    props.put("jboss.naming.client.ejb.context", true);
    Boolean isEmployee=false;
    IBank bank=null;
    try {
        JBoss7JndiLookupHelper jndiHelper = new JBoss7JndiLookupHelper(new InitialContext(props), "ds-finance-bank-ear", "ds-finance-bank-ejb", "");
        //TODO: Implement the rest of the functionality
        bank = (IBank) jndiHelper.lookup("BankService", IBank.class);
        
        isEmployee=bank.checkEmployee();
        System.out.println("isEmployee: " + isEmployee.toString());
    }catch (NamingException e) {
      log.error("Failed to initialize InitialContext.",e);
    }
    
    //System.out.println("Hello World");
    if(bank==null){
        System.out.println("No connection to Bank");
        return;
    }
    
    String username = bank.getLoginName();
    System.out.println("Welcome " + username);
    
    ConsoleUI ui = new ConsoleUI(bank);
    ui.show();
    
  }
}
