/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import static entitymanager.BookManager.printCitiesFromBooks;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hallur
 */
public class Thread1 extends Thread{
     private ArrayList<String> books;
    private AbstractSequenceClassifier<CoreLabel> classifier;

    public Thread1(ArrayList<String> books, AbstractSequenceClassifier<CoreLabel> classifier) {
        this.books = books;
        this.classifier = classifier;
    }

    @Override
    public void run() {
        try {
            printCitiesFromBooks(classifier, books);
        } catch (IOException ex) {
            Logger.getLogger(Thread1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
             Logger.getLogger(Thread1.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}