package AdminServer;

import Controller.Member;
import Controller.MemberDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class MemberServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String teamID = request.getParameter("teamID");

        if (teamID == null || teamID.isEmpty()) {
            response.sendRedirect("TeamServer");
            return;
        }

        try {
            switch (action != null ? action : "view") {
                case "delete":
                    handleDeleteGetRequest(request, response, teamID);
                    break;
                case "edit":
                    handleEditGetRequest(request, response, teamID);
                    break;
                default:
                    handleViewMatches(request, response, teamID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleViewMatches(HttpServletRequest request, HttpServletResponse response, String teamID) throws ServletException, IOException {
        try {
            MemberDAO memberDAO = new MemberDAO();
            List<Member> members = memberDAO.getMembersByTeamID(Integer.parseInt(teamID));
            request.setAttribute("members", members);
            request.setAttribute("teamID", teamID);
            request.getRequestDispatcher("/Member.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to load members.");
        }
    }

    private void handleEditGetRequest(HttpServletRequest request, HttpServletResponse response, String teamID) throws ServletException, IOException {
        String memberID = request.getParameter("memberID");

        // Kiểm tra nếu memberID hợp lệ
        if (memberID != null && !memberID.isEmpty()) {
            try {
                MemberDAO memberDAO = new MemberDAO();
                // Truy vấn thành viên theo memberID
                Member member = memberDAO.getMemberByID(Integer.parseInt(memberID));

                // Kiểm tra xem có thành viên nào với memberID này không
                if (member != null) {
                    // Nếu có, gán thông tin thành viên vào request
                    request.setAttribute("member", member);
                    request.setAttribute("teamID", teamID);
                    // Chuyển hướng tới trang chỉnh sửa (EditMember.jsp)
                    request.getRequestDispatcher("/EditMember.jsp").forward(request, response);
                } else {
                    // Nếu không tìm thấy thành viên, thông báo lỗi và chuyển hướng về danh sách thành viên
                    response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Member not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Nếu có lỗi xảy ra, thông báo lỗi và chuyển hướng về trang danh sách thành viên
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Error loading member data.");
            }
        } else {
            // Nếu không có memberID, chuyển hướng về danh sách các thành viên của đội bóng
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Invalid member ID.");
        }
    }

    private void handleDeleteGetRequest(HttpServletRequest request, HttpServletResponse response, String teamID) throws ServletException, IOException {
        String memberID = request.getParameter("memberID");
        if (memberID != null && !memberID.isEmpty()) {
            try {
                MemberDAO memberDAO = new MemberDAO();
                boolean success = memberDAO.deleteMember(Integer.parseInt(memberID));
                if (success) {
                    response.sendRedirect("MemberServer?teamID=" + teamID);
                } else {
                    response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to delete the member.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Error deleting member.");
            }
        } else {
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Invalid member ID.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String teamID = request.getParameter("teamID");

        if (teamID == null || teamID.isEmpty()) {
            response.sendRedirect("TeamServer");
            return;
        }

        try {
            MemberDAO memberDAO = new MemberDAO();
            switch (action) {
                case "add":
                    handleAddMember(request, response, memberDAO, teamID);
                    break;
                case "edit":
                    handleEditPostRequest(request, response, memberDAO);
                    break;
                case "delete":
                    handleDeletePostRequest(request, response, memberDAO, teamID);
                    break;
                default:
                    response.sendRedirect("TeamServer?teamID=" + teamID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleAddMember(HttpServletRequest request, HttpServletResponse response, MemberDAO memberDAO, String teamID) throws ServletException, IOException {
        try {
            String fullName = request.getParameter("fullName");
            String position = request.getParameter("position");
            String birthDate = request.getParameter("birthDate");
            String jerseyNumber = request.getParameter("jerseyNumber");

            int teamIDInt = Integer.parseInt(teamID);
            int jerseyNumberInt = Integer.parseInt(jerseyNumber);

            // Kiểm tra số áo có trùng trong đội không
            if (memberDAO.isJerseyNumberExistsInTeam(teamIDInt, jerseyNumberInt)) {
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Jersey number already exists in this team.");
                return;
            }

            Date birthDateParsed = java.sql.Date.valueOf(birthDate);

            Member member = new Member(0, teamIDInt, fullName, position, birthDateParsed, jerseyNumberInt);
            boolean success = memberDAO.addMember(member);

            if (success) {
                response.sendRedirect("MemberServer?teamID=" + teamID);
            } else {
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to add the member.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to add the member.");
        }
    }

    private void handleEditPostRequest(HttpServletRequest request, HttpServletResponse response, MemberDAO memberDAO) throws ServletException, IOException {
        String memberID = request.getParameter("memberID");
        String fullName = request.getParameter("fullName");
        String position = request.getParameter("position");
        String birthDate = request.getParameter("birthDate");
        String jerseyNumber = request.getParameter("jerseyNumber");
        String teamID = request.getParameter("teamID");

        if (teamID == null || memberID == null || fullName == null || position == null || birthDate == null || jerseyNumber == null ||
            teamID.isEmpty() || memberID.isEmpty() || fullName.isEmpty() || position.isEmpty() || birthDate.isEmpty() || jerseyNumber.isEmpty()) {
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Invalid parameters for editing.");
            return;
        }

        try {
            int memberIDInt = Integer.parseInt(memberID);
            int jerseyNumberInt = Integer.parseInt(jerseyNumber);
            
            if (memberDAO.isMemberExists(Integer.parseInt(teamID), jerseyNumberInt, memberIDInt)) {
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Jersey number already exists in this team.");
                return;
            }

            Date birthDateParsed = java.sql.Date.valueOf(birthDate);

            Member member = new Member(memberIDInt, Integer.parseInt(teamID), fullName, position, birthDateParsed, jerseyNumberInt);
            boolean success = memberDAO.updateMember(member);

            if (success) {
                response.sendRedirect("MemberServer?teamID=" + teamID);
            } else {
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to edit the member.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to edit the member.");
        }
    }

    private void handleDeletePostRequest(HttpServletRequest request, HttpServletResponse response, MemberDAO memberDAO, String teamID) throws ServletException, IOException {
        String memberID = request.getParameter("memberID");

        if (memberID == null || memberID.isEmpty()) {
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Invalid member ID for deletion.");
            return;
        }

        try {
            boolean success = memberDAO.deleteMember(Integer.parseInt(memberID));
            if (success) {
                response.sendRedirect("MemberServer?teamID=" + teamID);
            } else {
                response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Failed to delete the member.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("MemberServer?teamID=" + teamID + "&errorMessage=" + "Error deleting member.");
        }
    }
}
