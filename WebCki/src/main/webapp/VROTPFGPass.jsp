<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Verify OTP</title>
    <style>
        /* Reset và cấu hình font */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        /* Cơ sở giao diện */
        body {
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* Khung chính */
        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }

        /* Tiêu đề */
        .container h2 {
            text-align: center;
            margin-bottom: 15px;
            color: #333;
        }

        /* Input - OTP */
        .container input[type="text"] {
            width: 100%;
            height: 42px; /* Chiều cao input đồng bộ với nút */
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
            font-size: 14px;
        }

        /* Nút submit với gradient xanh */
        .container button {
            width: 100%;
            height: 42px; /* Chiều cao nút đồng bộ với input */
            padding: 10px;
            background: linear-gradient(90deg, #4facfe, #00f2fe);
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        /* Hiệu ứng hover */
        .container button:hover {
            background: linear-gradient(90deg, #00b4db, #0083ff);
            transform: scale(1.05);
        }

        /* Thông báo lỗi */
        .error {
            color: red;
            font-size: 0.9em;
            text-align: center;
            margin: 10px 0;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Verify OTP</h2>

    <form action="VRFGPServer" method="post">
        <label for="otp">Enter OTP:</label>
        <input type="text" id="otp" name="otp" placeholder="Enter OTP" required />

        <%-- Hiển thị thông báo lỗi nếu có --%>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null) {
        %>
        <div class="error"><%= errorMessage %></div>
        <% } %>

        <button type="submit">Verify OTP</button>
    </form>
</div>

</body>
</html>
