package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Method.DataConnect;

public class SeasonDAO {

    // Lấy danh sách tất cả mùa giải
    public List<Season> getAllSeasons() {
        List<Season> seasons = new ArrayList<>();
        String query = "SELECT * FROM Seasons ORDER BY SeasonID ASC"; // Thêm sắp xếp ở đây

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int seasonID = resultSet.getInt("SeasonID");
                String detail = resultSet.getString("Detail");  // Đổi 'Year' thành 'Detail'
                seasons.add(new Season(seasonID, detail));  // Sử dụng 'detail' thay vì 'year'
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return seasons;
    }

    // Kiểm tra xem mùa giải đã tồn tại chưa
    public boolean isSeasonExists(String detail) {  // Đổi 'year' thành 'detail'
        String query = "SELECT COUNT(*) FROM Seasons WHERE Detail = ?";  // Đổi 'Year' thành 'Detail'

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, detail);  // Sử dụng 'detail' thay vì 'year'
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu > 0 thì mùa giải đã tồn tại
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Thêm mùa giải mới
    public boolean addSeason(Season season) {
        String query = "INSERT INTO Seasons (Detail) VALUES (?)";  // Đổi 'Year' thành 'Detail'
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, season.getDetail());  // Sử dụng 'detail' thay vì 'year'
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin mùa giải (edit)
    public boolean editSeason(int seasonID, String detail) {  // Đổi 'year' thành 'detail'
        String query = "UPDATE Seasons SET Detail = ? WHERE SeasonID = ?";  // Đổi 'Year' thành 'Detail'
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, detail);  // Sử dụng 'detail' thay vì 'year'
            preparedStatement.setInt(2, seasonID);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa thông tin mùa giải
 // Cập nhật phương thức xóa mùa giải với việc xóa các đối tượng liên quan
 // Cập nhật phương thức xóa mùa giải với việc xóa các đối tượng liên quan
 // Cập nhật phương thức xóa mùa giải với việc xóa các đối tượng liên quan
    public boolean deleteSeason(int seasonID) {
        // Bắt đầu trong một giao dịch để đảm bảo tính toàn vẹn dữ liệu
        String deleteMatchParticipantsQuery = "DELETE FROM MatchParticipants WHERE MatchID IN (SELECT MatchID FROM Matches WHERE SeasonID = ?)";
        String deleteMatchesQuery = "DELETE FROM Matches WHERE SeasonID = ?";
        String deleteTeamMembersQuery = "DELETE FROM TeamMembers WHERE TeamID IN (SELECT TeamID FROM Teams WHERE TeamID IN (SELECT HomeTeamID FROM Matches WHERE SeasonID = ?)) OR TeamID IN (SELECT TeamID FROM Teams WHERE TeamID IN (SELECT AwayTeamID FROM Matches WHERE SeasonID = ?))";
        String deleteTeamsQuery = "DELETE FROM Teams WHERE TeamID IN (SELECT HomeTeamID FROM Matches WHERE SeasonID = ?) OR TeamID IN (SELECT AwayTeamID FROM Matches WHERE SeasonID = ?)";
        String deleteSeasonQuery = "DELETE FROM Seasons WHERE SeasonID = ?";

        Connection connection = null;
        try {
            // Thiết lập kết nối CSDL
            connection = DataConnect.getConnection();

            // Bắt đầu giao dịch
            connection.setAutoCommit(false);

            try (PreparedStatement deleteMatchParticipantsStmt = connection.prepareStatement(deleteMatchParticipantsQuery);
                 PreparedStatement deleteMatchesStmt = connection.prepareStatement(deleteMatchesQuery);
                 PreparedStatement deleteTeamMembersStmt = connection.prepareStatement(deleteTeamMembersQuery);
                 PreparedStatement deleteTeamsStmt = connection.prepareStatement(deleteTeamsQuery);
                 PreparedStatement deleteSeasonStmt = connection.prepareStatement(deleteSeasonQuery)) {

                // Xóa các bản ghi MatchParticipants liên quan đến mùa giải
                deleteMatchParticipantsStmt.setInt(1, seasonID);
                deleteMatchParticipantsStmt.executeUpdate();

                // Xóa các trận đấu liên quan đến mùa giải
                deleteMatchesStmt.setInt(1, seasonID);
                deleteMatchesStmt.executeUpdate();

                // Xóa các thành viên đội bóng có liên quan đến trận đấu trong mùa giải
                deleteTeamMembersStmt.setInt(1, seasonID);
                deleteTeamMembersStmt.setInt(2, seasonID);
                deleteTeamMembersStmt.executeUpdate();

                // Xóa các đội bóng có liên quan đến trận đấu trong mùa giải
                deleteTeamsStmt.setInt(1, seasonID);
                deleteTeamsStmt.setInt(2, seasonID);
                deleteTeamsStmt.executeUpdate();

                // Xóa mùa giải
                deleteSeasonStmt.setInt(1, seasonID);
                deleteSeasonStmt.executeUpdate();

                // Commit giao dịch
                connection.commit();
                return true;

            } catch (SQLException e) {
                // Nếu có lỗi trong block con, rollback giao dịch
                connection.rollback();
                e.printStackTrace();
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }



    // Lấy thông tin mùa giải theo ID
    public Season getSeasonById(int seasonID) {
        String query = "SELECT * FROM Seasons WHERE SeasonID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, seasonID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String detail = resultSet.getString("Detail");  // Đổi 'Year' thành 'Detail'
                return new Season(seasonID, detail);  // Trả về đối tượng Season với 'detail'
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;  // Trả về null nếu không tìm thấy
    }
}
