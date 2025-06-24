package model;

public class ElectronicItem extends Item {
    public ElectronicItem(String id, String name, double price, int quantity) {
        super(id, name, price, quantity);
    }

    public ElectronicItem(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    @Override
    public String getCategory() {
        return "Electronic";
    }
}
