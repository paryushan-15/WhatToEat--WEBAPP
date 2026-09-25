package com.bytebites.model;

public class UserDietitianConsultationModel {

    private String consultationId;

    private String userId;
    private String userName;

    private String dietitianId;
    private String dietitianName;

    private String requestedDate;
    private String requestedTime;

    private String confirmedDate;
    private String confirmedTime;

    private String reason;

    private String dietitianMessage;

    private String status;

    private String meetingLink;

    private long createdAt;
    private long acceptedAt;
    private long confirmedAt;
    private long completedAt;
    private long cancelledAt;


    // =====================================================
    // EMPTY CONSTRUCTOR
    // =====================================================

    public UserDietitianConsultationModel() {
    }


    // =====================================================
    // OPTIONAL CONSTRUCTOR
    // =====================================================

    public UserDietitianConsultationModel(
            String userId,
            String userName,
            String dietitianId,
            String dietitianName,
            String requestedDate,
            String requestedTime,
            String reason
    ) {

        this.userId = userId;

        this.userName = userName;

        this.dietitianId = dietitianId;

        this.dietitianName = dietitianName;

        this.requestedDate = requestedDate;

        this.requestedTime = requestedTime;

        this.reason = reason;

        this.status = "PENDING";

        this.createdAt =
                System.currentTimeMillis();
    }


    // =====================================================
    // CONSULTATION ID
    // =====================================================

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(
            String consultationId
    ) {

        this.consultationId =
                consultationId;
    }


    // =====================================================
    // USER ID
    // =====================================================

    public String getUserId() {
        return userId;
    }

    public void setUserId(
            String userId
    ) {

        this.userId =
                userId;
    }


    // =====================================================
    // USER NAME
    // =====================================================

    public String getUserName() {
        return userName;
    }

    public void setUserName(
            String userName
    ) {

        this.userName =
                userName;
    }


    // =====================================================
    // DIETITIAN ID
    // =====================================================

    public String getDietitianId() {
        return dietitianId;
    }

    public void setDietitianId(
            String dietitianId
    ) {

        this.dietitianId =
                dietitianId;
    }


    // =====================================================
    // DIETITIAN NAME
    // =====================================================

    public String getDietitianName() {
        return dietitianName;
    }

    public void setDietitianName(
            String dietitianName
    ) {

        this.dietitianName =
                dietitianName;
    }


    // =====================================================
    // REQUESTED DATE
    // =====================================================

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(
            String requestedDate
    ) {

        this.requestedDate =
                requestedDate;
    }


    // =====================================================
    // REQUESTED TIME
    // =====================================================

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(
            String requestedTime
    ) {

        this.requestedTime =
                requestedTime;
    }


    // =====================================================
    // CONFIRMED DATE
    // =====================================================

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public void setConfirmedDate(
            String confirmedDate
    ) {

        this.confirmedDate =
                confirmedDate;
    }


    // =====================================================
    // CONFIRMED TIME
    // =====================================================

    public String getConfirmedTime() {
        return confirmedTime;
    }

    public void setConfirmedTime(
            String confirmedTime
    ) {

        this.confirmedTime =
                confirmedTime;
    }


    // =====================================================
    // REASON
    // =====================================================

    public String getReason() {
        return reason;
    }

    public void setReason(
            String reason
    ) {

        this.reason =
                reason;
    }


    // =====================================================
    // DIETITIAN MESSAGE
    // =====================================================

    public String getDietitianMessage() {
        return dietitianMessage;
    }

    public void setDietitianMessage(
            String dietitianMessage
    ) {

        this.dietitianMessage =
                dietitianMessage;
    }


    // =====================================================
    // STATUS
    // =====================================================

    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status
    ) {

        this.status =
                status;
    }


    // =====================================================
    // MEETING LINK
    // =====================================================

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(
            String meetingLink
    ) {

        this.meetingLink =
                meetingLink;
    }


    // =====================================================
    // CREATED AT
    // =====================================================

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(
            long createdAt
    ) {

        this.createdAt =
                createdAt;
    }


    // =====================================================
    // ACCEPTED AT
    // =====================================================

    public long getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(
            long acceptedAt
    ) {

        this.acceptedAt =
                acceptedAt;
    }


    // =====================================================
    // CONFIRMED AT
    // =====================================================

    public long getConfirmedAt() {
        return confirmedAt;
    }

    public void setConfirmedAt(
            long confirmedAt
    ) {

        this.confirmedAt =
                confirmedAt;
    }


    // =====================================================
    // COMPLETED AT
    // =====================================================

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(
            long completedAt
    ) {

        this.completedAt =
                completedAt;
    }


    // =====================================================
    // CANCELLED AT
    // =====================================================

    public long getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(
            long cancelledAt
    ) {

        this.cancelledAt =
                cancelledAt;
    }
}