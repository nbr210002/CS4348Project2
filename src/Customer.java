import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer extends Thread
{
    private int id;
    private static final Random rand = new Random();

    public Customer(int id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }

    @Override
    public void run()
    {
        try
        {
            // Wait before entering the bank
            Thread.sleep(rand.nextInt(100));

            // Enter the bank
            Bank.bankDoor.acquire();
            System.out.println("Customer " + id + " enters the bank.");

            // Wait for teller
            Bank.availableTellers.acquire();

            Teller assignedTeller = null;

            // Find a free teller
            synchronized (Bank.tellers)
            {
                for (Teller t : Bank.tellers)
                {
                    if (t.isFree())
                    {
                        assignedTeller = t;
                        t.setBusy(true);
                        break;
                    }
                }
            }

            // Random deposit/withdrawl
            String transactionType = rand.nextBoolean() ? "Deposit" : "Withdraw";

            // Transaction
            assignedTeller.transaction(this, transactionType);

            // Teller is free again; avoid deadlock
            assignedTeller.setBusy(false);

            // Release teller permit
            Bank.availableTellers.release();

            //Customer exits and releases spot
            System.out.println("Customer " + id + " leaves the bank.");
            Bank.bankDoor.release();
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
//
