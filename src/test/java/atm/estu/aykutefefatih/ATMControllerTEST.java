package atm.estu.aykutefefatih;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ATMControllerTEST{

    private ATMController controller;

    @BeforeEach
    void setUp() {
        controller = ATMController.getCentralSystem();
        //Testlerin her birinin peşpeşe doğru çalıştığını gözlemlemek için en başta bir initilization yapıyoruz
        controller.setCurrentAccount("12345");
    }

    // BURADAKİ TEST RAPORUMUZDAKİYLE AYNI DOĞRULTUDA O YÜZDEN ORDER VAR
    @Test
    @Order(1)
    void testSetCurrentAccount_InvalidCardNumber() {
        //Sistemde kayıtlı olmayan bir numara deneyelim "1234"
        controller.setCurrentAccount("1234");

        // Hesap null oldu mu kontrol ediyoruz
        assertNull(controller.getCurrentAccount(), "Account must be null when invalid card is entered.");

        // Null olduysa başarıyla geçti
        System.out.println("TEST PASSED: Invalid card number scenario.");
    }

    @Test
    @Order(2)
    void testAuthCustomer_WrongPin() {
        //12345 hesabının şifresi 1234 yani yanlış
        boolean result = controller.authCustomer("123");
        
        //Şifre yanlışlığını kontrol ediyoruz
        assertFalse(result, "Should return false when wrong PIN is entered.");
        
        //Şifre yanlışsa testi başarıyla geçti.
        System.out.println("TEST PASSED: Wrong PIN check.");
    }

    @Test
    @Order(3)
    void testAuthCustomer_CorrectPin() {
        //12345 hesabının şifresi 1234 yani doğru
        boolean result = controller.authCustomer("1234");
        
        //Şifre doğruluğu kontrol ediyoruz
        assertTrue(result, "Should return true when correct PIN is entered.");
        
        // Şifre doğruysa testi başarıyla geçti
        System.out.println("TEST PASSED: Correct PIN check.");
    }
    
    @Test
    @Order(4)
    void testCheckBalance(){
        double balance = controller.checkBalance();
        assertEquals(5000.0, balance);
        System.out.println("TEST PASSED: Check Balance operation returned correct amount.");
    }
    
    @Test
    @Order(5)
    void testDeposit() {
        double initialBalance = controller.checkBalance();
        double depositAmount = 1000.0;

        controller.deposit(depositAmount);

        double expectedBalance = initialBalance + depositAmount;
        
        assertEquals(expectedBalance, controller.checkBalance(), "Deposit operation must correctly increase the balance.");
        
        System.out.println("TEST PASSED: Deposit operation with amount %.2f, Current Balance: %.2f".formatted(depositAmount,controller.checkBalance()));
    }

    @Test
    @Order(6)
    void testWithdraw_InsufficientFunds() {
        double initialBalance = controller.checkBalance();
        double withdrawAmount = initialBalance + 1.0;

        boolean result = controller.withdraw(withdrawAmount);

        assertFalse(result, "Operation must return false if funds are insufficient.");
        assertEquals(initialBalance, controller.checkBalance(), "Money must not decrease if funds are insufficient.");
        
        System.out.println("TEST PASSED: Insufficient funds check with amount %.2f, Current Balance: %.2f".formatted(withdrawAmount,controller.checkBalance()));
    }

    @Test
    @Order(7)
    void testWithdraw_SufficientFunds() {
        double initialBalance = controller.checkBalance();
        double withdrawAmount = 3000.0;

        boolean result = controller.withdraw(withdrawAmount);

        assertTrue(result, "Withdraw operation must return true if sufficient funds exist.");
        assertEquals(initialBalance - withdrawAmount, controller.checkBalance(), "Withdrawn amount must be deducted from balance.");
        
        System.out.println("TEST PASSED: Withdraw operation with sufficient amount %.2f, Current Balance: %.2f".formatted(withdrawAmount,controller.checkBalance()));
    }

    @Test
    @Order(8)
    void testLogOut() {
        controller.logOut();
        
        assertNull(controller.getCurrentAccount(), "currentAccount must be null when logged out.");
        
        System.out.println("TEST PASSED: Logout operation.");
    }
    
    @Test
    @Order(9)
    void testLogging() {
        //LOG LİSTEMİZİ ALDIK
        var logList = BankCentralSystem.getCentralSystem().getLogList();
        //LOG listesi boyutu
        int initialLogSize = logList.size();
        //Son işlemi alır (Son işlemimiz 3000 TRY çekmekti, withdraw olmalı)
        String lastLog = logList.get(initialLogSize-1);
        double withdrawAmount = 3000.0;
        assertTrue(lastLog.contains("WITHDRAW"), "Log entry must contain 'WITHDRAW'.");
        assertTrue(lastLog.contains(String.valueOf(withdrawAmount)), "Log entry must contain the withdraw amount.");
        System.out.println("Verified Log 1: " + lastLog);

        //Son işlemden önceki işlemi alır (Son işlemden önceki işlem 1000 TRY yatırmaktı, deposit olmalı)
        String newLastLog = logList.get(initialLogSize-2); 
        double depositAmount = 1000.0;
        assertTrue(newLastLog.contains("DEPOSIT"), "Log entry must contain 'DEPOSIT'.");
        assertTrue(newLastLog.contains(String.valueOf(depositAmount)), "Log entry must contain the correct amount.");
        assertTrue(newLastLog.contains("12345"), "Log entry must contain the current card number.");
        
        System.out.println("Verified Log 1: " + newLastLog);
    }

    @Test
    @Order(10)
    void testAuthCustomer_3TimesWrongLog() {
        System.out.println("=== 3 CONSECUTIVE WRONG PINS SCENARIO STARTING ===");
        
        //Kartı 1234 yaptık
        controller.setCurrentAccount("1234");
        //1. Durum YANLIŞ KART DOĞRU PİN
        boolean attempt1 = controller.authCustomer("1234");
        assertFalse(attempt1, "1st attempt must fail.");
        System.out.println("1. Correct PIN wrong card attempted. Result: " + attempt1);

        //Kartı 12345 yaptık
        controller.setCurrentAccount("12345");
        // 2. Durum DOĞRU KART YANLIŞ PİN
        boolean attempt2 = controller.authCustomer("123");
        assertFalse(attempt2, "2nd attempt must fail.");
        System.out.println("2. Wrong PIN correct card attempted. Result: " + attempt2);

        // 3. YANLIŞ KART YANLIŞ PİN
        //Kartı 123 yaptık
        controller.setCurrentAccount("123");
        boolean attempt3 = controller.authCustomer("123");
        assertFalse(attempt3, "3rd attempt must fail.");
        System.out.println("3. Wrong PIN wrong card attempted. Result: " + attempt3);
        
        assertEquals(0, controller.getRemainingAttempts());
        System.out.println("TEST PASSED: Remaining attempts '%d'".formatted(controller.getRemainingAttempts()));
        
        System.out.println("=== SCENARIO COMPLETED SUCCESSFULLY ===");
    }

    
}