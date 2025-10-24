package main;

import Service.InventoryService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InventoryService service = new InventoryService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Inventory Management System =====");
            System.out.println("1. Add Provider");
            System.out.println("2. Add Product");
            System.out.println("3. Update Stock");
            System.out.println("4. Search Product");
            System.out.println("5. View All Products");
            System.out.println("6. Delete Product");
            System.out.println("7. Exit");
            System.out.print("Enter your Next Step No: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: service.addProvider(); break;
                case 2: service.addProduct(); break;
                case 3: service.updateStock(); break;
                case 4: service.searchProduct(); break;
                case 5: service.viewAllProducts(); break;
                case 6: service.deleteProduct(); break;
                case 7: System.out.println("Exiting..."); System.exit(0);
                default: System.out.println("Invalid choice!");
            }
        }
    }
}
