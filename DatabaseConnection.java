package tracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public static void main(String[] args) {
	String url = "jdbc:mysql://localhost:3306/expense_tracker";
	String username = "root"; 
	String password = "9502779324";
	   try {
           Connection conn = DriverManager.getConnection(url, username, password);
           System.out.println("Connection Successful!");
           conn.close();
       } catch (SQLException e) {
           System.out.println("Connection Failed!");
           e.printStackTrace();
       }
	}

}
