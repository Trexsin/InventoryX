package model;

public abstract class Item {
    protected String id;
    protected String name;
    protected double price;
    protected int quantity;

    public Item(String id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Item(String name, double price, int quantity) {
        this(String.valueOf(System.currentTimeMillis()), name, price, quantity);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public abstract String getCategory();

    public String toCSV() {
        return String.join(",", id, name, getCategory(), String.valueOf(price), String.valueOf(quantity));
    }
}
