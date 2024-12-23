package LoginServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Method.DataConnect;

/**
 * Servlet implementation class RegisServer
 */
public class RegisServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisServer() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String regusername = request.getParameter("email");
        String reguserpass = request.getParameter("password");
        String regemail = request.getParameter("email");

        try {
            if (registerUser(regusername, reguserpass, regemail)) {
            	response.sendRedirect("Login.jsp?successMessage=" + "Account registered successfully!");
            } else {
                response.sendRedirect("Login.jsp?errorMessage="+"Duplicate username");
            }
        } catch (SQLException | ClassNotFoundException e) {
        	response.sendRedirect("Login.jsp?errorMessage=" + "Failed");
        }
    }

    private boolean registerUser(String username, String password, String email) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement userStmt = null;
        PreparedStatement checkStmt = null;

        try {
            conn = DataConnect.getConnection();
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            // Kiểm tra trùng lặp tài khoản
            String checkUserSQL = "SELECT COUNT(*) FROM users WHERE username = ?";
            checkStmt = conn.prepareStatement(checkUserSQL);
            checkStmt.setString(1, username);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }

            // Chèn dữ liệu vào bảng users
            String insertUserSQL = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            userStmt = conn.prepareStatement(insertUserSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, password);
            userStmt.setString(3, email);

            int affectedRows = userStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            conn.commit(); // Xác nhận giao dịch
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác nếu có lỗi
            }
            throw e;
        } finally {
            if (checkStmt != null) checkStmt.close();
            if (userStmt != null) userStmt.close();
            if (conn != null) DataConnect.closeConnection(conn);
        }
    }

}
