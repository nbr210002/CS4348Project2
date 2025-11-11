import java.util.Random;
import java.util.concurrent.Semaphore;

public class Teller extends Thread
{
    private int id;
    private static final Random rand = new Random();

    public Teller(int id)
    {
        this.id = id;
    }

    @Override
    public void run()
    {
        System.out.println("Teller " + id + " is ready");
    }

    public void transaction(Customer customer, String transactionType) throws InterruptedException
    {
        System.out.println("Teller " + id + "[Customer " + customer.getID() + "] asks for transaction. " );

        // In case of withdraw, get manager
        if (transactionType.equals("Withdraw"))
        {
            System.out.println("Teller " + id + "[Customer " + customer.getID() + "] requests manager permission " );
            Bank.manager.acquire();
            System.out.println("Teller " + id + "[Manager]: interacting... ");
            Thread.sleep(rand.nextInt(26) + 5);
            System.out.println("Teller " + id + " [Manager] is done. ");
            Bank.manager.release();
        }

        // Enter safe
        System.out.println("Teller " + id + " [Customer " + customer.getID() + "] is waiting to enter the safe");
        Bank.safe.acquire();
        System.out.println("Teller " + id + " [Customer " + customer.getID() + "] is performing a transaction in the sage");
        Thread.sleep(rand.nextInt(41) + 10);

        // Deposit or withdraw
        synchronized (Bank.sharedAccount)
        {
            if (transactionType.equals("Deposit"))
            {
                Bank.sharedAccount.deposit(10);
            }
            else
            {
                Bank.sharedAccount.withdraw(10);
            }
        }
        //Free teller/completed
        Bank.safe.release();
        System.out.println("Teller " + id + " [Customer " + customer.getID() + "] transaction is complete");
        Bank.availableTellers.release();
    }
}
//