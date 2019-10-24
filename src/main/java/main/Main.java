package main;

import com.mongodb.client.MongoCollection;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import entity.Book;
import entity.City;
import entitymanager.BookManager;
import entitymanager.CityManager;
import entitymanager.CSVHelper;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import mongodb.MongoConnector;
import mongodb.MongoMapper;
import mysql.SQLDataMapper;
import threads.Thread1;
import threads.Thread2;
import threads.Thread3;
import threads.Thread4;
import threads.Thread5;
import threads.Thread6;
import threads.Thread7;
import threads.Thread8;

public class Main {

    public static void main(String[] args) throws IOException, SQLException, ClassCastException, ClassNotFoundException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        MongoCollection coll = MongoConnector.getCollection();
        MongoMapper m = new MongoMapper();
        SQLDataMapper sm = new SQLDataMapper();
        System.out.println("hello and welcome to gutenbeg");
        while (true) {
            System.out.println("please type: mongo or sql (more commands: 'generateFiles', 'exit')");
            String firstLine = scanner.nextLine();
            if (firstLine.equals("mongo")) {
                System.out.println("type from 1-3 to choose query.");
                String nextLine = scanner.nextLine();
                if (nextLine.equals("1")) {
                    System.out.println("type a cityname");
                    m.query1(coll, scanner.nextLine());
                    continue;
                }
                if (nextLine.equals("2")) {
                    System.out.println("type a title for a book");
                    m.query2(coll, scanner.nextLine());
                    continue;
                }
                if (nextLine.equals("3")) {
                    System.out.println("type a an author name... example : 'Various'");
                    m.query3(coll, scanner.nextLine());
                    continue;
                }
            }
            if (firstLine.equals("sql")) {
                System.out.println("type from 1-4 to choose query");
                String nextLine = scanner.nextLine();
                if (nextLine.equals("1")) {
                    System.out.println("type a cityname");
                    sm.query1(scanner.nextLine());
                    continue;
                }
                if (nextLine.equals("2")) {
                    System.out.println("type a title for a book");
                    sm.query2(scanner.nextLine());
                    continue;
                }
                if (nextLine.equals("3")) {
                    System.out.println("type a an author name... example : 'Various'");
                    sm.query3(scanner.nextLine());
                    continue;
                }
                if (nextLine.equals("4")) {
                    System.out.println("type lat, lon and km... example: '12.56 55.67 200");
                    String[] splitted = scanner.nextLine().split(" ");
                    sm.query4(Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]), Integer.parseInt(splitted[2]));
                    continue;
                }
            }
            if (firstLine.equals("generateFiles")) {
                CityManager cityManager = new CityManager();
                List<City> cities = cityManager.readCities();
                cityManager.createCitiesCSV(cities);

                String[] folderNames = new File(System.getProperty("user.dir") + "/books/").list();
//                int endAt = folderNames.length / 8;
               // System.out.println(endAt);
                String serializedClassifier = "classifiers/english.all.3class.distsim.crf.ser.gz";
                AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);
                ArrayList<String> firstPart = new ArrayList<>();
                for (int i = 0; i < 375; i++) {
                    firstPart.add(folderNames[i]);
                }
                ArrayList<String> secondPart = new ArrayList<>();
                for (int i = 375; i < 750; i++) {
                    secondPart.add(folderNames[i]);
                }
                ArrayList<String> thirdPart = new ArrayList<>();
                for (int i = 750; i < 1125; i++) {
                    thirdPart.add(folderNames[i]);
                }
                ArrayList<String> fourthPart = new ArrayList<>();
                for (int i = 1125; i < 1500; i++) {
                    fourthPart.add(folderNames[i]);
                }
                ArrayList<String> fifthPart = new ArrayList<>();
                for (int i = 1500; i < 1875; i++) {
                    fifthPart.add(folderNames[i]);
                }
                ArrayList<String> sixthPart = new ArrayList<>();
                for (int i = 1875; i < 2250; i++) {
                    sixthPart.add(folderNames[i]);
                }
                ArrayList<String> seventhPart = new ArrayList<>();
                for (int i = 2250; i < 2625; i++) {
                    seventhPart.add(folderNames[i]);
                }
                ArrayList<String> eightPart = new ArrayList<>();
                for (int i = 2625; i < 3000; i++) {
                    eightPart.add(folderNames[i]);
                }
                Thread1 t1 = new Thread1(firstPart, classifier);
                Thread2 t2 = new Thread2(secondPart, classifier);
                Thread3 t3 = new Thread3(thirdPart, classifier);
                Thread4 t4 = new Thread4(fourthPart, classifier);
                Thread5 t5 = new Thread5(fifthPart, classifier);
                Thread6 t6 = new Thread6(sixthPart, classifier);
                Thread7 t7 = new Thread7(seventhPart, classifier);
                Thread8 t8 = new Thread8(eightPart, classifier);
                Thread[] threads = new Thread[]{t1, t2, t3, t4, t5, t6, t7, t8};
                ExecutorService es = Executors.newCachedThreadPool();
                for (int i = 0; i < 8; i++) {
                    es.execute(threads[i]);
                }
                es.shutdown();
                boolean finished = es.awaitTermination(1, TimeUnit.DAYS);
                if (finished) {
                    //System.out.println("size? : " + BookManager.jsonList.get(1).getCityQuantity());
                    BookManager bookManager = new BookManager();
                    List<Book> books = BookManager.readBooks();
                    BookManager.createBooksCSV(books);
                    BookManager.createCityMentionCSV(books);
                    BookManager.createAuthorBookCSV(books);
                    CSVHelper.createJson(books, cities);
                    System.out.println(books.get(13).getCities().toString());
                }
                continue;
            }
            System.out.println("something went wrong, please type mongo or sql");
        }
        //SQLDataMapper.createSchema();

// all tasks have finished or the time has been reached.

        /*  CSVHelper csvHelper = new CSVHelper();
        csvHelper.executeMySqlCommands("some-mysql");
        csvHelper.setCorrectSecurefilePath();*/
//        csvHelper.executeMongoCommands("dbms")
        // }
    }

}
