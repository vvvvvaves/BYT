package b_Money;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		Integer expected = 20000;
		Assert.assertEquals(expected, SEK200.getAmount());
	}

	@Test
	public void testGetCurrency() {
		Assert.assertEquals(SEK, SEK200.getCurrency());
	}

	@Test
	public void testToString() {
		Assert.assertEquals(SEK200.toString(), "200 SEK");
	}

	@Test
	public void testGlobalValue() {
		Integer expected = (int)(SEK200.getAmount()*SEK.getRate());
		Assert.assertEquals(SEK200.universalValue(), expected);
	}

	@Test
	public void testEqualsMoney() {
		Money equalInDKK = new Money(DKK.valueInThisCurrency(SEK200.getAmount(), SEK), DKK);
		Assert.assertEquals(equalInDKK.universalValue(), SEK200.universalValue());
		Assert.assertEquals(SEK200, equalInDKK);
	}

	@Test
	public void testAdd() {
		Money equalInDKK = new Money(DKK.valueInThisCurrency(SEK200.getAmount(), SEK), DKK);
		Money actualSEK400 = SEK200.add(equalInDKK);
		Money expectedSEK400 = 	new Money(40000, SEK);
		Assert.assertEquals(actualSEK400, expectedSEK400);
	}

	@Test
	public void testSub() {
		Money equalInDKK = new Money(DKK.valueInThisCurrency(SEK200.getAmount(), SEK), DKK);
		Money actualSEK0 = SEK200.sub(equalInDKK);
		Money expectedSEK0 = SEK0;
		Assert.assertEquals(actualSEK0, (expectedSEK0));
	}

	@Test
	public void testIsZero() {
		Assert.assertTrue(SEK0.isZero() && SEK0.getAmount() == 0);
		Assert.assertTrue(!SEK200.isZero() && SEK200.getAmount() > 0);
		Money debt = new Money(-20000, SEK);
		Assert.assertTrue(!debt.isZero() && debt.getAmount() < 0);

	}

	@Test
	public void testNegate() {
		Assert.assertEquals(SEK200.negate(), new Money(-20000, SEK));
	}

	@Test
	public void testCompareTo() {
		Assert.assertTrue(SEK200.compareTo(SEK0) > 0);
		Assert.assertTrue(SEK0.compareTo(SEK200) < 0);
		Money equalInDKK = new Money(DKK.valueInThisCurrency(SEK200.getAmount(), SEK), DKK);
		assertEquals(0, SEK200.compareTo(equalInDKK));
	}
}
