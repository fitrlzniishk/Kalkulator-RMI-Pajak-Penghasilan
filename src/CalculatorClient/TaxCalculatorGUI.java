package CalculatorClient;

import ServerRMI.OperationRMI;
import java.rmi.Naming;
import java.text.DecimalFormat;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaxCalculatorGUI {

    private JFrame frame;
    private JTextField incomeField;
    private JComboBox<String> statusDropdown;
    private JTextArea resultArea;
    private JButton calculateButton;
    private JPanel tanggunganQueuePanel;  // Panel antrean untuk jumlah tanggungan
    private int tanggunganCount = 0;  // Untuk melacak jumlah tanggungan yang telah ditambahkan
    private JLabel tanggunganLabel;  // Label untuk jumlah tanggungan

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                TaxCalculatorGUI window = new TaxCalculatorGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TaxCalculatorGUI() {
        // Pengaturan Frame
        frame = new JFrame("Kalkulator Pajak dengan Tombol Angka");
        frame.setBounds(100, 100, 800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));
        headerPanel.setPreferredSize(new Dimension(frame.getWidth(), 60));
        JLabel titleLabel = new JLabel("Kalkulator Pajak", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Sans Serif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Panel Konten Utama
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        frame.add(contentPanel, BorderLayout.CENTER);

        // Input Field dan Tombol Angka
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Label Input
        JLabel incomeLabel = new JLabel("Masukkan Gaji Perbulan:");
        incomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        incomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        inputPanel.add(incomeLabel, BorderLayout.NORTH);

        // Bidang Input
        incomeField = new JTextField();
        incomeField.setFont(new Font("Arial", Font.PLAIN, 24));
        incomeField.setEditable(false);
        inputPanel.add(incomeField, BorderLayout.CENTER);

        // Tombol Angka
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int i = 1; i <= 9; i++) {
            int num = i;
            JButton btn = new JButton(String.valueOf(num));
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(e -> incomeField.setText(incomeField.getText() + num));
            keypadPanel.add(btn);
        }

        JButton btnClear = new JButton("C");
        btnClear.setFont(new Font("Arial", Font.BOLD, 20));
        btnClear.setBackground(new Color(255, 69, 58));
        btnClear.setForeground(Color.WHITE);
        btnClear.addActionListener(e -> incomeField.setText(""));
        keypadPanel.add(btnClear);

        JButton btnZero = new JButton("0");
        btnZero.setFont(new Font("Arial", Font.BOLD, 20));
        btnZero.addActionListener(e -> incomeField.setText(incomeField.getText() + "0"));
        keypadPanel.add(btnZero);

        JButton btnBackspace = new JButton("<");
        btnBackspace.setFont(new Font("Arial", Font.BOLD, 20));
        btnBackspace.addActionListener(e -> {
            String text = incomeField.getText();
            if (!text.isEmpty()) {
                incomeField.setText(text.substring(0, text.length() - 1));
            }
        });
        keypadPanel.add(btnBackspace);

        inputPanel.add(keypadPanel, BorderLayout.SOUTH);
        contentPanel.add(inputPanel, BorderLayout.NORTH);

        // Dropdown Status dan Jumlah Tanggungan
        JPanel additionalInputPanel = new JPanel(new BorderLayout());
        additionalInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Dropdown Status
        String[] statuses = {"Pilih Status", "Lajang", "Menikah"};
        statusDropdown = new JComboBox<>(statuses);
        statusDropdown.setFont(new Font("Arial", Font.PLAIN, 18));
        additionalInputPanel.add(statusDropdown, BorderLayout.NORTH);

        // Panel antrean jumlah tanggungan
        tanggunganQueuePanel = new JPanel();
        tanggunganQueuePanel.setLayout(new FlowLayout());
        tanggunganQueuePanel.setVisible(false);

        tanggunganLabel = new JLabel("Jumlah Tanggungan: 0");
        tanggunganLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        tanggunganQueuePanel.add(tanggunganLabel);

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.addActionListener(e -> {
            if (tanggunganCount < 3) {
                tanggunganCount++;
                tanggunganLabel.setText("Jumlah Tanggungan: " + tanggunganCount);
            }
        });
        tanggunganQueuePanel.add(addButton);

        JButton subtractButton = new JButton("-");
        subtractButton.setFont(new Font("Arial", Font.BOLD, 20));
        subtractButton.addActionListener(e -> {
            if (tanggunganCount > 0) {
                tanggunganCount--;
                tanggunganLabel.setText("Jumlah Tanggungan: " + tanggunganCount);
            }
        });
        tanggunganQueuePanel.add(subtractButton);

        additionalInputPanel.add(tanggunganQueuePanel, BorderLayout.SOUTH);
        inputPanel.add(additionalInputPanel, BorderLayout.EAST);

        // Mengupdate tampilan berdasarkan status yang dipilih
        statusDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStatus = (String) statusDropdown.getSelectedItem();
                if ("Menikah".equals(selectedStatus)) {
                    tanggunganQueuePanel.setVisible(true);  // Tampilkan panel tanggungan jika Menikah
                } else {
                    tanggunganQueuePanel.setVisible(false);  // Sembunyikan panel tanggungan jika bukan Menikah
                    tanggunganCount = 0;  // Reset jumlah tanggungan
                    tanggunganLabel.setText("Jumlah Tanggungan: 0");
                }
            }
        });

        // Area Hasil
        resultArea = new JTextArea();
        resultArea.setFont(new Font("Arial", Font.PLAIN, 16));
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Tombol Hitung Pajak
        calculateButton = new JButton("Hitung Pajak");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.setBackground(new Color(0, 123, 255));
        calculateButton.setForeground(Color.WHITE);
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        try {
            double penghasilanBulanan = Double.parseDouble(incomeField.getText());
            double penghasilanTahunan = penghasilanBulanan * 12;
            String status = (String) statusDropdown.getSelectedItem();

            //cek apakah status masih dipiliha "Pilih Status"
            if ("Pilih Status".equals(status)){
                resultArea.setText("Pilih status perorangan terlebih dahulu");
                return;
            }
            // Ganti dengan IP server RMI
            String serverIP = "localhost";  // Ganti dengan alamat IP server RMI
            OperationRMI kalkulatorPajak = (OperationRMI) Naming.lookup("rmi:///TaxCalculator");

            double pajak = kalkulatorPajak.calculateTax(penghasilanTahunan, status, tanggunganCount);
            
            // Menambahkan informasi PTKP ke dalam hasil
            double ptkp = 0;
            if ("Lajang".equalsIgnoreCase(status)) {
                ptkp = 54000000;
            } else if ("Menikah".equalsIgnoreCase(status)) {
                ptkp = 58500000 + (tanggunganCount * 4500000);
            }
            
            //Menghitung pkp
            double pkp = penghasilanTahunan - ptkp;
            if (pkp < 0); //Jika PKP kurang dari 0, set PKP menjadi 0
            

            // Tentukan golongan tarif pajak
            String golonganPajak;
            if (pkp <= 0) {
                golonganPajak = "Tidak perlu membayar pajak";
            }else if (pkp <= 60000000 ) {
                golonganPajak = "5%";
            } else if (pkp <= 250000000) {
                golonganPajak = "15%";
            } else if (pkp <= 500000000){
                golonganPajak = "25%"; 
            } else if (pkp <= 1000000000) {
                golonganPajak = "30%";
            }else { 
                golonganPajak = "35%";
            }
            
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            StringBuilder hasil = new StringBuilder();
            hasil.append("Penghasilan Tahunan: Rp ").append(formatter.format(penghasilanTahunan)).append("\n");
            hasil.append("Penghasilan Tidak Kena Pajak (PTKP): Rp ").append(formatter.format(ptkp)).append("\n");
            hasil.append("Penghasilan Kena Pajak (PKP): Rp ").append(formatter.format(pkp)).append("\n");            
            hasil.append("Pajak yang Harus Dibayar: Rp ").append(formatter.format(pajak)).append("\n");
            hasil.append("Golongan Pajak: ").append(golonganPajak).append("\n");

            resultArea.setText(hasil.toString());

        } catch (Exception ex) {
            resultArea.setText("Terjadi kesalahan: " + ex.getMessage());
        }
    }
});
        contentPanel.add(calculateButton, BorderLayout.SOUTH);
    }
}
