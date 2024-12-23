CREATE DATABASE webcki;
USE webcki;
CREATE TABLE users
(
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE  -- Thêm UNIQUE constraint để email không bị trùng lặp
);
USE webcki;
-- Bảng Seasons (Mùa giải)
-- Tạo bảng Seasons (Mùa giải)
CREATE TABLE Seasons (
    SeasonID INT AUTO_INCREMENT PRIMARY KEY,
    Detail VARCHAR(50) NOT NULL UNIQUE -- Ví dụ: '2023/2024' hoặc '2023-2024 Season'
);

-- Bảng Teams (Đội bóng)
CREATE TABLE Teams (
    TeamID INT AUTO_INCREMENT PRIMARY KEY,
    SeasonID INT NOT NULL,             -- Liên kết với mùa giải
    TeamName VARCHAR(100) NOT NULL,    -- Tên đội bóng
    StadiumName VARCHAR(100),          -- Tên sân vận động
    FoundedYear INT,                   -- Năm thành lập
    FOREIGN KEY (SeasonID) REFERENCES Seasons(SeasonID) ON DELETE CASCADE
);

-- Bảng Matches (Trận đấu)
CREATE TABLE Matches (
    MatchID INT AUTO_INCREMENT PRIMARY KEY,
    SeasonID INT NOT NULL,             -- Liên kết với mùa giải
    MatchDate DATE NOT NULL,           -- Ngày diễn ra trận đấu
    HomeTeamID INT NOT NULL,           -- Đội chủ nhà
    AwayTeamID INT NOT NULL,           -- Đội khách
    HomeScore INT DEFAULT 0,           -- Số bàn thắng của đội chủ nhà
    AwayScore INT DEFAULT 0,           -- Số bàn thắng của đội khách
    FOREIGN KEY (SeasonID) REFERENCES Seasons(SeasonID) ON DELETE CASCADE,
    FOREIGN KEY (HomeTeamID) REFERENCES Teams(TeamID) ON DELETE CASCADE,
    FOREIGN KEY (AwayTeamID) REFERENCES Teams(TeamID) ON DELETE CASCADE
);

-- Bảng TeamMembers (Thành viên đội bóng)
CREATE TABLE TeamMembers (
    MemberID INT AUTO_INCREMENT PRIMARY KEY,
    TeamID INT NOT NULL,               -- Liên kết với đội bóng
    FullName VARCHAR(100) NOT NULL,    -- Tên đầy đủ của thành viên
    Position VARCHAR(50),              -- Vị trí (e.g., Forward, Midfielder)
    BirthDate DATE,                    -- Ngày sinh
    JerseyNumber INT,                  -- Số áo
    FOREIGN KEY (TeamID) REFERENCES Teams(TeamID) ON DELETE CASCADE
);




