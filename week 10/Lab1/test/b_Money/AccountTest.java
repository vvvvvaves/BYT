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
	
	@Test
	public void testAddRemoveTimedPayment() throws AccountDoesNotExistException {
		Assert.assertTrue(!testAccount.timedPaymentExists("001"));
		testAccount.addTimedPayment( "001", 10, 0, new Money(0, SEK), SweBank, "Alice");
		Assert.assertTrue(testAccount.timedPaymentExists("001"));
		testAccount.removeTimedPayment("001");
		Assert.assertTrue(!testAccount.timedPaymentExists("001"));

	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {

		String fromaccount = "Hans";
		String toaccount = "Alice";
		Money timedPayment = new Money(10000, DKK);
		Integer interval = 10;
		Integer next = 0;

		testAccount.addTimedPayment( "001", 10, 0, timedPayment, SweBank, toaccount);

		Money initialHans = testAccount.getBalance();
		Money initialAlice = new Money(SweBank.getBalance(toaccount), SEK);
		for (int i = 0; i < 10; i++) {
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

	@Test
	public void testAddWithdraw() {
		Money initial = new Money(10000000, DKK);
		Money add = new Money(10000, SEK);
		testAccount.deposit(add);
		Assert.assertEquals(testAccount.getBalance(), initial.add(add));
		testAccount.withdraw(add);
		Assert.assertEquals(testAccount.getBalance(), initial);
	}
	
	@Test
	public void testGetBalance() {
		Money expected = new Money(10000000, DKK);
		Assert.assertEquals(testAccount.getBalance(), expected);
	}
}
