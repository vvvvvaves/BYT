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

	@Test
	public void testGetName() {
		assertEquals("DanskeBank", DanskeBank.getName());
		assertEquals("Nordea", Nordea.getName());
		assertEquals("SweBank", SweBank.getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(DKK, DanskeBank.getCurrency());
		assertEquals(SEK, Nordea.getCurrency());
		assertEquals(SEK, SweBank.getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException {
		Assert.assertThrows(AccountExistsException.class, () -> {
			DanskeBank.openAccount("Gertrud");
		});

		boolean opened = DanskeBank.openAccount("Vika");
		Assert.assertTrue(opened);

	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException, AccountExistsException {

		Money money = new Money(200050, DKK);
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.deposit("SomeNonExistent", money);
		});

		Integer expectedAmount = 200050;
		DanskeBank.deposit("Gertrud", money);
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException, AccountExistsException {
		Money money = new Money(200050, DKK);
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.withdraw("SomeNonExistent", money);
		});

		Integer expectedAmount = 0;
		DanskeBank.deposit("Gertrud", money);
		DanskeBank.withdraw("Gertrud", money);
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException, AccountExistsException {
		Assert.assertThrows(AccountDoesNotExistException.class, () -> {
			DanskeBank.getBalance("SomeNonExistent");
		});

		Integer expectedAmount = 200050;
		DanskeBank.deposit("Gertrud", new Money(200050, DKK));
		Integer actual = DanskeBank.getBalance("Gertrud");
		Assert.assertEquals(expectedAmount, actual);
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException, AccountExistsException {
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

		Integer expectedTo = SweBank.getBalance("Bob") +  SEK.valueInThisCurrency(timedPayment.getAmount(), timedPayment.getCurrency());
		for (int i = 0; i < 10; i++) {
			DanskeBank.tick();
		}

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
