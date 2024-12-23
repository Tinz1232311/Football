<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Season" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Season</title>
    <style>
        /* Giao diện chính */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(to bottom right, #f0f4f8, #d9e2ec);
            color: #333;
        }

        .container {
            padding: 20px;
            max-width: 600px;
            margin: auto;
            margin-top: 50px;
            background-color: white;
            box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2);
            border-radius: 8px;
        }

        h2 {
            text-align: center;
        }

        /* Styles cho form */
        form input[type="text"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        form button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            border: none;
            color: white;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        form button:hover {
            background-color: #0056b3;
        }

        /* Error message */
        .error-message {
            background-color: #ffdddd;
            color: #d8000c;
            padding: 10px;
            border: 1px solid #d8000c;
            border-radius: 4px;
            text-align: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Edit Season</h2>
        
        <%-- Hiện thông báo lỗi nếu có --%>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>

        <%-- Lấy thông tin mùa giải cần chỉnh sửa từ request --%>
        <% 
            Season season = (Season) request.getAttribute("season"); 
            if (season == null) {
                season = new Season(); // Tránh trường hợp không có thông tin mùa giải
            }
        %>

        <!-- Form Edit Season -->
        <form action="AdminServer" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="id" value="<%= season.getSeasonID() %>">
            <input type="text" name="detail" value="<%= season.getDetail() %>" placeholder="Enter new detail">
            <button type="submit">Update Season</button>
        </form>
    </div>
</body>
</html>
