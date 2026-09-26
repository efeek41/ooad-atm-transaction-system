package atm.estu.aykutefefatih;

import java.util.Scanner;

public class ATMUI {
    //simülasyon için input sağlayıcısı
    private static Scanner sc = new Scanner(System.in);
    //aggregation
    private static ATMController controllerInstance = ATMController.getCentralSystem();
    
    public static void main(String[] args) {
        logInMenu();       
    }
    
    //cli için komutlar
    private static void clearScreen() {  
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }  
    //gecikme için simülasyon
    private static void sleep(double time){
        try {
            Thread.sleep((int)(time * 1000));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
    //gecikme için simülasyon mesajlı
    private static void simulateDelay(double time, String word){
        clearScreen();
            System.out.println(word+".");
            sleep(time);
            clearScreen();
            System.out.println(word+"..");
            sleep(time);
            clearScreen();
            System.out.println(word+"...");
            sleep(time);
            clearScreen();
    }
    //buradaki her şey ui yönlendirmesi
    private static void logInMenu() {
        while (controllerInstance.getRemainingAttempts() > 0) {
            clearScreen();
            System.out.println("Please enter Card Number (For simulation, press X to show logs.): ");
            String tempInput = sc.nextLine();
            if(tempInput.equalsIgnoreCase("x")){
                clearScreen();
                controllerInstance.printAllLogs();
                System.out.println("Press ENTER to continue");
                sc.nextLine();
                controllerInstance.resetRemainingAttempts();
                continue;
            }
            controllerInstance.setCurrentAccount(tempInput);
            System.out.println("Please enter PIN: ");
            String tempPIN = sc.nextLine();
            simulateDelay(1,"Authenticating");
            if (controllerInstance.authCustomer(tempPIN)) {
                showMainMenu();
                continue;
            }else{
                System.out.printf("Card Number or PIN is invalid, %d attempts are left.\n", controllerInstance.getRemainingAttempts());
            }
            sleep(1); 
        }
    } 
    private static void showMainMenu(){
        int selection;
        while(controllerInstance.getCurrentAccount() != null){  
            System.out.println(
            """
                Please select the transaction:
                1-Deposit
                2-Withdraw
                3-Check Balance
                4-Cancel (Log out)
            """);
            selection = Integer.parseInt(sc.nextLine());
            simulateDelay(0.6, "Please wait");
            switch (selection) {
                case 1:    
                    depositMenu();
                    break;
                case 2:
                    withdrawMenu();
                    break;
                case 3:
                    checkBalanceMenu();
                    break;
                case 4:
                    controllerInstance.logOut();
                    simulateDelay(0.5, "Logging out");
                    break;
                default:
                    System.out.println("");
                    break;
            }
        }
    }
    private static void depositMenu(){
        int localAmount;
        System.out.println("Please enter the amount you want to deposit (Press X to cancel): ");
        String input = sc.nextLine();
        if(input.equalsIgnoreCase("x")){
            clearScreen();
            System.out.println("Deposit canceled.");
            return;
        }
        localAmount = Integer.valueOf(input);
        simulateDelay(0.5, "Processing");
        controllerInstance.deposit(localAmount);
        System.out.println("Deposit Successful!");
    }
    private static void withdrawMenu(){
        clearScreen();
        int localAmount;
        while (true) { 
            System.out.printf("Please enter the amount you want to withdraw (Press X to cancel)(Current Balance: %.2f TRY): \n",controllerInstance.checkBalance());
            String input = sc.nextLine();
            if(input.equalsIgnoreCase("x")){
                clearScreen();
                System.out.println("Withdraw canceled.");
                break;
            }

            localAmount = Integer.valueOf(input);

            simulateDelay(0.5, "Processing");

            if(controllerInstance.withdraw(localAmount) == true){
                System.out.println("Withdraw Successful!");
                break;
            }else{
                System.out.printf("Please enter an amount in range of your balance  (Current Balance: %.2f TRY): \n",controllerInstance.checkBalance());
            }
            sleep(2);
            clearScreen();
        }
    }
    private static void checkBalanceMenu(){
        System.out.printf("Current balance: %.2f TRY\n",controllerInstance.checkBalance());
    }
    
}
