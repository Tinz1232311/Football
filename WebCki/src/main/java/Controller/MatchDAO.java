package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Date; // Dùng cho kiểu DATE trong MySQL
import java.util.ArrayList;
import java.util.List;

import Method.DataConnect;

public class MatchDAO {

	public boolean isMatchDuplicate(int seasonID, int homeTeamID, int awayTeamID, Date matchDate) {
	    String query = "SELECT COUNT(*) FROM Matches WHERE HomeTeamID = ? AND AwayTeamID = ? AND MatchDate = ? AND SeasonID = ?";
	    try (Connection connection = DataConnect.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, homeTeamID);
	        preparedStatement.setInt(2, awayTeamID);
	        preparedStatement.setDate(3, matchDate);
	        preparedStatement.setInt(4, seasonID);

	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            return resultSet.getInt(1) > 0; // Nếu số lượng trận đấu trùng lặp > 0, trả về true
	        }
	    } catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return false;
	}


    // Lấy danh sách các trận đấu theo SeasonID
    public List<Match> getMatchesBySeasonID(int seasonID) {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM Matches WHERE SeasonID = ? ORDER BY MatchID ASC";

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, seasonID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int matchID = resultSet.getInt("MatchID");
                Date matchDate = resultSet.getDate("MatchDate");
                int homeTeamID = resultSet.getInt("HomeTeamID");
                int awayTeamID = resultSet.getInt("AwayTeamID");
                int homeScore = resultSet.getInt("HomeScore");
                int awayScore = resultSet.getInt("AwayScore");

                matches.add(new Match(matchID, seasonID, matchDate, homeTeamID, awayTeamID, homeScore, awayScore));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return matches;
    }

    // Lấy thông tin chi tiết trận đấu theo MatchID
    public Match getMatchByID(int matchID) {
        String query = "SELECT * FROM Matches WHERE MatchID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, matchID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int seasonID = resultSet.getInt("SeasonID");
                Date matchDate = resultSet.getDate("MatchDate");
                int homeTeamID = resultSet.getInt("HomeTeamID");
                int awayTeamID = resultSet.getInt("AwayTeamID");
                int homeScore = resultSet.getInt("HomeScore");
                int awayScore = resultSet.getInt("AwayScore");

                return new Match(matchID, seasonID, matchDate, homeTeamID, awayTeamID, homeScore, awayScore);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null; // Không tìm thấy trận đấu
    }

    // Thêm trận đấu mới
    public boolean addMatch(Match match) {
        // Kiểm tra nếu trận đấu đã tồn tại trong cơ sở dữ liệu
        if (isMatchDuplicate(match.getSeasonID(), match.getHomeTeamID(), match.getAwayTeamID(), new Date(match.getMatchDate().getTime()))) {
            return false; // Trả về false nếu trận đấu trùng lặp
        }

        String query = "INSERT INTO Matches (SeasonID, MatchDate, HomeTeamID, AwayTeamID, HomeScore, AwayScore) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, match.getSeasonID());
            preparedStatement.setDate(2, new Date(match.getMatchDate().getTime()));
            preparedStatement.setInt(3, match.getHomeTeamID());
            preparedStatement.setInt(4, match.getAwayTeamID());
            preparedStatement.setInt(5, match.getHomeScore());
            preparedStatement.setInt(6, match.getAwayScore());

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin trận đấu
    public boolean updateMatch(Match match) {
        java.sql.Date matchDateSql = new java.sql.Date(match.getMatchDate().getTime()); 
        if (isMatchExists(match.getSeasonID(), match.getMatchID(), matchDateSql, match.getHomeTeamID(), match.getAwayTeamID())) {
            return false; 
        }

        String query = "UPDATE Matches SET MatchDate = ?, HomeTeamID = ?, AwayTeamID = ?, HomeScore = ?, AwayScore = ? WHERE MatchID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Thiết lập các tham số cho câu lệnh SQL
            preparedStatement.setDate(1, matchDateSql);  
            preparedStatement.setInt(2, match.getHomeTeamID()); 
            preparedStatement.setInt(3, match.getAwayTeamID()); 
            preparedStatement.setInt(4, match.getHomeScore()); 
            preparedStatement.setInt(5, match.getAwayScore()); 
            preparedStatement.setInt(6, match.getMatchID());  

            // Thực hiện câu lệnh update và kiểm tra số dòng ảnh hưởng
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace(); // Xử lý ngoại lệ
        }
        return false;  // Trả về false nếu có lỗi
    }


    // Xóa trận đấu theo MatchID
    public boolean deleteMatch(int matchID) {
        String deleteMatchQuery = "DELETE FROM Matches WHERE MatchID = ?";
        
        try (Connection connection = DataConnect.getConnection()) {
 
            connection.setAutoCommit(false);


            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteMatchQuery)) {
                preparedStatement.setInt(1, matchID);
                int rowsAffected = preparedStatement.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Nếu xóa trận đấu thành công, commit giao dịch
                    connection.commit();
                    return true;
                } else {
                    // Nếu không xóa được trận đấu, rollback giao dịch
                    connection.rollback();
                    return false;
                }
            } catch (SQLException e) {
                // Nếu có lỗi trong quá trình xóa trận đấu, rollback giao dịch
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean isMatchExists(int seasonID, int matchID, Date matchDate, int homeTeamID, int awayTeamID) {
        String query = "SELECT COUNT(*) FROM Matches " +
                       "WHERE SeasonID = ? " +
                       "AND MatchID != ? " +  
                       "AND MatchDate = ? " +  
                       "AND HomeTeamID = ? " + 
                       "AND AwayTeamID = ?";   
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {        	

            preparedStatement.setInt(1, seasonID);
            preparedStatement.setInt(2, matchID);
            preparedStatement.setDate(3, matchDate); 
            preparedStatement.setInt(4, homeTeamID); 
            preparedStatement.setInt(5, awayTeamID);
            
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
