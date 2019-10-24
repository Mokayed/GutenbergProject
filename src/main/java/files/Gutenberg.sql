drop schema if exists Gutenberg;

create schema Gutenberg;
use Gutenberg;

create table Cities(
id int,
cityName varchar(100),
latitude DECIMAL(10,8),
longitude DECIMAL(11,8),
population int,
countryCode varchar(45),
continent varchar(100),
primary key (id)
);