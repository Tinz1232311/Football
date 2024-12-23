package AdminServer;

import Controller.MatchDetailsDAO;
import Controller.MatchDetail;
import Method.DataConnect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MatchDetailServer extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	Connection connection = null;
        try {
            connection = DataConnect.getConnection();

            MatchDetailsDAO matchDetailsDAO = new MatchDetailsDAO(connection);

            int matchID = Integer.parseInt(request.getParameter("matchID"));

            MatchDetail matchDetail = matchDetailsDAO.getMatchDetails(matchID);

            request.setAttribute("matchDetail", matchDetail);
            request.getRequestDispatcher("/MatchDetails.jsp").forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            response.getWriter().append("Lỗi kết nối cơ sở dữ liệu: ").append(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
