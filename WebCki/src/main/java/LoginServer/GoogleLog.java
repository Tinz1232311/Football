package LoginServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import Controller.GoogleAccount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import Method.DataConnect;
import Method.Google;

// Import các lớp cần thiết từ Method
import Method.EmailSender;

public class GoogleLog extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    public GoogleLog() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        Google gg = new Google();
        String accessToken = gg.getToken(code);
        GoogleAccount acc = gg.getUserInfo(accessToken);
        String email = acc.getEmail();  // Giả sử email trả về là acc.getEmail()

        // Kiểm tra xem email đã tồn tại trong CSDL chưa
        if (isEmailExists(email)) {
            // Nếu đã tồn tại, chuyển hướng đến trang Home.jsp
            response.sendRedirect("AdminServer");
        } else {
            // Nếu chưa tồn tại, tạo tài khoản mới
            String password = generateRandomPassword();
            insertUserToDB(email, password);
            
            // Gửi email với mật khẩu cho người dùng
            sendEmail(email, password);
            
            // Sau khi tạo tài khoản, chuyển hướng đến trang Home.Email doesjsp
            response.sendRedirect("AdminServer");
        }
    }

    // Kiểm tra xem email đã tồn tại trong CSDL chưa
    private boolean isEmailExists(String email) {
        try (Connection conn = DataConnect.getConnection()) {
            String sql = "SELECT 1 FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Email already exists: " + email);  // Kiểm tra kết quả
                    return true;  // Email đã tồn tại
                } else {
                    System.out.println(" not exist: " + email);  // Email không tồn tại
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Email chưa tồn tại
    }

    // Chèn tài khoản mới vào CSDL với vai trò mặc định là 'customer' (role = 3)
    private void insertUserToDB(String email, String password) {
        try (Connection conn = DataConnect.getConnection()) {
            String sql = "INSERT INTO users (email, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, email);
                stmt.setString(2, email);  // Username là email
                stmt.setString(3, password);
                stmt.executeUpdate();

                // Lấy user_id của người dùng vừa được tạo
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long userId = generatedKeys.getLong(1);

                        // Thêm vào bảng user_roles với role = 3 (customer)
                        String insertRoleSQL = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
                        try (PreparedStatement roleStmt = conn.prepareStatement(insertRoleSQL)) {
                            roleStmt.setLong(1, userId);
                            roleStmt.setInt(2, 3); // 3 là role_id cho 'customer'
                            roleStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo mật khẩu ngẫu nhiên
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        Random rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {  // Mật khẩu dài 10 ký tự
            password.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return password.toString();
    }

    // Gửi email chứa mật khẩu cho người dùng
    private void sendEmail(String to, String password) {
        String subject = "Your New Account Details";
        String message = "Your new account has been created. Your password is: " + password;

        // Sử dụng phương thức sendEmail từ lớp EmailSender
        boolean emailSent = EmailSender.sendEmail(to, subject, message);

        if (emailSent) {
            System.out.println("Email sent successfully to " + to);
        } else {
            System.out.println("Failed to send email to " + to);
        }
    }
}
