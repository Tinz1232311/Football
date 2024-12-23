<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Match" %>
<%@ page import="Controller.Team" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Matches</title>
    <style>
        /* Các style của trang */
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(to bottom right, #e0eafc, #cfdef3);
            color: #333;
        }

        /* Header Section với nền trắng */
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            background-color: #ffffff;
            border-bottom: 1px solid #ccc;
            padding: 20px 20px;
        }

        .logo-container {
            display: flex;
            align-items: center;
        }

        .logo-image img {
            width: 40px;
            height: 40px;
            margin-right: 10px;
        }

        .logo-text {
            font-size: 20px;
            font-weight: bold;
            background: linear-gradient(to right, #00c6ff, #0052d1);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
        
        .nav-buttons a {
            background: linear-gradient(to right, #00c6ff, #0052d1); /* Gradient */
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
            text-decoration: none; /* Loại bỏ đường kẻ chân dưới */
        }

        .nav-buttons a:hover {
            background: linear-gradient(to right, #0052d1, #00c6ff); /* Gradient hover */
        }

        .container {
            padding: 20px;
        }

        /* Section: Add/Edit Matches */
        .crud-section {
            background-color: #FFF;
            padding: 20px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            text-align: center;
        }

        .crud-section form {
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        /* Style cho các ô nhập */
        .crud-section form input[type="text"], 
        .crud-section form input[type="number"], 
        .crud-section form input[type="date"], 
        .crud-section form select {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
            margin-bottom: 10px;
            width: 80%;
            box-sizing: border-box;
        }

        .crud-section form label {
            font-size: 16px;
            margin-bottom: 5px;
            text-align: left;
            width: 80%;
        }

        /* Button Style */
        .crud-section form button {
            padding: 8px 20px; 
            background-color: #00c6ff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px; 
            font-weight: bold;
        }

        .crud-section form button:hover {
            background-color: #0052d1;
        }

        /* Danh sách Matches */
        .match-list {
            background-color: #FFF;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .match-list table {
            width: 100%;
            border-collapse: collapse;
            margin: 10px 0;
        }

        .match-list th,
        .match-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        .match-list th {
            background: linear-gradient(to right, #00c6ff, #5facff);
            color: white;
        }

        .match-list tr:hover {
            background-color: #f2f2f2;
        }

        /* Error Message */
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
    <!-- Header Section -->
    <div class="header">
        <div class="logo-container">
            <div class="logo-image">
                <img src="Assets/logoweb.png" alt="Logo">
            </div>
            <div class="logo-text">Admin - Match Management</div>
        </div>
        <div class="nav-buttons">
        <a href="AdminServer" class="nav-button">Back</a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <div class="crud-section">
            <h2>Manage Matches</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <% String errorMessage = (String) request.getParameter("errorMessage"); %>
            <% if (errorMessage != null) { %>
                <div class="error-message"><%= errorMessage %></div>
            <% } %>

            <!-- Form để thêm Match mới -->
            <form action="MatchServer" method="POST">
                <input type="hidden" name="seasonID" value="<%= request.getParameter("seasonID") %>" />
                
                <label for="matchDate">Match Date</label>
                <input type="date" id="matchDate" name="matchDate" required>
                
                <label for="homeTeamID">Home Team</label>
                <select name="homeTeamID" id="homeTeamID" required>
                    <% 
                        List<Team> teams = (List<Team>) request.getAttribute("teams");
                        for (Team team : teams) { 
                    %>
                        <option value="<%= team.getTeamID() %>">
                            <%= team.getTeamName() %>
                        </option>
                    <% } %>
                </select>
                
                <label for="awayTeamID">Away Team</label>
                <select name="awayTeamID" id="awayTeamID" required>
                    <% 
                        for (Team team : teams) { 
                    %>
                        <option value="<%= team.getTeamID() %>">
                            <%= team.getTeamName() %>
                        </option>
                    <% } %>
                </select>

                <label for="homeScore">Home Score</label>
                <input type="number" id="homeScore" name="homeScore" placeholder="Enter Home Team Score" required>
                
                <label for="awayScore">Away Score</label>
                <input type="number" id="awayScore" name="awayScore" placeholder="Enter Away Team Score" required>
                
                <button type="submit" name="action" value="add">Add Match</button>
            </form>
        </div>

        <!-- Danh sách Matches -->
        <div class="match-list">
            <h2>All Matches</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Home Team</th>
                        <th>Away Team</th>
                        <th>Match Date</th>
                        <th>Home Score</th>
                        <th>Away Score</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Match> matches = (List<Match>) request.getAttribute("matches");
                        for (Match match : matches) {
                    %>
                        <tr>
                            <td><%= match.getMatchID() %></td>
                            <td><%= match.getHomeTeamID() %></td>
                            <td><%= match.getAwayTeamID() %></td>
                            <td><%= match.getMatchDate() %></td>
                            <td><%= match.getHomeScore() %></td>
                            <td><%= match.getAwayScore() %></td>
                            <td>
                                <!-- Form Edit -->
                                <form action="MatchServer" method="GET" style="display:inline;">
                                    <input type="hidden" name="action" value="edit">
                                    <input type="hidden" name="seasonID" value="<%= match.getSeasonID() %>">
                                    <input type="hidden" name="matchID" value="<%= match.getMatchID() %>">
                                    <button type="submit" style="background:none; border:none; color:blue; text-decoration:underline; cursor:pointer;">Edit</button>
                                </form>

                                <!-- Form Delete -->
                                <form action="MatchServer" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="seasonID" value="<%= match.getSeasonID() %>">
                                    <input type="hidden" name="matchID" value="<%= match.getMatchID() %>">
                                    <button type="submit" style="background:none; border:none; color:red; text-decoration:underline; cursor:pointer;">Delete</button>
                                </form>

                                <!-- Form View -->
                                <form action="MatchDetailServer" method="GET" style="display:inline;">
                                	<input type="hidden" name="seasonID" value="<%= match.getSeasonID() %>">
                                    <input type="hidden" name="matchID" value="<%= match.getMatchID() %>">
                                    <button type="submit" style="background:none; border:none; color:green; text-decoration:underline; cursor:pointer;">View</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
