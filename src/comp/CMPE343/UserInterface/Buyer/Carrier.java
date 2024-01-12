package comp.CMPE343.UserInterface.Buyer;

public class Carrier {
    public String name;
    public String password;
    public int id = -1;

    public int getId() {
        return id;
    }

    public Carrier(String name, String password) {
        this.name = name;
        this.password = password;
    }


    @Override
    public String toString() {
        if(id != -1){
            return "Driver " + id + " : " + name + "-" + password;
        }
        else{
            return "New Driver : " + name + "-" + password;
        }
    }
}
