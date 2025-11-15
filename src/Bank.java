import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Bank
{
    // Total number of tellers
    public static final int NUM_TELLERS = 3;

    // Total number of customers
    public static final int NUM_CUSTOMERS = 50;

    // 2 customers at the door at once
    public static Semaphore bankDoor = new Semaphore(2, true);
    // 1 customer at manager at once
    public static Semaphore manager = new Semaphore(1, true);
    // 2 tellers at the safe at once
    public static Semaphore safe = new Semaphore(2, true);

    public static final BlockingQueue<Integer> customerQueue = new LinkedBlockingQueue<>();

    // Customer waits for available teller
    public static final Semaphore[] customerCalled = new Semaphore[NUM_CUSTOMERS];
    // Teller waits for customer
    public static final Semaphore[] customerArrived = new Semaphore[NUM_TELLERS];
    // Teller asks what the transaction is
    public static final Semaphore[] tellerAsked = new Semaphore[NUM_TELLERS];
    // Customer says what they want
    public static final Semaphore[] transactionSaid = new Semaphore[NUM_TELLERS];
    // Teller does the transaction
    public static final Semaphore[] transactionDone = new Semaphore[NUM_TELLERS];
    // Customer leaves
    public static final Semaphore[] customerLeaves = new Semaphore[NUM_TELLERS];

    // Customer to teller
    public static final int[] tellerForCustomer = new int[NUM_CUSTOMERS];

    public static Account sharedAccount = new Account(100);
    public static Teller[] tellers = new Teller[NUM_TELLERS];

    // Wait for all tellers to be ready
    public static final CountDownLatch readyLatch = new CountDownLatch(NUM_TELLERS);
    // Count of customers that have been helped
    public static final AtomicInteger servedCount = new AtomicInteger(0);

    // Initialize semaphores
    static
    {
        for (int i = 0; i < NUM_CUSTOMERS; i++)
            customerCalled[i] = new Semaphore(0);

        for (int t = 0; t < NUM_TELLERS; t++)
        {
            customerArrived[t] = new Semaphore(0);
            tellerAsked[t] = new Semaphore(0);
            transactionSaid[t] = new Semaphore(0);
            transactionDone[t] = new Semaphore(0);
            customerLeaves[t] = new Semaphore(0);
        }
    }

    // Open bank and start threads
    public void openBank() throws InterruptedException
    {
        System.out.println("Bank tellers are starting");

        // Start teller threads
        for (int i = 0; i < NUM_TELLERS; i++)
        {
            tellers[i] = new Teller(i);
            tellers[i].start();
        }

        // Wait for all tellers to be ready
        readyLatch.await();
        System.out.println("All bank tellers are ready. Bank is now Open.");

        // Start customer threads
        Thread[] customers = new Thread[NUM_CUSTOMERS];
        for (int c = 0; c < NUM_CUSTOMERS; c++)
        {
            customers[c] = new Customer(c);
            customers[c].start();
        }

        // Wait for all customers to finish
        for (Thread c : customers) c.join();

        // Send -1 to tellers to signal no more customers
        for (int i = 0; i < NUM_TELLERS; i++)
            customerQueue.put(-1);

        // Wait for all tellers to finish
        for (Teller t : tellers) t.join();

        // Print final
        System.out.println("Bank: all customers served (" + servedCount.get() + "). Closing.");
        System.out.println("Bank: final balance = $" + sharedAccount.getBalance());
    }

    public static void main(String[] args)
    {
        try
        {
            new Bank().openBank();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}