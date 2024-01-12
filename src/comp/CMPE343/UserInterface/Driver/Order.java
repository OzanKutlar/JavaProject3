package comp.CMPE343.UserInterface.Driver;

import comp.CMPE343.Logger;
import comp.CMPE343.UserInterface.Buyer.Product;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Order {

    public static Order fromResultSet(ResultSet resultSet){
        Order o = new Order();
        try{
            o.id = resultSet.getInt("id");
            o.adres = resultSet.getString("address");
            o.toBeDelivered = resultSet.getDate("toBeDelivered");
            o.total = resultSet.getDouble("total");
            o.customerName = resultSet.getString("customerName");
            o.products = new ArrayList<>();
            for (String s : resultSet.getString("orderItems").split(",")){
                try{
                    int productID = Integer.parseInt(s.split(":")[0]);
                    double productCount = Double.parseDouble(s.split(":")[1]);
                    Product p = new Product();
                    p.id = productID;
                    p.stock = productCount;
                    o.products.add(p);
                }
                catch(Exception e){
                    Logger.log("Parse Failed");
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return o;
    }

    public int id;
    public String adres;
    public String customerName;

    // Teslimatın ürünleri
    public ArrayList<Product> products;

    // Teslimatın total fiyatı
    public double total;

    // Ne zaman getirilcek bu teslimat?
    public Date toBeDelivered;

    // Sürücü aldı mı bunu?
    public boolean taken;


    @Override
    public String toString() {
        return "Order " + id + " : " + total + " TL, By :  " + customerName + ", to be delivered to : " + adres + " by " + toBeDelivered;
    }
}
