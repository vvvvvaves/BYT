package main.java;

import java.util.Objects;

public class Item {

    private String item;
    private double price;
    private ConcreteColleague owner;
    private int ownerCode;

    public Item(String item, double price, ConcreteColleague owner) {
        this.item = item;
        this.price = price;
        this.owner = owner;
        ownerCode = owner.getID();
    }

    public ConcreteColleague getOwner() {
        return owner;
    }

    public String getItem() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public void setOwner(ConcreteColleague owner) {
        this.owner = owner;
        ownerCode = owner.getID();
    }

    @Override
    public String toString() {
        return getItem() + " " + getPrice() + " " + getOwner().getID();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item1)) return false;
        return Double.compare(item1.getPrice(), getPrice()) == 0 && ownerCode == item1.ownerCode && Objects.equals(getItem(), item1.getItem());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItem(), getPrice(), getOwner());
    }
}
