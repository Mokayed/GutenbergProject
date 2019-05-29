# GutenbergProject

> _By Group 4 - Murched Kayed, Hallur vid Neyst & Zaeem Shafiq_

<h1>Introduction</h1>

<p>This is a solution for the <a href="https://bit.ly/2EyCDsk" rel="https://github.com/datsoftlyngby/soft2019spring-databases/tree/master/Exam"> Gutenber project </a>, using Java to generate the needed csv files to import into the database, there ben used two diffrent databases SQL and MongodDB</p>

<h1>SQL</h1>
<h4>To run the quries you need to run this <a href="https://bit.ly/2EyCDsk" rel="https://github.com/Mokayed/GutenbergProject/blob/master/dump.sql">dump file</a> into you SQL-Database.</h4>

<h2>index's</h2>

```sql
create index authors_book_id_index on authors(book_id);
```
<h2>Queries</h2>

<h3>1-Given a city name your application returns all book titles with corresponding authors that mention this city.</h3>

```sql
select title, authorName from book
inner join cityMention on cityMention.bookId = book.id
inner join Cities on Cities.id = cityMention.cityId
inner join authorBooks on authorBooks.bookId = book.id
where Cities.cityName = 'London';
```
<h3>2-Given a book title, your application plots all cities mentioned in this book.</h3>

```sql
 SELECT book.id, cityName as cityMentioned, latitude, longitude, cityMention.count as cityOccurences, title
 FROM book INNER JOIN cityMention ON book.id = cityMention.bookId INNER JOIN Cities
 ON Cities.id = cityMention.cityId WHERE book.title = ?;
```

<h3>3-Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h3>

```sql
 SELECT authorName, cityMention.bookId, cityName as mentionedCity , latitude, longitude, title as bookTitle FROM authorBooks 
INNER JOIN cityMention ON cityMention.bookId = authorBooks.bookId
INNER JOIN book ON authorBooks.bookId = book.id
INNER JOIN Cities ON Cities.id = cityMention.cityId
 WHERE authorBooks.authorName = ?;
```


<h1>MongoDB</h1>

<h3>1-Given a city name your application returns all book titles with corresponding authors that mention this city 'London'.</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.cities.cityName" : "London"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.cities.cityName"}}}
])
```

<h3>2-Given a book title, your application plots all cities mentioned in this book "The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)".</h3>

```mongo
db.authors.aggregate([
{ "$unwind": "$books" },
{"$unwind" : "$books.cities"},
{"$match" : {"books.title" : "The Life and Most Surprising Adventures of Robinson Crusoe, of York, Mariner (1801)"}},
{"$group" : {"_id" : {"_id" : "$_id", "book" : "$books.title", "cities" : "$books.cities.cityName"}}}
])
```
