import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class HotelManagementGUI {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    private JFrame frame;
    private JTextField customerNameField;
    private JTextField roomTypeField;
    private JTextArea bookingsTextArea;
    private JButton saveButton;
    private JButton displayButton;

    public HotelManagementGUI() {
        frame = new JFrame("Hotel Management System");
        frame.setLayout(new BorderLayout());

        // Create a panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameField = new JTextField();
        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeField = new JTextField();

        saveButton = new JButton("Save Booking");
        displayButton = new JButton("Display Bookings");

        inputPanel.add(customerNameLabel);
        inputPanel.add(customerNameField);
        inputPanel.add(roomTypeLabel);
        inputPanel.add(roomTypeField);
        inputPanel.add(saveButton);
        inputPanel.add(displayButton);

        // Create a text area for displaying bookings
        bookingsTextArea = new JTextArea(10, 40);
        bookingsTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(bookingsTextArea);

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Set up actions for buttons
        saveButton.addActionListener(e -> saveBooking());
        displayButton.addActionListener(e -> displayBookings());

        // Set up frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelManagementGUI::new);
    }

    private void saveBooking() {
        String customerName = customerNameField.getText();
        String roomType = roomTypeField.getText();
        String bookingTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        if (customerName.isEmpty() || roomType.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter both customer name and room type.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertQuery = "INSERT INTO bookings (customer_name, room_type, booking_time) VALUES (?, ?, ?)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

                preparedStatement.setString(1, customerName);
                preparedStatement.setString(2, roomType);
                preparedStatement.setString(3, bookingTime);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Booking saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to save booking.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "MySQL JDBC Driver not found. Please add the driver to your classpath.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error while interacting with the database.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void displayBookings() {
        String selectQuery = "SELECT * FROM bookings";
        bookingsTextArea.setText(""); // Clear previous results

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("booking_id");
                    String name = resultSet.getString("customer_name");
                    String room = resultSet.getString("room_type");
                    String time = resultSet.getString("booking_time");

                    bookingsTextArea.append(String.format("ID: %d, Name: %s, Room Type: %s, Booking Time: %s%n", id, name, room, time));
                }
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "MySQL JDBC Driver not found. Please add the driver to your classpath.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error while interacting with the database.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
