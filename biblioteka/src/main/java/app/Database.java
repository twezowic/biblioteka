package app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
    public static ArrayList<Book> GetAllBooks()
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select b.*, a.name, a.surname from Books b join Authors a using(author_id)");
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static ArrayList<Book> GetBooksByAuthor(String name)
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select b.*, a.name, a.surname from Books b join Authors a using(author_id) where a.name ='" + name + "' or a.surname ='" + name + "'");
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static ArrayList<Book> GetBooksByTitle(String name)
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select b.*, a.name, a.surname from Books b join Authors a using(author_id) where b.title ='" + name + "'");
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static ArrayList<Book> GetBooksByISBN(String name)
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select b.*, a.name, a.surname from Books b join Authors a using(author_id) where b.isbn ='" + name + "'");
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }

    public static ArrayList<Book> GetBooksByLibrary(String name)
    //TODO
    {
        ArrayList<Book> books = new ArrayList<>();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection(dbURL, dbusername, dbpassword);
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select b.*, a.name, a.surname from Books b join Authors a using(author_id) where b.title ='" + name + "'");
            books = GetBooksFromResult(rs);
            con.close();
        }catch(Exception e){ System.out.println(e);}
        return books;
    }
    public static void AddBook(Book book)
    //TODO  wykonywać dml
    {
        String SQL = "INSERT INTO Books Values(";
        SQL += book.getTitle() + ", ";
        int authorID = CheckAuthor(book.getAuthor());

    }

    public static int CheckAuthor(String name)
    //TODO sprawdzać czy autor jest w bazie danych i jeżeli nie dodać o nim informację
    {
        return -1;
    }

    public static void AddInitialData() throws IOException
    //TODO zmienić aby przekazywało dane do AddBook zamiast wypisywało
    {
        URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=dragon&maxResults=20&printType=books");

        String out = new Scanner(url.openStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        JSONObject json = new JSONObject(out);
        JSONArray items = json.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject volumeInfo = items.getJSONObject(i).getJSONObject("volumeInfo");

            String title = volumeInfo.getString("title");
            String author = volumeInfo.getJSONArray("authors").getString(0);

            int pages=-1;
            if (volumeInfo.has("industryIdentifiers"))
                pages = volumeInfo.getInt("pageCount");

            JSONArray industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");
            String ISBN = industryIdentifiers.getJSONObject(0).getString("identifier");

            int year=-1;
            if (volumeInfo.has("publishedDate"))
                year = Integer.parseInt(volumeInfo.getString("publishedDate").split("-")[0]);

            String genre="Null";
            if (volumeInfo.has("categories"))
                genre = volumeInfo.getJSONArray("categories").getString(0);

            System.out.println(title + " by " + author + " (" + year + ")");
            System.out.println("ISBN: " + ISBN + ", pages: " + pages);
            System.out.println("genre: "+ genre);
            Book book = new Book(title, author, pages, ISBN, year, genre);
            AddBook(book);
        }
    }
}
