package Controller;

public class Season {
    private int seasonID;
    private String detail;  // Đổi từ 'year' thành 'detail'

    // Constructor không tham số
    public Season() {
    }

    // Constructor đầy đủ với tham số
    public Season(int seasonID, String detail) {
        this.seasonID = seasonID;
        this.detail = detail;  // Sử dụng 'detail' thay vì 'year'
    }

    // Getter và Setter cho seasonID
    public int getSeasonID() {
        return seasonID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    // Getter và Setter cho detail (thay vì year)
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
