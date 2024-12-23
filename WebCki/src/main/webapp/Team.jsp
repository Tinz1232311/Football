<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Team" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Teams</title>
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
            padding: 20px 20px; /* Tăng chiều cao của header */
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
            background: linear-gradient(to right, #00c6ff, #0052d1); /* Gradient màu xanh */
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .nav-buttons {
            display: flex;
            justify-content: flex-end; /* Dời các nút ra phía bên phải */
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

        /* Section: Add/Edit Teams */
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

        .crud-section form input[type="text"] {
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 16px;
            margin-bottom: 10px;
            width: 80%;
        }

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

        /* Danh sách Teams */
        .team-list {
            background-color: #FFF;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .team-list table {
            width: 100%;
            border-collapse: collapse;
            margin: 10px 0;
        }

        .team-list th,
        .team-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        .team-list th {
            background: linear-gradient(to right, #00c6ff, #5facff);
            color: white;
        }

        .team-list tr:hover {
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
            <div class="logo-text">Admin - Team Management</div>
        </div>
        <div class="nav-buttons">
            <a href="AdminServer" class="nav-button">Home</a>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <div class="crud-section">
            <h2>Manage Teams</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <% String errorMessage = (String) request.getParameter("errorMessage"); %>
            <% if (errorMessage != null) { %>
                <div class="error-message"><%= errorMessage %></div>
            <% } %>

            <!-- Form để thêm Team mới -->
            <form action="TeamServer" method="POST">
            	<input type="hidden" name="seasonID" value = <%= request.getParameter("seasonID") %>>
                <input type="text" name="teamName" placeholder="Enter Team Name" required>
                <input type="text" name="stadiumName" placeholder="Enter Stadium Name" required>
                <input type="text" name="foundedYear" placeholder="Enter Founded Year" required>
                <button type="submit" name="action" value="add">Add Team</button>
            </form>
        </div>

        <!-- Danh sách Teams -->
        <div class="team-list">
            <h2>All Teams</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Team Name</th>
                        <th>Stadium Name</th>
                        <th>Founded Year</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Team> teams = (List<Team>) request.getAttribute("teams");
                        for (Team team : teams) {
                    %>
                        <tr>
                            <td><%= team.getTeamID() %></td>
                            <td><%= team.getTeamName() %></td>
                            <td><%= team.getStadiumName() %></td>
                            <td><%= team.getFoundedYear() %></td>
                            <td>
                                <!-- Form Edit -->
                                <form action="TeamServer" method="GET" style="display:inline;">
                                    <input type="hidden" name="action" value="edit">
                                    <input type="hidden" name="seasonID" value="<%= team.getSeasonID() %>">
                                    <input type="hidden" name="teamID" value="<%= team.getTeamID() %>">
                                    <button type="submit" style="background:none; border:none; color:blue; text-decoration:underline; cursor:pointer;">Edit</button>
                                </form>

                                <!-- Form Delete -->
                                <form action="TeamServer" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="seasonID" value="<%= team.getSeasonID() %>">
                                    <input type="hidden" name="teamID" value="<%= team.getTeamID() %>">
                                    <button type="submit" style="background:none; border:none; color:red; text-decoration:underline; cursor:pointer;">Delete</button>
                                </form>

                                <!-- Form View -->
                                <form action="MemberServer" method="GET" style="display:inline;" onsubmit="deleteCookie()">
                                    <input type="hidden" name="action" value="view">
                                    <input type="hidden" name="seasonID" value="<%= team.getSeasonID() %>">
                                    <input type="hidden" name="teamID" value="<%= team.getTeamID() %>">
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
