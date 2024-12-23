<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List, java.text.SimpleDateFormat" %>
<%@ page import="Controller.Member" %>
<%@ page import="Controller.MatchDetail" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Match Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background: linear-gradient(to bottom right, #e0eafc, #cfdef3);
            color: #333;
        }

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
            background: linear-gradient(to right, #00c6ff, #0052d1);
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
            text-decoration: none;
        }

        .nav-buttons a:hover {
            background: linear-gradient(to right, #0052d1, #00c6ff);
        }

        .container {
            padding: 20px;
        }

        .match-details-list {
            background-color: #FFF;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .match-details-list table {
            width: 100%;
            border-collapse: collapse;
            margin: 10px 0;
        }

        .match-details-list th,
        .match-details-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        .match-details-list th {
            background: linear-gradient(to right, #00c6ff, #5facff);
            color: white;
        }

        .match-details-list tr:hover {
            background-color: #f2f2f2;
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
    <div class="header">
        <div class="logo-container">
            <div class="logo-image">
                <img src="Assets/logoweb.png" alt="Logo">
            </div>
            <div class="logo-text">Admin - Match Details</div>
        </div>
        <div class="nav-buttons">
            <a href="AdminServer" class="nav-button">Back</a>
        </div>
    </div>

    <div class="container">
        <div class="match-details-list">

            <% 
                // Ensure matchDetail and SimpleDateFormat are properly initialized
                MatchDetail matchDetail = (MatchDetail) request.getAttribute("matchDetail"); // Get matchDetail from request
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Initialize SimpleDateFormat

                if (matchDetail != null) { 
            %>

            <!-- Home Team Information -->
            <h3>Home Team Information</h3>
            <table>
                <thead>
                    <tr>
                    	<th>Team Name</th>
                        <th>Stadium Name</th>
                        <th>Founded Year</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    	<td><%= matchDetail.getHomeTeam().getTeamName() %></td>
                        <td><%= matchDetail.getHomeTeam().getStadiumName() %></td>
                        <td><%= matchDetail.getHomeTeam().getFoundedYear() %></td>
                    </tr>
                </tbody>
            </table>

            <!-- Home Team Members -->
            <h3>Home Team Members</h3>
            <table>
                <thead>
                    <tr>
                        <th>Member ID</th>
                        <th>Name</th>
                        <th>Position</th>
                        <th>Birth Date</th>
                        <th>Jersey Number</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Member> homeTeamMembers = matchDetail.getHomeTeam().getMembers();
                        for (Member member : homeTeamMembers) {
                    %>
                    <tr>
                        <td><%= member.getMemberID() %></td>
                        <td><%= member.getFullName() %></td>
                        <td><%= member.getPosition() %></td>
                        <td><%= sdf.format(member.getBirthDate()) %></td>
                        <td><%= member.getJerseyNumber() %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>

            <!-- Away Team Information -->
            <h3>Away Team Information</h3>
            <table>
                <thead>
                    <tr>
                    	<th>Team Name</th>
                        <th>Stadium Name</th>
                        <th>Founded Year</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                    	<td><%= matchDetail.getAwayTeam().getTeamName() %></td>
                        <td><%= matchDetail.getAwayTeam().getStadiumName() %></td>
                        <td><%= matchDetail.getAwayTeam().getFoundedYear() %></td>
                    </tr>
                </tbody>
            </table>

            <!-- Away Team Members -->
            <h3>Away Team Members</h3>
            <table>
                <thead>
                    <tr>
                        <th>Member ID</th>
                        <th>Name</th>
                        <th>Position</th>
                        <th>Birth Date</th>
                        <th>Jersey Number</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Member> awayTeamMembers = matchDetail.getAwayTeam().getMembers();
                        for (Member member : awayTeamMembers) {
                    %>
                    <tr>
                        <td><%= member.getMemberID() %></td>
                        <td><%= member.getFullName() %></td>
                        <td><%= member.getPosition() %></td>
                        <td><%= sdf.format(member.getBirthDate()) %></td>
                        <td><%= member.getJerseyNumber() %></td>
                    </tr>
                    <% } %>
                </tbody>
            </table>

            <% } else { %>
                <p class="error-message">Match detail not found or data has been updated. Please try again.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
