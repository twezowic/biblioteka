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

    private static ArrayList<Book> GetBooksFromResult(ResultSet rs) throws SQLException {
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
            String sql = "select distinct genre from books;";
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
    
//    private static Library getLibraryFromResult(ResultSet rs) throws SQLException {//TODO zmienić gdzie przechowuje się work_times
//        String name, street, city;
//        int phone;
//        rs.next();
//        name = rs.getString(1);
//        phone = rs.getInt(2);
//        street = rs.getInt(3)  + ' ' + rs.getString(4);
//        city = rs.getString(5);
//        return new Library(name, street, city, phone);
//    }
    
//    public static Library getLibraryInfo(String name) //TODO
//    {
//        Library lib = null;
//        try{
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
//            Statement stmt=con.createStatement();
//            String sql = "select l.name, l.phone_number, a.street, a.street_num, a.city from LIBRARIES l join ADDRESSES a using (address_id) where l.name like '%Miejska%'";
//            ResultSet rs=stmt.executeQuery(sql);
//            lib = getLibraryFromResult(rs);
//            con.close();
//        }catch(Exception e){ System.out.println(e);}
//        return lib;
//    }
//    public static void get
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
//-sprawdzanie odległości między czytelnikiem a biblitoeką TODO
//-edytowanie autorów, jedynie edytowanie danych o dacie i narodowości TODO
//-rezerwacja poprzez nadanie is_available w copies na 0 i dodanie orders i orders_history TODO
//-poprawić funckję przekazującą informacje o bibliotece TODO
//-wypożyczenie książki poprzez modyfikację orders i orders_history i nadanie 1 dla is_available TODO
//-oddanie książki przez modyfikację orders, orders_history i ewentualnie penalties_history TODO
//-wyświetlanie orders_histories dla pracowników TODO
//-edytować penalties history aby wiedzieć czy kara jest aktualna TODO
//-sprwadzanie czy użytkownik posiada karę i uniemożliwianie TODO
//-otrzymywanie gatunków różnych