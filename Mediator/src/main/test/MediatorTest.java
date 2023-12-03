package main.test;

import main.java.ConcreteColleague;
import main.java.Item;
import main.java.ConcreteMediator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MediatorTest {

    private ConcreteMediator mediator;
    private ConcreteColleague colleague1, colleague2, colleague3, colleague4;
    private Item item11, item21, item31, item41, requested11, requested21, requested22, requested31, requested41;
    List<ConcreteColleague> expectedResult;
    @Before
    public void before() {
        mediator = new ConcreteMediator();

        colleague1 = new ConcreteColleague(mediator);
        item11 = new Item("Lamp", 40, colleague1);
        requested11 = new Item("Book", 15, colleague1);

        colleague2 = new ConcreteColleague(mediator);
        item21 = new Item("Book", 10.5, colleague2);
        requested21 = new Item("Pen", 4, colleague2);
        requested22 = new Item("Phone", 400, colleague2);

        colleague3 = new ConcreteColleague(mediator);
        item31 = new Item("Phone", 500, colleague3);
        requested31 = new Item("Lamp", 60, colleague3);

        colleague4 = new ConcreteColleague(mediator);
        item41 = new Item("Pen", 1.5, colleague4);
        requested41 = new Item("Phone", 700, colleague4);

        ConcreteColleague.resetCount();

        ConcreteColleague expected1 = new ConcreteColleague(mediator);
        ConcreteColleague expected2 = new ConcreteColleague(mediator);
        ConcreteColleague expected3 = new ConcreteColleague(mediator);
        ConcreteColleague expected4 = new ConcreteColleague(mediator);
        expected1.setBought(List.of(new Item("Book", 10.5, expected1)));
        expected1.setSold(List.of(new Item("Lamp", 40, expected3)));

        expected2.setBought(List.of(new Item("Pen", 1.5, expected2)));
        expected2.setSold(List.of(new Item("Book", 10.5, expected1)));

        expected3.setBought(List.of(new Item("Lamp", 40, expected3)));
        expected3.setSold(List.of(new Item("Phone", 500, expected4)));

        expected4.setBought(List.of(new Item("Phone", 500, expected4)));
        expected4.setSold(List.of(new Item("Pen", 1.5, expected2)));

        expectedResult = List.of(expected1, expected2, expected3, expected4);
    }

    @Test
    public void test() {
        colleague1.sell(item11);
        colleague1.buy(requested11);

        colleague2.sell(item21);
        colleague2.buy(requested21);
        colleague2.buy(requested22);

        colleague3.sell(item31);
        colleague3.buy(requested31);

        colleague4.sell(item41);
        colleague4.buy(requested41);

        Assert.assertEquals(expectedResult, List.of(colleague1, colleague2, colleague3, colleague4));
    }

}
