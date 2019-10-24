/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import org.bson.Document;

/**
 *
 * @author hallur
 */
public class MongoMapper {

    static Block<Document> printBlock = new Block<Document>() {
        @Override
        public void apply(final Document document) {
            System.out.println(document.toJson());
        }
    };

    public void query1(MongoCollection coll, String city) {
        AggregateIterable<Document> output = coll.aggregate(Arrays.asList(
                new Document("$unwind", "$books"),
                new Document("$unwind", "$books.cities"),
                new Document("$match", new Document("books.cities.cityName", city)),
                new Document("$group", new Document("_id", new Document("_id", "$_id").append("cityName", "$books.cities.cityName")))
        ));
        output.forEach(printBlock);
    }

    public void query2(MongoCollection coll, String title) {
        AggregateIterable<Document> output2 = coll.aggregate(Arrays.asList(
                new Document("$unwind", "$books"),
                new Document("$unwind", "$books.cities"),
                new Document("$match", new Document("books.title", title)),
                new Document("$group", new Document("_id", new Document("_id", "$_id").append("book", "$books.title").append("cities", "$books.cities.cityName\"")))
        ));
        output2.forEach(printBlock);
    }

    public void query3(MongoCollection coll, String authorName) {
        AggregateIterable<Document> output3 = coll.aggregate(Arrays.asList(
                new Document("$unwind", "$books"),
                new Document("$match", new Document("authorName", authorName)),
                new Document("$group", new Document("_id", new Document("_id", "$_id").append("book", "$books.title").append("cities", "$books.cities")))
        ));
        output3.forEach(printBlock);
    }
}
