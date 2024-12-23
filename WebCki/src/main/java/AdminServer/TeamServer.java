package AdminServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import Controller.Match;
import Controller.Team;
import Controller.TeamDAO;

public class TeamServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String seasonID = request.getParameter("seasonID");

        if (seasonID == null || seasonID.isEmpty()) {
            response.sendRedirect("AdminServer");
            return;
        }

        try {
            switch (action != null ? action : "viewTeam") {
                case "edit":
                    handleEditGetRequest(request, response, seasonID);
                    break;
                default:
                    handleViewTeams(request, response, seasonID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleViewTeams(HttpServletRequest request, HttpServletResponse response, String seasonID) throws ServletException, IOException {
        try {
            TeamDAO teamDAO = new TeamDAO();
            List<Team> teams = teamDAO.getTeamsBySeasonID(Integer.parseInt(seasonID));
            request.setAttribute("teams", teams);
            request.setAttribute("seasonID", seasonID);
            request.getRequestDispatcher("/Team.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Failed to load teams.");
        }
    }

    private void handleEditGetRequest(HttpServletRequest request, HttpServletResponse response, String seasonID) throws ServletException, IOException {
        String teamID = request.getParameter("teamID");

        if (teamID != null && !teamID.isEmpty()) {
            try {
                TeamDAO teamDAO = new TeamDAO();
                Team team = teamDAO.getTeamByID(Integer.parseInt(teamID));

                if (team != null) {
                    request.setAttribute("team", team);
                    request.setAttribute("seasonID", seasonID);
                    request.getRequestDispatcher("/EditTeam.jsp").forward(request, response);
                } else {
                    response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Team not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error loading team data.");
            }
        } else {
            response.sendRedirect("TeamServer?seasonID=" + seasonID);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String seasonID = request.getParameter("seasonID");

        if (seasonID == null || seasonID.isEmpty()) {
            response.sendRedirect("AdminServer");
            return;
        }

        try {
            TeamDAO teamDAO = new TeamDAO();
            switch (action) {
                case "add":
                    handleAddTeam(request, response, teamDAO, seasonID);
                    break;
                case "edit":
                    handleEditPostRequest(request, response, teamDAO);
                    break;
                case "delete":
                    handleDeletePostRequest(request, response, seasonID, teamDAO);
                    break;
                default:
                    response.sendRedirect("TeamServer?seasonID=" + seasonID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleDeletePostRequest(HttpServletRequest request, HttpServletResponse response, String seasonID, TeamDAO teamDAO) throws ServletException, IOException {
        String teamID = request.getParameter("teamID");

        if (teamID != null && !teamID.isEmpty()) {
            try {
                boolean success = teamDAO.deleteTeam(Integer.parseInt(teamID));
                if (success) {
                    response.sendRedirect("TeamServer?seasonID=" + seasonID);
                } else {
                    response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Failed to delete the team.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error deleting team.");
            }
        } else {
            response.sendRedirect("TeamServer?seasonID=" + seasonID);
        }
    }

    private void handleAddTeam(HttpServletRequest request, HttpServletResponse response, TeamDAO teamDAO, String seasonID) throws ServletException, IOException {
        try {
            String teamName = request.getParameter("teamName");
            String stadiumName = request.getParameter("stadiumName");
            String foundedYear = request.getParameter("foundedYear");

            if (seasonID == null || teamName == null || stadiumName == null || foundedYear == null ||
                seasonID.isEmpty() || teamName.isEmpty() || stadiumName.isEmpty() || foundedYear.isEmpty()) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "All fields are required.");
                return;
            }
            
            if (teamDAO.isTeamDuplicate(Integer.parseInt(seasonID), teamName, stadiumName)) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Duplicate team found.");
                return;
            }

            int foundedYearInt = Integer.parseInt(foundedYear);
            Team team = new Team(0, Integer.parseInt(seasonID), teamName, stadiumName, foundedYearInt);

            boolean success = teamDAO.addTeam(team);

            if (success) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID);
            } else {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error adding team.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error processing your request.");
        }
    }

    private void handleEditPostRequest(HttpServletRequest request, HttpServletResponse response, TeamDAO teamDAO) throws ServletException, IOException {
        String seasonID = request.getParameter("seasonID");
        try 
        {
        	String teamID = request.getParameter("teamID");
            String teamName = request.getParameter("teamName");
            String stadiumName = request.getParameter("stadiumName");
            String foundedYear = request.getParameter("foundedYear");
            
            if (teamID == null || teamName == null || stadiumName == null || foundedYear == null ||
                teamID.isEmpty() || teamName.isEmpty() || stadiumName.isEmpty() || foundedYear.isEmpty()) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "All fields are required.");
                return;
            }

            int foundedYearInt = Integer.parseInt(foundedYear);

            Team team = teamDAO.getTeamByID(Integer.parseInt(teamID));

            if (team == null) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Team not found.");
                return;
            }
            
            if (teamDAO.isTeamExists(Integer.parseInt(seasonID), teamName, stadiumName, Integer.parseInt(teamID))) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Duplicate team found.");
                return;
            }            
            
            team.setTeamName(teamName);  
            team.setStadiumName(stadiumName);  
            team.setFoundedYear(foundedYearInt);  

            boolean success = teamDAO.updateTeam(team);

            if (success) {
                response.sendRedirect("TeamServer?seasonID=" + seasonID);
            } else {
                response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error updating team.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TeamServer?seasonID=" + seasonID + "&errorMessage=" + "Error processing your request.");
        }
    }

}
