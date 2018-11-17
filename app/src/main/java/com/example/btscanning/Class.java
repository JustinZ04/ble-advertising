package com.example.btscanning;

public class Class {

    public String database_id;
    public String course_id;
    public String name;
    public String startTime;
    public String endTime;

    public Class(String database_id, String course_id, String name, String startTime, String endTime) {
        this.database_id = database_id;
        this.course_id = course_id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDatabase_id() {
        return database_id;
    }

    public void setDatabase_id(String database_id) {
        this.database_id = database_id;
    }


}
