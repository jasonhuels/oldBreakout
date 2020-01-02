package breakout;

import java.sql.*;
import java.util.ArrayList;

public class HighscoreDB {
    private static final String FIREDUP_URL = "jdbc:jtds:sqlserver://localhost;databaseName=HighScore";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "user";
    private static final String CUSTOMER_SQL = "SELECT Name FROM CUSTOMER";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FIREDUP_URL,USERNAME,PASSWORD);
    }

    public ArrayList<String> readCustomerBasics(){
        ArrayList<String> customers = new ArrayList<>();
        try (
                Connection conn = getConnection();
                //additional resources will go here
                PreparedStatement stmt = conn.prepareStatement(CUSTOMER_SQL);
                ResultSet rs = stmt.executeQuery()
        ) {
            //code	utilizing resources	will go here
            while(rs.next()){
                customers.add(rs.getString("Name"));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return customers;
    }

    public ArrayList<String> readCustomers(){
        ArrayList<String> customers = readCustomerBasics();
        return customers;
    }
}
