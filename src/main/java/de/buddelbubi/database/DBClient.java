package de.buddelbubi.database;

public abstract class DBClient {

    /**
     * Executes the query on the database
     * @implNote executeQuery("SELECT * FROM Database.table WHERE id = ?", 1234)
     * @param query
     * @param params
     * @return ResultSetList - A copy of the database result
     */
    public abstract ResultSetList executeQuery(String query, Object... params);

    /**
     * Executes an update statement on the database
     * @param query
     * @param params
     * @return int - the response from the database
     */
    public abstract int executeUpdate(String query, Object... params);

}
