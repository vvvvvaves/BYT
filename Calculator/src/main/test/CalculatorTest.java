package main.test;

import main.java.BaseHandler;
import main.java.Concrete.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

    private BaseHandler<Double, Double> beginningOfTheChain;
    private AdditionHandler additionHandler;
    private SubtractionHandler subtractionHandler;
    private MultiplicationHandler multiplicationHandler;
    private DivisionHandler divisionHandler;
    private CalculatorRequest numbers;

    @Before
    public void before() {
        numbers = new CalculatorRequest(16.75, 24.3,"/");

        additionHandler = new AdditionHandler();
        subtractionHandler = new SubtractionHandler();
        multiplicationHandler = new MultiplicationHandler();
        divisionHandler = new DivisionHandler();

        beginningOfTheChain = additionHandler;
        additionHandler.setNext(subtractionHandler);
        subtractionHandler.setNext(multiplicationHandler);
        multiplicationHandler.setNext(divisionHandler);
    }

    @Test
    public void handle() {
        Double result = beginningOfTheChain.handle(numbers);
        Assert.assertEquals(result, 16.75/24.3, 0.000001);
    }
}
