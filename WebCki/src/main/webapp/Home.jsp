<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Season" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Seasons</title>
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

        /* Section: Add/Edit Seasons */
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

        /* Danh sách Seasons */
        .season-list {
            background-color: #FFF;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .season-list table {
            width: 100%;
            border-collapse: collapse;
            margin: 10px 0;
        }

        .season-list th,
        .season-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        .season-list th {
            background: linear-gradient(to right, #00c6ff, #5facff);
            color: white;
        }

        .season-list tr:hover {
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
            <div class="logo-text">Admin - Season Management</div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <div class="crud-section">
            <h2>Manage Seasons</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <% String errorMessage = (String) request.getParameter("errorMessage"); %>
            <% if (errorMessage != null) { %>
                <div class="error-message"><%= errorMessage %></div>
            <% } %>

            <!-- Form để thêm Season mới -->
            <form action="AdminServer" method="POST">
                <input type="text" name="detail" placeholder="Enter Season Detail" required>
                <button type="submit" name="action" value="add">Add Season</button>
            </form>
        </div>

        <!-- Danh sách Seasons -->
        <div class="season-list">
            <h2>All Seasons</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Detail</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Season> seasons = (List<Season>) request.getAttribute("seasons");
                        for (Season season : seasons) {
                    %>
                        <tr>
                            <td><%= season.getSeasonID() %></td>
                            <td><%= season.getDetail() %></td>
                            <td>
                                <!-- Form Edit -->
                                <form action="AdminServer" method="GET" style="display:inline;">
                                    <input type="hidden" name="action" value="edit">
                                    <input type="hidden" name="id" value="<%= season.getSeasonID() %>">
                                    <button type="submit" style="background:none; border:none; color:blue; text-decoration:underline; cursor:pointer;">Edit</button>
                                </form>

                                <!-- Form Delete -->
                                <form action="AdminServer" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="id" value="<%= season.getSeasonID() %>">
                                    <button type="submit" style="background:none; border:none; color:red; text-decoration:underline; cursor:pointer;">Delete</button>
                                </form>

                                <!-- Form View -->
								<form action="MatchServer" method="GET" style="display:inline;">
								    <input type="hidden" name="action" value="viewMatch">
								    <input type="hidden" name="seasonID" value="<%= season.getSeasonID() %>">
								    <button type="submit" style="background:none; border:none; color:green; text-decoration:underline; cursor:pointer;">Match Management</button>
								</form>
								
								<form action="TeamServer" method="GET" style="display:inline;">
								    <input type="hidden" name="action" value="viewTeam">
								    <input type="hidden" name="seasonID" value="<%= season.getSeasonID() %>">
								    <button type="submit" style="background:none; border:none; color:black; text-decoration:underline; cursor:pointer;">Team Management</button>
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
