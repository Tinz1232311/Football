<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Controller.Match" %>
<%@ page import="Controller.Team" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Match</title>
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

        form input, form select {
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
        <h2>Edit Match</h2>
        
        <%-- Hiện thông báo lỗi nếu có --%>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>

        <%-- Lấy thông tin trận đấu cần chỉnh sửa từ request --%>
        <% 
            Match match = (Match) request.getAttribute("match"); 
            if (match == null) {
                match = new Match(); // Tránh lỗi khi không có dữ liệu
            }
        %>
        
        <% 
            List<Team> teams = (List<Team>) request.getAttribute("teams"); 
            if (teams == null) {
                teams = new ArrayList<>(); // Tránh lỗi khi không có dữ liệu
            }
        %>

        <!-- Form Edit Match -->
        <form action="MatchServer" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="seasonID" value="<%= request.getParameter("seasonID") %>">
            <input type="hidden" name="matchID" value="<%= match.getMatchID() %>">

            <label for="matchDate">Match Date:</label>
            <input type="date" name="matchDate" id="matchDate" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(match.getMatchDate()) %>">

            <label for="homeTeamID">Home Team:</label>
            <select name="homeTeamID" id="homeTeamID">
                <% for (Team team : teams) { %>
                    <option value="<%= team.getTeamID() %>" <%= team.getTeamID() == match.getHomeTeamID() ? "selected" : "" %> >
                        <%= team.getTeamName() %>
                    </option>
                <% } %>
            </select>

            <label for="awayTeamID">Away Team:</label>
            <select name="awayTeamID" id="awayTeamID">
                <% for (Team team : teams) { %>
                    <option value="<%= team.getTeamID() %>" <%= team.getTeamID() == match.getAwayTeamID() ? "selected" : "" %> >
                        <%= team.getTeamName() %>
                    </option>
                <% } %>
            </select>

            <label for="homeScore">Home Team Score:</label>
            <input type="number" name="homeScore" id="homeScore" value="<%= match.getHomeScore() %>" placeholder="Enter Home Score">

            <label for="awayScore">Away Team Score:</label>
            <input type="number" name="awayScore" id="awayScore" value="<%= match.getAwayScore() %>" placeholder="Enter Away Score">

            <button type="submit">Update Match</button>
        </form>
    </div>
</body>
</html>
