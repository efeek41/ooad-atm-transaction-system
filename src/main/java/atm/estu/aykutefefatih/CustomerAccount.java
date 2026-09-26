package atm.estu.aykutefefatih;

public class CustomerAccount {
    private String cardNumber;
    private String pin;
    private double balance;
    
    public CustomerAccount(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }
        
    //kart numarasının değeri immutable olmalı o yüzden encapsulation uyguladık
    protected String getCardNumber() {
        return cardNumber;
    }
    
    //şifre değiştirmeyi desteklemiyor o yüzden immutable
    protected String getPin() {
        return pin;
    }
    
    //değeri yanlışlıkla değişmesin diye encapsulation
    protected double getBalance() {
        return balance;
    } 
    protected void setBalance(double balance) {
        this.balance = balance;
    }
    //
}