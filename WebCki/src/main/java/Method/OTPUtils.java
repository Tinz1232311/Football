package Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import Method.EmailSender;

public class OTPUtils {

    // Phương thức tạo OTP ngẫu nhiên
    public static String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }
    
    public static boolean isValidOTP(HttpSession session, String userOTP) {
        // Lấy OTP đã lưu trong session
        String generatedOTP = (String) session.getAttribute("otp");

        // Kiểm tra xem OTP người dùng nhập có hợp lệ không
        return generatedOTP != null && generatedOTP.equals(userOTP);
    }

    // Phương thức gửi OTP qua email
    public static boolean sendOTP(HttpServletRequest request, String email) {
        try {
            // Tạo OTP
            String otp = generateOTP();
            String subject = "Your OTP Code";
            String message = "Your OTP code is: " + otp;

            // Lưu OTP vào session để có thể kiểm tra sau này
            HttpSession session = request.getSession();
            session.setAttribute("otp", otp);

            // Gửi email sử dụng lớp EmailSender
            return EmailSender.sendEmail(email, subject, message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}