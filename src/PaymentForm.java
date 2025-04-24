import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentForm extends JFrame {
    private JLabel movieLabel, theaterLabel, dateLabel, timeLabel, ticketLabel, fareLabel;
    private JRadioButton creditCard, debitCard, netBanking, paytmWallet;
    private JButton makePayment, goBack;
    private ButtonGroup paymentGroup;
    private JPanel paymentMethodPanel;

    public PaymentForm(String username, String movie, String theater, 
                      String date, String time, String tickets, int fare) {
        setTitle("Payment Details");
        setSize(700, 500); // Reduced height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 240, 240));
        setLayout(null);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 700, 50);
        headerPanel.setBackground(new Color(50, 50, 100));
        headerPanel.setLayout(null);
        
        JLabel confirmLabel = new JLabel("CONFIRM DETAILS");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 20));
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setBounds(250, 10, 300, 30);
        headerPanel.add(confirmLabel);
        add(headerPanel);

        // Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBounds(20, 60, 650, 100);
        detailsPanel.setBackground(new Color(220, 230, 240));
        detailsPanel.setLayout(new GridLayout(3, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

        movieLabel = new JLabel("MOVIE: " + movie);
        movieLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(movieLabel);

        theaterLabel = new JLabel("THEATER: " + theater);
        theaterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(theaterLabel);

        ticketLabel = new JLabel("NO OF TICKETS: " + tickets);
        ticketLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(ticketLabel);

        dateLabel = new JLabel("DATE: " + date);
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(dateLabel);

        fareLabel = new JLabel("FARE: ₹" + fare);
        fareLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(fareLabel);

        timeLabel = new JLabel("TIME: " + time);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        detailsPanel.add(timeLabel);

        add(detailsPanel);

        // Payment Methods Panel
        paymentMethodPanel = new JPanel();
        paymentMethodPanel.setBounds(20, 170, 650, 180); // Adjusted position
        paymentMethodPanel.setBackground(new Color(220, 230, 240));
        paymentMethodPanel.setLayout(new GridLayout(2, 2, 15, 15));
        paymentMethodPanel.setBorder(BorderFactory.createTitledBorder("Select Payment Method"));

        creditCard = createPaymentMethod("Credit Card", "credit_card.png");
        debitCard = createPaymentMethod("Debit Card", "debit_card.png");
        netBanking = createPaymentMethod("Net Banking", "net_banking.png");
        paytmWallet = createPaymentMethod("Paytm Wallet", "paytm.png");

        paymentGroup = new ButtonGroup();
        paymentGroup.add(creditCard);
        paymentGroup.add(debitCard);
        paymentGroup.add(netBanking);
        paymentGroup.add(paytmWallet);

        paymentMethodPanel.add(creditCard);
        paymentMethodPanel.add(debitCard);
        paymentMethodPanel.add(netBanking);
        paymentMethodPanel.add(paytmWallet);
        add(paymentMethodPanel);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(150, 370, 400, 50); // Adjusted position
        buttonPanel.setLayout(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false);

        makePayment = new JButton("Make Payment");
//        makePayment.setBackground(Color.GREEN);
        buttonPanel.add(makePayment);

        goBack = new JButton("Go Back");
//        goBack.setBackground(Color.red);
        buttonPanel.add(goBack);
        add(buttonPanel);

        makePayment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (creditCard.isSelected() || debitCard.isSelected()) {
                    new CardPaymentForm(username, movie, theater, date, time, tickets, fare).setVisible(true);
                    dispose();
                } else if (netBanking.isSelected() || paytmWallet.isSelected()) {
                    try {
                        simulatePaymentProcessing();
                        DatabaseUtil.saveBooking(username, movie, theater, date, time,Integer.parseInt(tickets));
                        JOptionPane.showMessageDialog(null, "Payment Successful!", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        new Receipt(username, movie, theater, date, time, tickets, fare).setVisible(true);
                        dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Invalid ticket number", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a payment method", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookingForm(username).setVisible(true);
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JRadioButton createPaymentMethod(String text, String iconPath) {
        JRadioButton button = new JRadioButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(220, 230, 240));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/" + iconPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("Image not found: " + iconPath);
        }
        
        return button;
    }

    private void simulatePaymentProcessing() {
        final JDialog progressDialog = new JDialog(this, "Processing Payment", true);
        progressDialog.setSize(300, 150);
        progressDialog.setLayout(new BorderLayout());
        progressDialog.setLocationRelativeTo(this);

        JLabel progressLabel = new JLabel("Processing your payment...", JLabel.CENTER);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        progressDialog.add(progressLabel, BorderLayout.CENTER);
        progressDialog.add(progressBar, BorderLayout.SOUTH);
        
        new Thread(() -> {
            try {
                SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
                Thread.sleep(3000);
                SwingUtilities.invokeLater(() -> progressDialog.dispose());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        new PaymentForm("TestUser", "Avengers: Endgame", "PVR Cinemas", "15-06-2023", 
            "10:00 AM", "2", 800).setVisible(true);
    }
}




