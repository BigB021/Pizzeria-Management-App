import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    // Insert a new staff member into the STAFF table
    public void insertStaff(Staff staff, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO STAFF (ID_STAFF, NAME, TELEPHONE, EMAIL, PASSWORD, ROLE) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, staff.getId());
                preparedStatement.setString(2, staff.getName());
                preparedStatement.setString(3, staff.getTelephone());
                preparedStatement.setString(4, staff.getEmail());
                preparedStatement.setString(5, password);
                preparedStatement.setString(6, staff.getRole().name());

                preparedStatement.executeUpdate();
                System.out.println("Staff member added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing staff member in the STAFF table
    public void updateStaff(Staff staff) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE STAFF SET NAME = ?, TELEPHONE = ?, EMAIL = ?, PASSWORD = ?, ROLE = ? " +
                    "WHERE ID_STAFF = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, staff.getName());
                preparedStatement.setString(2, staff.getTelephone());
                preparedStatement.setString(3, staff.getEmail());
                preparedStatement.setString(4, staff.getPassword());
                preparedStatement.setString(5, staff.getRole().name());
                preparedStatement.setInt(6, staff.getId());

                preparedStatement.executeUpdate();
                System.out.println("Staff member updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a staff member from the STAFF table by ID
    public void deleteStaff(int staffId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM STAFF WHERE ID_STAFF = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, staffId);
                preparedStatement.executeUpdate();
                System.out.println("Staff member deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve a staff member from the STAFF table by ID
    public Staff getStaffById(int staffId) {
        Staff staff = null;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM STAFF WHERE ID_STAFF = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, staffId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        staff = new Staff(
                                resultSet.getInt("ID_STAFF"),
                                resultSet.getString("NAME"),
                                resultSet.getString("TELEPHONE"),
                                resultSet.getString("EMAIL"),
                                Staff.StaffRole.valueOf(resultSet.getString("ROLE"))
                        );
                        staff.setPassword(resultSet.getString("PASSWORD"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM STAFF";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("ID_STAFF");
                    String name = resultSet.getString("NAME");
                    String telephone = resultSet.getString("TELEPHONE");
                    String email = resultSet.getString("EMAIL");
                    String role = resultSet.getString("ROLE");
                    String password = resultSet.getString("PASSWORD");

                    Staff.StaffRole staffRole = Staff.StaffRole.valueOf(role.toUpperCase());

                    Staff staff = new Staff(id, name, telephone, email, staffRole);
                    staff.setPassword(password);

                    staffList.add(staff);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return staffList;
    }
    public int getNextStaffId() {
        String sequenceQuery = "SELECT COALESCE(MAX(ID_STAFF), 0) + 1 FROM STAFF";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sequenceQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // or throw an exception if something goes wrong
    }
}
