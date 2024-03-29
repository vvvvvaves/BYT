package b_Money;

public class Currency {
	private String name;
	private Double rate;
	
	/**
	 * New Currency
	 * The rate argument of each currency indicates that Currency's "universal" exchange rate.
	 * Imagine that we define the rate of each currency in relation to some universal currency.
	 * This means that the rate of each currency defines its value compared to this universal currency.
	 *
	 * @param name The name of this Currency
	 * @param rate The exchange rate of this Currency
	 */
	Currency (String name, Double rate) {
		this.name = name;
		this.rate = rate;
	}

	/** Convert an amount of this Currency to its value in the general "universal currency"
	 * (As mentioned in the documentation of the Currency constructor)
	 * 
	 * @param amount An amount of cash of this currency.
	 * @return The value of amount in the "universal currency"
	 */

	/**
	 * IMPLEMENTED: Because it was not specified, I assumed that converting money to universal value means multiplying the amount
	 * by the currency rate and converting the result to Integer (the float values that get cut are the third and fourth order).
	 * **/
	public int universalValue(Integer amount) {
		return (int)(amount*rate);
	}

	/** Get the name of this Currency.
	 * @return name of Currency
	 *
	 * IMPLEMENTED: Added a return statement.
	 */
	public String getName() {
		return name;
	}
	
	/** Get the rate of this Currency.
	 * 
	 * @return rate of this Currency
	 *
	 * IMPLEMENTED: Added a return statement.
	 */
	public Double getRate() {
		return rate;
	}
	
	/** Set the rate of this currency.
	 * 
	 * @param rate New rate for this Currency
	 *
	 * IMPLEMENTED: Implemented set method.
	 */
	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	/** Convert an amount from another Currency to an amount in this Currency
	 * 
	 * @param amount Amount of the other Currency
	 * @param othercurrency The other Currency
	 *
	*/

	/**
	 * IMPLEMENTED:
	 * Because it was not specified, I assumed that converting money to the other currency means dividing the amount by its
	 * current currency and multiplying by the new one (refer to universalValue function).
	 * **/
	public int valueInThisCurrency(Integer amount, Currency othercurrency) {
		return (int)(amount*othercurrency.rate/this.rate);
	}
}
