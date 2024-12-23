package Controller;

import java.util.Date;

public class Member {
    
    private int memberID;
    private int teamID;
    private String fullName;
    private String position;
    private Date birthDate;
    private int jerseyNumber;

    // Constructor
    public Member(int memberID, int teamID, String fullName, String position, Date birthDate, int jerseyNumber) {
        this.memberID = memberID;
        this.teamID = teamID;
        this.fullName = fullName;
        this.position = position;
        this.birthDate = birthDate;
        this.jerseyNumber = jerseyNumber;
    }

    public Member() {
    }
    // Getters and Setters
    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    // Override toString() để hiển thị thông tin thành viên
    @Override
    public String toString() {
        return "Member [MemberID=" + memberID + ", TeamID=" + teamID + ", FullName=" + fullName + 
               ", Position=" + position + ", BirthDate=" + birthDate + ", JerseyNumber=" + jerseyNumber + "]";
    }
}
