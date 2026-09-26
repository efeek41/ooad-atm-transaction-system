package atm.estu.aykutefefatih;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class ATMController {
    // burası direkt aggregation relationship
    private static ATMController controllerInstance;
    private static BankCentralSystem centralSystemInstance = BankCentralSystem.getCentralSystem();
    private CustomerAccount currentAccount = null;
    private int failedAttempts = 0;
    private final int MAX_ATTEMPTS = 3;

    // singleton için constructor gizledik
    private ATMController() {
    }

    // singleton pattern
    protected static ATMController getCentralSystem() {
        if (controllerInstance == null) {
            controllerInstance = new ATMController();
        }
        return controllerInstance;
    }

    // anlık giriş yapılı hesabı döndürüyor
    protected CustomerAccount getCurrentAccount() {
        return currentAccount;
    }

    // anlık giriş yapılı hesabı ayarlıyor
    protected void setCurrentAccount(String cardNumber) {
        controllerInstance.currentAccount = centralSystemInstance.findCurrentAccount(cardNumber);
    }

    // çıkış yapıyor
    protected void logOut() {
        controllerInstance.setCurrentAccount(null);
    }

    // auth başlangıcı
    protected boolean authCustomer(String inputPin) {
        try {
            boolean result = currentAccount.getPin().equals(inputPin);
            if(!result){
                failedAttempts++;
            }else{
                controllerInstance.resetRemainingAttempts();
            }
            return result;

        } catch (Exception e) {
            failedAttempts++;
            return false;
        }

    }

    protected int getRemainingAttempts() {
        return MAX_ATTEMPTS - failedAttempts;
    }
    protected void resetRemainingAttempts(){
        controllerInstance.failedAttempts = 0;
    }
    // parasal işlemlerin hepsi burada
    protected void deposit(double amount) {
        centralSystemInstance.updateBalance(amount + currentAccount.getBalance());
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        logTransaction("DEPOSIT", String.valueOf(amount), currentDate);
    }

    protected boolean withdraw(double amount) {
        if (currentAccount.getBalance() >= amount) {
            centralSystemInstance.updateBalance(currentAccount.getBalance() - amount);
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            logTransaction("WITHDRAW", String.valueOf(amount), currentDate);
            return true;
        }
        return false;
    }

    public double checkBalance() {
        return currentAccount.getBalance();
    }
    //

    //İşlemleri geçmiş gibi kaydediyor
    private void logTransaction(String transactionType, String amount, String date) {
        String cardID = currentAccount.getCardNumber();
        LinkedList<String> transactionList = centralSystemInstance.getLogList();

        String logEntry = String.format("[%s] %s - Amount: %s - Card: %s", date, transactionType, amount, cardID);
        transactionList.add(logEntry);
    }
    // Simüle etmek için ekledik gerçek kodda olmaması gerekiyor
    public void printAllLogs() {
        LinkedList<String> transactionList = centralSystemInstance.getLogList();
        if (transactionList.isEmpty()) {
            System.out.println("There is no log to show.");
            return;
        }

        System.out.println("\n--- TRANSACTION HISTORY (LOGS) ---");
        for (String log : transactionList) {
            System.out.println(log);
        }
        System.out.println("------------------------------\n");
    }
}
