package de.buddelbubi.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Buddelbubi
 * ResultSetList creates a copy of the database result. By doing so, the developer does not have to deal with memory leaks caused by unclosed ResultSets
 */
public class ResultSetList {
	
    private final List<Map<String, Object>> data;
    private int currentIndex;
    private Map<String, Object> currentRow;

    protected ResultSetList(List<Map<String, Object>> data) {
        this.data = data;
        this.currentIndex = -1;
    }

    public boolean next() {
        if (currentIndex + 1 < data.size()) {
            currentIndex++;
            currentRow = data.get(currentIndex);
            return true;
        } else {
            return false;
        }
    }

    public Set<String> getAvailableKeys() {
        return currentRow.keySet();
    }

    public int getInt(String columnLabel) {
    	Object ob = getObject(columnLabel);
    	if (ob instanceof Boolean bool) {
    		return bool ? 1 : 0;
    	} else return (Integer) ob;
    }

    public int getInt(int columnIndex) {
        return (Integer) getObject(columnIndex);
    }

    public long getLong(String columnLabel) {
        return (Long) getObject(columnLabel);
    }

    public long getLong(int columnIndex) {
        return (Long) getObject(columnIndex);
    }

    public String getString(String columnLabel) {
        return (String) getObject(columnLabel);
    }

    public String getString(int columnIndex) {
        return (String) getObject(columnIndex);
    }

    public Object getObject(String columnLabel) {
        return currentRow.get(columnLabel);
    }

    public Object getObject(int columnIndex) {
        String columnName = new ArrayList<>(currentRow.keySet()).get(columnIndex - 1);
        return currentRow.get(columnName);
    }

    public int getRow() {
        return currentIndex + 1;
    }

    public int getRowCount() {
        return data.size();
    }
    
    public void startOver() {
    	currentIndex = -1;
    	currentRow = null;
    }
}
