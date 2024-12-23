package Controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchDetailsDAO {

    private Connection connection;

    public MatchDetailsDAO(Connection connection) {
        this.connection = connection;
    }

    public MatchDetail getMatchDetails(int matchID) throws SQLException {
        // Cập nhật truy vấn SQL lấy thông tin về đội và thành viên, không lấy lại MatchID
        String query = "SELECT " +
                       "t.TeamID, t.TeamName, t.StadiumName, t.FoundedYear, " +
                       "tm.MemberID, tm.FullName, tm.Position, tm.BirthDate, tm.JerseyNumber, " +
                       "CASE " +
                       "  WHEN t.TeamID = m.HomeTeamID THEN 'HomeTeam' " +
                       "  WHEN t.TeamID = m.AwayTeamID THEN 'AwayTeam' " +
                       "END AS TeamType " +
                       "FROM Matches m " +
                       "JOIN Teams t ON t.TeamID IN (m.HomeTeamID, m.AwayTeamID) " +
                       "LEFT JOIN TeamMembers tm ON tm.TeamID = t.TeamID " +
                       "WHERE m.MatchID = ?";

        MatchDetail matchDetail = new MatchDetail();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, matchID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String teamType = rs.getString("TeamType");
                    if (teamType == null) continue;

                    // Kiểm tra và thêm đội vào đối tượng MatchDetail
                    if ("HomeTeam".equals(teamType)) {
                        if (matchDetail.getHomeTeam() == null) {
                            matchDetail.setHomeTeam(new MatchDetail.Team(
                                rs.getInt("TeamID"),
                                rs.getString("TeamName"),
                                rs.getString("StadiumName"),
                                rs.getInt("FoundedYear")
                            ));
                        }
                        addMemberToTeam(rs, matchDetail.getHomeTeam());
                    } 
                    else if ("AwayTeam".equals(teamType)) {
                        if (matchDetail.getAwayTeam() == null) {
                            matchDetail.setAwayTeam(new MatchDetail.Team(
                                rs.getInt("TeamID"),
                                rs.getString("TeamName"),
                                rs.getString("StadiumName"),
                                rs.getInt("FoundedYear")
                            ));
                        }
                        addMemberToTeam(rs, matchDetail.getAwayTeam());
                    }
                }
            }
        }

        return matchDetail;
    }

    private void addMemberToTeam(ResultSet rs, MatchDetail.Team team) throws SQLException {
        int memberID = rs.getInt("MemberID");
        if (memberID > 0) {
            Member member = new Member(
                memberID,
                rs.getInt("TeamID"),
                rs.getString("FullName"),
                rs.getString("Position"),
                rs.getDate("BirthDate"),
                rs.getInt("JerseyNumber")
            );
            team.addMember(member); // Thêm thành viên vào đội
        }
    }

}
