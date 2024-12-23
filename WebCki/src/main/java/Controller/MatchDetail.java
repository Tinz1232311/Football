package Controller;

import java.util.ArrayList;
import java.util.List;

public class MatchDetail {

    private Team homeTeam;
    private Team awayTeam;

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public static class Team {
        private int teamID;
        private String teamName;
        private String stadiumName;
        private int foundedYear;
        private List<Member> members;

        public Team(int teamID, String teamName, String stadiumName, int foundedYear) {
            this.teamID = teamID;
            this.teamName = teamName;
            this.stadiumName = stadiumName;
            this.foundedYear = foundedYear;
            this.members = new ArrayList<>();
        }

        public int getTeamID() {
            return teamID;
        }

        public String getTeamName() {
            return teamName;
        }

        public String getStadiumName() {
            return stadiumName;
        }

        public int getFoundedYear() {
            return foundedYear;
        }

        public List<Member> getMembers() {
            return members;
        }

        // Thêm cầu thủ vào đội
        public void addMember(Member member) {
            members.add(member);
        }
    }
}
