/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author hallur
 */
public class MongoConnector {
    public static MongoCollection getCollection() {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("dbms");
        MongoCollection<org.bson.Document> coll = database.getCollection("authors");
        return coll;
    }
}
