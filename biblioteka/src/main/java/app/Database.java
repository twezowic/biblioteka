package app;

import java.sql.*;


class Database {
    static final String dbURL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    static final String dbusername = "z32";
    static final String dbpassword = "dprwka";

    public static int ValidateLoginData(String username, char[] password) {
        int user = 0;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select password, is_admin from users_data where login='"+username+"'");
            if(rs.next())
                if(String.valueOf(password).equals(rs.getString(1)))
                    if (rs.getInt(2) == 0) {
                        user = 1;
                    }
                    else
                        user = 2;
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return user;
    }
}
