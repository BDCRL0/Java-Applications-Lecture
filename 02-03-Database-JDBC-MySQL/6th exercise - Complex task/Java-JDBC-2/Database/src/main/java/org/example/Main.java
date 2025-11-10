package org.example;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws SQLException {
        Driver myDriver = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(myDriver);
        String url = "jdbc:mysql://localhost:3306/exercise?user=root";
        Connection conn = DriverManager.getConnection(url);
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM students");
        // list for the codes:
        ArrayList<String> codeList = new ArrayList<>();
        int i = 1;
        String code;
        while (rs.next()) {
            code = rs.getString("Code");
            codeList.add(code);
            System.out.println(String.format("%d. %s", i, code));
            i++;
        }
        System.out.println("Please select a number!:");
        Scanner sc = new Scanner(System.in);
        // read the number
        int codeNr = sc.nextInt();
        // searches for the associated code
        String selectedCode = codeList.get(codeNr - 1);
        System.out.println(String.format("You chose this code: %s", selectedCode));
// 2-table query: program query, printout
        PreparedStatement ps = conn.prepareStatement("SELECT Id, name FROM students " +
                "INNER JOIN programs ON Id = ProgramId " + "WHERE students.Code = ?");
        ps.setString(1, selectedCode);
        rs = ps.executeQuery();
        rs.next();
        System.out.println(String.format("The program of the student: %s", rs.getString("name")));

        int programId = rs.getInt("Id");	// szak azonosito
// 2-table query: querying courses based on the program
        ps = conn.prepareStatement("SELECT name FROM programcourse " +
                "INNER JOIN courses ON Id = CourseId WHERE ProgramId = ?");
        ps.setInt(1, programId);
        rs = ps.executeQuery();
        System.out.println("The courses of the program:");
        while (rs.next()) {
            String name = rs.getString("name");
            System.out.println(name);
        }
    }
}
