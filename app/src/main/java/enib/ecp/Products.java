package enib.ecp;

/**
 * Created by Tom on 27/04/2017.
 */

public class Products {
    private String name, price, majority;

    int quantite=0;
    public Products(String name, String price, String majority){
        this.setName(name);
        this.setPrice(price);
        this.setMajority(majority);

    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMajority() {
        return majority;
    }

    public void setMajority(String majority) {
        this.majority = majority;
    }
}
