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
        client.executeUpdate("INSERT INTO Persons (PersonID, LastName, FirstName) VALUES (0, 'Bubi', 'Buddel')");
    }

    @Test
    public void testRead() {
        DBClient client = openDatabase();
        ResultSetList set = client.executeQuery("SELECT * FROM Persons WHERE PersonID = ?", 0);
        while (set.next()) {
            System.out.println(set.getString("LastName") + ", " + set.getString("FirstName"));
        }
    }
}
