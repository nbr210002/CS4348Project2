11/16/2025 3:51PM

There will be 5 files: Account, Bank, Customer, Teller, and TellerData.

The Account file is representing the bank account, it stores a balance and you can deposit and withdraw from it.

The Customer file stores customer information, including what accounts they hold.

The TellerData file will store the customer name, account number, and the type of transaction, a deposit, withdraw, transfer or check balance.

The Teller file will perform the transactions. This file will also use the TellerData.

The Bank file will initialize accounts, and create and coordinate the execution of teller threads. 
