package app;

class Database { //temporary
    public static int ValidateLoginData(String username, char[] password) {
        if (username.compareTo("admin") ==0 )
        {
            return 2;
        }
        else if (username.compareTo("ala")==0)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
