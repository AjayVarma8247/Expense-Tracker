package tracker;
import java.sql.*;
public class FetchExpenses {

	public static void main(String[] args) {
     String url="jdbc:mysql://localhost:3306/expense_tracker";
     String username="root";
     String password="9502779324";
     try {
    	  Connection conn = DriverManager.getConnection(url, username, password);
          String query = "SELECT * FROM expenses";
          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(query);

          while (rs.next()) {
              int id = rs.getInt("id");
              String category = rs.getString("category");
              double amount = rs.getDouble("amount");
              Date date = rs.getDate("date");

              System.out.println(id + " | " + category + " | " + amount + " | " + date);
          }

          conn.close();
      } catch (SQLException e) {
          e.printStackTrace();
     }
	}

}
