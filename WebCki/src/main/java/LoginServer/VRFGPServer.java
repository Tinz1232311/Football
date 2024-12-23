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
import java.sql.SQLException;
import java.util.Random;

import Method.DataConnect;
import Method.EmailSender;  // Giả sử có lớp EmailSender để gửi email

public class VRFGPServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public VRFGPServer() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredOTP = request.getParameter("otp");  // OTP người dùng nhập
        HttpSession session = request.getSession();
        String generatedOTP = (String) session.getAttribute("otp");  // OTP đã gửi qua email và lưu trong session
        String email = (String) session.getAttribute("email");  // Lấy email từ session

        // Kiểm tra tính hợp lệ của OTP
        if (generatedOTP != null && generatedOTP.equals(enteredOTP)) {
            // Nếu OTP hợp lệ, tạo mật khẩu mới và cập nhật vào CSDL
            String newPassword = generateNewPassword();  // Tạo mật khẩu mới
            try {
                // Cập nhật mật khẩu mới vào CSDL (không cần mã hóa)
                boolean isUpdated = updatePasswordInDatabase(email, newPassword);

                if (isUpdated) {
                    // Gửi mật khẩu mới qua email cho người dùng
                    boolean emailSent = EmailSender.sendEmail(email, "Mật khẩu mới", "Mật khẩu mới của bạn là: " + newPassword);
                    
                    if (emailSent) {
                        // Chuyển hướng đến trang login sau khi thành công
                        response.sendRedirect("Login.jsp");
                    } else {
                        request.setAttribute("errorMessage", "Không thể gửi email. Vui lòng thử lại.");
                        getServletContext().getRequestDispatcher("/VROTPFGPass.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "Không thể cập nhật mật khẩu. Vui lòng thử lại.");
                    getServletContext().getRequestDispatcher("/VROTPFGPass.jsp").forward(request, response);
                }
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Lỗi khi cập nhật mật khẩu. Vui lòng thử lại.");
                getServletContext().getRequestDispatcher("/VROTPFGPass.jsp").forward(request, response);
            }
        } else {
            // Nếu OTP sai, gửi thông báo lỗi về JSP
            request.setAttribute("errorMessage", "Mã OTP không chính xác. Vui lòng thử lại.");
            getServletContext().getRequestDispatcher("/VROTPFGPass.jsp").forward(request, response);  // Chuyển về trang nhập OTP
        }
    }

    // Tạo mật khẩu ngẫu nhiên
    private String generateNewPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        
        for (int i = 0; i < 8; i++) {  // Tạo mật khẩu 8 ký tự
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        
        return password.toString();
    }

    // Cập nhật mật khẩu mới vào CSDL (Không mã hóa mật khẩu)
    private boolean updatePasswordInDatabase(String email, String newPassword) {
        Connection conn = null;  // Kết nối CSDL (được tạo từ DataSource hoặc DriverManager)
        PreparedStatement ps = null;

        try {
            // Đăng ký driver JDBC nếu chưa được đăng ký
            try {
                // Driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                // Xử lý ngoại lệ nếu không tìm thấy driver
                e.printStackTrace();
                throw new ServletException("Không tìm thấy driver MySQL JDBC. Vui lòng kiểm tra cấu hình.");
            }

            // Kết nối với CSDL (Giả sử có DataSource hoặc sử dụng DriverManager để tạo kết nối)
            conn = DataConnect.getConnection();  // Thay thế bằng kết nối thực tế của bạn

            // SQL để cập nhật mật khẩu
            String sql = "UPDATE users SET password = ? WHERE email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, newPassword);  // Lưu mật khẩu chưa mã hóa vào CSDL
            ps.setString(2, email);

            // Thực thi câu lệnh cập nhật
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;  // Trả về true nếu có ít nhất một dòng bị ảnh hưởng
        } catch (SQLException e) {
            // Log lỗi và trả về false nếu có lỗi xảy ra
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // Log lỗi và trả về false nếu có lỗi khác xảy ra
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
