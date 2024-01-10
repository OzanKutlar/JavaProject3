package comp.CMPE343.UserInterface.Driver;

import comp.CMPE343.UserInterface.Buyer.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Order {

    UUID id;
    String adres;
    String customerName;

    // Teslimatın ürünleri
    ArrayList<Product> products;

    // Teslimatın total fiyatı
    float total;

    // Ne zaman getirilcek bu teslimat?
    Date toBeDelivered;

}
