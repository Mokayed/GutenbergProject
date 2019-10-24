package mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import static mysql.SQLConnector.getConnection;

public class SQLDataMapper {
    
    
    public void query1(String cityName) throws SQLException{
      Connection con = getConnection();
        try {
            String sql = "select title, authorName from book\n"
                    + "inner join cityMention on cityMention.bookId = book.id\n"
                    + "inner join Cities on Cities.id = cityMention.cityId\n"
                    + "inner join authorBooks on authorBooks.bookId = book.id\n"
                    + "where Cities.cityName = ?;";
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setString(1, cityName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }
        } catch (Exception e) {
        } finally {
            con.close();
        }
    }
    public void query2(String bookId) throws SQLException{
     Connection con = getConnection();
        try {
            String sql = " SELECT book.id, cityName as cityMentioned, latitude, longitude, cityMention.count as cityOccurences, title\n"
                    + " FROM book INNER JOIN cityMention ON book.id = cityMention.bookId INNER JOIN Cities\n"
                    + " ON Cities.id = cityMention.cityId WHERE book.id = ? ;";
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("cityMentioned"));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }
    }
    public void query3(String authorName) throws SQLException{
     Connection con = getConnection();
        try {
            String sql = " SELECT authorName, cityMention.bookId, cityName , latitude, longitude, title as bookTitle FROM authorBooks \n"
                    + "INNER JOIN cityMention ON cityMention.bookId = authorBooks.bookId\n"
                    + "INNER JOIN book ON authorBooks.bookId = book.id\n"
                    + "INNER JOIN Cities ON Cities.id = cityMention.cityId\n"
                    + " WHERE authorBooks.authorName = ?;";
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setString(1, authorName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Book_id:" + " " + rs.getString("bookId") + " " + "Book_title:" + " " + rs.getString("bookTitle") + " " + "City_mentioned:" + " " + rs.getString("cityName"));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }
    }
    public void query4(double lat, double longit, int km) throws SQLException{
      Connection con = getConnection();
        try {
            String sql = "  select ROUND(ST_Distance_Sphere(\n"
                    + "            point(? , ?),\n"
                    + "            point(12.568337, 55.676098)\n"
                    + "        ) / 1000,2) as km_distance, cityName as city_in_area, title as title_of_book_mentioning_city from Cities \n"
                    + " inner join cityMention as cm on Cities.id = cm.cityId\n"
                    + " inner join book as b on cm.bookId = b.id\n"
                    + " where ST_Distance_Sphere(\n"
                    + "            point(longitude, latitude),\n"
                    + "            point(?, ?)\n"
                    + "        ) / 1000 < ?;";
            PreparedStatement pstmt = getConnection().prepareStatement(sql);
            pstmt.setDouble(1, lat);
            pstmt.setDouble(2, longit);           
            pstmt.setDouble(3, lat);         
            pstmt.setDouble(4, longit);       
            pstmt.setInt(5, km);
            ;
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("km_distence:" + " " + rs.getString("km_distance") + " " + "city_in_area:" + " " + rs.getString("city_in_area") + " " + "title_of_book_mentioning_city:" + " " + rs.getString("title_of_book_mentioning_city"));
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            con.close();
        }
    }

    public static List<String> getCityNames() throws SQLException {
        Connection con = SQLConnector.getConnection();
        List<String> cities = new ArrayList<>();
        try {
            PreparedStatement ps = con.prepareStatement("select cityName from Cities");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cities.add(rs.getString("cityName"));
            }
        } catch (Exception e) {
        } finally{
        con.close();
        }
        return cities;
    }

    public static void createSchema() throws SQLException {
        Connection con = SQLConnector.getConnection();
        Statement stmt = con.createStatement();

        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/files/Gutenberg.sql"); // source path is the absolute path of dumpfile.

            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = null, old = "";
            line = bf.readLine();
            while (line != null) {
                //q = q + line + "\n";
                if (line.endsWith(";")) {
                    stmt.executeUpdate(old + line);
                    old = "";
                } else {
                    old = old + "\n" + line;
                }
                line = bf.readLine();
            }

            System.out.println("Created schema Gutenberg");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
        con.close();
        }
    }

    public static int getCityId(String cityName) throws SQLException {
        Connection con = SQLConnector.getConnection();
        int id = 0;
        try {
            PreparedStatement ps = con.prepareStatement("select id from Cities where cityName = ?");
            ps.setString(1, cityName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
        } catch (Exception e) {
        } finally{
        con.close();
        }
        return id;
    }

    public static void insertBooks() throws SQLException {
        Connection con = SQLConnector.getConnection();
        Statement stmt = con.createStatement();
        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/files/InsertCsvBooks.sql"); // source path is the absolute path of dumpfile.

            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = null, old = "";
            line = bf.readLine();
            while (line != null) {
                //q = q + line + "\n";
                if (line.endsWith(";")) {
                    stmt.executeUpdate(old + line);
                    old = "";
                } else {
                    old = old + "\n" + line;
                }
                line = bf.readLine();
            }

            System.out.println("Inserted all books into MySQL");
        } catch (Exception e) {

        } finally{
        con.close();
        }
    }

    public static void insertCities() throws SQLException {
        Connection con = SQLConnector.getConnection();
        Statement stmt = con.createStatement();
        try {
            File f = new File(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql"); // source path is the absolute path of dumpfile.

            BufferedReader bf = new BufferedReader(new FileReader(f));
            String line = null, old = "";
            line = bf.readLine();
            while (line != null) {
                //q = q + line + "\n";
                if (line.endsWith(";")) {
                    stmt.executeUpdate(old + line);
                    old = "";
                } else {
                    old = old + "\n" + line;
                }
                line = bf.readLine();
            }

            System.out.println("Inserted all cities into MySQL");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
        con.close();
        }
    }

    public static String getSecureFilePath() throws SQLException {
         Connection con = SQLConnector.getConnection();
        String path = null;
        try {
           
            PreparedStatement ps = con.prepareStatement("show variables like 'secure_file_priv';");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                path = rs.getString("Value");
                System.out.println(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
        con.close();
        }
        return path;
    }

}
