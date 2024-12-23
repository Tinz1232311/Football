package AdminServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

import Controller.Match;
import Controller.MatchDAO;
import Controller.Team;
import Controller.TeamDAO;

public class MatchServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String seasonID = request.getParameter("seasonID");

        if (seasonID == null || seasonID.isEmpty()) {
            response.sendRedirect("AdminServer");
            return;
        }

        try {
            switch (action != null ? action : "viewMatch") {
                case "edit":
                    handleEditGetRequest(request, response, seasonID);
                    break;
                default:
                    handleViewMatches(request, response, seasonID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleViewMatches(HttpServletRequest request, HttpServletResponse response, String seasonID) throws ServletException, IOException {
        try {
            MatchDAO matchDAO = new MatchDAO();
            List<Match> matches = matchDAO.getMatchesBySeasonID(Integer.parseInt(seasonID));
            TeamDAO teamDAO = new TeamDAO();
            List<Team> team = teamDAO.getTeamsBySeasonID(Integer.parseInt(seasonID));
            request.setAttribute("teams", team);
            request.setAttribute("matches", matches);
            request.setAttribute("seasonID", seasonID);
            request.getRequestDispatcher("/Match.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Failed to load matches.");
        }
    }

    private void handleEditGetRequest(HttpServletRequest request, HttpServletResponse response, String seasonID) throws ServletException, IOException {
        String matchID = request.getParameter("matchID");

        if (matchID != null && !matchID.isEmpty()) {
            try {
                MatchDAO matchDAO = new MatchDAO();
                Match match = matchDAO.getMatchByID(Integer.parseInt(matchID));
                TeamDAO teamDAO = new TeamDAO();
                List<Team> team = teamDAO.getTeamsBySeasonID(Integer.parseInt(seasonID));
                request.setAttribute("teams", team);

                if (match != null) {
                	request.setAttribute("teams", team);
                    request.setAttribute("match", match);
                    request.setAttribute("seasonID", seasonID);
                    request.getRequestDispatcher("/EditMatch.jsp").forward(request, response);
                } else {
                    response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Match not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Error loading match data.");
            }
        } else {
            response.sendRedirect("MatchServer?seasonID=" + seasonID);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String seasonID = request.getParameter("seasonID");

        if (seasonID == null || seasonID.isEmpty()) {
            response.sendRedirect("AdminServer");
            return;
        }

        try {
            MatchDAO matchDAO = new MatchDAO();
            switch (action) {
                case "add":
                    handleAddMatch(request, response, matchDAO, seasonID);
                    break;
                case "edit":
                    handleEditPostRequest(request, response, matchDAO);
                    break;
                case "delete": // Xử lý xóa trận đấu qua POST
                    handleDeletePostRequest(request, response, seasonID, matchDAO);
                    break;
                default:
                    response.sendRedirect("MatchServer?seasonID=" + seasonID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleDeletePostRequest(HttpServletRequest request, HttpServletResponse response, String seasonID, MatchDAO matchDAO) throws ServletException, IOException {
        String matchID = request.getParameter("matchID");

        if (matchID != null && !matchID.isEmpty()) {
            try {
                boolean success = matchDAO.deleteMatch(Integer.parseInt(matchID));
                if (success) {
                    response.sendRedirect("MatchServer?seasonID=" + seasonID);
                } else {
                    response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Failed to delete the match.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Error deleting match.");
            }
        } else {
            response.sendRedirect("MatchServer?seasonID=" + seasonID);
        }
    }

    private void handleAddMatch(HttpServletRequest request, HttpServletResponse response, MatchDAO matchDAO, String seasonID) throws ServletException, IOException {
        try {
            String matchDateStr = request.getParameter("matchDate");
            String homeTeamID = request.getParameter("homeTeamID");
            String awayTeamID = request.getParameter("awayTeamID");
            String homeScore = request.getParameter("homeScore");
            String awayScore = request.getParameter("awayScore");

            if (seasonID == null || matchDateStr == null || homeTeamID == null || awayTeamID == null || homeScore == null || awayScore == null ||
                seasonID.isEmpty() || matchDateStr.isEmpty() || homeTeamID.isEmpty() || awayTeamID.isEmpty() || homeScore.isEmpty() || awayScore.isEmpty()) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "All fields are required.");
                return;
            }

            if (homeTeamID.equals(awayTeamID)) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Home and away teams cannot be the same.");
                return;
            }

            // Chuyển đổi chuỗi matchDateStr thành java.util.Date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date matchDate = dateFormat.parse(matchDateStr);

            // Chuyển đổi java.util.Date thành java.sql.Date
            java.sql.Date sqlMatchDate = new java.sql.Date(matchDate.getTime());

            // Kiểm tra trùng lặp trận đấu
            if (matchDAO.isMatchDuplicate(Integer.parseInt(seasonID), Integer.parseInt(homeTeamID), Integer.parseInt(awayTeamID), sqlMatchDate)) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Duplicate match found.");
                return;
            }

            int homeTeamIDInt = Integer.parseInt(homeTeamID);
            int awayTeamIDInt = Integer.parseInt(awayTeamID);
            int homeScoreInt = Integer.parseInt(homeScore);
            int awayScoreInt = Integer.parseInt(awayScore);

            Match match = new Match(0, Integer.parseInt(seasonID), matchDate, homeTeamIDInt, awayTeamIDInt, homeScoreInt, awayScoreInt);

            boolean success = matchDAO.addMatch(match);

            if (success) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID);
            } else {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Doi bong khong ton tai");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Error processing your request.");
        }
    }

    private void handleEditPostRequest(HttpServletRequest request, HttpServletResponse response, MatchDAO matchDAO) throws ServletException, IOException {
        String seasonID = request.getParameter("seasonID");
        try {
            String matchID = request.getParameter("matchID");
            String matchDate = request.getParameter("matchDate");
            String homeTeamID = request.getParameter("homeTeamID");
            String awayTeamID = request.getParameter("awayTeamID");
            String homeScore = request.getParameter("homeScore");
            String awayScore = request.getParameter("awayScore");

            if (seasonID == null || matchID == null || homeScore == null || awayScore == null || seasonID.isEmpty() || matchID.isEmpty() || homeScore.isEmpty() || awayScore.isEmpty()) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "All fields are required.");
                return;
            }


            if (homeTeamID.equals(awayTeamID)) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Home and away teams cannot be the same.");
                return;
            }


            int homeScoreInt = Integer.parseInt(homeScore);
            int awayScoreInt = Integer.parseInt(awayScore);

            // Lấy thông tin trận đấu cần sửa
            Match match = matchDAO.getMatchByID(Integer.parseInt(matchID));

            // Nếu không tìm thấy trận đấu, trả về thông báo lỗi
            if (match == null) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Match not found.");
                return;
            }

            // Kiểm tra trùng lặp: Kiểm tra xem trận đấu mới có tồn tại trong CSDL chưa (đặc biệt là nếu chỉ có đội và ngày thi đấu thay đổi)
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            // Xử lý matchDate để chuyển sang java.sql.Date
            Date matchDateObj = dateFormat.parse(matchDate); // Convert to Date object
            java.sql.Date sqlMatchDate = new java.sql.Date(matchDateObj.getTime()); // Convert to java.sql.Date
            
            System.out.println("seasonID: " + seasonID);
            System.out.println("matchID: " + matchID);
            System.out.println("matchDate: " + sqlMatchDate);
            System.out.println("homeTeamID: " + homeTeamID);
            System.out.println("awayTeamID: " + awayTeamID);


            // Kiểm tra trùng lặp, không cho phép cập nhật nếu trận đấu đã tồn tại trong CSDL
            if (matchDAO.isMatchExists(Integer.parseInt(seasonID), Integer.parseInt(matchID), sqlMatchDate, Integer.parseInt(homeTeamID), Integer.parseInt(awayTeamID))) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Duplicate match found.");
                return;
            }
            
            System.out.print(matchDAO.isMatchExists(Integer.parseInt(seasonID), Integer.parseInt(matchID), sqlMatchDate, Integer.parseInt(homeTeamID), Integer.parseInt(awayTeamID)));

            // Cập nhật tất cả các thông tin của trận đấu
            match.setHomeTeamID(Integer.parseInt(homeTeamID));  // Cập nhật đội chủ nhà
            match.setAwayTeamID(Integer.parseInt(awayTeamID));  // Cập nhật đội khách
            match.setHomeScore(homeScoreInt);  // Cập nhật điểm số đội chủ nhà
            match.setAwayScore(awayScoreInt);  // Cập nhật điểm số đội khách
            match.setMatchDate(sqlMatchDate);  // Cập nhật ngày thi đấu

            // Cập nhật trận đấu vào CSDL
            boolean success = matchDAO.updateMatch(match);

            // Kiểm tra kết quả cập nhật
            if (success) {
                response.sendRedirect("MatchServer?seasonID=" + seasonID);
            } else {
                response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Error updating match.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MatchServer?seasonID=" + seasonID + "&errorMessage=" + "Error processing your request.");
        }
    }
}
