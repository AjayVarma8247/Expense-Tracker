package tracker;
import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;

public class AddExpense {
      public static void main(String[] args) {
    	  String url="jdbc:mysql://localhost:3306/expense_tracker";
    	  String username="root";
    	  String password="9502779324";
    	  
    	  Scanner scan=new Scanner(System.in);
    	  System.out.print("Enter Expense Category: ");
    	  String category=scan.nextLine();
    	  System.out.println("Enter Expense Amount: ");
    	  double amount = scan.nextDouble();
          scan.nextLine(); // consume leftover newline
          System.out.print("Enter Date (YYYY-MM-DD): ");
          String date = scan.nextLine();
          
          try {
        	  Connection conn=DriverManager.getConnection(url,username,password);
        	  String query = "INSERT INTO expenses (category, amount, date) VALUES (?, ?, ?)";
              
              PreparedStatement pstmt = conn.prepareStatement(query);
              pstmt.setString(1, category);
              pstmt.setDouble(2, amount);
              pstmt.setString(3, date);

              int rows = pstmt.executeUpdate();

              if (rows > 0) {
                  System.out.println("Expense Added Successfully!");
              } else {
                  System.out.println("Failed to Add Expense.");
              }

              conn.close();
          } catch (SQLException e) {
              e.printStackTrace();
          }
          
       scan.close();   
      }
}
