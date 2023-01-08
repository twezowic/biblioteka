package app;

import classes.*;

import java.sql.*;
import java.util.ArrayList;
class Database {
    private static final String dbURL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private static final String dbusername = "z32";
    private static final String dbpassword = "dprwka";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet Select(String sql)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(dbURL, dbusername, dbpassword);
            stmt = con.createStatement();
            return stmt.executeQuery(sql);
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
    private static ArrayList<Book> GetBooksFromResult(ResultSet rs) throws SQLException
    {
        ArrayList<Book> books = new ArrayList<>();
        while(rs.next()) {
            int bookID = rs.getInt(1);
            String title = rs.getString(2);
            String author = rs.getString(8) + ' ' + rs.getString(9);
            int pages = rs.getInt(4);
            String ISBN = rs.getString(5);
            int year = rs.getInt(6);
            String genre = rs.getString(7);
            Book book = new Book(bookID, title, author, pages, ISBN, year, genre);
            books.add(book);
        }
        return books;
    }
    private static ArrayList<Order> GetOrdersFromResult(ResultSet rs) throws SQLException
    {
        ArrayList<Order> orders = new ArrayList<>();
        while(rs.next()) {
            int orderID = rs.getInt(1);
            String status = rs.getString(2);
            String dateBorrow = rs.getString(3);
            String dateReturn = rs.getString(4);
            String bookTitle = rs.getString(5);
            Order order = new Order(orderID, status,dateBorrow, dateReturn,bookTitle);
            orders.add(order);
        }
        return orders;
    }
    private static void AddAuthor(String firstName, String lastName) throws SQLException
    {
        String insert = "INSERT INTO Authors Values(Null, '"
                + firstName + "', '"
                + lastName + "', Null, Null)";
        DML(insert);
    }
    private static int CheckAuthor(String fullName, Boolean canAdd)
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
    public static ArrayList<Book> getBooks(String title, String author, String ISBN, String genre, String library_name)
    {
        ArrayList<Book> books = new ArrayList<>();
        String SQL = "select b.*, a.name, a.surname " +
                " from copies c join libraries l on (c.LIBRARY_ID = l.LIBRARY_ID)" +
                " join BOOKS b on (c.BOOK_ID=b.BOOK_ID)" +
                " join AUTHORS a on (b.AUTHOR_ID=a.AUTHOR_ID) " +
                "where " + AddCondition("b.title", title) + " and " ;
        int authorID = CheckAuthor(author, false);
        if (authorID != -1) {
            SQL += "b.author_id = " + authorID + "and ";
        }
        SQL +=  AddCondition("b.ISBN", ISBN)+ "and " +
                AddCondition("b.genre", genre)+ "and " +
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
    public static void addBook(Book book)
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
    public static ArrayList<String> getGenres()
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
    public static Boolean isPenalty(int userID)
    {
        String SQL = "select count(*) from penalties_history where USER_ID =" + userID + " and is_paid = 0";
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
    public static Library getLibraryInfo(String name)
    {
        Library lib = null;
        WorkTime workTimes;
        ArrayList<String> opening = new ArrayList<>();
        ArrayList<String> closing = new ArrayList<>();
        String SQL = "select l.library_id, l.name, l.phone_number, a.street, a.street_num, a.city " +
                "from LIBRARIES l join ADDRESSES a using (address_id) where " +AddCondition("l.name", name);
        try {
            ResultSet rs = Select(SQL);
            rs.next();
            int libID = rs.getInt(1);
            String libName = rs.getString(2);
            int phone = rs.getInt(3);
            String street = rs.getString(4) + ' ' + rs.getInt(5);
            String city = rs.getString(6);
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
            lib = new Library(libID, libName, street, city, phone, workTimes);
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return lib;
    }
    public static void modifyAuthor(int authorID, String date, String nationality)
    {
        String update = "UPDATE authors " +
                "SET birth_date = TO_DATE('" + date + "', 'DD-MM-YYYY'), nationality = '" + nationality + "' " +
                "WHERE author_id="+ authorID;
        DML(update);
    }
    public static ArrayList<Order> getOrders(int userID)
    {
        ArrayList<Order> orders = new ArrayList<>();
        String SQL ="select o.order_id, oh.status, o.DATE_BORROW, o.DATE_RETURN, b.TITLE " +
                "from ORDERS_HISTORY oh join orders o on(oh.order_id=o.order_id) " +
                "join COPIES c on (c.copy_id=o.order_id) " +
                "join BOOKS b on (c.book_id=b.book_id)";
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
    public static void orderBook(int userID, int copyID)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "INSERT INTO Orders Values(Null, "
                    + copyID + ", Null, Null)";
            int orderID = 0;
            stmt.executeUpdate(SQLOrder);

            ResultSet rs = stmt.executeQuery("select order_id from orders order by order_Id desc fetch next 1 rows only");
            if (rs.next()) {
                orderID = rs.getInt(1);
            }
            rs.close();
            String SQLOrderHistory = "INSERT INTO Orders_History Values(Null, "
                    + orderID + ", " + userID + ", 'Rezerwacja')";
            stmt.executeUpdate(SQLOrderHistory);
            String updateCopies = "UPDATE Copies " +
                    "SET is_available =0 " +
                    "WHERE copy_id="+ copyID;
            stmt.executeUpdate(updateCopies);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void borrowBook(int orderID)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "UPDATE Orders " +
                    "set date_borrow = TO_DATE(TO_CHAR(SYSDATE, 'DD-MM-YYYY'), 'DD-MM-YYYY') " +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrder);
            String SQLOrderHistory = "Update Orders_History " +
                    "set status = 'Wypozyczona' " +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrderHistory);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void returnBook(int orderID, int penaltyID)
    {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String SQLOrder = "UPDATE Orders " +
                    "set date_return = TO_DATE(TO_CHAR(SYSDATE, 'DD-MM-YYYY'), 'DD-MM-YYYY') " +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrder);
            String SQLOrderHistory = "Update Orders_History " +
                    "set status = 'Zwrocona'" +
                    "where order_id = " + orderID;
            stmt.executeUpdate(SQLOrderHistory);
            String SQL = "Select o.copy_id, oh.user_id " +
                    "from ORDERS_HISTORY oh join orders o on (oh.order_id=o.order_id) " +
                    "where oh.order_id = " + orderID;
            ResultSet rs = stmt.executeQuery(SQL);
            rs.next();
            int copyID = rs.getInt(1);
            int userID = rs.getInt(2);
            rs.close();
            String updateCopies = "UPDATE Copies " +
                    "SET is_available =1" +
                    "WHERE copy_id="+ copyID;
            stmt.executeUpdate(updateCopies);
            if (penaltyID != 0)
            {
                String SQLPenaltiesHistory = "INSERT INTO Penalties_history Values(Null, "
                        + penaltyID + ", " + userID + ", 0)";
                stmt.executeUpdate(SQLPenaltiesHistory);
            }
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<Penalty> getPenalties()
    {
        ArrayList<Penalty> penalties = new ArrayList<>();
        String SQL = "select * from penalties";
        try {
            ResultSet rs = Select(SQL);
            while (rs.next()) {
                int penaltyID = rs.getInt(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                int value = rs.getInt(4);
                Penalty penalty = new Penalty(penaltyID, name, description, value);
                penalties.add(penalty);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return penalties;
    }
    public static void payPenalty(int userID)
    {
        String Update = "Update penalties_history " +
                "set is_paid = 1 " +
                "where user_id = " + userID;
        DML(Update);
    }
    public static ArrayList<Integer> getAvailableCopies(int bookID, int libraryID) throws SQLException {
        ArrayList<Integer> copies = new ArrayList<>();
        String sql = "select copy_id from books join COPIES using (book_id) " +
                "where IS_AVAILABLE = 1 and book_id = " + bookID;
        if (libraryID != 0)
        {
            sql += "  and  LIBRARY_ID = " +libraryID;
        }
        ResultSet rs = Select(sql);
        while (rs.next())
        {
            copies.add(rs.getInt(1));
        }
        rs.close();
        stmt.close();
        con.close();
        return copies;
    }
    public static void registerUser(User user) {
        String insertData = "Insert into Users_data Values(Null, '" + user.getLogin() +
                "', '" + user.getPassword() + "', 0)";
        String sql = "Select user_data_id from users_data order by " +
                "user_data_id desc fetch next 1 rows only";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt = con.createStatement();
            stmt.executeUpdate(insertData);
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int userdataID = rs.getInt(1);
            rs.close();
            String insertUser = "Insert into Users Values(Null, "+
                    userdataID + ", '" + user.getName() + "', '"
                    + user.getSurname() + "')";
            stmt.executeUpdate(insertUser);
            stmt.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

//TODO dodawanie kopii do biblioteki


// tables wyświetlanie, dodawanie, modyfikacja, operacje
//address - tylko dla biblioteki do wyświetlania
//authors - automatyczne dodawanie, i modyfikacja
//books - wyświetlanie, dodawanie
//copies - dodawanie, rezerwacja
//libraries - wyświetlanie, brak dodawania
//orders
//orders_history
//penalties - wyswietlanie
//penalties_history -
//users - logowanie, dodawanie
//users_data - logowanie, dodawanie
//work_times - dla bibliotek