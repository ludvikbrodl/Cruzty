package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database is a class that specifies the interface to the
 * movie database. Uses JDBC and the MySQL Connector/J driver.
 */
public class Database {
    /**
     * The database connection.
     */
    private Connection conn;

    private static Database instance;

    /**
     * Create the database interface object. Connection to the database
     * is performed later.
     */
    public Database() {
        conn = null;
    }

    /**
     * Open a connection to the database, using the specified user name
     * and password.
     *
     * @param userName The user name.
     * @param password The user's password.
     * @return true if the connection succeeded, false if the supplied
     * user name and password were not recognized. Returns false also
     * if the JDBC driver isn't found.
     */
    public boolean openConnection(String userName, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection
                    ("jdbc:mysql://puccini.cs.lth.se/" + userName + "?zeroDateTimeBehavior=convertToNull",
                            userName, password);
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn = null;

        System.err.println("Database connection closed.");
    }

    /**
     * Check if the connection to the database has been established
     *
     * @return true if the connection has been established
     */
    public boolean isConnected() {
        return conn != null;
    }

    public boolean login(String uname) {
        String sql = "select * from Users WHERE Users.userName = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uname);
            ResultSet user = ps.executeQuery();
            return user.next();
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<String> movieDates(String movieName) {
        try {
            String sql = "select performanceDate from Performance where Performance.movieName = ? order by performanceDate";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieName);
            ResultSet result = ps.executeQuery();
            ArrayList<String> movieDates = new ArrayList<>();
            while (result.next()) {
                movieDates.add(result.getString("performanceDate"));
            }
            return movieDates;
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public ArrayList<String> movieTitles() {
        try {
            String sql = "select * from Movies order by movieName";
            PreparedStatement ps1 = conn.prepareStatement(sql);
            ResultSet result = ps1.executeQuery();
            ArrayList<String> movieList = new ArrayList<>();
            while (result.next()) {
                movieList.add(result.getString("movieName"));
            }
            return movieList;
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public Show getShowData(String mTitle, String mDate) {
        //Integer mFreeSeats = 42;
        //String mVenue = "Kino 2";
        try {
            String sql = "select * from Performance where Performance.movieName = ? and Performance.performanceDate = ? order by performanceDate";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, mTitle);
            ps.setString(2, mDate);
            ResultSet result = ps.executeQuery();
            result.next();
            return new Show(result.getString("movieName"), result.getString("performanceDate"), result.getString("theaterName"), result.getInt("remainingSeats"));
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
            return new Show();
        }

    }

    public int bookTicket(String movie, String date, String uname) {
        int resultcode = -1;
        try {
            conn.setAutoCommit(false);
            String check = "select remainingSeats from Performance where Performance.movieName = ? and Performance.performanceDate = ? for update;";
            PreparedStatement ch = conn.prepareStatement(check);
            ch.setString(1, movie);
            ch.setString(2, date);
            ResultSet seatCheck = ch.executeQuery();
            if (seatCheck.next() && seatCheck.getInt("remainingSeats") == 0) {
                conn.rollback();
            } else {


                String updateSql = "update Performance set remainingSeats = remainingSeats - 1 where Performance.movieName = ? and Performance.performanceDate = ?;";
                PreparedStatement up = conn.prepareStatement(updateSql); // updates remainingSeats

                up.setString(1, movie);
                up.setString(2, date);
                up.executeUpdate();

                String sql = "INSERT INTO Ticket (userName,performanceDate,movieName) VALUES (?, ?, ?);";
                PreparedStatement ps = conn.prepareStatement(sql);

                ps.setString(1, uname);
                ps.setString(2, date);
                ps.setString(3, movie);
                ps.executeUpdate();

                // ResultSet id = conn.prepareStatement("SELECT SCOPE_IDENTITY();").executeQuery();
                resultcode = 0;
            }
        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        } finally {
            try {
                conn.commit();
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultcode;
        }
    }

    public ArrayList<String> getCookieTypes() {
        ArrayList<String> cookieTypes = new ArrayList<>();
        try {

            String sql = "select cookieName from COOKIES";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cookieTypes.add(rs.getString("cookieName"));
            }

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return cookieTypes;

    }

    public void createPallet(String selectedCookieType, String orderId) {
        try {
            PreparedStatement ps1;
            if (orderId == null) {
                String sql = "insert into Pallets (cookieName, orderId) values (?, NULL)";
                ps1 = conn.prepareStatement(sql);
                ps1.setString(1, selectedCookieType);
            } else {
                String sql = "insert into Pallets (cookieName, orderId) values (?, ?)";
                ps1 = conn.prepareStatement(sql);
                ps1.setString(1, selectedCookieType);
                ps1.setString(2, orderId);
            }

            ps1.executeUpdate();


            String updateIngredients =
                    "update INGREDIENTS SET storedAmount = storedAmount - " +
                            "(select amount from COOKIES_INGREDIENTS where cookieName = ?" +
                            "AND COOKIES_INGREDIENTS.ingredientName = INGREDIENTS.ingredientName)" +
                            "where ingredientName in" +
                            "(select ingredientName from Cookies natural join COOKIES_INGREDIENTS where cookieName = ?)";

            ps1 = conn.prepareStatement(updateIngredients);
            ps1.setString(1, selectedCookieType);
            ps1.setString(2, selectedCookieType);

            System.out.println(ps1.executeUpdate());

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        //TODO
    }


    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Pallet getPallet(String idOfPallet) {
        Pallet p = new Pallet();
        try {
            String sql = "select * from PALLETS left outer join ORDERS " +
                    "on PALLETS.orderId = ORDERS.orderId " +
                    "where palletId = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(idOfPallet));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                p.id = rs.getString("palletId");
                p.orderId = rs.getString("orderId");
                p.isBlocked = rs.getBoolean("isBlocked");
                p.prodDate = rs.getString("productionDate");
                p.cookieName = rs.getString("cookieName");
                p.deliveryDate = rs.getString("deliveryDate");
                p.location = rs.getString("location");
                p.customer = rs.getString("customerName");
            }

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return p;
    }

    public List<String> getAllPalletIds() {
        ArrayList<String> ids = new ArrayList<>();
        try {
            String sql = "select palletId from PALLETS";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getString("palletId"));
            }

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return ids;
    }

    public List<String> getFilteredPalletIds(String fromDate, String toDate, boolean showOnlyBlocked, String cookieName) {
        ArrayList<String> ids = new ArrayList<>();
        String sql;
        try {

            if (fromDate == null && toDate == null) {
                sql = "select palletId, isBlocked from PALLETS";
            } else if (fromDate != null && toDate == null) {
                sql = "select palletId, isBlocked from PALLETS where productionDate > '" + fromDate + "'";
            } else if (fromDate == null && toDate != null) {
                sql = "select palletId, isBlocked from PALLETS where productionDate < '" + toDate + "'";
            } else {
                sql = "select palletId, isBlocked from PALLETS where productionDate BETWEEN '" + fromDate + "' AND '" + toDate + "'";
            }

            if (cookieName != null && fromDate == null && toDate == null) {
                sql += " WHERE cookieName = '" + cookieName + "'";
            } else if (cookieName != null) {
                sql += " AND cookieName = '" + cookieName + "'";
            }
            System.out.println(sql);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                if (showOnlyBlocked && rs.getBoolean("isBlocked") || !showOnlyBlocked) {
                    ids.add(rs.getString("palletId"));
                }
            }

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return ids;
    }

    public void blockPallet(String palletId) {
        String sql = "update Pallets SET isBlocked = NOT isBlocked where palletId = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, palletId);
            System.out.println(stmt.execute());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNumPalletsProduced(String startTime, String endTime, String cookieType) {
        String sql = "select count(*) from Pallets where cookieName = ? AND productionDate BETWEEN ? AND ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cookieType);
            stmt.setString(2, startTime);
            stmt.setString(3, endTime);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public ArrayList<String> getAllOrders() {
        ArrayList<String> cookieTypes = new ArrayList<>();
        try {

            String sql = "select orderId, customerName from ORDERS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cookieTypes.add(rs.getString("orderId") + "-" + rs.getString("customerName"));
            }

        } catch (SQLException e) {
            System.err.println(e);
            e.printStackTrace();
        }
        return cookieTypes;

    }
}
