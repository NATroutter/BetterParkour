package fi.natroutter.betterparkour.files;

public class Config {

    private MySQL mySQL;

    public MySQL getMySQL() {return mySQL;}

    public Config() {
        this.mySQL = new MySQL();
    }

    public String language = "en_us";
    public boolean InvisibleInCourse = true;

    public class MySQL {
        private String host = "";
        private Integer port = 3306;
        private String user = "";
        private String pass = "";
        private String database = "";

        public String getHost() {return host;}
        public Integer getPort() {return port;}
        public String getUser() {return user;}
        public String getPass() {return pass;}
        public String getDatabase() {return database;}
    }

}
