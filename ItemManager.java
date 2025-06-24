package controller;

import model.*;
import java.io.*;
import java.util.*;

public class ItemManager {
    private final String FILE = "items.csv";

    public void save(Item item) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true));
        bw.write(item.toCSV());
        bw.newLine();
        bw.close();
    }

    public List<Item> loadAll() throws IOException {
        List<Item> list = new ArrayList<>();
        File file = new File(FILE);
        if (!file.exists()) return list;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                String id = parts[0], name = parts[1], category = parts[2];
                double price = Double.parseDouble(parts[3]);
                int qty = Integer.parseInt(parts[4]);

                Item item = category.equalsIgnoreCase("Grocery") 
                    ? new GroceryItem(id, name, price, qty) 
                    : new ElectronicItem(id, name, price, qty);

                list.add(item);
            }
        }
        br.close();
        return list;
    }

    public void update(Item updatedItem) throws IOException {
        List<Item> items = loadAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Item i : items) {
                if (i.getId().equals(updatedItem.getId()))
                    bw.write(updatedItem.toCSV());
                else
                    bw.write(i.toCSV());
                bw.newLine();
            }
        }
    }

    public void delete(String id) throws IOException {
        List<Item> items = loadAll();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE))) {
            for (Item i : items) {
                if (!i.getId().equals(id)) {
                    bw.write(i.toCSV());
                    bw.newLine();
                }
            }
        }
    }
}
