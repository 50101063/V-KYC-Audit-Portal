package com.vkyc.auditportal.model;

// This is a simple POJO for now, not a JPA Entity.
// The Database Developer will define the actual JPA Entity.
public class RecordMetadata {
    private String userId;
    private String callDuration;
    private String status;
    private String time;
    private String date;
    private String nfsVkycUploadTime;
    private String nfsPath; // Path to the video on NFS

    public RecordMetadata() {
    }

    public RecordMetadata(String userId, String callDuration, String status, String time, String date, String nfsVkycUploadTime, String nfsPath) {
        this.userId = userId;
        this.callDuration = callDuration;
        this.status = status;
        this.time = time;
        this.date = date;
        this.nfsVkycUploadTime = nfsVkycUploadTime;
        this.nfsPath = nfsPath;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getNfsVkycUploadTime() {
        return nfsVkycUploadTime;
    }

    public String getNfsPath() {
        return nfsPath;
    }

    // Setters (optional, depending on immutability needs)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNfsVkycUploadTime(String nfsVkycUploadTime) {
        this.nfsVkycUploadTime = nfsVkycUploadTime;
    }

    public void setNfsPath(String nfsPath) {
        this.nfsPath = nfsPath;
    }
}
