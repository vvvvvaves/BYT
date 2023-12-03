package main.java;

import java.util.ArrayList;
import java.util.List;

public class ConcreteMediator implements Mediator {

    private List<Item> itemsToSell;
    private List<Item> itemsToBuy;

    public ConcreteMediator() {
        this.itemsToSell = new ArrayList<>();
        this.itemsToBuy = new ArrayList<>();
    }


    /**
     *
     * The logic is as follows:
     *  itemsToSell list contains actual items available for sale, with their actual price and actual owners.
     *  itemsToBuy list contains items with the Colleague who wishes to buy this item in the owner field and the maximum price he/she is willing to give in the price field.
     *  When wished item is bought through the buy function, mediator finds actual suitable item and adds it to "sold" and "bought" lists with the actual price and the new owner.
     *  When actual item is sold through the sell function, mediator finds wished item with high enough payment capability and buys this item for the wished owner,
     *  changing the price to actual lower price and the owner field from the previous owner to the new one (wished owner).
     *
     * Colleagues do not need to communicate. All the items are stored in lists and, if supplied and demanded products match, they are bought/sold and removed from the list.
     * **/

    public void sell(Item actualItem) {
        boolean sold = false;
        Item wishedItem = null;
        for (Item available :
                itemsToBuy) {
            if (actualItem.getItem().equals(available.getItem()) && actualItem.getPrice() <= available.getPrice()) {
                sold = true;
                wishedItem = available;
                break;
            }
        }

        ConcreteColleague newOwner, previousOwner;
        if (sold) {
            itemsToBuy.remove(wishedItem);
            previousOwner = actualItem.getOwner();
            newOwner = wishedItem.getOwner();
            wishedItem.setPrice(actualItem.getPrice());
            newOwner.addToBought(wishedItem);
            previousOwner.addToSold(wishedItem);

        } else {
            itemsToSell.add(actualItem);
        }
    }

    public void buy(Item wishedItem) {
        boolean bought = false;
        Item actualItem = null;
        for (Item available :
                itemsToSell) {
            if (wishedItem.getItem().equals(available.getItem()) && wishedItem.getPrice() >= available.getPrice()) {
                bought = true;
                actualItem = available;
                break;
            }
        }

        ConcreteColleague previousOwner, newOwner;
        if (bought) {
            previousOwner = actualItem.getOwner();
            newOwner = wishedItem.getOwner();
            wishedItem.setPrice(actualItem.getPrice());
            wishedItem.setOwner(newOwner);
            previousOwner.addToSold(wishedItem);
            newOwner.addToBought(wishedItem);
            itemsToSell.remove(actualItem);

        } else {
            itemsToBuy.add(wishedItem);
        }

    }

}
