package de.buddelbubi.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Buddelbubi
 * This class creates a Connection Pool to your MySQL server (or any of its forks)
 */
public abstract class SQLClientPool extends DBClient {

    private final BlockingQueue<Connection> connectionPool = new LinkedBlockingQueue<>();

    protected final String url;

    public SQLClientPool(String url) {
        this.url = url;
    }

    @Override
    public final ResultSetList executeQuery(String query, Object... params) {
        Connection con = null;
        try {
            con = connectionPool.take();
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                try (ResultSet rs = stmt.executeQuery()) {
                    List<Map<String, Object>> results = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        int columnCount = rsmd.getColumnCount();
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(rsmd.getColumnName(i), rs.getObject(i));
                        }
                        results.add(row);
                    }
                    return new ResultSetList(results);
                }
            }
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.close();
                }
                con = this.createNew();
            } catch (SQLException ignored) {}
            return this.executeQuery(query, params);
        } finally {
            if (con != null) {
                this.offer(con);
            }
        }
    }

    @Override
    public final int executeUpdate(String query, Object... params) {
        Connection con = null;
        try {
            con = connectionPool.take();
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                return stmt.executeUpdate();
            }
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.close();
                }
                con = this.createNew();
            } catch (SQLException ignore){}
            return this.executeUpdate(query, params);
        } finally {
            if (con != null) {
                this.offer(con);
            }
        }
    }


    /**
     * Creates a certain amount of database connections and adds those to the pool
     * @param amount
     * @return amount of actually created connections
     */
    protected final int offer(int amount) {
        for(int i = 0; i < amount; i++) {
            if(!this.offer()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Creates a new connection and adds that to the pool
     * @return boolean
     */
    private boolean offer() {
        try {
            return this.offer(this.createNew());
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Adds a connection to the pool
     * @param connection
     * @return boolean
     */
    private boolean offer(Connection connection) {
        return this.connectionPool.offer(connection);
    }

    /**
     * Creates a new connection to your database
     * @return Connection
     */
    protected abstract Connection createNew() throws SQLException;

    /**
     * Removes the last connection from the pool and closes it.
     */
    private void remove() {
        try (Connection connection = this.connectionPool.remove()){
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the JDBC driver
     */
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
