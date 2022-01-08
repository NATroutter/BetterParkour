package net.natroutter.betterparkour.files;

public class Config {

    private MySQL mySQL;

    public MySQL getMySQL() {return mySQL;}

    public Config() {
        this.mySQL = new MySQL();
    }

    public class MySQL {
        private String host = "localhost";
        private Integer port = 3306;
        private String user = "root";
        private String pass = "mypassword";
        private String database = "betterparkour";

        public String getHost() {return host;}
        public Integer getPort() {return port;}
        public String getUser() {return user;}
        public String getPass() {return pass;}
        public String getDatabase() {return database;}
    }

}
