package ServerRMI;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class Services extends UnicastRemoteObject implements OperationRMI {

    protected Services() throws RemoteException {
        super();
    }

    @Override
    public double calculateTax(double annualIncome, String status, int tanggungan) throws RemoteException {
        double tax = 0;
        double ptkp = 0; //penghasilan tidak kena pajak
        
        //untuk menentukan penghasilan kena pajak
        // PKP = Penghasilan Tahunan - PTKP = Sisa penghasilan yang kena pajak 
        // Sisa penghasilan kena pajak x Tarif Progresif Pajak
        
        
        // Menghitung PTKP berdasarkan status perorangan
        if (status.equalsIgnoreCase("Lajang")) {
            ptkp = 54000000;  // gaji pertahun yang belum masuk dalam kategori PTKP (bagi yang masih lajang)
        } else if (status.equalsIgnoreCase("Menikah")) {
            ptkp = 58500000 + (tanggungan * 4500000);  // PTKP untuk Menikah + tanggungan
        }

        // Mengurangi PTKP dari penghasilan tahunan
        double pkp = annualIncome - ptkp;

        if (pkp <= 0) {
            // Jika penghasilan setelah PTKP kurang dari atau sama dengan 0
            return tax;
        } else if (pkp < 60000000) {
            // Penghasilan tahunan kurang dari atau sama dengan 60 juta
            tax = pkp * 0.05;
        } else if (pkp <= 250000000) {
            // Penghasilan tahunan antara 60 juta hingga 250 juta
            tax = pkp * 0.15;
        } else if (pkp <= 500000000) {
            // Penghasilan tahunan antara 250 juta hingga 500 juta
            tax = pkp * 0.25;
        } else if (pkp <= 1000000000) {
            // Penghasilan tahunan antara 500 juta hingga 1 miliar
            tax = pkp * 0.30;
        } else {
            // Penghasilan tahunan lebih dari 1 miliar
            tax = pkp * 0.35;
        }

        return tax;
    }
}