package app;

import java.sql.*;
import java.util.ArrayList;

class Database {
    private static final String dbURL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private static final String dbusername = "z32";
    private static final String dbpassword = "dprwka";

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

    public static ArrayList<Book> GetBooksFromResult(ResultSet rs) throws SQLException {
        ArrayList<Book> books = new ArrayList<>();
        String title, author, ISBN, genre;
        int pages, year;
        while(rs.next()) {
            title = rs.getString(2);
            author = rs.getString(8) + ' ' + rs.getString(9);
            pages = rs.getInt(4);
            ISBN = rs.getString(5);
            year = rs.getInt(6);
            genre = rs.getString(7);
            Book book = new Book(title, author, pages, ISBN, year, genre);
            books.add(book);
        }
        return books;
    }
    private static String AddCondition(String condName, String data)
    {
        return condName + " like '%" + data + "%'";
    }
    public static ArrayList<Book> GetBooks(String title, String author, String ISBN, String genre, String library_name)
    {
        ArrayList<Book> books = new ArrayList<>();
        String where = "where ";
        where += AddCondition("b.title", title) + "and";
        where += "b.author_id = "  + String.valueOf(CheckAuthor(author, false)) + "and";
        where += AddCondition("b.ISBN", ISBN)+ "and";
        where += AddCondition("b.genre", genre)+ "and";
        where += AddCondition("l.name", library_name);

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select b.*, a.name, a.surname from copies c join libraries l on (c.LIBRARY_ID = l.LIBRARY_ID) join BOOKS b on (c.BOOK_ID=b.BOOK_ID) join AUTHORS a on (b.AUTHOR_ID=a.AUTHOR_ID) ";
            sql += where;
            ResultSet rs=stmt.executeQuery(sql);
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static void AddBook(Book book)
    {
        String SQL = "INSERT INTO Books Values(Null, '";
        SQL += book.getTitle() + "', ";
        int authorID = CheckAuthor(book.getAuthor(), true);
        SQL += authorID + ", ";
        SQL += book.getPages()+ ", '";
        SQL += book.getISBN() + "', ";
        SQL += book.getYear() + ", '";
        SQL += book.getGenre() + "')";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            stmt.executeUpdate(SQL);
            stmt.close();
            con.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void AddAuthor(String firstName, String lastName, Statement stmt) throws SQLException {
        String SQL = "INSERT INTO Authors Values(Null,  '" + firstName + "', '" + lastName + "', Null, Null)";
        stmt.executeUpdate(SQL);
    }
    private static int CheckAuthor(String fullName, Boolean canAdd)
    {
        int author_id = -1;
        String[] nameParts = fullName.split(" ");
        String firstName = nameParts[0];
        String lastName = nameParts[1];
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select author_id from Authors where name ='" + firstName + "' and surname ='" + lastName +"'");
            if(rs.next()) {
                author_id = rs.getInt(1);
            }
            else {
                if (canAdd)
                {
                    AddAuthor(firstName, lastName, stmt);
                    rs=stmt.executeQuery("select author_id from Authors where name ='" + firstName + "' and surname ='" + lastName +"'");
                    if(rs.next()) {
                        author_id = rs.getInt(1);
                    }
                }
            }
            stmt.close();
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return author_id;
    }
    public static ArrayList<String> getGenres()
    {
        ArrayList<String> genres = new ArrayList<>();
        String genre;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select distinct genre from books";
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {
                genre = rs.getString(1);
                genres.add(genre);
            }
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return genres;
    }

    public static Boolean isPenalty(int userID)
    {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select count(*) from users where USER_ID =" + String.valueOf(userID);
            ResultSet rs=stmt.executeQuery(sql);
            rs.next();
            return rs.getInt(1) != 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static double getDistance(int address1, int address2)//TODO
    {
        double distance=0;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select * from ADDRESSES where ADDRESS_ID in (" + String.valueOf(address1) + " ," +String.valueOf(address2);
            ResultSet rs=stmt.executeQuery(sql);
            while (rs.next())
            {
                distance = 0;
            }
            return distance;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Library getLibraryInfo(String name)
    {
        Library lib = null;
        String street, city;
        WorkTime workTimes;
        ArrayList<String> opens = null;
        ArrayList<String> closere = null;
        int phone, libID;
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();

            String sql = "select l.library_id, l.phone_number, a.street, a.street_num, a.city from LIBRARIES l join ADDRESSES a using (address_id) where l.name like '%Miejska%'";
            ResultSet rs=stmt.executeQuery(sql);
            rs.next();
            libID = rs.getInt(1);
            phone = rs.getInt(2);
            street = rs.getInt(3)  + ' ' + rs.getString(4);
            city = rs.getString(5);

            sql = "select w.OPENING, w.CLOSING from WORK_TIMES w join LIBRARIES l using (library_id) where library_id = " + String.valueOf(libID);
            rs=stmt.executeQuery(sql);
            while(rs.next())
            {
                opens.add(rs.getString(1));
                closere.add(rs.getString(2));
            }
            workTimes = new WorkTime(opens, closere);
            lib = new Library(name, street, city, phone, workTimes);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return lib;
    }
    public static void modifyAuthor(int authorID, String date, String nationality)
    {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String update = "UPDATE authors" +
                    "SET birth_date =" + date + ", nationality = '" + nationality + "'" +
                    "WHERE author_id="+String.valueOf(authorID);
            stmt.executeUpdate(update);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private static ArrayList<Order> GetOrdersFromResult(ResultSet rs) throws SQLException {
        ArrayList<Order> orders = new ArrayList<>();
        String status,dateBorrow, dateReturn,bookTitle;
        while(rs.next()) {
            status = rs.getString(1);
            dateBorrow = rs.getString(2);
            dateReturn = rs.getString(3);
            bookTitle = rs.getString(4);
            Order order = new Order(status,dateBorrow, dateReturn,bookTitle);
            orders.add(order);
        }
        return orders;
    }
    public static ArrayList<Order> getOrders(int userID)
    {
        ArrayList<Order> orders = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            String sql = "select oh.status, o.DATE_BORROW, o.DATE_RETURN, b.TITLE from ORDERS_HISTORY oh join orders o using(order_id) join COPIES c using (copy_id) join BOOKS b using (book_id)";
            if (userID != -1) {
                sql += "where oh.user_id =" + String.valueOf(userID);
            }
            ResultSet rs=stmt.executeQuery(sql);
            orders = GetOrdersFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return orders;
    }

}
//TODO podział
//użytknownik:
//system kar - uniemożliwienie rezerwacji
//historia wypożyczeń
//informacja o bibliotece - sprawdzanie danych
//rezerwacja - zarezerwowanie książki

//pracownik
//system kar - nadawania kar przy oddaniu
//informacja o bibliotece - edytowanie danych
//rezerwacja -

//wszyscy:
//wyszukiwarka

//TODO funkcje:

//-rezerwacja poprzez nadanie is_available w copies na 0 i dodanie orders i orders_history TODO
//-wypożyczenie książki poprzez modyfikację orders i orders_history i nadanie 1 dla is_available TODO
//-oddanie książki przez modyfikację orders, orders_history i ewentualnie penalties_history TODO

//-sprawdzanie odległości między czytelnikiem a biblitoeką TODO
//-edytować penalties history aby wiedzieć czy kara jest aktualna TODO

// może stworzyć funkcję która wykonuje sql, update,insert itd.