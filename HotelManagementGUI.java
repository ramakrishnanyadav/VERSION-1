import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class HotelManagementGUI {
    private static final String DB_URL = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12751545";
    private static final String DB_USER = "sql12751545";
    private static final String DB_PASSWORD = "pDacH9KM7Y";

    private JFrame frame;
    private JTextField nameField, phoneField, addressField, personsField, checkoutField, cancelField, searchField;
    private JComboBox<String> roomComboBox;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private JTextArea roomDetailsArea, offersArea;
    private Map<String, Double> roomCosts;
    private JTextArea facilitiesArea;

    public HotelManagementGUI() {
        // Initialize room costs for different room types
        roomCosts = new HashMap<>();
       roomCosts.put("PRESIDENTIAL SUITE", 15000.0);
roomCosts.put("VIP ROOM", 7500.0);
roomCosts.put("AC ROOM", 3000.0);
roomCosts.put("STANDARD ROOM", 2500.0);
roomCosts.put("NON-AC ROOM", 1500.0);
roomCosts.put("SUITE", 6000.0);
roomCosts.put("LUXURY ROOM", 8000.0);
roomCosts.put("DELUXE ROOM", 4500.0);
roomCosts.put("TRADITION SUITE", 10000.0);
roomCosts.put("FAMILY ROOM", 4000.0);
roomCosts.put("EXECUTIVE ROOM", 5500.0);
roomCosts.put("PENTHOUSE SUITE", 20000.0);
roomCosts.put("ECONOMY ROOM", 1200.0);


        // Frame setup
        frame = new JFrame("Hotel Management System");
        frame.setSize(1100, 600);  // Increased the frame size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.decode("#E6E6FA")); // Lavender background

        // Left Panel - Inputs (Increased size)
        JPanel leftPanel = new JPanel(new GridLayout(8, 2, 5, 5)); // Adjusted for combo box
        leftPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        leftPanel.setBackground(Color.decode("#E6E6FA"));
        leftPanel.setPreferredSize(new Dimension(350, 600)); // Increased width of left panel

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(150, 25));

        JLabel phoneLabel = new JLabel("Phone No:");
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(150, 25));

        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        addressField.setPreferredSize(new Dimension(150, 25));

        JLabel roomLabel = new JLabel("Room Type:");
        roomComboBox = new JComboBox<>(roomCosts.keySet().toArray(new String[0]));
        roomComboBox.setPreferredSize(new Dimension(150, 25));

        JLabel personsLabel = new JLabel("No of Persons:");
        personsField = new JTextField();
        personsField.setPreferredSize(new Dimension(150, 25));

        // Adding Add Booking button
        JButton addButton = new JButton("Add Booking");
        addButton.setPreferredSize(new Dimension(150, 30));
        addButton.addActionListener(e -> saveBooking());

        // Cancellation button setup
        JLabel cancelLabel = new JLabel("Enter Name for Cancellation:");
        cancelField = new JTextField();
        cancelField.setPreferredSize(new Dimension(150, 25));

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setPreferredSize(new Dimension(150, 30));
        cancelButton.addActionListener(e -> cancelBooking());

        leftPanel.add(nameLabel);
        leftPanel.add(nameField);
        leftPanel.add(phoneLabel);
        leftPanel.add(phoneField);
        leftPanel.add(addressLabel);
        leftPanel.add(addressField);
        leftPanel.add(roomLabel);
        leftPanel.add(roomComboBox);
        leftPanel.add(personsLabel);
        leftPanel.add(personsField);
        leftPanel.add(new JLabel());
        leftPanel.add(addButton);
        leftPanel.add(cancelLabel);
        leftPanel.add(cancelField);
        leftPanel.add(new JLabel());
        leftPanel.add(cancelButton);

        // Right Panel - Room List (with more space for Room Details)
JPanel rightPanel = new JPanel();
rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

rightPanel.setBorder(BorderFactory.createTitledBorder("Room Availability"));
rightPanel.setBackground(Color.decode("#E6E6FA"));
rightPanel.setPreferredSize(new Dimension(400, 600)); // Increased width for right panel

// Create JTextArea to display room types and their costs (more space for this)
roomDetailsArea = new JTextArea(15, 10); // Increased the height of this area for better visibility
roomDetailsArea.setEditable(false);
roomDetailsArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
roomDetailsArea.setText(getRoomDetails());

JScrollPane roomScrollPane = new JScrollPane(roomDetailsArea);
roomScrollPane.setPreferredSize(new Dimension(300, 300)); // Increased scroll pane size

rightPanel.add(roomScrollPane);

// Middle Panel - Hotel Facilities and Offers (Offers moved to middle panel)
JPanel middlePanel = new JPanel();
middlePanel.setLayout(new BorderLayout());
middlePanel.setBorder(BorderFactory.createTitledBorder("Hotel Facilities & Offers"));
middlePanel.setBackground(Color.decode("#E6E6FA"));
middlePanel.setPreferredSize(new Dimension(250, 600));

// Create JTextArea for Facilities
facilitiesArea = new JTextArea(10, 10);
facilitiesArea.setEditable(false);
facilitiesArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
facilitiesArea.setText(getFacilities());
JScrollPane facilitiesScrollPane = new JScrollPane(facilitiesArea);
facilitiesScrollPane.setPreferredSize(new Dimension(200, 200));

// Create JTextArea for Offers (moved here from right panel)
offersArea = new JTextArea(10, 10);
offersArea.setEditable(false);
offersArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
offersArea.setText(getOffers()); // Offers text
JScrollPane offersScrollPane = new JScrollPane(offersArea);
offersScrollPane.setPreferredSize(new Dimension(200, 200));

JPanel topPanel = new JPanel();
topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
topPanel.add(facilitiesScrollPane);
topPanel.add(offersScrollPane);
middlePanel.add(topPanel, BorderLayout.CENTER);

// Create Search Field and Button
JPanel searchPanel = new JPanel();
JLabel searchLabel = new JLabel("Search Booking:");
searchField = new JTextField(15);
JButton searchButton = new JButton("Search");
searchButton.addActionListener(e -> searchBooking());
searchPanel.add(searchLabel);
searchPanel.add(searchField);
searchPanel.add(searchButton);

middlePanel.add(searchPanel, BorderLayout.NORTH);

// Frame Layout
frame.add(leftPanel, BorderLayout.WEST);
frame.add(middlePanel, BorderLayout.CENTER);
frame.add(rightPanel, BorderLayout.EAST);

// Bottom Panel - Table (No change)
JPanel bottomPanel = new JPanel(new BorderLayout());
bottomPanel.setBorder(BorderFactory.createTitledBorder("Bookings"));
bottomPanel.setBackground(Color.decode("#E6E6FA"));
tableModel = new DefaultTableModel(new Object[]{"Booking ID", "Name", "Phone", "Address", "Room No", "Persons", "Amount", "Cancellation Status", "Checkout Status", "Checkout Time"}, 0);  
bookingTable = new JTable(tableModel);
bottomPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

JButton confirmButton = new JButton("Confirm Booking");
confirmButton.setPreferredSize(new Dimension(150, 30));
confirmButton.addActionListener(e -> confirmBooking());

JButton checkoutButton = new JButton("Checkout");
checkoutButton.setPreferredSize(new Dimension(150, 30));
checkoutButton.addActionListener(e -> checkoutBooking());

JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
buttonPanel.add(confirmButton);
buttonPanel.add(checkoutButton);

bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

// Frame Layout
frame.add(bottomPanel, BorderLayout.SOUTH);


        // Database setup
        createBookingsTable();
        displayBookings();

        frame.setVisible(true);
    }

    // Implement the missing methods:
    private String getRoomDetails() {
        StringBuilder details = new StringBuilder();
        for (Map.Entry<String, Double> entry : roomCosts.entrySet()) {
            details.append(entry.getKey()).append(": ₹").append(entry.getValue()).append("\n");
        }
        return details.toString();
    }

    private String getOffers() {
        return "SPECIAL OFFER: 10% OFF ON ALL ROOMS!\n" +
"BOOK A SUITE AND GET A FREE BREAKFAST FOR TWO!\n" +
"BOOK FOR FIVE NIGHTS AND GET A FREE SPA TREATMENT OR MASSAGE FOR ONE!\n" +
"BOOK A ROOM AT LEAST A MONTH IN ADVANCE AND RECEIVE A 10% DISCOUNT ON YOUR STAY!\n" +
"BOOK ANY ROOM TYPE DURING THE HOLIDAY SEASON AND GET A 5% DISCOUNT ON YOUR STAY.\n" +
"BOOK A ROOM AT LEAST A MONTH IN ADVANCE AND RECEIVE A 10% DISCOUNT ON YOUR STAY.\n" +
"BOOK ANY ROOM DURING YOUR BIRTHDAY MONTH AND ENJOY A FREE BIRTHDAY CAKE AND CHAMPAGNE.\n";

    }

    private String getFacilities() {
        return
	 "1. SWIMMING POOL\n" +
	"2. GYM\n" +
	"3. SPA\n" +
	"4. FREE WI-FI\n" +
	"5. RESTAURANT\n" +
	"6. 24/7 ROOM SERVICE";

    }

    private void saveBooking() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String roomType = (String) roomComboBox.getSelectedItem();
        int persons = Integer.parseInt(personsField.getText());
        double amount = roomCosts.get(roomType);
        String cancellationStatus = "Not Cancelled";
        String checkoutStatus = "Not Checked Out";
        String checkoutTime = getCurrentTime();

        String insertSQL = "INSERT INTO bookings (name, phone, address, room_no, persons, amount, cancellation_status, checkout_status, checkout_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, roomType);
            preparedStatement.setInt(5, persons);
            preparedStatement.setDouble(6, amount);
            preparedStatement.setString(7, cancellationStatus);
            preparedStatement.setString(8, checkoutStatus);
            preparedStatement.setString(9, checkoutTime);

            preparedStatement.executeUpdate();
            displayBookings(); // Refresh the table
            JOptionPane.showMessageDialog(frame, "Booking Added Successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error saving booking: " + ex.getMessage());
        }
    }

    private void cancelBooking() {
        String name = cancelField.getText();
        String updateSQL = "UPDATE bookings SET cancellation_status = 'Cancelled' WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, name);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                displayBookings(); // Refresh the table
                JOptionPane.showMessageDialog(frame, "Booking Cancelled Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Booking with that name not found.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error canceling booking: " + ex.getMessage());
        }
    }

    private void confirmBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookingID = tableModel.getValueAt(selectedRow, 0).toString();
            String updateSQL = "UPDATE bookings SET cancellation_status = 'Confirmed' WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

                preparedStatement.setInt(1, Integer.parseInt(bookingID));
                preparedStatement.executeUpdate();
                displayBookings(); // Refresh table
                JOptionPane.showMessageDialog(frame, "Booking Confirmed!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error confirming booking: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a booking to confirm.");
        }
    }

    private void checkoutBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookingID = tableModel.getValueAt(selectedRow, 0).toString();
            String updateSQL = "UPDATE bookings SET checkout_status = 'Checked Out', checkout_time = ? WHERE id = ?";

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

                preparedStatement.setString(1, getCurrentTime());
                preparedStatement.setInt(2, Integer.parseInt(bookingID));
                preparedStatement.executeUpdate();
                displayBookings(); // Refresh table
                JOptionPane.showMessageDialog(frame, "Customer Checked Out Successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error during checkout: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a booking to check out.");
        }
    }

    private void displayBookings() {
        String query = "SELECT * FROM bookings";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear existing data
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getString("room_no"),
                    resultSet.getInt("persons"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("cancellation_status"),
                    resultSet.getString("checkout_status"),
                    resultSet.getString("checkout_time")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error displaying bookings: " + ex.getMessage());
        }
    }

    private void searchBooking() {
        String searchTerm = searchField.getText().toLowerCase();
        String query = "SELECT * FROM bookings WHERE LOWER(name) LIKE ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Clear existing data
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getString("room_no"),
                    resultSet.getInt("persons"),
                    resultSet.getDouble("amount"),
                    resultSet.getString("cancellation_status"),
                    resultSet.getString("checkout_status"),
                    resultSet.getString("checkout_time")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error searching bookings: " + ex.getMessage());
        }
    }

    private String getCurrentTime() {
        // Method to get current date and time as a string
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
    }

    private void createBookingsTable() {
        // Create bookings table if not exists
        String createTableSQL = "CREATE TABLE IF NOT EXISTS bookings ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(100), "
                + "phone VARCHAR(20), "
                + "address TEXT, "
                + "room_no VARCHAR(50), "
                + "persons INT, "
                + "amount DOUBLE, "
                + "cancellation_status VARCHAR(20), "
                + "checkout_status VARCHAR(20), "
                + "checkout_time VARCHAR(20))";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(createTableSQL);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error creating bookings table: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new HotelManagementGUI();
    }
}
