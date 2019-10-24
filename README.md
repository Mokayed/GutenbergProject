# Gutenberg Project <g-emoji class="g-emoji" alias="book" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f4d6.png">📖</g-emoji>

> _By cool guys - Murched Kayed, Hallur vid Neyst & Zaeem Shafiq_

<h1>Introduction <g-emoji class="g-emoji" alias="memo" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f4dd.png">📝</g-emoji></h1>

<p>This is our solution to the <a href="https://github.com/datsoftlyngby/soft2019spring-databases/tree/master/Exam"> Gutenberg project </a>. We have used Java as programming language and the two databases used are MySQL and MongoDB.
</p>
<p>We chose to use a command-line interface (using Scanner in Java) as the user interface of our application, which is used to execute the queries in our application.</p>

<Strong>Our report can be found <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/Gutenberg%20rapport.pdf">here</a></strong>.

<h1>MySQL <img src="http://icons.iconarchive.com/icons/papirus-team/papirus-apps/48/mysql-workbench-icon.png" style="margin-top:40px;" title="Mysql-workbench" alt="Mysql-workbench icon" width="48" height="48"></h1>

<h4>To run the queries you need to run this <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/Dump20190530.sql">dump file</a> into your MySQL Database.</h4>

<h2>Indexes</h2>

<p>The dump file also contains foreign keys, which will auto generate indexes on all the foreign keys.
To improve the query performance, we created two extra indexes on 'authorName' in the authorBooks table and 'cityName' in the cities table:
</p>

```sql
create index cityName_index on Cities(cityName);
create index authorBooks_index on authorBooks(authorName);
```

<p>We considered whether we should create an index on 'title' in the book table, but since the title is a long VARCHAR column, we thought it would be a bad idea to add an index as it will be very bulky and inefficient.</p>

<h2>SQL Queries <g-emoji class="g-emoji" alias="mag" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f50d.png">🔍</g-emoji></h2>

<h3>1. Given a city name your application returns all book titles with corresponding authors that mention this city.</h3>

```sql
SELECT 
    title, authorName
FROM
    book
        INNER JOIN
    cityMention ON cityMention.bookId = book.id
        INNER JOIN
    Cities ON Cities.id = cityMention.cityId
        INNER JOIN
    authorBooks ON authorBooks.bookId = book.id
WHERE
    Cities.cityName = ?;
```
<h3>2. Given a book title, your application plots all cities mentioned in this book.</h3>

```sql
SELECT 
    book.id,
    cityName AS cityMentioned,
    latitude,
    longitude,
    cityMention.count AS cityOccurences,
    title
FROM
    book
        INNER JOIN
    cityMention ON book.id = cityMention.bookId
        INNER JOIN
    Cities ON Cities.id = cityMention.cityId
WHERE
    book.title = ?;
```

<h3>3. Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h3>

```sql
SELECT 
    authorName,
    cityMention.bookId,
    cityName AS mentionedCity,
    latitude,
    longitude,
    title AS bookTitle
FROM
    authorBooks
        INNER JOIN
    cityMention ON cityMention.bookId = authorBooks.bookId
        INNER JOIN
    book ON authorBooks.bookId = book.id
        INNER JOIN
    Cities ON Cities.id = cityMention.cityId
WHERE
    authorBooks.authorName = ?;
```
<h3>4. Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h3>

```sql
SELECT 
    ROUND(ST_DISTANCE_SPHERE(POINT(longitude, latitude),
                    POINT(12.568337, 55.676098)) / 1000,
            2) AS km_distance,
    cityName AS city_in_area,
    title AS title_of_book_mentioning_city
FROM
    Cities
        INNER JOIN
    cityMention AS cm ON Cities.id = cm.cityId
        INNER JOIN
    book AS b ON cm.bookId = b.id
WHERE
    ST_DISTANCE_SPHERE(POINT(longitude, latitude),
            POINT(12.568337, 55.676098)) / 1000 < 200;
```

<h1>MongoDB <img style="-webkit-user-select: none;" src="https://sitejerk.com/images/mongodb-png-10.png" width="45" height="45"></h1>

<h4>To run the queries, you need to import this <a href="https://github.com/Hallur20/GutenbergDatabaseExamProject/blob/master/authorsJson.json">json file</a> into your MongoDB Database using the following command:</h4>

<code>
 mongoimport --db <strong>your db Name</strong> --collection authors --file authorsJson.json --jsonArray
</code>

<h2>MongoDB Queries <g-emoji class="g-emoji" alias="mag" fallback-src="https://github.githubassets.com/images/icons/emoji/unicode/1f50d.png">🔍</g-emoji></h2>

<h3>1. Given a city name (London) your application returns all book titles with corresponding authors that mention this city.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.cities.cityName" : "London"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.cities.cityName"}}}
])
```

<h3>2. Given a book title ("The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)"), your application plots all cities mentioned in this book.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.title" : "The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities.cityName"}}}
])
```

<h3>3. Given an author name ('Various') your application lists all books written by that author and plots all cities mentioned in any of the books.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$match" : {"authorName" : "Various"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities"}}}
])
```
<h1>Measure Behavior</h1>
<h4>The application behavior measurements are obtained after creating a connection to the databases for each execute query (Time = DbConnectionTime + queryExcutionTime). That's why it took longer time then expected, but if we connected to the database before executing the queries, we would only have taken the time for the query execution. With that process we would have obtained better results (Time =  queryExcutionTime).</h4>
 
<table>
<thead>
<tr>
<th>Queries</th>
<th>Inputs</th>
<th>MongoDB-app</th>
<th>MySQL-app</th>
<th>MongoDB-shell</th>
<th>MySQL-shell</th>
</tr>
</thead>
<tbody>
<tr>
<td>Query_1</td>
<td>London</td>
<td>3.1 s</td>
<td>3.6 s</td>
<td>0.0 s</td>
<td>0.016 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Copenhagen</td>
<td>3.2 s</td>
<td>3.4 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Helsingborg</td>
<td>3.1 s</td>
<td>3.2 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Berlin</td>
<td>3.2 s</td>
<td>3.67 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_1</td>
<td>Nothing</td>
<td>3.1 s</td>
<td>3.1 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_3</td>
<td>Schlesinger, Max</td>
<td>3.2 s</td>
<td>3.3 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_3</td>
<td>Fontenoy, marquise de</td>
<td>2.9 s</td>
<td>3.4 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>

<tr>
<td>Query_2</td>
<td>The Three Musketeers</td>
<td>3.0 s</td>
<td>3.3 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>
<tr>
<td>Query_2</td>
<td>History of Modern PhilosophyFrom Nicolas of Cusa to the Present Time</td>
<td>2.7 s</td>
<td>3.2 s</td>
<td>0.0 s</td>
<td>0.0 s</td>
</tr>

<tr>
<td>Query_4</td>
<td>12.568337, 55.676098, 200</td>
<td>-</td>
<td>4.1 s</td>
<td>-</td>
<td>0.0593 s</td>
</tr>
<tr>
<td>Query_4</td>
<td>-0.1277583, 51.5073509, 200</td>
<td>-</td>
<td>7.2 s</td>
<td>-</td>
<td>0.078 s</td>
</tr>
</tbody>
</table>
