package comp.CMPE343.UserInterface.Buyer;

import java.util.UUID;

public class Product {

    public int id;

    public String productName;
    public float stock;
    public double price;
    public double markupNumber;

    public Product(){

    }

    public Product(String name, double price, double markup) {
        this.productName = name;
        this.price = price;
        this.markupNumber = markup;
    }
}
