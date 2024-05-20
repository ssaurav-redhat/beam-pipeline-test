package com.example.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class LogTable implements Serializable {

    private String uuid;

    private Timestamp time;

    public String getUuid() {
        return uuid;
    }

    public LogTable(String uuid, Timestamp time) {
        this.uuid = uuid;
        this.time = time;
    }

    @Override
    public String toString() {
        return "LogTable{" +
                "uuid='" + uuid + '\'' +
                ", time=" + time +
                '}';
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
