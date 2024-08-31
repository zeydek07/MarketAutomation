package sample;

public class Product {
    private String p_name, p_barcode;
    private int p_price, p_amount;

    public Product(String p_name, String p_barcode, int p_price, int p_amount) {
        this.p_name = p_name;
        this.p_barcode = p_barcode;
        this.p_price = p_price;
        this.p_amount = p_amount;
    }

    public Product(Product other){
        this.p_name = other.getP_name();
        this.p_barcode = other.getP_barcode();
        this.p_price = other.getP_price();
        this.p_amount = other.getP_amount();
    }
    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_barcode() {
        return p_barcode;
    }

    public void setP_barcode(String p_barcode) {
        this.p_barcode = p_barcode;
    }

    public int getP_price() {
        return p_price;
    }

    public void setP_price(int p_price) {
        this.p_price = p_price;
    }

    public int getP_amount() {
        return p_amount;
    }

    public void setP_amount(int p_amount) {
        this.p_amount = p_amount;
    }


}