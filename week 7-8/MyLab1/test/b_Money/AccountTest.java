package b_Money;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", DKK);
		testAccount.deposit(new Money(10000000, DKK));
		DanskeBank.openAccount("Hans");
		DanskeBank.deposit("Hans", new Money(10000000, DKK));
		SweBank.deposit("Alice", new Money(1000000, SEK));
	}

	/**
	 * To check if add/remove functions work correctly, test follows the steps:
	 * 	1. Check that Payment did not exist before.
	 * 	2. Add Payment.
	 * 	3. Check that this Payment exists, now.
	 * 	4. Remove Payment.
	 * 	5. Check that it does not exist after removal.
	 * **/
	@Test
	public void testAddRemoveTimedPayment() throws AccountDoesNotExistException {
		Assert.assertTrue(!testAccount.timedPaymentExists("001"));
		testAccount.addTimedPayment( "001", 10, 0, new Money(0, SEK), SweBank, "Alice");
		Assert.assertTrue(testAccount.timedPaymentExists("001"));
		testAccount.removeTimedPayment("001");
		Assert.assertTrue(!testAccount.timedPaymentExists("001"));

	}

	/**
	 * To check whether Timed Payments work correctly, test case follows the steps:
	 * 1. Add to Hans' account (referred to as 'fromaccount') Timed Payment to Alice (referred to as 'toaccount').
	 * 2. Get their initial balances in respected banks.
	 * 3. Tick 'fromaccount' 10 times (one interval).
	 * 4. Check that expected balances of 'fromaccount' and 'toaccount' are the same as actual ones.
	 * 5. Repeat this process for one more interval to check that TimedPayment is repetitive as expected.
	 * 6. Remove TimedPayment.
	 * 7. Go through one more interval.
	 * 8. Check that expected balances are not different from those recorded one interval ago.
	 * **/
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {

		String fromaccount = "Hans";
		String toaccount = "Alice";
		Money timedPayment = new Money(10000, DKK);
		Integer interval = 10;
		Integer next = 0;

		testAccount.addTimedPayment( "001", interval, next, timedPayment, SweBank, toaccount);

		Money initialHans = testAccount.getBalance();
		Money initialAlice = new Money(SweBank.getBalance(toaccount), SEK);
		for (int i = 0; i < interval; i++) {
			testAccount.tick();
		}

		Integer expectedFrom = initialHans.sub(timedPayment).getAmount();
		Integer actualFrom = testAccount.getBalance().getAmount();
		Integer expectedTo = initialAlice.add(timedPayment).getAmount();
		Integer actualTo = SweBank.getBalance(toaccount);

		Assert.assertEquals(expectedFrom, actualFrom);
		Assert.assertEquals(expectedTo, actualTo);

		for (int i = 0; i < 10; i++) {
			testAccount.tick();
		}

		expectedFrom = initialHans.sub(timedPayment).sub(timedPayment).getAmount();
		actualFrom = testAccount.getBalance().getAmount();
		expectedTo = initialAlice.add(timedPayment).add(timedPayment).getAmount();
		actualTo = SweBank.getBalance(toaccount);

		Assert.assertEquals(expectedFrom, actualFrom);
		Assert.assertEquals(expectedTo, actualTo);

		testAccount.removeTimedPayment("001");

		for (int i = 0; i < 10; i++) {
			testAccount.tick();
		}

		actualFrom = testAccount.getBalance().getAmount();
		actualTo = SweBank.getBalance(toaccount);


		Assert.assertEquals(expectedFrom, actualFrom);
		Assert.assertEquals(expectedTo, actualTo);
	}

	/**
	 * To check that functions deposit and withdraw work as expected, the test case follows the steps:
	 * 1. Records initial amount of money on the testAccount (the same as in setUp function)
	 * 2. Performs deposit adding the amount of money recorded in variable 'add'.
	 * 3. Checks that expected balance equals 'initial'+'add'.
	 * 4. Withdraws the 'add' amount.
	 * 5. Checks that current getBalance() equals 'initial' amount of money.
	 *
	 * **/
	@Test
	public void testAddWithdraw() {
		Money initial = new Money(10000000, DKK);
		Money add = new Money(10000, SEK);
		testAccount.deposit(add);
		Assert.assertEquals(testAccount.getBalance(), initial.add(add));
		testAccount.withdraw(add);
		Assert.assertEquals(testAccount.getBalance(), initial);
	}

	/**
	 * To check that function getBalance() works as expected, the test case follows the steps:
	 * 1. Records the amount of money on testAccount (the same amount as given in the setUp function).
	 * 2. Checks that result of getBalance() equals to that expected amount.
	 * **/
	@Test
	public void testGetBalance() {
		Money expected = new Money(10000000, DKK);
		Assert.assertEquals(testAccount.getBalance(), expected);
	}
}
