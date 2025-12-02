package de.buddelbubi.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Client extends SQLClientPool {

    public H2Client(File file) {
        this("jdbc:h2:file:" + file.getAbsolutePath() + ";DATABASE_TO_UPPER=false;MODE=MySQL");
    }

    public H2Client(String url) {
        super(url);
        this.offer(1);
    }

    @Override
    protected Connection createNew() throws SQLException {
        return DriverManager.getConnection(this.url);
    }
}
