<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="java.util.List" %>
<%@ page import="Controller.Member" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Members</title>
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

        /* Section: Add/Edit Member */
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
        
		.crud-section form select {
		    padding: 10px;
		    border: 1px solid #ccc;
		    border-radius: 4px;
		    font-size: 16px;
		    margin-bottom: 10px;
		    width: 80%; /* Chiều rộng giống với các input khác */
		    box-sizing: border-box;
		    display: inline-block; /* Đảm bảo rằng select có cùng kiểu hiển thị với input */
		    text-align: left; /* Đảm bảo nội dung trong select căn trái */
		}

        
        /* Style cho các ô nhập */
        .crud-section form input[type="text"], 
        .crud-section form input[type="number"], 
        .crud-section form input[type="date"] {
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

        /* Danh sách Members */
        .member-list {
            background-color: #FFF;
            padding: 20px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .member-list table {
            width: 100%;
            border-collapse: collapse;
            margin: 10px 0;
        }

        .member-list th,
        .member-list td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        .member-list th {
            background: linear-gradient(to right, #00c6ff, #5facff);
            color: white;
        }

        .member-list tr:hover {
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
            <div class="logo-text">Admin - Member Management</div>
        </div>
        <div class="nav-buttons">
        <a href="AdminServer" class="nav-button">Home</a>

    	</div>
    </div>

    <!-- Main Content -->
    <div class="container">
        <div class="crud-section">
            <h2>Manage Members</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <% String errorMessage = (String) request.getParameter("errorMessage"); %>
            <% if (errorMessage != null) { %>
                <div class="error-message"><%= errorMessage %></div>
            <% } %>

            <!-- Form để thêm Member mới -->
            <form action="MemberServer" method="POST">
                <input type="hidden" name="teamID" value="<%= request.getParameter("teamID") %>" />
                
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" placeholder="Enter Full Name" required>
                
                <label for="position">Position</label>
				<select id="position" name="position" required>
				    <option value="Forward">Forward</option>
				    <option value="Midfielder">Midfielder</option>
				    <option value="Defender">Defender</option>
				    <option value="Goalkeeper">Goalkeeper</option>
				</select>

                <label for="birthDate">Birth Date</label>
                <input type="date" id="birthDate" name="birthDate" required>
                
                <label for="jerseyNumber">Jersey Number</label>
                <input type="number" id="jerseyNumber" name="jerseyNumber" placeholder="Enter Jersey Number" required>
                
                <button type="submit" name="action" value="add">Add Member</button>
            </form>
        </div>

        <!-- Danh sách Members -->
        <div class="member-list">
            <h2>All Members</h2>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Position</th>
                        <th>Birth Date</th>
                        <th>Jersey Number</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% 
                        List<Member> members = (List<Member>) request.getAttribute("members");
                        for (Member member : members) {
                    %>
                        <tr>
                            <td><%= member.getMemberID() %></td>
                            <td><%= member.getFullName() %></td>
                            <td><%= member.getPosition() %></td>
                            <td><%= member.getBirthDate() %></td>
                            <td><%= member.getJerseyNumber() %></td>
                            <td>
                                <!-- Form Edit -->
                                <form action="MemberServer" method="GET" style="display:inline;">
                                    <input type="hidden" name="action" value="edit">
                                    <input type="hidden" name="teamID" value="<%= request.getParameter("teamID") %>">
                                    <input type="hidden" name="memberID" value="<%= member.getMemberID() %>">
                                    <button type="submit" style="background:none; border:none; color:blue; text-decoration:underline; cursor:pointer;">Edit</button>
                                </form>

                                <!-- Form Delete -->
                                <form action="MemberServer" method="POST" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="teamID" value="<%= request.getParameter("teamID") %>">
                                    <input type="hidden" name="memberID" value="<%= member.getMemberID() %>">
                                    <button type="submit" style="background:none; border:none; color:red; text-decoration:underline; cursor:pointer;">Delete</button>
                                </form>
                                
                                <!-- Form View -->
                                <form action="ParticipantServer" method="GET" style="display:inline;">
                                    <input type="hidden" name="action" value="view">
                                    <input type="hidden" name="teamID" value="<%= request.getParameter("teamID") %>">
                                    <input type="hidden" name="memberID" value="<%= member.getMemberID() %>">
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
