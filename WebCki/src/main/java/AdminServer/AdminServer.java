package AdminServer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import Controller.Season;
import Controller.SeasonDAO;

public class AdminServer extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Xử lý yêu cầu GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String errorMessage = request.getParameter("errorMessage");

        if ("edit".equals(action)) {
            handleEditGetRequest(request, response, id);
        } else if ("delete".equals(action)) {
            handleDeleteGetRequest(request, response, id);
        } else {
            try {
                SeasonDAO seasonDAO = new SeasonDAO();
                List<Season> seasons = seasonDAO.getAllSeasons();
                request.setAttribute("seasons", seasons);
                request.setAttribute("errorMessage", errorMessage); // Set error message for display
                request.getRequestDispatcher("/Home.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("AdminServer?errorMessage=" + "Error loading seasons.");
            }
        }
    }

    private void handleEditGetRequest(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException {
        if (id != null && !id.isEmpty()) {
            try {
                SeasonDAO seasonDAO = new SeasonDAO();
                Season season = seasonDAO.getSeasonById(Integer.parseInt(id));
                if (season != null) {
                    // Gửi thông tin mùa giải vào EditSeason.jsp
                    request.setAttribute("season", season);
                    request.getRequestDispatcher("/EditSeason.jsp").forward(request, response);
                } else {
                    response.sendRedirect("AdminServer?errorMessage=" + "Season not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("AdminServer?errorMessage=" + "Error loading season data.");
            }
        } else {
            response.sendRedirect("AdminServer");
        }
    }

    private void handleDeleteGetRequest(HttpServletRequest request, HttpServletResponse response, String id) throws ServletException, IOException {
        if (id != null && !id.isEmpty()) {
            try {
                SeasonDAO seasonDAO = new SeasonDAO();
                boolean success = seasonDAO.deleteSeason(Integer.parseInt(id));
                if (success) {
                    response.sendRedirect("AdminServer?successMessage=" + "Season deleted successfully.");
                } else {
                    response.sendRedirect("AdminServer?errorMessage=" + "Failed to delete the season.");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect("AdminServer?errorMessage=" + "Invalid ID provided.");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("AdminServer?errorMessage=" + "An error occurred while deleting the season.");
            }
        } else {
            response.sendRedirect("AdminServer");
        }
    }

    // Xử lý yêu cầu POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        SeasonDAO seasonDAO = new SeasonDAO();

        try {
            switch (action) {
                case "add":
                    handleAddSeason(request, response, seasonDAO);
                    break;
                case "edit":
                    handleEditSeason(request, response, seasonDAO);
                    break;
                case "delete":
                    handleDeleteSeason(request, response, seasonDAO);
                    break;
                default:
                    response.sendRedirect("AdminServer");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminServer?errorMessage=" + "An error occurred while processing your request.");
        }
    }

    private void handleAddSeason(HttpServletRequest request, HttpServletResponse response, SeasonDAO seasonDAO) throws ServletException, IOException {
        String detail = request.getParameter("detail");

        if (detail == null || detail.isEmpty()) {
            response.sendRedirect("AdminServer?errorMessage=" + "Detail is required.");
            return;
        }

        try {
            // Kiểm tra nếu season đã tồn tại
            if (seasonDAO.isSeasonExists(detail)) {
                response.sendRedirect("AdminServer?errorMessage=" + "Season with the entered detail already exists.");
            } else {
                seasonDAO.addSeason(new Season(0, detail));
                response.sendRedirect("AdminServer?successMessage=" + "Season added successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminServer?errorMessage=" + "An error occurred while adding the season.");
        }
    }

    private void handleEditSeason(HttpServletRequest request, HttpServletResponse response, SeasonDAO seasonDAO) throws ServletException, IOException {
        String id = request.getParameter("id");
        String detail = request.getParameter("detail");

        if (id == null || detail == null || id.isEmpty() || detail.isEmpty()) {
            response.sendRedirect("AdminServer?errorMessage=" + "Invalid parameters for editing.");
            return;
        }

        try {
            // Kiểm tra nếu season đã tồn tại trước khi thực hiện edit
            if (seasonDAO.isSeasonExists(detail)) {
                response.sendRedirect("AdminServer?errorMessage=" + "Season with the entered detail already exists.");
                return;
            }

            int seasonID = Integer.parseInt(id);
            boolean success = seasonDAO.editSeason(seasonID, detail);

            if (success) {
                response.sendRedirect("AdminServer?successMessage=" + "Season edited successfully.");
            } else {
                response.sendRedirect("AdminServer?errorMessage=" + "Failed to edit the season.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("AdminServer?errorMessage=" + "Invalid season ID.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminServer?errorMessage=" + "An error occurred while editing the season.");
        }
    }

    private void handleDeleteSeason(HttpServletRequest request, HttpServletResponse response, SeasonDAO seasonDAO) throws ServletException, IOException {
        String id = request.getParameter("id");

        if (id == null || id.isEmpty()) {
            response.sendRedirect("AdminServer?errorMessage=" + "Invalid parameters for deletion.");
            return;
        }

        try {
            boolean success = seasonDAO.deleteSeason(Integer.parseInt(id));
            if (success) {
                response.sendRedirect("AdminServer?successMessage=" + "Season deleted successfully.");
            } else {
                response.sendRedirect("AdminServer?errorMessage=" + "Failed to delete the season.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("AdminServer?errorMessage=" + "Error deleting the season.");
        }
    }
}
