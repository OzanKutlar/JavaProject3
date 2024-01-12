package comp.CMPE343.UserInterface.Buyer;

public class Product {

    public int id;

    public String productName;
    public double stock;
    public double price;
    public double markupNumber;

    public String imageLoc = "../Images/default.png";

    public int getId() {
        return id;
    }
    public void setId(int name) {
        this.id = name;
    }
    public String getName() {
        return productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getThreshold() {
        return stock;
    }

    public void setThreshold(double threshold) {
        this.stock = threshold;
    }
    public Product(){

    }

    public Product(String name, double price, double markup) {
        this.productName = name;
        this.price = price;
        this.markupNumber = markup;
    }
}
