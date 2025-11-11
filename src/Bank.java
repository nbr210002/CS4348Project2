import java.util.concurrent.Semaphore;

public class Bank
{
    public static final int NUM_TELLERS = 3;

    // Only two people, at a time, can go through the door
    public static Semaphore bankDoor = new Semaphore(2, true);
    // 1 person talks to manager at a time
    public static Semaphore manager = new Semaphore(1, true);
    // 2 tellers at the safe
    public static Semaphore safe = new Semaphore(2, true);
    // Available tellers
    public static Semaphore availableTellers = new Semaphore(NUM_TELLERS, true);

    public static Account sharedAccount = new Account(100);
    public static Teller[] tellers = new Teller[NUM_TELLERS];

    public void openBank()
    {
        System.out.println("Bank is opening");

        // Start teller threads
        for (int i = 0; i < NUM_TELLERS; i++)
        {
            tellers[i] = new Teller(i);
            tellers[i].start();
        }

        // Start customer threads
        for (int i = 0; i < NUM_TELLERS; i++)
        {
            new Customer(i).start();
        }
    }

    public static void main(String[] args)
    {
        Bank a = new Bank();
        a.openBank();
    }
}
