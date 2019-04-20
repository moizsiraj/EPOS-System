/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprojectoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.UIManager;

/**
 *
 * @author moizs
 */
public class FinalProjectOOP {

    public static Inventory inventory;
    public static Customer customer;
    public static Cart cart;
    public static File file;
    public static FileOutputStream fo;
    public static FileInputStream fi;
    public static ObjectInputStream input;
    public static ObjectOutputStream output;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, IOException {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /*if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                
                }*/
                UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        FinalProjectOOP.file = new File("file.txt");
        try {
            FinalProjectOOP.fi = new FileInputStream(FinalProjectOOP.file);
            FinalProjectOOP.input = new ObjectInputStream(FinalProjectOOP.fi);
            Cashier moiz = new Cashier("Moiz", "14818", "0311077588", "12345678");
            Cashier.addCashier(moiz);
            Login login = new Login();
            login.setVisible(true);
            inventory = new Inventory();
            while (true) {
                Product x;
                x = (Product) FinalProjectOOP.input.readObject();
                inventory.addProduct(x);
            }
        } catch (IOException | ClassNotFoundException ex) {

            //System.out.println(ex.getMessage());
        }
        FinalProjectOOP.input.close();
    }

}

class Cashier {

    private static ArrayList<Cashier> list = new ArrayList<>();
    private final String name;
    private final String empID;
    private String phoneNumber;
    private String password;

    public Cashier(String name, String empID, String phoneNumber, String password) {
        this.name = name;
        this.empID = empID;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    private int findItem(String empID) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpID().equalsIgnoreCase(empID)) {
                return i;
            }
        }
        return -1;
    }

    public Cashier setItem(String empID) {
        Cashier x;
        if (this.findItem(empID) >= 0) {
            x = list.get(this.findItem(empID));
            return x;
        } else {
            return new Cashier("xxx", "xxx", "0", "0");
        }
    }

    public static void addCashier(Cashier cashier) {
        list.add(cashier);
    }

    public String getName() {
        return name;
    }

    public String getEmpID() {
        return empID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean login(String empID, String password) {
        boolean login = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getEmpID().equalsIgnoreCase(empID)) {
                if (list.get(i).getPassword().equals(password)) {
                    login = true;
                }
            }
        }
        return login;
    }

    public void forgotPass(String phoneNumber, String password) {
        if (this.password.equalsIgnoreCase(this.getPhoneNumber())) {
            this.setPassword(password);
        } else {
            System.out.println("INVALID PHONE NUMBER");
        }
    }

}

abstract class Customer {

    private String name;
    private double payment;
    private double balance;
    private double total;
    Cart cart;

    public Customer(String name, double payment, Cart cart) {
        this.payment = payment;
        this.name = name;
        this.cart = cart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public double getPayment() {
        return payment;
    }

    public double getBalance() {
        return balance;
    }

    public double getTotal() {
        return total;
    }

    public boolean addItem(String barCode, int quantity, Cart cart, Inventory inventory) {
        if (inventory.findItem(barCode) != -1 && inventory.setItem(barCode).getQuantity() >= quantity) {
            cart.addItem(barCode, quantity, inventory);
            inventory.changeQuantityRemove(barCode, quantity);
            return true;
        } else {
            return false;
        }
    }

    public boolean changeItem(String barCode, int quantity, Cart cart, Inventory inventory) {
        if (cart.findItem(barCode) != -1 && inventory.setItem(barCode).getQuantity() >= quantity) {
            cart.changeItem(barCode, quantity);
            inventory.changeQuantityRemove(barCode, quantity);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeItem(String barCode, int quantity, Cart cart, Inventory inventory) {
        if (cart.findItem(barCode) != -1 && cart.setItem(barCode).getQuantity() >= quantity) {
            cart.removeItem(barCode, quantity);
            inventory.changeQuantityAdd(barCode, quantity);
            return true;
        } else {
            return false;
        }
    }

    public boolean pay(Order order, Customer customer, Cart cart, discount Discount) {
        order.total();
        total = Discount.applyDiscount(order);
        if (this.payment < total) {
            return false;
        } else {
            this.balance = this.payment - total;
            return true;
        }
    }
}

class GoldCustomer extends Customer {

    public GoldCustomer(String name, double payment, Cart cart) {
        super(name, payment, cart);
    }
}

class SilverCustomer extends Customer {

    public SilverCustomer(String name, double payment, Cart cart) {
        super(name, payment, cart);
    }
}

class NormCustomer extends Customer {

    public NormCustomer(String name, double payment, Cart cart) {
        super(name, payment, cart);
    }
}

class discount {

    private double disPercent;

    public discount(Customer customer) {
        if (customer instanceof GoldCustomer) {
            this.disPercent = 0.07;
        }
        if (customer instanceof SilverCustomer) {
            this.disPercent = 0.05;
        }
        if (customer instanceof NormCustomer) {
            this.disPercent = 0.00;
        }
    }

    public double amountDiscount(Order order) {
        double amount;
        amount = (order.getTotal() * this.disPercent);
        return amount;
    }

    public double applyDiscount(Order order) {
        double amount;
        amount = order.getTotal() - (order.getTotal() * this.disPercent);
        return amount;
    }

}

class Cart {

    private ArrayList<Product> Cuslist;
    private int noOfItems;

    public Cart() {
        Cuslist = new ArrayList<>();
    }

    public int getNoOfItems() {
        return noOfItems;
    }

    public int findItem(String barCode) {
        for (int i = 0; i < Cuslist.size(); i++) {
            if (Cuslist.get(i).getBarCode().equalsIgnoreCase(barCode) && Cuslist.get(i).getQuantity() > 0) {
                return i;
            }
        }
        return -1;
    }

    public Product setItem(String barCode) {
        Product x;
        if (this.findItem(barCode) >= 0) {
            x = Cuslist.get(this.findItem(barCode));
            return x;
        } else {
            return new Product("xxx", "xxx", 0, 0, false);
        }
    }

    public Product traverse(int i) {
        return this.Cuslist.get(i);
    }

    public int size() {
        return this.Cuslist.size();
    }

    public void addItem(String barCode, int quantity, Inventory inventory) {
        Product x;
        if (this.findItem(barCode) == -1) {
            x = new Product(inventory.setItem(barCode));
            if (x.getName().equalsIgnoreCase("xxx")) {
                System.out.println("ITEM NOT AVAILABLE");
            } else {
                this.Cuslist.add(x);
                this.Cuslist.get(this.Cuslist.indexOf(x)).setQuantity(quantity);
                noOfItems = noOfItems + quantity;
            }
        }
    }

    public void changeItem(String barCode, int quantity) {
        Product x;
        if (this.findItem(barCode) != -1) {
            x = this.setItem(barCode);
            if (x.getName().equalsIgnoreCase("xxx")) {
                System.out.println("ITEM NOT AVAILABLE");
            } else {
                this.Cuslist.get(this.Cuslist.indexOf(x)).changeQuantityAdd(quantity);
                noOfItems = noOfItems + quantity;
            }
        }
    }

    public void removeItem(String barCode, int quantity) {
        Product x;
        x = this.setItem(barCode);
        if (x.getName().equalsIgnoreCase("xxx")) {
            System.out.println("ITEM NOT AVAILABLE");
        } else {
            if (this.findItem(barCode) != -1 && this.setItem(barCode).getQuantity() >= quantity) {
                this.Cuslist.get(this.Cuslist.size() - 1).changeQuantityRemove(quantity);
                noOfItems = noOfItems - quantity;
            } else {
                System.out.println("INVALID OPERATION LESS ITEMS IN CART");
            }
        }
    }

    public void itemDetail() {
        for (int i = 0; i < Cuslist.size(); i++) {
            if (Cuslist.get(i).getQuantity() > 0) {
                System.out.print(Cuslist.get(i).getName() + "\t" + Cuslist.get(i).getPrice() + "\t" + Cuslist.get(i).getQuantity() + "\n");
            }
        }
    }

    public double total() {
        double sum = 0;
        for (int i = 0; i < this.Cuslist.size(); i++) {
            sum = sum + (this.Cuslist.get(i).getPrice() * this.Cuslist.get(i).getQuantity());
        }
        return sum;
    }

    public String cartReport() {
        String report = "";
        System.out.println("CART REPORT");
        System.out.println("Name\tQuantity\tPrice\tBarCode\tStatus\t");
        for (int i = 0; i < this.Cuslist.size(); i++) {
            if (this.Cuslist.get(i).getQuantity() > 0) {
                report = report + this.Cuslist.get(i).getName() + "\t" + this.Cuslist.get(i).getQuantity() + "\t\t" + this.Cuslist.get(i).getPrice() + "\t" + this.Cuslist.get(i).getBarCode() + "\t" + this.Cuslist.get(i).isStatus();

            }
        }
        return report;
    }
}

class Inventory {

    private ArrayList<Product> stock;
    private static int noOfProduct;

    public Inventory() {
        stock = new ArrayList<>();
    }

    public int findItem(String barCode) {
        for (int i = 0; i < stock.size(); i++) {
            if (stock.get(i).getBarCode().equalsIgnoreCase(barCode)) {
                return i;
            }
        }
        return -1;
    }

    public Product setItem(String barCode) {
        Product x;
        if (this.findItem(barCode) >= 0) {
            x = stock.get(this.findItem(barCode));
            return x;
        } else {
            return new Product("xxx", "xxx", 0, 0, false);
        }
    }

    public Product traverse(int i) {
        return this.stock.get(i);
    }

    public void addProduct(String name, String barCode, int quantity, double price, boolean status) {
        stock.add(new Product(name, barCode, quantity, price, status));
        noOfProduct++;
    }

    public void addProduct(Product product) {
        stock.add(new Product(product.getName(), product.getBarCode(), product.getQuantity(), product.getPrice(), true));
        noOfProduct++;
    }

    public void removeProduct(String barCode) {
        Product x = this.setItem(barCode);
        this.stock.remove(x);
        noOfProduct--;
    }

    public void changeQuantityAdd(String barCode, int quantity) {
        this.findItem(barCode);
        Product x = this.setItem(barCode);
        x.changeQuantityAdd(quantity);
        x.setStatus();
    }

    public void changeQuantityRemove(String barCode, int quantity) {
        this.findItem(barCode);
        Product x = this.setItem(barCode);
        if (x.getQuantity() >= quantity) {
            x.changeQuantityRemove(quantity);
            x.setStatus();
        } else {
            System.out.println("INVALID OPERATION LESS ITEMS IN INVENTORY");
        }
    }

    public void inventoryReport() {
        System.out.println("INVENTORY REPORT");
        System.out.println("Name\tQuantity\tPrice\tBarCode\tStatus\t");
        for (int i = 0; i < this.stock.size(); i++) {
            System.out.println(this.stock.get(i).getName() + "\t" + this.stock.get(i).getQuantity() + "\t\t" + this.stock.get(i).getPrice() + "\t" + this.stock.get(i).getBarCode() + "\t" + this.stock.get(i).isStatus());
        }
    }

    public static int getNoOfProduct() {
        return noOfProduct;
    }

}

class Product implements Serializable {

    private final String name;
    private final String barCode;
    private int quantity;
    private double price;
    private boolean status;

    public Product(String name, String barCode, int quantity, double price, boolean status) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.barCode = barCode;
    }

    public Product(Product x) {
        this.name = x.name;
        this.quantity = x.quantity;
        this.price = x.price;
        this.status = x.status;
        this.barCode = x.barCode;
    }

    public String getName() {
        return name;
    }

    public String getBarCode() {
        return barCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatus() {
        if (this.quantity <= 0) {
            this.status = false;
        } else {
            this.status = true;
        }
    }

    public void changeQuantityAdd(int quantity) {
        this.quantity = this.quantity + quantity;
    }

    public void changeQuantityRemove(int quantity) {
        this.quantity = this.quantity - quantity;
    }
}

class Order {

    private double total;
    private final int noOfItems;
    private final Cart cart;

    public Order(Cart cart) {
        this.cart = cart;
        this.noOfItems = cart.getNoOfItems();
    }

    public void total() {
        total = cart.total();
    }

    public double getTotal() {
        return total;
    }

    public int getNoOfItems() {
        return noOfItems;
    }

}
