package app;

import java.sql.*;
import java.util.ArrayList;
import classes.*;
class Database {
    private static final String dbURL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private static final String dbusername = "z32";
    private static final String dbpassword = "dprwka";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet Select(String SQL)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(dbURL, dbusername, dbpassword);
            stmt = con.createStatement();
            return stmt.executeQuery(SQL);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void DML(String dml)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(dml);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static String AddCondition(String condName, String data)
    {
        return condName + " like '%" + data + "%'";
    }
    private static ArrayList<Book> GetBooksFromResult(ResultSet rs) throws SQLException //TODO check
    {
        ArrayList<Book> books = new ArrayList<>();
        while(rs.next()) {
            String title = rs.getString(2);
            String author = rs.getString(8) + ' ' + rs.getString(9);
            int pages = rs.getInt(4);
            String ISBN = rs.getString(5);
            int year = rs.getInt(6);
            String genre = rs.getString(7);
            Book book = new Book(title, author, pages, ISBN, year, genre);
            books.add(book);
        }
        return books;
    }

    private static ArrayList<Order> GetOrdersFromResult(ResultSet rs) throws SQLException //TODO check
    {
        ArrayList<Order> orders = new ArrayList<>();
        while(rs.next()) {
            String status = rs.getString(1);
            String dateBorrow = rs.getString(2);
            String dateReturn = rs.getString(3);
            String bookTitle = rs.getString(4);
            Order order = new Order(status,dateBorrow, dateReturn,bookTitle);
            orders.add(order);
        }
        return orders;
    }
    private static void AddAuthor(String firstName, String lastName) throws SQLException //TODO check
    {
        String insert = "INSERT INTO Authors Values(Null, '"
                + firstName + "', '"
                + lastName + "', Null, Null)";
        DML(insert);
    }
    private static int CheckAuthor(String fullName, Boolean canAdd) //TODO check nie dziala
    {
        int author_id = -1;
        String[] nameParts = fullName.split(" ");
        if (nameParts.length != 2)
        {
            return author_id;
        }
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        String SQL = "select author_id from Authors where name = '" + firstName + "' and surname = '" + lastName + "'";
        try {
            ResultSet rs = Select(SQL);
            if (rs.next()) {
                author_id = rs.getInt(1);
                rs.close();
                stmt.close();
                con.close();
            } else if (canAdd) {
                AddAuthor(firstName, lastName);
                rs = Select(SQL);
                if (rs.next()) {
                    author_id = rs.getInt(1);
                }
                rs.close();
                stmt.close();
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return author_id;
    }

    private static double getDistance(int address1, int address2)//TODO implementacja
    {
        double distance=0;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select * from ADDRESSES where ADDRESS_ID in (" + address1 + " ," + address2;
            ResultSet rs=stmt.executeQuery(sql);
            while (rs.next())
            {
                distance = 0;
            }
            return distance;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static int ValidateLoginData(String username, char[] password) {
        int user = 0;
        try {
            ResultSet rs = Select("select password, is_admin from users_data where login='" + username + "'");
            assert rs != null;
            if (rs.next()) {
                if (String.valueOf(password).equals(rs.getString(1)))
                    if (rs.getInt(2) == 0) {
                        user = 1;
                    } else
                        user = 2;
            }
            rs.close();
            stmt.close();
            con.close();
        } catch(Exception e){ System.out.println(e);}
        return user;
    }

    public static ArrayList<Book> GetBooks(String title, String author, String ISBN, String genre, String library_name) //TODO check overload?
    {
        ArrayList<Book> books = new ArrayList<>();
        String SQL = "select b.*, a.name, a.surname +" +
                " from copies c join libraries l on (c.LIBRARY_ID = l.LIBRARY_ID)" +
                " join BOOKS b on (c.BOOK_ID=b.BOOK_ID)" +
                " join AUTHORS a on (b.AUTHOR_ID=a.AUTHOR_ID) " +
                "where " +
                AddCondition("b.title", title) + "and" +
                "b.author_id = "  + CheckAuthor(author, false) + "and" +
                AddCondition("b.ISBN", ISBN)+ "and" +
                AddCondition("b.genre", genre)+ "and" +
                AddCondition("l.name", library_name);
        try{
            ResultSet rs = Select(SQL);
            books = GetBooksFromResult(rs);
            rs.close();
            stmt.close();
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static void AddBook(Book book) // TODO check
    {
        String insert = "INSERT INTO Books Values(Null, '" +
                book.getTitle() + "', " +
                CheckAuthor(book.getAuthor(), true) + ", " +
                book.getPages()+ ", '" +
                book.getISBN() + "', " +
                book.getYear() + ", '" +
                book.getGenre() + "')";
        DML(insert);
    }

    public static ArrayList<String> getGenres() // TODO check
    {
        ArrayList<String> genres = new ArrayList<>();
        String SQL = "select distinct genre from books";
        try {
            ResultSet rs = Select(SQL);
            while (rs.next()) {
                String genre = rs.getString(1);
                genres.add(genre);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return genres;
    }

    public static Boolean isPenalty(int userID) //TODO check
    {
        String SQL = "select count(*) from users where USER_ID =" + userID;
        try{
            ResultSet rs = Select(SQL);
            rs.next();
            Boolean penalty = rs.getInt(1) != 0;
            rs.close();
            stmt.close();
            con.close();
            return penalty;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Library getLibraryInfo(String name) // TODO check
    {
        Library lib = null;
        WorkTime workTimes;
        ArrayList<String> opening = new ArrayList<>();
        ArrayList<String> closing = new ArrayList<>();
        String SQL = "select l.library_id, l.phone_number, a.street, a.street_num, a.city " +
                "from LIBRARIES l join ADDRESSES a using (address_id) where " +AddCondition("l.name", name);
        try {
            ResultSet rs = Select(SQL);
            rs.next();
            int libID = rs.getInt(1);
            int phone = rs.getInt(2);
            String street = rs.getInt(3) + ' ' + rs.getString(4);
            String city = rs.getString(5);
            rs.close();
            stmt.close();
            con.close();

            SQL = "select w.OPENING, w.CLOSING from WORK_TIMES w join LIBRARIES l using (library_id) where library_id = " + libID;
            rs = Select(SQL);
            while (rs.next()) {
                opening.add(rs.getString(1));
                closing.add(rs.getString(2));
            }
            workTimes = new WorkTime(opening, closing);
            lib = new Library(name, street, city, phone, workTimes);
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return lib;
    }
    public static void modifyAuthor(int authorID, String date, String nationality) //TODO refactor check
    {
        String update = "UPDATE authors" +
                "SET birth_date =" + date + ", nationality = '" + nationality + "'" +
                "WHERE author_id="+ authorID;
        DML(update);
    }

    public static ArrayList<Order> getOrders(int userID) // TODO check
    {
        ArrayList<Order> orders = new ArrayList<>();
        String SQL ="select oh.status, o.DATE_BORROW, o.DATE_RETURN, b.TITLE " +
                "from ORDERS_HISTORY oh join orders o using(order_id) join COPIES c using (copy_id) join BOOKS b using (book_id)";
        if (userID != -1) {
            SQL += "where oh.user_id =" + userID;
        }
        try {
            ResultSet rs = Select(SQL);
            orders = GetOrdersFromResult(rs);
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return orders;
    }

    public static void orderBook(int userID, int copyID) //TODO check
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "INSERT INTO Orders Values(Null, "
                    + copyID + ", Null, Null)";
            int orderID = 0;
            stmt.executeUpdate(SQLOrder);
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                orderID = (int) generatedKeys.getLong(1);
            }
            String SQLOrderHistory = "INSERT INTO Orders_History Values(Null, "
                    + orderID + ", " + userID + ", 'Rezerwacja')";
            stmt.executeUpdate(SQLOrderHistory);
            String updateCopies = "UPDATE Copies" +
                    "SET is_available =0" +
                    "WHERE copy_id="+ copyID;
            stmt.executeUpdate(updateCopies);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void borrowBook(int orderID) //TODO check
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "UPDATE OF Orders " +
                    "set date_borrow = TO_CHAR(SYSDATE, 'DD-MM-YYYY')" +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrder);
            String SQLOrderHistory = "Update of Orders_History " +
                    "set status = 'Wypozyczona'" +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrderHistory);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void returnBook(int orderID, int penaltyID) //TODO implement
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "UPDATE OF Orders " +
                    "set date_return = TO_CHAR(SYSDATE, 'DD-MM-YYYY')" +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrder);
            String SQLOrderHistory = "Update of Orders_History " +
                    "set status = 'Zwrocona'" +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrderHistory);
            String SQL = "Select o.copy_id, oh.user_id " +
                    "from ORDERS_HISTORY oh join orders o using (order_id) " +
                    "where oh.order_id = " + orderID;
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            int copyID = rs.getInt(1);
            int userID = rs.getInt(2);
            rs.close();
            String updateCopies = "UPDATE Copies" +
                    "SET is_available =0" +
                    "WHERE copy_id="+ copyID;
            stmt.executeUpdate(updateCopies);
            if (penaltyID != -1)
            {
                String SQLPenaltiesHistory = "INSERT INTO Penalties_history Values(Null, "
                        + penaltyID + ", " + userID + ")";
                stmt.executeUpdate(SQLOrderHistory);
            }
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

//-edytować penalties history aby wiedzieć czy kara jest aktualna TODO change