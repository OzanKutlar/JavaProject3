package comp.CMPE343.UserInterface.Buyer;

public class Carrier {
    private String name;
    private String address;
    public int id;

    public int getId() {
        return id;
    }

    public Carrier(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    // Additional methods and properties can be added based on your requirements
}
