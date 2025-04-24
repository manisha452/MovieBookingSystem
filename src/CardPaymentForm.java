import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardPaymentForm extends JFrame {
    private JTextField cardNumberField, cardHolderField;
    private JPasswordField cvvField;
    private JComboBox<String> expMonth, expYear;
    private JComboBox<String> cardTypeCombo;
    private JButton proceedButton;
    private String username, movie, theater, date, time, tickets;
    private int fare;

    public CardPaymentForm(String username, String movie, String theater,
                         String date, String time, String tickets, int fare) {
        this.username = username;
        this.movie = movie;
        this.theater = theater;
        this.date = date;
        this.time = time;
        this.tickets = tickets;
        this.fare = fare;

        setTitle("Card Payment");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JLabel bookingLabel = new JLabel("CONFIRM BOOKING");
        bookingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bookingLabel.setForeground(Color.RED);
        bookingLabel.setBounds(180, 10, 200, 30);
        add(bookingLabel);

        JLabel movieLabel = new JLabel("MOVIE: " + movie);
        movieLabel.setBounds(50, 50, 200, 20);
        add(movieLabel);

        JLabel theaterLabel = new JLabel("THEATER: " + theater);
        theaterLabel.setBounds(280, 50, 200, 20);
        add(theaterLabel);

        JLabel dateLabel = new JLabel("DATE: " + date);
        dateLabel.setBounds(280, 80, 200, 20);
        add(dateLabel);

        JLabel timeLabel = new JLabel("TIME: " + time);
        timeLabel.setBounds(280, 110, 200, 20);
        add(timeLabel);

        JLabel ticketLabel = new JLabel("NO OF TICKETS: " + tickets);
        ticketLabel.setBounds(50, 80, 200, 20);
        add(ticketLabel);

        JLabel fareLabel = new JLabel("FARE: ₹" + fare);
        fareLabel.setBounds(50, 110, 200, 20);
        add(fareLabel);

        JLabel cardDetailsLabel = new JLabel("CARD DETAILS");
        cardDetailsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cardDetailsLabel.setForeground(Color.BLUE);
        cardDetailsLabel.setBounds(180, 140, 200, 20);
        add(cardDetailsLabel);

        JLabel cardTypeLabel = new JLabel("Card Type");
        cardTypeLabel.setBounds(50, 170, 120, 20);
        add(cardTypeLabel);

        String[] cardTypes = {"Credit Card", "Debit Card"};
        cardTypeCombo = new JComboBox<>(cardTypes);
        cardTypeCombo.setBounds(180, 170, 200, 20);
        add(cardTypeCombo);

        JLabel cardNumberLabel = new JLabel("Card number");
        cardNumberLabel.setBounds(50, 200, 120, 20);
        add(cardNumberLabel);

        cardNumberField = new JTextField();
        cardNumberField.setBounds(180, 200, 200, 20);
        add(cardNumberField);

        JLabel cardHolderLabel = new JLabel("Card holder name");
        cardHolderLabel.setBounds(50, 230, 120, 20);
        add(cardHolderLabel);

        cardHolderField = new JTextField();
        cardHolderField.setBounds(180, 230, 200, 20);
        add(cardHolderField);

        JLabel cvvLabel = new JLabel("CVV");
        cvvLabel.setBounds(50, 260, 40, 20);
        add(cvvLabel);

        cvvField = new JPasswordField();
        cvvField.setBounds(90, 260, 50, 20);
        add(cvvField);

        JLabel expLabel = new JLabel("EXP");
        expLabel.setBounds(150, 260, 40, 20);
        add(expLabel);

        String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        expMonth = new JComboBox<>(months);
        expMonth.setBounds(190, 260, 50, 20);
        add(expMonth);

        String[] years = {"2024", "2025", "2026", "2027", "2028", "2029", "2030"};
        expYear = new JComboBox<>(years);
        expYear.setBounds(250, 260, 70, 20);
        add(expYear);

        proceedButton = new JButton("Proceed");
        proceedButton.setBounds(180, 300, 100, 30);
        proceedButton.addActionListener(new ProceedButtonListener());
        add(proceedButton);
    }

    private class ProceedButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!validateCardDetails()) {
                return;
            }

            try {
                int bookingId = saveBooking();
                if (bookingId <= 0) {
                    JOptionPane.showMessageDialog(null,
                        "Failed to save booking",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!saveCardDetails()) {
                    JOptionPane.showMessageDialog(null,
                        "Failed to save payment details",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!recordTransaction(bookingId)) {
                    JOptionPane.showMessageDialog(null,
                        "Failed to record transaction",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(null,
                    "Payment Successful! " , "Success", JOptionPane.INFORMATION_MESSAGE);
                
                new Receipt(username, movie, theater, date, time, tickets, fare).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                    "Error processing payment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private boolean validateCardDetails() {
            if (cardNumberField.getText().isEmpty() || 
                cardHolderField.getText().isEmpty() ||
                cvvField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null,
                    "Please fill all card details!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
            if (!cardNumber.matches("\\d{16}")) {
                JOptionPane.showMessageDialog(null,
                    "Please enter a valid 16-digit card number",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (cvvField.getPassword().length < 3 || cvvField.getPassword().length > 4) {
                JOptionPane.showMessageDialog(null,
                    "Please enter a valid CVV (3 or 4 digits)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        private int saveBooking() {
            return 12345;
        }

        private boolean saveCardDetails() {
            return true;
        }

        private boolean recordTransaction(int bookingId) {
            return true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CardPaymentForm("TestUser", "Sample Movie", "PVR", "01-01-2023",
                "10:00 AM", "2", 400).setVisible(true);
    });
    }
}