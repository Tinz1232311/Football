<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <style>
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
            font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
        }

        /* To cover the entire screen */
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

        /* Form background overlay */
        .background-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            z-index: 0;
        }

        /* Login Form */
        .login-frame {
            width: 20%;
            padding: 20px;
            background-color: rgba(255, 255, 255, 0.7);
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

        /* Heading inside form */
        .login-frame h2 {
            color: black;
            margin-bottom: 2px;
        }

        /* Input groups */
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

        /* Buttons container */
        .button-group {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            width: 100%;
            gap: 10px;
            margin-top: 10px;
        }

        /* Login and Register Button Style */
        .login-button,
        .register-button {
            width: calc(50% - 5px);
            padding: 10px;
            background: linear-gradient(to right, #4facfe, #00f2fe);
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        .login-button:hover,
        .register-button:hover {
            background: linear-gradient(to right, #00c6ff, #0072ff);
            transform: scale(1.05);
        }

        /* Google Button Style - Black */
        .google-button {
            width: 100%;
            padding: 10px;
            background-color: #333;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        .google-button:hover {
            background-color: #555;
            transform: scale(1.05);
        }

        .google-logo {
            width: 20px;
            height: auto;
        }

        /* Background video clip */
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
            line-height: 20px;
        }

        .error-dialog .close-btn:hover {
            background-color: rgba(255, 255, 255, 0.8);
        }

        .forgot-password-link {
            color: white;
            text-decoration: underline;
            font-weight: bold;
            margin-top: 15px;
            display: inline-block;
        }

        .forgot-password-link:hover {
            color: #f1f1f1;
        }
    </style>
</head>
<body>
    <div class="page">
        <form action="LoginServer" method="post" class="login-frame">
            <h2>Login</h2>
            <img src="Assets/logoweb.png" alt="Logo">
            <%-- Hiển thị thông báo lỗi nếu có --%>
            <% String errorMessage = (String) request.getParameter("errorMessage"); %>
            <% if (errorMessage != null) { %>
                <div class="error-message"><%= errorMessage %></div>
            <% } %>
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Enter your email">
            </div>
<!--             <div class="input-group"> -->
<!--                 <label for="username">Username</label> -->
<!--                 <input type="text" id="username" name="username" placeholder="Enter your username"> -->
<!--             </div> -->
            <div class="input-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password">
            </div>
            <div class="button-group">
                <input type="submit" value="Login" name="action" class="login-button">
                <input type="submit" value="Register" name="action" class="register-button">
                <button type="submit" name="action" value="Google" class="google-button">
                    <img src="Assets/google-logo.png" alt="Google Logo" class="google-logo">Google
                </button>
            </div>
        </form>
        <video autoplay loop muted playsinline class="background-clip">
            <source src="Assets/background-clip.mp4" type="video/mp4">
        </video>
        <div class="background-overlay"></div>
    </div>
    
        <div id="errorDialog" class="error-dialog">
            <span class="close-btn" onclick="closeErrorDialog()">X</span>
            <p id="errorMessage">Invalid username or password</p>
            <a href="FGPass.jsp" class="forgot-password-link">Forgot Password?</a>
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
