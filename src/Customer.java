import java.util.Random;
import java.util.concurrent.Semaphore;

public class Customer extends Thread
{
    private int id;

    private static final Random rand = new Random();

    // Assigns id to customer
    public Customer(int id)
    {
        this.id = id;
    }

    // Return customers id
    public int getID()
    {
        return id;
    }

    @Override
    public void run()
    {
        try
        {
            // Deside transaction type
            String helper = rand.nextBoolean() ? "Deposit" : "Withdraw";

            // Wait time
            Thread.sleep(rand.nextInt(101));

            // Customer enters bank
            Bank.bankDoor.acquire();
            System.out.println("Customer " + id + ": enters the bank.");

            // Customer get in line to wait for teller
            Bank.customerQueue.put(id);
            System.out.println("Customer " + id + ": gets in line.");

            // Customer waits to be called
            Bank.customerCalled[id].acquire();

            // Teller and customer assigned to each other
            int assignedTellerID = Bank.tellerForCustomer[id];
            System.out.println("Customer " + id + " [Teller " + assignedTellerID + "]: selects teller");

            // Teller gets signal that customer has arrived
            Bank.customerArrived[assignedTellerID].release();
            System.out.println("Customer " + id + " [Teller " + assignedTellerID + "]: introduces themselves.");

            // Teller asks for transaction
            Bank.tellerAsked[assignedTellerID].acquire();

            // Teller receives transaction type
            TellerData.setTransactionForTeller(assignedTellerID, helper);
            System.out.println("Customer " + id + " [Teller " + assignedTellerID + "]: tells transaction " + helper);
            Bank.transactionSaid[assignedTellerID].release();

            // Teller completes transaction
            Bank.transactionDone[assignedTellerID].acquire();
            System.out.println("Customer " + id + " [Teller " + assignedTellerID + "]: transaction complete.");

            // Customer leaves the bank
            Bank.customerLeaves[assignedTellerID].release();
            Bank.bankDoor.release();
        }

        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt(); // reset interrupted status if thread is interrupted
        }
    }
}