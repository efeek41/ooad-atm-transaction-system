package atm.estu.aykutefefatih;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BankCentralSystem {
    private static BankCentralSystem centralSystemInstance;
    private static ATMController controllerInstance = ATMController.getCentralSystem();
    private Map<String, CustomerAccount> customerAccounts; 
    private LinkedList<String> log = new LinkedList<>();
    
    //singleton için gizledik
    private BankCentralSystem() {}

    //singleton pattern
    protected static BankCentralSystem getCentralSystem(){
        if(centralSystemInstance == null){
            centralSystemInstance = new BankCentralSystem();
            centralSystemInstance.initializeTestData();
        }
        return centralSystemInstance;
    }
    
    //burası simülasyon için gerçekte yok, database simülasyonu
    protected void initializeTestData() {
        // Simüle etmek için yaptık, örnek olarak hesaplar oluşturduk
        customerAccounts = new HashMap<>();
        customerAccounts.put("12345", new CustomerAccount("12345", "1234", 5000.0));
        customerAccounts.put("13579", new CustomerAccount("13579", "5678", 3000.0));
        customerAccounts.put("02468", new CustomerAccount("02468", "0000", 10000.0));
    }

    //şifre doğrular (databaseden)
    protected boolean validatePIN(String inputPIN){
        return inputPIN.equals(customerAccounts.get(controllerInstance.getCurrentAccount().getCardNumber()).getPin());
    }

    //müşteri bulur
    protected CustomerAccount findCurrentAccount(String cardNumber){
        CustomerAccount localCurrentAccount = customerAccounts.get(cardNumber);
        return localCurrentAccount;
    }

    //database günceller
    protected void updateBalance(double newBalance) {
        CustomerAccount current = controllerInstance.getCurrentAccount();
        if (current != null) {
            current.setBalance(newBalance);
        }
    }
    
    //Burası da simülasyon için
    public LinkedList<String> getLogList(){
        return log;
    }
}
