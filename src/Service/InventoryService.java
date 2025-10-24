package Service;

import Model.Product;
import Service.Provider;
import java.util.*;

public class InventoryService {
    private List<Provider> providers = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private int providerCount = 0;
    private int productCount = 0;

    // Add Provider
    public void addProvider() {
        System.out.print("Enter Provider Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Contact: ");
        String contact = sc.nextLine();
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();

        String providerID = "PR" + (++providerCount);
        providers.add(new Provider(providerID, name, contact, email, address));
        System.out.println("Provider added! ID: " + providerID);
    }

    // Add Product
    public void addProduct() {
        if (providers.isEmpty()) {
            System.out.println("No providers available! Add a provider first.");
            return;
        }

        System.out.print("Enter Product Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Category (Electronics/Clothing/Food/Books): ");
        String category = sc.nextLine();

        System.out.println("\nAvailable Providers:");
        for (Provider p : providers) {
            System.out.println(p.getProviderID() + " - " + p.getName());
        }

        System.out.print("Enter Provider ID: ");
        String pid = sc.nextLine();

        Optional<Provider> providerOpt = providers.stream()
                .filter(p -> p.getProviderID().equalsIgnoreCase(pid))
                .findFirst();

        if (providerOpt.isPresent()) {
            String productID = "P" + (++productCount);
            products.add(new Product(productID, pid, name, price, qty, category));
            System.out.println("Product added! ID: " + productID);
        } else {
            System.out.println("Invalid Provider ID!");
        }
    }

    // Update Stock
    public void updateStock() {
        System.out.print("Enter Product ID: ");
        String pid = sc.nextLine();

        Optional<Product> productOpt = products.stream()
                .filter(p -> p.getProductID().equalsIgnoreCase(pid))
                .findFirst();

        if (productOpt.isPresent()) {
            System.out.print("Enter quantity to add/remove (use negative to remove): ");
            int qty = sc.nextInt();
            sc.nextLine();
            Product p = productOpt.get();
            p.setQuantity(p.getQuantity() + qty);
            System.out.println("Stock updated. New quantity: " + p.getQuantity());
        } else {
            System.out.println("Product not found!");
        }
    }

    // Search Product
    public void searchProduct() {
        System.out.println("Search by: 1. Product ID  2. Product Name");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice == 1) {
            System.out.print("Enter Product ID: ");
            String pid = sc.nextLine();
            products.stream()
                    .filter(p -> p.getProductID().equalsIgnoreCase(pid))
                    .findFirst()
                    .ifPresentOrElse(this::showProductWithProvider,
                            () -> System.out.println("Product not found!"));
        } else {
            System.out.print("Enter Product Name: ");
            String name = sc.nextLine();
            products.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .ifPresentOrElse(this::showProductWithProvider,
                            () -> System.out.println("Product not found!"));
        }
    }

    // View All Products
    public void viewAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            return;
        }

        System.out.printf("%-6s %-15s %-12s %-10s %-8s %-10s\n",
                "PID", "Name", "Category", "Price", "Qty", "Provider");
        for (Product p : products) {
            Provider pr = providers.stream()
                    .filter(s -> s.getProviderID().equalsIgnoreCase(p.getProviderID()))
                    .findFirst().orElse(null);
            System.out.printf("%-6s %-15s %-12s %-10.2f %-8d %-10s\n",
                    p.getProductID(), p.getName(), p.getCategory(),
                    p.getPrice(), p.getQuantity(), (pr != null ? pr.getName() : "Unknown"));
        }
    }

    // Delete Product
    public void deleteProduct() {
        System.out.print("Enter Product ID to delete: ");
        String pid = sc.nextLine();
        boolean removed = products.removeIf(p -> p.getProductID().equalsIgnoreCase(pid));
        System.out.println(removed ? "Product deleted." : "Product not found.");
    }
    // Helper
    private void showProductWithProvider(Product p) {
        System.out.println("\n" + p);
        providers.stream()
                .filter(pr -> pr.getProviderID().equalsIgnoreCase(p.getProviderID()))
                .findFirst()
                .ifPresent(pr -> System.out.println("\nProvider Info:\n" + pr));
    }
}
