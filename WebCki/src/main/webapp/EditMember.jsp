<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="Controller.Member" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Member</title>
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
        <h2>Edit Member</h2>
        
        <%-- Hiện thông báo lỗi nếu có --%>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (errorMessage != null) { %>
            <div class="error-message"><%= errorMessage %></div>
        <% } %>

        <%-- Lấy thông tin thành viên cần chỉnh sửa từ request --%>
        <% 
            Member member = (Member) request.getAttribute("member"); 
            if (member == null) {
                member = new Member(); // Tránh lỗi khi không có dữ liệu
            }
        %>

        <!-- Form Edit Member -->
        <form action="MemberServer" method="POST">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="teamID" value="<%= request.getParameter("teamID") %>">
            <input type="hidden" name="memberID" value="<%= member.getMemberID() %>">
            
            <label for="fullName">Full Name:</label>
            <input type="text" name="fullName" id="fullName" value="<%= member.getFullName() %>">

            <label for="position">Position:</label>
            <input type="text" name="position" id="position" value="<%= member.getPosition() %>">

            <label for="birthDate">Birth Date:</label>
            <input type="date" name="birthDate" id="birthDate" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(member.getBirthDate()) %>">

            <label for="jerseyNumber">Jersey Number:</label>
            <input type="number" name="jerseyNumber" id="jerseyNumber" value="<%= member.getJerseyNumber() %>">
            <button type="submit">Update Member</button>
        </form>
    </div>
</body>
</html>
