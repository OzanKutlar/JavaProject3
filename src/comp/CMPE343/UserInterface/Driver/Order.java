package comp.CMPE343.UserInterface.Driver;

import comp.CMPE343.UserInterface.Buyer.Product;

import java.util.ArrayList;
import java.util.Date;

public class Order {

    public int id;
    public String adres;
    public String customerName;

    // Teslimatın ürünleri
    public ArrayList<Product> products;

    // Teslimatın total fiyatı
    public float total;

    // Ne zaman getirilcek bu teslimat?
    public Date toBeDelivered;

}
