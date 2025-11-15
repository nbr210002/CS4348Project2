import java.util.Random;
import java.util.concurrent.Semaphore;

public class Account
{
    private int balance;

    // Set account with starting balance
    public Account(int initialBalance)
    {
        balance = initialBalance;
    }

    // Deposit money into account
    public synchronized void deposit(int amount)
    {
        // increment balance
        balance += amount;
        System.out.println("Account Deposit of $" + amount + " | New Balance = $" + balance);
    }

    // Withdraw money from account
    public synchronized void withdraw(int amount)
    {
        // decrement balance
        balance -= amount;
        System.out.println("Account Withdrawal of $" + amount + " | New Balance = $" + balance);
    }

    // Return current balance
    public synchronized int getBalance()
    {
        return balance;
    }
}