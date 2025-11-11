public class Account
{
    private int balance;

    // Initial Balance
    public Account( int initialBalance )
    {
        this.balance = initialBalance;
    }

    // Add to amount
    public synchronized void deposit(int amount)
    {
        balance += amount;

        System.out.println("Account Deposit of $" + amount + " | New Balance = $" + balance );
    }

    // Decrease balance
    public synchronized void withdraw(int amount)
    {
        balance -= amount;
        System.out.println("Account Withdrawal of $" + amount + " | New Balance = $" + balance);
    }

    // Get the balance
    public synchronized int getBalance()
    {
        return balance;
    }
}
