import java.util.Random;
import java.util.concurrent.Semaphore;

public class TellerData
{
    // Stores transaction per teller
    private static final String[] helperByTeller = new String[Bank.NUM_TELLERS];

    // Set transaction type for teller
    public static synchronized void setTransactionForTeller(int tellerID, String helper)
    {
        helperByTeller[tellerID] = helper;
    }

    // Get transaction type for teller
    public static synchronized String getTransactionForTeller(int tellerID)
    {
        return helperByTeller[tellerID];
    }
}