<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Page</title>
    <style>
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
            font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
        }

        /* Page container */
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

        /* Overlay effect */
        .background-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            z-index: 0;
        }

        /* Main registration frame */
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

        /* Logo styles */
        img {
            width: 60px;
            height: auto;
            margin-bottom: 10px;
        }

        h2 {
            color: white;
            margin-bottom: 10px;
        }

        /* Input group styles */
        .input-group {
            width: 100%;
            display: flex;
            flex-direction: column;
        }

        .input-group label {
            margin-bottom: 5px;
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

        /* Button Group */
        .button-group {
            display: flex;
            justify-content: center;
            width: 100%;
            margin-top: 10px;
        }

        /* Submit button với màu xanh */
        .button-group input[type="submit"] {
            width: 48%;
            padding: 10px;
            background: linear-gradient(to right, #4facfe, #00f2fe); /* Gradient màu xanh */
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
            transition: background 0.3s ease, transform 0.2s ease;
        }

        /* Hover effect với màu xanh đậm hơn */
        .button-group input[type="submit"]:hover {
            background: linear-gradient(to right, #00c6ff, #0072ff);
            transform: scale(1.05);
        }

        /* Background video */
        .background-clip {
            position: absolute;
            right: 0;
            bottom: 0;
            z-index: -1;
        }

        /* Responsive video adjustment */
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
    </style>
</head>
<body>
    <div class="page">
        <!-- Thêm form đăng ký -->
        <form action="RegisServer" method="post" class="login-frame">
            <h2>Register</h2>
            <img src="Assets/logoweb.png" alt="Logo">
            
            <!-- Input fields -->
<!--             <div class="input-group"> -->
<!--                 <label for="username">Username</label> -->
<!--                 <input type="text" id="username" name="username" placeholder="Enter your username" required> -->
<!--             </div> -->
            <div class="input-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" placeholder="Enter your email address" required>
            </div>
            <div class="input-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>
            
            <!-- Submit Button -->
            <div class="button-group">
                <input type="submit" value="Submit" name="Submit">
            </div>
        </form>

        <!-- Background video -->
        <video autoplay loop muted plays-inline class="background-clip">
            <source src="Assets/background-clip.mp4" type="video/mp4">
        </video>

        <!-- Overlay effect -->
        <div class="background-overlay"></div>
    </div>
</body>
</html>
