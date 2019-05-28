# GutenbergProject


# GutenbergDatabaseExamProject

1. You need to do some extra steps to make this project work (apart from running it in Netbeans idé)...



2. go into your docker container containing mysql-server -> /etc/mysql/my.cnf.

change whatever secure-file-priv says to: secure-file-priv=/home

if your docker container name is not "some-mysql" then you can change it to your name at line: 58 in the Main file.

we are using localhost...

we are using root user...

we are using password 123...

the program will copy the generated csv file to your home folder of your running docker container.

<h1>SQL</h1>

<h2>1-Given a city name your application returns all book titles with corresponding authors that mention this city.</h2>

```sql
select title, authorName from book
inner join cityMention on cityMention.bookId = book.id
inner join Cities on Cities.id = cityMention.cityId
inner join authorBooks on authorBooks.bookId = book.id
where Cities.cityName = 'London';
```
<h2>2-Given a book title, your application plots all cities mentioned in this book.</h2>

```sql
 SELECT book.id, cityName as cityMentioned, latitude, longitude, cityMention.count as cityOccurences, title FROM book INNER JOIN cityMention ON book.id = cityMention.bookId INNER JOIN Cities
 ON Cities.id = cityMention.cityId WHERE book.title = ?;
```

<h2>3-Given an author name your application lists all books written by that author and plots all cities mentioned in any of the book</h2>

```sql
 SELECT authorName, cityMention.bookId, cityName as mentionedCity , latitude, longitude, title as bookTitle FROM authorBooks 
INNER JOIN cityMention ON cityMention.bookId = authorBooks.bookId
INNER JOIN book ON authorBooks.bookId = book.id
INNER JOIN Cities ON Cities.id = cityMention.cityId
 WHERE authorBooks.authorName = ?;
```


<h1>MongoDB</h1>
