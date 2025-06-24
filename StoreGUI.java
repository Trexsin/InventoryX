package view;

import model.*;
import controller.*;
import utils.*;

import javax.swing.table.TableRowSorter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class StoreGUI extends JFrame {
    private JTextField nameField, priceField, quantityField, searchField;
    private JComboBox<String> categoryBox;
    private JTable table;
    private DefaultTableModel model;
    private ItemManager manager = new ItemManager();

    public StoreGUI() {
        setTitle("Store Inventory System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        categoryBox = new JComboBox<>(new String[]{"Grocery", "Electronic"});
        searchField = new JTextField();

        form.add(new JLabel("Item Name:"));
        form.add(nameField);
        form.add(new JLabel("Price:"));
        form.add(priceField);
        form.add(new JLabel("Quantity:"));
        form.add(quantityField);
        form.add(new JLabel("Category:"));
        form.add(categoryBox);
        form.add(new JLabel("Search by Name:"));
        form.add(searchField);

        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Category", "Price", "Qty"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);
        add(buttons, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addItem());
        updateBtn.addActionListener(e -> updateItem());
        deleteBtn.addActionListener(e -> deleteItem());
        clearBtn.addActionListener(e -> clearFields());
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        loadItems();
    }

    private void addItem() {
        String name = nameField.getText();
        String priceText = priceField.getText();
        String qtyText = quantityField.getText();
        String category = (String) categoryBox.getSelectedItem();

        if (name.isEmpty() || !Validator.isValidNumber(priceText) || !Validator.isValidInt(qtyText)) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
            return;
        }

        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);

        Item item = category.equals("Grocery") 
            ? new GroceryItem(name, price, qty) 
            : new ElectronicItem(name, price, qty);

        try {
            manager.save(item);
            loadItems();
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = (String) model.getValueAt(row, 0);
        String name = nameField.getText();
        String priceText = priceField.getText();
        String qtyText = quantityField.getText();
        String category = (String) categoryBox.getSelectedItem();

        if (name.isEmpty() || !Validator.isValidNumber(priceText) || !Validator.isValidInt(qtyText)) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
            return;
        }

        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);

        Item updated = category.equals("Grocery")
            ? new GroceryItem(id, name, price, qty)
            : new ElectronicItem(id, name, price, qty);

        try {
            manager.update(updated);
            loadItems();
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        String id = (String) model.getValueAt(row, 0);

        try {
            manager.delete(id);
            loadItems();
            clearFields();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        categoryBox.setSelectedIndex(0);
        table.clearSelection();
    }

    private void loadItems() {
        model.setRowCount(0);
        try {
            for (Item i : manager.loadAll()) {
                model.addRow(new Object[]{i.getId(), i.getName(), i.getCategory(), i.getPrice(), i.getQuantity()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterTable() {
        String keyword = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
    }
}
