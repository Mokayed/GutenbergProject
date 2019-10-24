package entitymanager;

import entity.Book;
import entity.City;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import mysql.SQLDataMapper;

public class CSVHelper {

    public static void createJson(List<Book> books, List<City> cities) throws IOException {

        ArrayList<String> authors = new ArrayList<String>();
        for (int i = 0; i < books.size(); i++) {
            for (int j = 0; j < books.get(i).getAuthors().size(); j++) {
                if (!authors.contains(books.get(i).getAuthors().get(j))) {
                    authors.add(books.get(i).getAuthors().get(j));
                }
            }
        }
        String json = "[";
        for (int i = 0; i < authors.size(); i++) {
            json += "{authorName: \"" + authors.get(i) + "\",\n";
            json += "books: [";
            for (int j = 0; j < books.size(); j++) {
                if (books.get(j).getAuthors().contains(authors.get(i))) {
                    json += "{title: \"" + books.get(j).getTitle() + "\"\n, cities: [";
                    for (int k = 0; k < books.get(j).getCities().size(); k++) {
                        String city = (String) books.get(j).getCities().keySet().toArray()[k];
                        int count = books.get(j).getCities().get(city);
                        Double latitude = null;
                        Double longitude = null;
                        for (int l = 0; l < cities.size(); l++) {
                            if (cities.get(l).getCityName().equals(city)) {
                                latitude = cities.get(l).getLatitude();
                                longitude = cities.get(l).getLongitude();
                            }
                        }
                        if (k == books.get(j).getCities().size() - 1) {
                            json += "{cityName: \"" + city + "\"," + "latitude : \"" + latitude + "\", longitude: \"" + longitude + "\", count: \"" + count + "\"}\n";
                        } else {
                            json += "{cityName: \"" + city + "\"," + "latitude : \"" + latitude + "\", longitude: \"" + longitude + "\", count: \"" + count + "\"},\n";
                        }
                    }
                    json += "]";
                } else {
                    continue;
                }
                json+="}\n";

                
            }
            if(i == authors.size()-1){
            json += "]\n";
            } else {
                json += "]},\n";
            }
        }
        json += "}]";
        Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/main/java/files/json.json");
        writer.write(json);
        writer.close();
    }

    public void setCorrectSecurefilePath() throws IOException, SQLException {
        String secureFilePath = SQLDataMapper.getSecureFilePath();
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql");
        Path path2 = Paths.get(System.getProperty("user.dir") + "/src/main/java/files/InsertCsv.sql");
        Charset charset = StandardCharsets.UTF_8;
        String content = new String(Files.readAllBytes(path), charset);
        String content2 = new String(Files.readAllBytes(path), charset);
        content = content.replaceAll("%path%", "'" + secureFilePath + "cities.csv'");
        content2 = content2.replaceAll("%path%", "'" + secureFilePath + "books.csv");
        Files.write(path, content.getBytes(charset));
        Files.write(path2, content2.getBytes(charset));
//        SQLDataMapper.insertCities();

        SQLDataMapper.insertBooks();

        content = content.replaceAll("'" + secureFilePath + "cities.csv'", "%path%");
        content2 = content2.replaceAll("'" + secureFilePath + "cities.csv'", "%path%");
        Files.write(path, content.getBytes(charset));
        Files.write(path2, content2.getBytes(charset));
    }

    public void executeMySqlCommands(String containerName) {
        String dockerMysqlName = containerName;
        String[] mySQLcommands = new String[]{"/bin/bash", "docker cp " + System.getProperty("user.dir") + "/src/main/java/files/citiesForDocker.csv " + dockerMysqlName + ":/home/cities.csv",
            "docker cp " + System.getProperty("user.dir") + "/src/main/java/files/booksForDocker.csv " + dockerMysqlName + ":/home/books.csv"};
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command1 = null;
        try {
            for (String command : mySQLcommands) {
                command1 = command;
                p = r.exec(command1);
            }
            System.out.println("Reading csv into Database");
        } catch (Exception e) {
            System.out.println("Error executing " + command1 + e.toString());
        }
    }

    public void executeMongoCommands(String containerName) {
        String dockerMongodbName = containerName;
        String[] mongoCommands = new String[]{"/bin/bash", "docker cp " + System.getProperty("user.dir") + "/src/main/java/Files/citiesForDocker.csv " + dockerMongodbName + ":/home/cities.csv",
            "docker exec -d dbms mongoimport -d mydb -c gutenberg --type csv --file /home/cities.csv --headerline"};
        Runtime r = Runtime.getRuntime();
        Process p = null;
        String command1 = null;
        try {
            for (String command : mongoCommands) {
                command1 = command;
                p = r.exec(command1);
            }
            System.out.println("Reading csv into Database");

        } catch (Exception e) {
            System.out.println("Error executing " + command1 + e.toString());
        }
    }
}
