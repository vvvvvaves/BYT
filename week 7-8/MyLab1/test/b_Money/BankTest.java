package b_Money;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	/**
	 * This test case checks that an actual naming is equal to getName() result.
	 * **/
	@Test
	public void testGetName() {
		assertEquals("DanskeBank", DanskeBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("SweBank", SweBank.getName());
	}

	/**
	 * This test case assures that actual currency created in setUp function is equal to the result of getCurrency() function.
	 * **/
	@Test
	public void testGetCurrency() {
		assertEquals(DKK, DanskeBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(SEK, SweBank.getCurrency());
	}


	/**
	 * This test case assures that:
	 * 1. When there is an attempt to create an account with an existing 'accountid', AccountExistsException is thrown.
	 * 2. When an account with non-existent 'accountid' is created, it is put into hashMap.
	 * 		If the put() operation of HashMap is successful, and the key recorded is a new one, it returns null.
	 * 		openAccount returns boolean on the basis of return_val == null.
	 * **/
	@Test
	public void testOpenAccount() throws AccountExistsException {
		Assert.assertThrows(AccountExistsException.class, () -> {
			DanskeBank.openAccount("Gertrud");
		});

		boolean opened = DanskeBank.openAccount("Vika");
		Assert.assertTrue(opened);

	}

	/**
	 * This test case assures that:
	 * 1. When one tries to deposit money to a non-existent account, AccountDoesNotExistException is thrown.
	 * 2. When an amount of money is deposited on an existing account, the money is recorded on the receiving account.
	 * 		In this case, account with an id "Gertrud" had zero on her balance,
 	* 		so the result of the deposit is an amount put in.
	 * **/
	@Test
	public void testDeposit() throws AccountDoesNotExistException {

		Money money = new Money(200050, DKK);
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.deposit("SomeNonExistent", money);
		});

		Integer expectedAmount = 200050;
		DanskeBank.deposit("Gertrud", money);
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}


	/**
	 * To assure that withdraw function in Bank class works as expected, this test case follows the steps:
	 * 1. Checks that when one tries to withdraw money from a non-existent account, AccountDoesNotExistException is thrown.
	 * 2. Deposits 'money' to an account with zero balance.
	 * 3. Checks that the deposited amount is equal to the amount put in.
	 * 4. Withdraws amount put in.
	 * 5. Checks that getBalance() is zero, as it initially was.
	 *
	 * **/
	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		Money money = new Money(200050, DKK);
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.withdraw("SomeNonExistent", money);
		});

		Integer expectedAmount = 0;
		DanskeBank.deposit("Gertrud", money);
		Assert.assertEquals(money.getAmount(), DanskeBank.getBalance("Gertrud"));
		DanskeBank.withdraw("Gertrud", money);
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}
	/**
	 * To assure that getBalance function works as expected, this test case follows the steps:
	 * 1. Checks that when one tries to getBalance from a non-existent account, AccountDoesNotExistException is thrown.
	 * 2. Deposits 'expectedAmount' to an account with zero balance.
	 * 3. Checks that the result of getBalance function equals the 'expectedAmount' put in before.
	 * **/
	@Test
	public void testGetBalance() throws AccountDoesNotExistException {
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.getBalance("SomeNonExistent");
		});

		Integer expectedAmount = 200050;
		DanskeBank.deposit("Gertrud", new Money(200050, DKK));
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}

	/**
	 * To assure that transfer function works as expected, this test case follows the steps:
	 * 1. Checks that when one tries to transfer money from a non-existent account, AccountDoesNotExistException is thrown.
	 * 2. Checks that when one tries to transfer money to a non-existent account, AccountDoesNotExistException is thrown.
	 * 3. Records initial balances on 'fromaccount' and 'toaccount'.
	 * 4. Deposits transferred amount ('money') to 'fromaccount'.
	 * 5. Performs transfer between 'fromaccount' and 'toaccount'.
	 * 6. Checks that getBalance of 'fromaccount' equals to its initial balance.
	 * 7. Checks that getBalance of 'toaccount' equals to 'initial'.add('money')
	 * **/
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		String fromaccount = "Gertrud";
		String toaccount = "Bob";
		Money money = new Money(200050, DKK);
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.transfer("SomeNonExistent", SweBank, toaccount, money);
		});

		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.transfer(fromaccount, SweBank, "SomeNonExistent", money);
		});

		Integer expectedAmount_from = DanskeBank.getBalance(fromaccount);
		Integer expectedAmount_to = SweBank.getBalance(toaccount) + SEK.valueInThisCurrency(money.getAmount(), money.getCurrency());

		DanskeBank.deposit(fromaccount, money);
		DanskeBank.transfer(fromaccount, SweBank, toaccount, money);

		Integer actual_to = SweBank.getBalance(toaccount);
		Integer actual_from = DanskeBank.getBalance(fromaccount);

		Assert.assertEquals(expectedAmount_from, actual_from);
		Assert.assertEquals(expectedAmount_to, actual_to);
	}

	/**
	 * To check whether Timed Payments work correctly, test case follows the steps:
	 * 1. Checks that when one tries to add TimedPayment to a non-existent account, AccountDoesNotExistException is thrown.
	 * 2. Checks that when one tries to establish the receiver of TimedPayment with non-existent accountid, AccountDoesNotExistException is thrown.
	 * 3. Deposits 'money' into 'fromaccount', which had zero balance in the setUp function.
	 * 4. Adds to Gertrud's account (referred to as 'fromaccount') TimedPayment to Bob (referred to as 'toaccount').
	 * 5. Ticks the bank (DanskeBank) of 'fromaccount' (Gertrud) 10 times (one interval).
	 * 6. Checks that, after one interval, getBalance() of 'fromaccount' equals previous balance minus the amount of timedPayment.
	 * 7. Checks that, after one interval, getBalance() of 'toaccount' equals previous balance plus the amount of timedPayment.
	 * 8. Repeats this process for one more interval to check that TimedPayment is repetitive as expected.
	 * **/
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		String fromaccount = "Gertrud";
		String toaccount = "Bob";
		Money money = new Money(200050, DKK);
		Money timedPayment = new Money(10000, DKK);
		Integer interval = 10;
		Integer next = 0;
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.addTimedPayment("SomeNonExistent", "1ID", interval, next, money,  SweBank, toaccount);
		});

		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.addTimedPayment(fromaccount, "1ID", interval, next, money,  SweBank, "SomeNonExistent");
		});

		DanskeBank.deposit(fromaccount, money);
		DanskeBank.addTimedPayment(fromaccount, "1ID", interval, next, timedPayment, SweBank, toaccount);

		for (int i = 0; i < 10; i++) {
			DanskeBank.tick();
		}

		Integer expectedTo = SweBank.getBalance("Bob") +  SEK.valueInThisCurrency(timedPayment.getAmount(), timedPayment.getCurrency());
		Integer expectedFrom = money.sub(timedPayment).getAmount();
		Integer actualFrom = DanskeBank.getBalance(fromaccount);
		Integer actualTo = SweBank.getBalance(toaccount);

		Assert.assertEquals(expectedFrom, actualFrom);
		Assert.assertEquals(expectedTo, actualTo);

		expectedTo = SweBank.getBalance("Bob") +  SEK.valueInThisCurrency(timedPayment.getAmount(), timedPayment.getCurrency());
		for (int i = 0; i < 10; i++) {
			DanskeBank.tick();
		}

		 expectedFrom = money.sub(timedPayment).sub(timedPayment).getAmount();
		 actualFrom = DanskeBank.getBalance(fromaccount);
		 actualTo = SweBank.getBalance(toaccount);

		Assert.assertEquals(expectedFrom, actualFrom);
		Assert.assertEquals(expectedTo, actualTo);


	}
}
