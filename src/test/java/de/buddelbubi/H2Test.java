package de.buddelbubi;

import de.buddelbubi.database.DBClient;
import de.buddelbubi.database.H2Client;
import de.buddelbubi.database.ResultSetList;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class H2Test {

    public DBClient openDatabase() {
        try {
            File file = new File(new File(H2Client.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile() + "/test/", "testDb");
            DBClient client = new H2Client(file);
            return client;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testOpenDatabase() {
        openDatabase();
    }

    @Test
    public void testWrite() {
        DBClient client = openDatabase();
        client.executeUpdate("CREATE TABLE IF NOT EXISTS Persons (" +
                "    PersonID int AUTO_INCREMENT," +
                "    LastName varchar(255)," +
                "    FirstName varchar(255)" +
                ")");
        client.executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                "    userid VARCHAR(50)," +
                "    lastname VARCHAR(50)," +
                "    language VARCHAR(5)," +
                "    customui TINYINT DEFAULT 1," +
                "    prefix TINYINT DEFAULT 0," +
                "    coins INT DEFAULT 0," +
                "    gplaytime BIGINT DEFAULT 0," +
                "    friendrequests VARCHAR(2000) DEFAULT '[]'," +
                "    discordid VARCHAR(50)," +
                "    season_xp INT DEFAULT 0," +
                "    xp_global INT DEFAULT 0," +
                "    gems INT DEFAULT 0," +
                "    votes INT DEFAULT 0," +
                "    login_streak INT DEFAULT 0," +
                "    firstjoin TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB;");
        client.executeUpdate("INSERT INTO Persons (PersonID, LastName, FirstName) VALUES (0, 'Bubi', 'Buddel')");
        client.executeUpdate("INSERT INTO players (userid) VALUES ('test')");
    }

    @Test
    public void testRead() {
        DBClient client = openDatabase();
        ResultSetList set = client.executeQuery("SELECT * FROM Persons WHERE PersonID = ?", 0);
        while (set.next()) {
            System.out.println(set.getString("LastName") + ", " + set.getString("FirstName"));
        }
        ResultSetList setList = client.executeQuery("SELECT * FROM players");
        while (setList.next()) {
            System.out.println(setList.getAvailableKeys());
        }
    }
}
