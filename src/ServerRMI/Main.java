package ServerRMI;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) {
        try {
            // Membuat registry pada port 1099
            LocateRegistry.createRegistry(1099);

            // Membuat instance service
            Services taxService = new Services();

            // Mendaftarkan service ke registry dengan nama "TaxCalculator"
            Naming.rebind("rmi://localhost/TaxCalculator", taxService);

            System.out.println("Server RMI siap dan berjalan...");
        } catch (Exception e) {
            System.err.println("Server RMI gagal dijalankan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
