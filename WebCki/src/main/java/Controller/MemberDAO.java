package Controller;

import Method.DataConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // Lấy danh sách thành viên theo TeamID
    public List<Member> getMembersByTeamID(int teamID) {
        List<Member> members = new ArrayList<>();
        String query = "SELECT * FROM TeamMembers WHERE TeamID = ? ORDER BY MemberID ASC"; // Lọc theo TeamID và sắp xếp

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, teamID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int memberID = resultSet.getInt("MemberID");
                String fullName = resultSet.getString("FullName");
                String position = resultSet.getString("Position");
                Date birthDate = resultSet.getDate("BirthDate");
                int jerseyNumber = resultSet.getInt("JerseyNumber");
                members.add(new Member(memberID, teamID, fullName, position, birthDate, jerseyNumber)); // Tạo đối tượng Member và thêm vào danh sách
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return members;
    }

    // Thêm thành viên mới
    public boolean addMember(Member member) {
    	
    	if (isJerseyNumberExistsInTeam(member.getTeamID(), member.getJerseyNumber())) {
    	    return false;  // Nếu đội bóng đã tồn tại, không cho phép cập nhật
    	}
    	
        String query = "INSERT INTO TeamMembers (TeamID, FullName, Position, BirthDate, JerseyNumber) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, member.getTeamID());
            preparedStatement.setString(2, member.getFullName());
            preparedStatement.setString(3, member.getPosition());
            preparedStatement.setDate(4, new java.sql.Date(member.getBirthDate().getTime()));
            preparedStatement.setInt(5, member.getJerseyNumber());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cập nhật thông tin thành viên
    public boolean updateMember(Member member) {
        // Cập nhật thông tin thành viên nhưng không cho phép sửa TeamID
    	if (isMemberExists(member.getTeamID(), member.getJerseyNumber(), member.getMemberID())) {
    	    return false;  // Nếu đội bóng đã tồn tại, không cho phép cập nhật
    	}   	
    	
        String query = "UPDATE TeamMembers SET FullName = ?, Position = ?, BirthDate = ?, JerseyNumber = ? WHERE MemberID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Cập nhật các trường ngoài TeamID
            preparedStatement.setString(1, member.getFullName());
            preparedStatement.setString(2, member.getPosition());
            preparedStatement.setDate(3, new java.sql.Date(member.getBirthDate().getTime()));
            preparedStatement.setInt(4, member.getJerseyNumber());
            preparedStatement.setInt(5, member.getMemberID());
            
            // Thực hiện cập nhật
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Xóa thành viên theo ID
 // Xóa thành viên theo ID và xóa luôn các quan hệ của nó ở các bảng có khóa ngoại
    public boolean deleteMember(int memberID) {
        String deleteTeamMembersQuery = "DELETE FROM TeamMembers WHERE MemberID = ?";
        
        Connection connection = null;
        try {
            connection = DataConnect.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement deleteTeamMembersStmt = connection.prepareStatement(deleteTeamMembersQuery)) {

                // Xóa thành viên trong TeamMembers
                deleteTeamMembersStmt.setInt(1, memberID);
                deleteTeamMembersStmt.executeUpdate();

                // Commit giao dịch
                connection.commit();
                return true;

            } catch (SQLException e) {
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



    // Lấy thông tin thành viên theo ID
    public Member getMemberByID(int memberID) {
        String query = "SELECT * FROM TeamMembers WHERE MemberID = ?";
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, memberID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int teamID = resultSet.getInt("TeamID");
                String fullName = resultSet.getString("FullName");
                String position = resultSet.getString("Position");
                Date birthDate = resultSet.getDate("BirthDate");
                int jerseyNumber = resultSet.getInt("JerseyNumber");
                return new Member(memberID, teamID, fullName, position, birthDate, jerseyNumber); // Trả về đối tượng Member với các thông tin đã lấy
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy thành viên
    }
    
 // Kiểm tra xem cầu thủ đã tồn tại theo số áo chưa
 // Kiểm tra xem cầu thủ có bị lặp lại trong cùng một đội dựa trên số áo
    public boolean isJerseyNumberExistsInTeam(int teamID, int jerseyNumber) {
        String query = "SELECT COUNT(*) FROM TeamMembers WHERE TeamID = ? AND JerseyNumber = ?";

        try (Connection connection = DataConnect.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, teamID);
            stmt.setInt(2, jerseyNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu > 0 thì có cầu thủ bị lặp lại trong đội
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false; // Trả về false nếu không có cầu thủ nào bị lặp lại
    }
    
    public boolean isMemberExists(int teamID, int jerseyNumber, int memberID) {
        String query = "SELECT COUNT(*) FROM TeamMembers " +
                       "WHERE TeamID = ? " +
                       "AND JerseyNumber = ? " + // Thêm điều kiện kiểm tra số áo
                       "AND MemberID != ?";     // Loại trừ thành viên hiện tại nếu cần
        
        try (Connection connection = DataConnect.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, teamID);           // Đội bóng cần kiểm tra
            preparedStatement.setInt(2, jerseyNumber);     // Số áo cần kiểm tra
            preparedStatement.setInt(3, memberID);         // ID của thành viên cần loại trừ
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;        // Trả về true nếu số áo bị trùng
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu không có lỗi hoặc không trùng số áo
    }

}
