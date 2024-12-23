package LoginServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Method.OTPUtils;  // Import OTPUtils
import Method.DataConnect;  // Đảm bảo lớp kết nối cơ sở dữ liệu của bạn

public class FGServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FGServer() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        // Kiểm tra xem email có tồn tại trong cơ sở dữ liệu hay không
        if (isEmailExists(email)) {
            try {
                boolean otpSent = OTPUtils.sendOTP(request, email);  // Sử dụng OTPUtils để gửi OTP
                if (otpSent) {
                    // Lưu email vào session để sử dụng trong bước xác thực OTP
                    HttpSession session = request.getSession();
                    session.setAttribute("email", email);
                    response.sendRedirect("VROTPFGPass.jsp");  // Chuyển hướng đến trang xác thực OTP
                } else {
                    // Nếu gửi OTP thất bại
                    request.setAttribute("error", "Failed to send OTP. Please try again.");
                    getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
                }
            } catch (Exception e) {
                // Xử lý lỗi trong quá trình gửi OTP
                e.printStackTrace();
                request.setAttribute("error", "Error occurred while sending OTP: " + e.getMessage());
                getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
            }
        } else {
            // Nếu email không tồn tại trong cơ sở dữ liệu
            request.setAttribute("error", "Email not found. Please check your email and try again.");
            getServletContext().getRequestDispatcher("/FGPass.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);  // Chuyển tiếp yêu cầu POST cho doGet
    }

    // Kiểm tra email có tồn tại trong cơ sở dữ liệu không
    private boolean isEmailExists(String email) {
        try (Connection conn = DataConnect.getConnection()) {
            String sql = "SELECT 1 FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                return rs.next();  // Nếu có dòng dữ liệu trả về, email tồn tại
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Email không tồn tại
    }
}
