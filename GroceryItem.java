package model;

public class GroceryItem extends Item {
    public GroceryItem(String id, String name, double price, int quantity) {
        super(id, name, price, quantity);
    }

    public GroceryItem(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    @Override
    public String getCategory() {
        return "Grocery";
    }
}
