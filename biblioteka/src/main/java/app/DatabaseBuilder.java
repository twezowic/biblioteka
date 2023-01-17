package app;

public class DatabaseBuilder {
    private String DBURL = "jdbc:oracle:thin:@//ora4.ii.pw.edu.pl:1521/pdb1.ii.pw.edu.pl";
    private String DBUSERNAME = "z32";
    private String DBPASSWORD = "dprwka";

    public DatabaseBuilder setDBURL(String dburl) {
        DBURL = dburl;
        return this;
    }

    public DatabaseBuilder setDBUSERNAME(String dbusername) {
        DBUSERNAME = dbusername;
        return this;
    }

    public DatabaseBuilder setDBPASSWORD(String dbpassword) {
        DBPASSWORD = dbpassword;
        return this;
    }

    public Database build() {
        return new Database(DBURL, DBUSERNAME, DBPASSWORD);
    }

}
