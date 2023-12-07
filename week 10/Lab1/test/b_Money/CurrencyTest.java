package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals(SEK.getName(), "SEK");
		assertEquals(DKK.getName(), "DKK");
		assertEquals(EUR.getName(), "EUR");
	}
	
	@Test
	public void testGetRate() {
		assertEquals(SEK.getRate(), 0.15, 0.000000001);
		assertEquals(DKK.getRate(), 0.20, 0.000000001);
		assertEquals(EUR.getRate(), 1.5, 0.000000001);
	}
	
	@Test
	public void testSetRate() {
		NOK = new Currency("NOK", 0.5);
		NOK.setRate(1.222);
		assertEquals(NOK.getRate(), 1.222, 0.00000000000001);
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(SEK.universalValue(200050), (int)(0.15*200050));
		assertEquals(DKK.universalValue(30775), (int)(0.20*30775));
		assertEquals(EUR.universalValue(5), (int)(1.5*5));
	}
	
	@Test
	public void testValueInThisCurrency() {
		Money example = new Money(10000, DKK);
		assertEquals(SEK.valueInThisCurrency(example.getAmount(), example.getCurrency()),
				(int)(example.getAmount()* example.getCurrency().getRate()/ SEK.getRate()));
	}

}
