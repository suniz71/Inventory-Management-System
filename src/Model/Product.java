package Model;

public class Product {
    private String productID;
    private String providerID;
    private String name;
    private double price;
    private int quantity;
    private String category;

    public Product(String productID, String providerID, String name, double price, int quantity, String category) {
        this.productID = productID;
        this.providerID = providerID;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getProductID() { return productID; }
    public String getProviderID() { return providerID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Product ID: " + productID +
                "\nName: " + name +
                "\nCategory: " + category +
                "\nPrice: " + price +
                "\nQuantity: " + quantity;
    }
}
