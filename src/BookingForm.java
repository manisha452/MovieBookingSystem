import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;

public class BookingForm extends JFrame {
    private JComboBox<String> movieDropdown, theaterDropdown, timeDropdown;
    private JTextField ticketField, dateField;
    private JButton submitButton, dateButton;
    private Connection connection;

    public BookingForm(String username) {
  setTitle("Movie Ticket Booking");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 248, 255));
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel() {
        };
        titlePanel.setPreferredSize(new Dimension(500, 60));
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("MOVIE TICKET BOOKING", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel userLabel = new JLabel("User: " + username, SwingConstants.RIGHT);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        titlePanel.add(userLabel, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setBackground(Color.WHITE);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        formPanel.add(createFormRow("SELECT MOVIE:", movieDropdown = new JComboBox<>(
            new String[]{"Dear Zindagi", "Sita Ramam", "Your Name", "Interstellar", "Avengers"})));

        formPanel.add(createFormRow("SELECT THEATRE:", theaterDropdown = new JComboBox<>(
            new String[]{"Divine Cinemas", "INOX Megaplex", "PVR Gold", "Cinepolis VIP", "IMAX"})));

        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.setBackground(Color.WHITE);
        datePanel.add(new JLabel("SELECT DATE:"), BorderLayout.WEST);
        dateField = new JTextField();
        dateField.setEditable(false);
        datePanel.add(dateField, BorderLayout.CENTER);
        dateButton = new JButton("📅");
        dateButton.addActionListener(e -> showDatePicker());
        datePanel.add(dateButton, BorderLayout.EAST);
        formPanel.add(datePanel);

        formPanel.add(createFormRow("SELECT TIME:", timeDropdown = new JComboBox<>(
            new String[]{"10:00 AM", "1:00 PM", "4:00 PM", "7:00 PM", "10:00 PM"})));

        formPanel.add(createFormRow("NO. OF TICKETS:", ticketField = new JTextField()));

        submitButton = new JButton("💳 PROCEED TO PAYMENT");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(createSubmitActionListener(username));

        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(submitButton);

        contentPanel.add(formPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private JPanel createFormRow(String labelText, JComponent component) {
        JPanel rowPanel = new JPanel(new BorderLayout(10, 0));
        rowPanel.setBackground(Color.WHITE);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(70, 130, 180));
        rowPanel.add(label, BorderLayout.WEST);

        component.setFont(new Font("Arial", Font.PLAIN, 12));
        if (component instanceof JTextField) {
            ((JTextField) component).setColumns(15);
        }
        rowPanel.add(component, BorderLayout.CENTER);

        return rowPanel;
    }

    private void showDatePicker() {
        JDialog dateDialog = new JDialog(this, "Select Date", true);
        dateDialog.setSize(350, 350);
        dateDialog.setLayout(new BorderLayout());

        JCalendar calendar = new JCalendar();
        calendar.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("calendar".equals(evt.getPropertyName())) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");
                    dateField.setText(sdf.format(calendar.getDate()));
                    dateDialog.dispose();
                }
            }
        });

        dateDialog.add(calendar, BorderLayout.CENTER);
        dateDialog.setLocationRelativeTo(this);
        dateDialog.setVisible(true);
    }


    private boolean saveBooking(String username, String movie, String theater, 
                              String date, String time, int tickets, double fare) {
        Database db=new Database();
        try{
        String sql = "INSERT INTO bookings(username, movie, theater, date, time, tickets, fare) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement pstmt = db.con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, movie);
            pstmt.setString(3, theater);
            pstmt.setString(4, date);
            pstmt.setString(5, time);
            pstmt.setInt(6, tickets);
            pstmt.setDouble(7, fare);
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to save booking: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private ActionListener createSubmitActionListener(String username) {
        return e -> {
            String tickets = ticketField.getText();
            if (!tickets.matches("\\d+") || Integer.parseInt(tickets) < 1) {
                JOptionPane.showMessageDialog(this, "Please enter valid number of tickets", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a date", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String movie = (String) movieDropdown.getSelectedItem();
            String theater = (String) theaterDropdown.getSelectedItem();
            String date = dateField.getText();
            String time = (String) timeDropdown.getSelectedItem();
            int ticketCount = Integer.parseInt(tickets);
            int totalPrice = ticketCount * 200; // Assuming 200 per ticket
            if (saveBooking(username, movie, theater, date, time, ticketCount, totalPrice)) {
                new CardPaymentForm(username,movie,theater,date,time,tickets,totalPrice).setVisible(true);
                dispose();
            }
        };
    }

    @Override
    public void dispose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingForm("TestUser").setVisible(true));
    }
}

class JCalendar extends JPanel {
    private Calendar calendar;
    private JButton[][] dayButtons;
    private Color headerColor = new Color(70, 130, 180);
    private Color dayNameColor = new Color(100, 149, 237);
    private Color todayColor = new Color(220, 240, 255);

    public JCalendar() {
        calendar = Calendar.getInstance();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel monthLabel = new JLabel(getMonthYear(), SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        monthLabel.setForeground(Color.WHITE);

        JButton prevButton = createNavButton("<");
        JButton nextButton = createNavButton(">");

        prevButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextButton.addActionListener(e -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        JPanel dayNamePanel = new JPanel(new GridLayout(1, 7));
        dayNamePanel.setBackground(Color.WHITE);

        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setForeground(dayNameColor);
            dayNamePanel.add(dayLabel);
        }
        add(dayNamePanel, BorderLayout.CENTER);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        daysPanel.setBackground(Color.WHITE);
        dayButtons = new JButton[6][7];

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton dayButton = new JButton();
                dayButton.setFont(new Font("Arial", Font.PLAIN, 12));
                dayButton.setBackground(Color.WHITE);
                dayButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                dayButton.setOpaque(true);
                dayButton.setFocusPainted(false);

                dayButton.addActionListener(e -> {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayButton.getText()));
                    firePropertyChange("calendar", null, calendar.getTime());
                });

                dayButtons[row][col] = dayButton;
                daysPanel.add(dayButton);
            }
        }

        add(daysPanel, BorderLayout.SOUTH);
        updateCalendar();
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(headerColor);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFocusPainted(false);
        return button;
    }

    private void updateCalendar() {
        ((JLabel)((JPanel)getComponent(0)).getComponent(1)).setText(getMonthYear());

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                dayButtons[row][col].setText("");
                dayButtons[row][col].setEnabled(false);
                dayButtons[row][col].setBackground(Color.WHITE);
            }
        }

        Calendar temp = (Calendar) calendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = temp.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);

        Calendar today = Calendar.getInstance();
        boolean currentMonth = (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH));

        for (int day = 1, row = 0, col = firstDay; day <= daysInMonth; day++, col++) {
            if (col == 7) {
                col = 0;
                row++;
            }
            dayButtons[row][col].setText(String.valueOf(day));
            dayButtons[row][col].setEnabled(true);

            if (currentMonth && day == today.get(Calendar.DAY_OF_MONTH)) {
                dayButtons[row][col].setBackground(todayColor);
                dayButtons[row][col].setFont(new Font("Arial", Font.BOLD, 12));
            }
        }
    }

    private String getMonthYear() {
        return new SimpleDateFormat("MMMM yyyy").format(calendar.getTime());
    }

    public Date getDate() {
        return calendar.getTime();
    }
}