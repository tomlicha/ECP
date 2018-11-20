package enib.ecp;

/**
 * Created by Tom on 30/04/2017.
 */

public class Client {
    private String id;
    private int balance;
    public Client(String id,int balance){
        this.setId(id);
        this.setBalance(balance);
    }
    public int getBalance() {
        return balance;
    }

    public void setBalance(int banlance) {
        this.balance = banlance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
