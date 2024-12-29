package ServerRMI;

public interface OperationRMI extends java.rmi.Remote {
    double calculateTax(double annualIncome, String status, int jumlahTanggungan) throws java.rmi.RemoteException;
}
