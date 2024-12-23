<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <style>
        /* Reset mặc định và font */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body {
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        /* Khung chính */
        .container {
            width: 400px;
            padding: 20px;
            background-color: white;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        /* Tiêu đề */
        h2 {
            text-align: center;
            margin-bottom: 15px;
        }

        /* Label */
        label {
            font-size: 14px;
            margin-bottom: 5px;
            display: block;
            color: #333;
        }

        /* Input - Email */
        input[type="email"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
            box-sizing: border-box;
        }

        /* Nút submit với gradient xanh */
        input[type="submit"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            background: linear-gradient(to right, #4facfe, #00f2fe); /* Gradient xanh */
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        input[type="submit"]:hover {
            background: linear-gradient(to right, #00b4db, #0083ff);
            transform: scale(1.05);
        }

        /* Error thông báo */
        .error, .success {
            text-align: center;
            color: red;
            font-size: 14px;
            margin: 10px 0;
        }

        /* Đảm bảo responsive */
        @media (max-width: 500px) {
            .container {
                width: 90%;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Forgot Password</h2>
    <form action="FGServer" method="get">
        <label for="email">Enter your email address:</label>
        <input type="email" id="email" name="email" required placeholder="Enter Your Email">
        
        <input type="submit" value="Check Account" name="action">
    </form>

    <!-- Hiển thị thông báo lỗi -->
    <div class="error">
        <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
    </div>

    <!-- Hiển thị thông báo thành công -->
    <div class="success">
        <%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %>
    </div>
</div>

</body>
</html>
