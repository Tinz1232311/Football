<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify OTP</title>
    <style>
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
            font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
        }
        .page {
            width: 100%;
            height: 100vh;
            background-color: rgba(0, 0, 0, 0.1);
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
            overflow: hidden;
        }
        .background-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4); /* Lớp phủ tối */
            z-index: 0;
        }
        .login-frame {
            width: 20%;
            padding: 20px;
            background-color: rgba(255, 255, 255, 0.5);
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            display: flex;
            flex-direction: column;
            gap: 15px;
            align-items: center;
            z-index: 1;
        }
        img {
            width: 60px;
            height: auto;
            margin-bottom: 2px;
        }
        .login-frame h2 {
            color: white;
            margin-bottom: 2px;
        }
        .input-group {
            width: 100%;
            display: flex;
            flex-direction: column;
        }
        .input-group label {
            margin-bottom: 2px;
            color: #333;
            font-size: 14px;
        }
        .input-group input {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
            width: 100%;
        }
        .button-group {
            display: flex;
            flex-wrap: wrap; /* Cho phép các nút xuống dòng */
            justify-content: space-between;
            width: 100%;
            gap: 10px; /* Khoảng cách giữa các nút */
            margin-top: 10px;
        }
        .button-group input[type="submit"] {
		    width: calc(50% - 5px);
		    padding: 10px;
		    background: linear-gradient(to right, #4facfe, #00f2fe); /* Gradient xanh */
		    color: white;
		    border: none;
		    border-radius: 5px;
		    cursor: pointer;
		    font-weight: bold;
		    transition: background 0.3s ease, transform 0.2s ease;
		}
		
		.button-group input[type="submit"]:hover {
		    background: linear-gradient(to right, #00b4db, #0083ff); /* Gradient xanh đậm hơn khi hover */
		    transform: scale(1.05);
		}
        /* Nút Gmail riêng biệt */
        .gmail-button {
            width: 100%; /* Chiếm toàn bộ chiều rộng */
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 5px; /* Khoảng cách giữa logo và text */
            padding: 10px;
            background: linear-gradient(to right, #ff7eb3, #ff758c); /* Gradient giống các nút khác */
            color: white; /* Chữ màu trắng */
            font-weight: bold;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.2s ease;
        }
        
        .gmail-button:hover {
            background: linear-gradient(to right, #ff5473, #ff758c); /* Gradient đậm hơn khi hover */
            transform: scale(1.05); /* Phóng to nhẹ */
        }
        
        .gmail-button img {
            width: 20px; /* Kích thước logo */
            height: 20px;
        }
        
        .background-clip {
            position: absolute;
            right: 0;
            bottom: 0;
            z-index: -1;
        }
        @media (min-aspect-ratio: 16/9) {
            .background-clip {
                width: 100%;
                height: auto;
            }
        }
        @media (max-aspect-ratio: 16/9) {
            .background-clip {
                width: auto;
                height: 100%;
            }
        }

        /* Styles for error dialog */
        .error-dialog {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            padding: 30px;
            background-color: red;
            color: white;
            border-radius: 10px;
            z-index: 1000;
            width: 350px;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
        }

        .error-dialog button {
            margin-top: 10px;
            padding: 5px 10px;
            background-color: white;
            color: red;
            border: none;
            cursor: pointer;
            font-weight: bold;
        }

        .error-dialog button:hover {
            background-color: #f1f1f1;
        }

        .error-dialog .close-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            font-size: 18px;
            color: red;
            cursor: pointer;
            width: 20px;
            height: 20px;
            text-align: center;
            background-color: rgba(255, 255, 255, 0.5);
            border-radius: 5px;
            border: 1px solid rgba(255, 255, 255, 0.5);
            line-height: 20px; /* Center the "X" inside the button */
        }

        .error-dialog .close-btn:hover {
            background-color: rgba(255, 255, 255, 0.8);
        }

        .forgot-password-link {
            color: white;
            text-decoration: underline;
            font-weight: bold;
            display: block;
            margin-top: 10px;
        }

        .forgot-password-link:hover {
            color: #f1f1f1;
        }
    </style>
</head>
<body>
    <div class="page">
        <form action="VerifyOTP" method="post" class="login-frame">
            <h2>Verify OTP</h2>
            <img src="Assets/logoweb.png" alt="Logo">
            <div class="input-group">
                <label for="otp">Enter OTP</label>
                <input type="text" id="otp" name="otp" placeholder="Enter OTP">
            </div>
            <div class="button-group">
                <input type="submit" value="Verify" name="action">
                <input type="submit" value="Resend OTP" name="action">
            </div>
        </form>

        <!-- Video background -->
        <video autoplay loop muted playsinline class="background-clip">
            <source src="Assets/background-clip.mp4" type="video/mp4">
        </video>

        <!-- Thêm overlay vào -->
        <div class="background-overlay"></div>

        <!-- Dialog thông báo lỗi -->
        <div id="errorDialog" class="error-dialog">
            <span class="close-btn" onclick="closeErrorDialog()">X</span>
            <p id="errorMessage">Invalid OTP</p>
        </div>
    </div>

    <script>
        const errorMessage = '<%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>';
        if (errorMessage) {
            document.getElementById('errorMessage').textContent = errorMessage;
            document.getElementById('errorDialog').style.display = 'block';
        }
        function closeErrorDialog() {
            document.getElementById('errorDialog').style.display = 'none';
        }
    </script>
</body>
</html>
