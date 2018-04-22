import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/" + KeyStore.DB_NAME + "?" +
                    "user=" + KeyStore.LOGIN + "&password=" + KeyStore.PASSWORD);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM CLIENTS");

            System.out.println(rs);

            conn.close();

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
