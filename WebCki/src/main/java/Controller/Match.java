package Controller;

import java.util.Date;

public class Match {
    private int matchID;
    private int seasonID;
    private Date matchDate;
    private int homeTeamID;  // Đổi thành kiểu int
    private int awayTeamID;  // Đổi thành kiểu int
    private int homeScore;
    private int awayScore;

    // Constructor
    public Match(int matchID, int seasonID, Date matchDate, int homeTeamID, int awayTeamID, int homeScore, int awayScore) {
        this.matchID = matchID;
        this.seasonID = seasonID;
        this.matchDate = matchDate;
        this.homeTeamID = homeTeamID;
        this.awayTeamID = awayTeamID;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
    
    public Match()
    {
    	
    }

    // Getters and Setters
    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public Date getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(Date matchDate) {
        this.matchDate = matchDate;
    }

    public int getHomeTeamID() {
        return homeTeamID;
    }

    public void setHomeTeamID(int homeTeamID) {
        this.homeTeamID = homeTeamID;
    }

    public int getAwayTeamID() {
        return awayTeamID;
    }

    public void setAwayTeamID(int awayTeamID) {
        this.awayTeamID = awayTeamID;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }
}
