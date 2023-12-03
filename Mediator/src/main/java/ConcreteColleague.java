package main.java;

import java.util.ArrayList;
import java.util.List;

public class ConcreteColleague implements Colleague {

    private ConcreteMediator salesMediator;

    private List<Item> bought;
    private List<Item> sold;
    private static int colleagueCount = 0;
    private int ID;

    public ConcreteColleague(ConcreteMediator salesMediator) {
        this.salesMediator = salesMediator;
        bought = new ArrayList<>();
        sold = new ArrayList<>();
        ID = ++colleagueCount;
    }

    public void sell(Item item) {
        salesMediator.sell(item);
    }

    public void buy(Item item) {
        salesMediator.buy(item);
    }


    public void addToBought(Item item) {
        bought.add(item);
    }

    public void addToSold(Item item) {
        sold.add(item);
    }

    public int getID() {
        return ID;
    }

    public void setBought(List<Item> bought) {
        this.bought = bought;
    }

    public void setSold(List<Item> sold) {
        this.sold = sold;
    }

    @Override
    public boolean equals(Object obj) {
        ConcreteColleague c = (ConcreteColleague) obj;
        return this.sold.equals(c.sold) && this.bought.equals(c.bought)
                 && this.salesMediator.equals(c.salesMediator);
    }

    public static void resetCount() {
        colleagueCount = 0;
    }

    @Override
    public String toString() {
        return ID + ", sold: " + sold + ", bought: " + bought;
    }
}
