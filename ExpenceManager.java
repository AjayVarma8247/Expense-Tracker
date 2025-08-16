package tracker;

import java.sql.*;
import java.util.Scanner;

public class ExpenceManager {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/expense_tracker";
        String username = "root";
        String password = "9502779324";

        Scanner scanner = new Scanner(System.in);
        int choice;

        // Login
        try {
            Connection conn = DriverManager.getConnection(url, username, password);

            System.out.println("Pls Login to Expense Tracker");
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();

            System.out.print("Enter Password: ");
            String loginPass = scanner.nextLine();

            String loginQuery = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement loginStmt = conn.prepareStatement(loginQuery);
            loginStmt.setString(1, loginUser);
            loginStmt.setString(2, loginPass);

            ResultSet rs = loginStmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login Successful. Welcome " + loginUser + "!");
            } else {
                System.out.println("❌ Invalid username or password. Exiting...");
                conn.close();
                return;
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Main Menu
        while (true) {
            System.out.println("\n----- Expense Tracker Menu -----");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. Exit");
            System.out.println("4. Reports & Filters");
            System.out.println("5. Edit Expense");
            System.out.println("6. Delete Expense");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // clear newline

            if (choice == 1) {
                // ADD EXPENSE
                System.out.print("Enter Category: ");
                String category = scanner.nextLine();

                System.out.print("Enter Amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Enter Date (YYYY-MM-DD): ");
                String date = scanner.nextLine();

                try {
                    Connection conn = DriverManager.getConnection(url, username, password);
                    String query = "INSERT INTO expenses (category, amount, date) VALUES (?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, category);
                    pstmt.setDouble(2, amount);
                    pstmt.setString(3, date);
                    int rows = pstmt.executeUpdate();

                    if (rows > 0) {
                        System.out.println("✅ Expense Added Successfully!");
                    } else {
                        System.out.println("❌ Failed to Add Expense.");
                    }
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error inserting expense:");
                    e.printStackTrace();
                }

            } else if (choice == 2) {
                // VIEW EXPENSES
                try {
                    Connection conn = DriverManager.getConnection(url, username, password);
                    String query = "SELECT * FROM expenses";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    System.out.println("\n----- All Expenses -----");
                    System.out.printf("%-5s %-15s %-10s %-12s\n", "ID", "Category", "Amount", "Date");
                    System.out.println("----------------------------------------");

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String category = rs.getString("category");
                        double amount = rs.getDouble("amount");
                        Date date = rs.getDate("date");

                        System.out.printf("%-5d %-15s %-10.2f %-12s\n", id, category, amount, date);
                    }

                    conn.close();
                } catch (SQLException e) {
                    System.out.println("❌ Error fetching expenses:");
                    e.printStackTrace();
                }

            } else if (choice == 3) {
                System.out.println("👋 Exiting. Thank you!");
                break;

            } else if (choice == 4) {
                // REPORTS & FILTERS
                while (true) {
                    System.out.println("\n--- Reports & Filters ---");
                    System.out.println("1. View by Date");
                    System.out.println("2. View by Category");
                    System.out.println("3. View Monthly Total");
                    System.out.println("4. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    int subChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (subChoice == 1) {
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String searchDate = scanner.nextLine();
                        try {
                            Connection conn = DriverManager.getConnection(url, username, password);
                            String query = "SELECT * FROM expenses WHERE date = ?";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, searchDate);
                            ResultSet rs = pstmt.executeQuery();

                            System.out.println("\nExpenses on " + searchDate + ":");
                            while (rs.next()) {
                                System.out.println(rs.getInt("id") + " | " + rs.getString("category") + " | ₹" + rs.getDouble("amount"));
                            }
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (subChoice == 2) {
                        System.out.print("Enter Category: ");
                        String searchCategory = scanner.nextLine();
                        try {
                            Connection conn = DriverManager.getConnection(url, username, password);
                            String query = "SELECT * FROM expenses WHERE category = ?";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, searchCategory);
                            ResultSet rs = pstmt.executeQuery();

                            System.out.println("\nExpenses in category \"" + searchCategory + "\":");
                            while (rs.next()) {
                                System.out.println(rs.getInt("id") + " | ₹" + rs.getDouble("amount") + " | " + rs.getDate("date"));
                            }
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (subChoice == 3) {
                        System.out.print("Enter Month (YYYY-MM): ");
                        String monthInput = scanner.nextLine();
                        try {
                            Connection conn = DriverManager.getConnection(url, username, password);
                            String query = "SELECT SUM(amount) AS total FROM expenses WHERE DATE_FORMAT(date, '%Y-%m') = ?";
                            PreparedStatement pstmt = conn.prepareStatement(query);
                            pstmt.setString(1, monthInput);
                            ResultSet rs = pstmt.executeQuery();

                            if (rs.next()) {
                                double total = rs.getDouble("total");
                                System.out.println("📊 Total expenses for " + monthInput + ": ₹" + total);
                            } else {
                                System.out.println("No data for this month.");
                            }
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else if (subChoice == 4) {
                        break; // back to main menu
                    } else {
                        System.out.println("❌ Invalid choice in reports menu.");
                    }
                }

            } else if (choice == 5) {
                // EDIT EXPENSE
                System.out.print("Enter Expense ID to Edit: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Enter New Category: ");
                String newCategory = scanner.nextLine();

                System.out.print("Enter New Amount: ");
                double newAmount = scanner.nextDouble();
                scanner.nextLine();

                System.out.print("Enter New Date (YYYY-MM-DD): ");
                String newDate = scanner.nextLine();

                try {
                    Connection conn = DriverManager.getConnection(url, username, password);
                    String updateQuery = "UPDATE expenses SET category = ?, amount = ?, date = ? WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(updateQuery);
                    pstmt.setString(1, newCategory);
                    pstmt.setDouble(2, newAmount);
                    pstmt.setString(3, newDate);
                    pstmt.setInt(4, id);

                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("✅ Expense updated successfully!");
                    } else {
                        System.out.println("❌ Expense ID not found.");
                    }

                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else if (choice == 6) {
                // DELETE EXPENSE
                System.out.print("Enter Expense ID to Delete: ");
                int id = scanner.nextInt();
                scanner.nextLine();

                try {
                    Connection conn = DriverManager.getConnection(url, username, password);
                    String deleteQuery = "DELETE FROM expenses WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(deleteQuery);
                    pstmt.setInt(1, id);

                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("✅ Expense deleted successfully.");
                    } else {
                        System.out.println("❌ Expense ID not found.");
                    }

                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("❌ Invalid choice. Try again.");
            }
        }

        scanner.close();
    }
}
