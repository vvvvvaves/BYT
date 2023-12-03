package main.java.Concrete;

import main.java.BinaryRequest;

public class CalculatorRequest extends BinaryRequest<Double> {

    public CalculatorRequest(Double element1, Double element2, String operation) {
        super(element1, element2, operation);
    }
}
