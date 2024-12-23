package Controller;

public class Team {
    private int teamID;
    private int seasonID;
    private String teamName;
    private String stadiumName;
    private int foundedYear;

    // Constructor không tham số
    public Team() {}

    // Constructor có tham số
    public Team(int teamID, int seasonID, String teamName, String stadiumName, int foundedYear) {
        this.teamID = teamID;
        this.seasonID = seasonID;
        this.teamName = teamName;
        this.stadiumName = stadiumName;
        this.foundedYear = foundedYear;
    }

    // Getter và Setter cho các thuộc tính
    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        this.foundedYear = foundedYear;
    }

    @Override
    public String toString() {
        return "Team [teamID=" + teamID + ", seasonID=" + seasonID + ", teamName=" + teamName + ", stadiumName=" + stadiumName + ", foundedYear=" + foundedYear + "]";
    }
}
