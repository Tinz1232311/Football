package LoginServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Content.Iconstant;
import Method.DataConnect;
import Method.EmailSender;
import Method.OTPUtils;

public class LoginServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginServer() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("Login".equals(action)) {
            String username = request.getParameter("email");
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // Kiểm tra đầu vào rỗng
            try {
                boolean isValid = isValidUser(username, email, password);
                if (isValid) {
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("email", email);
                    
                    // Gửi OTP qua email
                    boolean otpSent = OTPUtils.sendOTP(request, email);
                    if (otpSent) {
                        response.sendRedirect("LoginOTP.jsp");
                    } else {
                        request.setAttribute("error", "Something went wrong while sending OTP.");
                        getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("error", "Invalid username, email, or password.");
                    getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("error", "Database connection error: " + e.getMessage());
                getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
            }
        } else if ("Register".equals(action)) {
            getServletContext().getRequestDispatcher("/Register.jsp").forward(request, response);
        } else if ("Google".equals(action)) {
            String scope = "email";
            String responseType = "code";
            String googleAuthUrl = "https://accounts.google.com/o/oauth2/auth"
                    + "?scope=" + scope
                    + "&redirect_uri=" + Iconstant.GOOGLE_REDIRECT_URI
                    + "&response_type=" + responseType
                    + "&client_id=" + Iconstant.GOOGLE_CLIENT_ID;

            response.sendRedirect(googleAuthUrl);
        }
    }

    private boolean isValidUser(String username, String email, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DataConnect.getConnection();
        String sql = "SELECT 1 FROM users WHERE username = ? AND email = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } finally {
            DataConnect.closeConnection(conn);
        }
    }
}
