package com.example.model;

import java.util.ArrayList;

public class DataFile {

    private String uuid;

    @Override
    public String toString() {
        return "DataFile{" +
                "uuid='" + uuid + '\'' +
                ", tableData=" + tableData +
                '}';
    }

    public DataFile(String uuid, ArrayList<LogTable> tableData) {
        this.uuid = uuid;
        this.tableData = tableData;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ArrayList<LogTable> getTableData() {
        return tableData;
    }

    public void setTableData(ArrayList<LogTable> tableData) {
        this.tableData = tableData;
    }

    private ArrayList<LogTable> tableData;
}
