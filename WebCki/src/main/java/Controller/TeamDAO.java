package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Method.DataConnect;

public class TeamDAO {

    // Kiểm tra nếu đội bóng đã tồn tại trong cơ sở dữ liệu
    public boolean isTeamDuplicate(int seasonID, String teamName, String stadiumName) {
        String query = "SELECT COUNT(*) FROM Teams WHERE SeasonID = ? AND (TeamName = ? OR StadiumName = ?)";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, seasonID);
            preparedStatement.setString(2, teamName);
            preparedStatement.setString(3, stadiumName);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Nếu số lượng đội bóng trùng lặp > 0, trả về true
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách các đội bóng theo SeasonID
    public List<Team> getTeamsBySeasonID(int seasonID) {
        List<Team> teams = new ArrayList<>();
        String query = "SELECT * FROM Teams WHERE SeasonID = ? ORDER BY TeamID ASC";

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, seasonID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int teamID = resultSet.getInt("TeamID");
                String teamName = resultSet.getString("TeamName");
                String stadiumName = resultSet.getString("StadiumName");
                int foundedYear = resultSet.getInt("FoundedYear");

                teams.add(new Team(teamID, seasonID, teamName, stadiumName, foundedYear));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return teams;
    }

    // Lấy thông tin chi tiết đội bóng theo TeamID
    public Team getTeamByID(int teamID) {
        String query = "SELECT * FROM Teams WHERE TeamID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, teamID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int seasonID = resultSet.getInt("SeasonID");
                String teamName = resultSet.getString("TeamName");
                String stadiumName = resultSet.getString("StadiumName");
                int foundedYear = resultSet.getInt("FoundedYear");

                return new Team(teamID, seasonID, teamName, stadiumName, foundedYear);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null; // Không tìm thấy đội bóng
    }

    // Thêm đội bóng mới
    public boolean addTeam(Team team) {
        // Kiểm tra nếu đội bóng đã tồn tại trong cơ sở dữ liệu
        if (isTeamDuplicate(team.getSeasonID(), team.getTeamName(), team.getStadiumName())) {
            return false; // Trả về false nếu đội bóng trùng lặp
        }

        String query = "INSERT INTO Teams (SeasonID, TeamName, StadiumName, FoundedYear) VALUES (?, ?, ?, ?)";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, team.getSeasonID());
            preparedStatement.setString(2, team.getTeamName());
            preparedStatement.setString(3, team.getStadiumName());
            preparedStatement.setInt(4, team.getFoundedYear());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin đội bóng
    public boolean updateTeam(Team team) {
        // Kiểm tra nếu đội bóng mới trùng lặp với đội bóng khác
        if (isTeamExists(team.getSeasonID(), team.getTeamName(), team.getStadiumName(), team.getTeamID())) {
            return false; // Trả về false nếu đội bóng trùng lặp
        }

        String query = "UPDATE Teams SET TeamName = ?, StadiumName = ?, FoundedYear = ? WHERE TeamID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, team.getTeamName());
            preparedStatement.setString(2, team.getStadiumName());
            preparedStatement.setInt(3, team.getFoundedYear());
            preparedStatement.setInt(4, team.getTeamID());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa đội bóng theo TeamID
    public boolean deleteTeam(int teamID) {
        String query = "DELETE FROM Teams WHERE TeamID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, teamID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean isTeamExists(int seasonID, String teamName, String stadiumName, int teamID) {
        String query = "SELECT COUNT(*) FROM Teams " +
                       "WHERE SeasonID = ? " +
                       "AND TeamName = ? OR StadiumName = ?) " +  
                       "AND TeamID != ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, seasonID); // SeasonID cần phải được truyền vào phương thức này
            preparedStatement.setString(2, teamName);
            preparedStatement.setString(3, stadiumName);
            preparedStatement.setInt(4, teamID);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


}
