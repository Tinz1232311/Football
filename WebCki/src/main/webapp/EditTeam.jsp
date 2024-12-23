<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Team" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Team</title>
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

        form input[type="number"] {
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
        <h2>Edit Team</h2>
        
        <%-- Hiện thông báo lỗi nếu có --%>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>

        <%-- Lấy thông tin đội bóng cần chỉnh sửa từ request --%>
        <% 
            Team team = (Team) request.getAttribute("team"); 
            if (team == null) {
                team = new Team(); // Tránh trường hợp không có thông tin đội bóng
            }
        %>

        <!-- Form Edit Team -->
        <form action="TeamServer" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="seasonID" value="<%= request.getParameter("seasonID") %>">
            <input type="hidden" name="teamID" value="<%= team.getTeamID() %>">
            
            <label for="teamName">Team Name:</label>
            <input type="text" name="teamName" id="teamName" value="<%= team.getTeamName() %>" placeholder="Enter team name">
            
            <label for="stadiumName">Stadium Name:</label>
            <input type="text" name="stadiumName" id="stadiumName" value="<%= team.getStadiumName() %>" placeholder="Enter stadium name">
            
            <label for="foundedYear">Founded Year:</label>
            <input type="number" name="foundedYear" id="foundedYear" value="<%= team.getFoundedYear() %>" placeholder="Enter founded year">
            
            <button type="submit">Update Team</button>
        </form>
    </div>
</body>
</html>
