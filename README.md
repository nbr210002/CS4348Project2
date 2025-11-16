This project simulates a bank system in java. It has 5 files. 

The Bank.java is the main program, initializing accounts, starting teller threads and coordinating customer and teller executions. The Account.java holds the bank balance, allowing withdrawls and deposits. The Customer.java is the one selecting and waiting for the transaction. The Teller.java processes the customers transactions, accesses the safe, and coordinates with the manager. Lastly, the TellerData.java stores the transaction type, and helps make sure that customer teller transactions run smoothly. This simulation uses semaphores.

To compile: type in: javac Bank.java Account.java Customer.java Teller.java TellerData.java

To run: type in: java Bank