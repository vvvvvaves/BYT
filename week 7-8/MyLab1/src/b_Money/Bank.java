package b_Money;

import java.util.Hashtable;
import java.util.Objects;

public class Bank {
	private Hashtable<String, Account> accountlist = new Hashtable<String, Account>();
	private String name;
	private Currency currency;
	
	/**
	 * New Bank
	 * @param name Name of this bank
	 * @param currency Base currency of this bank (If this is a Swedish bank, this might be a currency class representing SEK)
	 */
	Bank(String name, Currency currency) {
		this.name = name;
		this.currency = currency;
	}
	
	/**
	 * Get the name of this bank
	 * @return Name of this bank
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the Currency of this bank
	 * @return The Currency of this bank
	 */
	public Currency getCurrency() {
		return currency;
	}
	
	/**
	 * Open an account at this bank.
	 * @param accountid The ID of the account
	 * @throws AccountExistsException If the account already exists
	 *
	 * BUG FOUND:
	 * changed accountlist.get(accountid) to accountlist.put(...)
	 *
	 * This function follows the steps:
	 * 1. Check that this accountid has not yet been added to the accountlist.
	 * 2. If it has been, throw AccountExistsException.
	 * 3. If not, put this accountid with new Account as its value into accountlist and record return_val
	 * 4. If accountid has been successfully added, and it is a new key, accountlist will return null, hence
	 * 5. If return_val == null, account has been successfully opened.
	 */
	public boolean openAccount(String accountid) throws AccountExistsException {
		if (accountlist.containsKey(accountid)) {
			throw new AccountExistsException();
		}
		else {
			Object return_val = accountlist.put(accountid, new Account("", currency));
			return return_val == null;
		}
	}
	
	/**
	 * Deposit money to an account
	 * @param accountid Account to deposit to
	 * @param money Money to deposit.
	 * @throws AccountDoesNotExistException If the account does not exist
	 *
	 * BUG FOUND: AccountDoesNotExistException was thrown at accountlist.containsKey.
	 * 			  I added negation to the if-statement.
	 */
	public void deposit(String accountid, Money money) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			account.deposit(money);
		}
	}
	
	/**
	 * Withdraw money from an account
	 * @param accountid Account to withdraw from
	 * @param money Money to withdraw
	 * @throws AccountDoesNotExistException If the account does not exist
	 *
	 * BUG FOUND:
	 *
	 * changed account.deposit(...) to account.withdraw(...) in 'else' statement.
	 */
	public void withdraw(String accountid, Money money) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			account.withdraw(money);
		}
	}
	
	/**
	 * Get the balance of an account
	 * @param accountid Account to get balance from
	 * @return Balance of the account
	 * @throws AccountDoesNotExistException If the account does not exist
	 */
	public Integer getBalance(String accountid) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		}
		else {
			return accountlist.get(accountid).getBalance().getAmount();
		}
	}

	/**
	 * Transfer money between two accounts
	 * @param fromaccount Id of account to deduct from in this Bank
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 */
	public void transfer(String fromaccount, Bank tobank, String toaccount, Money amount) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(fromaccount) || !tobank.accountlist.containsKey(toaccount)) {
			throw new AccountDoesNotExistException();
		}
		else {
			accountlist.get(fromaccount).withdraw(amount);
			tobank.accountlist.get(toaccount).deposit(amount);
		}		
	}

	/**
	 * Transfer money between two accounts on the same bank
	 * @param fromaccount Id of account to deduct from
	 * @param toaccount Id of receiving account
	 * @param amount Amount of Money to transfer
	 * @throws AccountDoesNotExistException If one of the accounts do not exist
	 *
	 * BUG FOUND: changed
 * 						transfer(fromaccount, this, fromaccount, amount);
* 				  to
	 * 					 transfer(fromaccount, this, toaccount, amount);
	 */
	public void transfer(String fromaccount, String toaccount, Money amount) throws AccountDoesNotExistException {
		transfer(fromaccount, this, toaccount, amount);
	}

	/**
	 * Add a timed payment
	 * @param accountid Id of account to deduct from in this Bank
	 * @param payid Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 *
 	 * BUG FOUND: This method did not handle AccountDoesNotExistException.
	 * 				Subsequent actions on non-existent account would have lead to NullPointerException.
	 *				I implemented handling of AccountDoesNotExistException.
	 */
	public void addTimedPayment(String accountid, String payid, Integer interval, Integer next, Money amount, Bank tobank, String toaccount) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid) || !tobank.accountlist.containsKey(toaccount)) {
			throw new AccountDoesNotExistException();
		}
		else {
			Account account = accountlist.get(accountid);
			account.addTimedPayment(payid, interval, next, amount, tobank, toaccount);
		}
	}
	
	/**
	 * Remove a timed payment
	 * @param accountid Id of account to remove timed payment from
	 * @param id Id of timed payment
	 *
	 * BUG FOUND: This method did not handle AccountDoesNotExistException.
	 * 				Subsequent actions on non-existent account would have lead to NullPointerException.
	 *				I implemented handling of AccountDoesNotExistException.
	 */
	public void removeTimedPayment(String accountid, String id) throws AccountDoesNotExistException {
		if (!accountlist.containsKey(accountid)) {
			throw new AccountDoesNotExistException();
		} else {
			Account account = accountlist.get(accountid);
			account.removeTimedPayment(id);
		}
	}
	
	/**
	 * A time unit passes in the system
	 *
	 * ALTERNATION:
	 *
	 * I added a check of whether account actually exists, and the key does not refer to null, in order to avoid
	 * NullPointerException.
	 */
	public void tick() throws AccountDoesNotExistException {
		for (Account account : accountlist.values()) {
			if (account == null) {
				throw new AccountDoesNotExistException();
			}
			account.tick();
		}
	}	
}
