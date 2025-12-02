package de.buddelbubi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLClient extends SQLClientPool {

    private final String user;
    private final String password;

    public MySQLClient(String url, String user, String password) {
        this(url, user, password, 1);
    }

    public MySQLClient(String url, String user, String password, int connections) {
        super(url);
        this.user = user;
        this.password = password;
        if(this.offer(connections) != connections - 1) {
            throw new RuntimeException(String.format("Could not open {} connections to the database.", connections));
        }
    }

    @Override
    protected Connection createNew() throws SQLException {
        return DriverManager.getConnection(this.url, this.user, this.password);
    }
}
