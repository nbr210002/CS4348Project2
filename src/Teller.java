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

        // Signal that teller is ready
        Bank.readyLatch.countDown();

        try
        {
            while (true)
            {
                // Wait for next customer from queue
                int custID = Bank.customerQueue.take();

                // Check for term signal
                if (custID == -1)
                {
                    System.out.println("Teller " + id + ": no more customers. Bank is closing.");
                    break;
                }

                // Map customer to teller
                Bank.tellerForCustomer[custID] = id;

                // Signal customer to approach teller
                Bank.customerCalled[custID].release();

                // Teller waits for customer to arrive
                Bank.customerArrived[id].acquire();
                System.out.println("Teller " + id + " [Customer " + custID + "]: asks for transaction.");
                Bank.tellerAsked[id].release();

                // Teller waits for customer to say what they want
                Bank.transactionSaid[id].acquire();
                String helper = TellerData.getTransactionForTeller(id);

                // Withdraws require manager approval; go through steps
                if ("Withdraw".equals(helper))
                {
                    System.out.println("Teller " + id + " [Customer " + custID + "]: going to manager.");
                    Bank.manager.acquire();
                    System.out.println("Teller " + id + ": using manager.");
                    Thread.sleep(rand.nextInt(26) + 5);
                    System.out.println("Teller " + id + ": done with manager.");
                    Bank.manager.release();
                }

                // Access safe
                System.out.println("Teller " + id + " [Customer " + custID + "]: going to safe.");
                Bank.safe.acquire();

                // Perform transaction
                System.out.println("Teller " + id + " [Customer " + custID + "]: performing transaction in safe.");
                Thread.sleep(rand.nextInt(41) + 10);

                if ("Deposit".equals(helper))
                    Bank.sharedAccount.deposit(10);
                else
                    Bank.sharedAccount.withdraw(10);

                System.out.println("Teller " + id + " [Customer " + custID + "]: done in safe.");
                Bank.safe.release();

                // Signal that transaction is done
                Bank.transactionDone[id].release();

                // Wait for customer to leave
                Bank.customerLeaves[id].acquire();

                // Update
                int servedSoFar = Bank.servedCount.incrementAndGet();
                System.out.println("Teller " + id + " [Customer " + custID + "]: finished. Total served = " + servedSoFar);
            }
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}