 DROP TABLE IF EXISTS film_like_list CASCADE;
 DROP TABLE IF EXISTS user_friend_list CASCADE;
 DROP TABLE IF EXISTS users CASCADE;
 DROP TABLE IF EXISTS film_genres CASCADE;
 DROP TABLE IF EXISTS genres CASCADE;
 DROP TABLE IF EXISTS films CASCADE;
 DROP TABLE IF EXISTS mpa_ratings CASCADE;

 CREATE TABLE IF NOT EXISTS mpa_ratings
 (
     rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name          VARCHAR(50) NOT NULL UNIQUE
 );

 CREATE TABLE IF NOT EXISTS films
 (
     film_id             INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name                VARCHAR(50) NOT NULL,
     description         VARCHAR(200),
     release_date        DATE,
     duration INTEGER CHECK (duration > 0),
     rating_id       INTEGER REFERENCES mpa_ratings (rating_id)
 );

 CREATE TABLE IF NOT EXISTS genres
 (
     genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name     VARCHAR(50) NOT NULL UNIQUE
 );

 CREATE TABLE IF NOT EXISTS film_genres
 (
     film_id  INTEGER  NOT NULL REFERENCES films (film_id),
     genre_id INTEGER NOT NULL REFERENCES genres (genre_id)
 );

 CREATE TABLE IF NOT EXISTS users
 (
     user_id  INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     email    VARCHAR(50) NOT NULL UNIQUE,
     login    VARCHAR(50) NOT NULL UNIQUE,
     name     VARCHAR(50) NOT NULL,
     birthday DATE NOT NULL
 );

 CREATE TABLE IF NOT EXISTS user_friend_list
 (
     from_user_id INTEGER  NOT NULL REFERENCES users (user_id),
     to_user_id   INTEGER  NOT NULL REFERENCES users (user_id),
     Boolean_status     BOOLEAN NOT NULL,
     PRIMARY KEY (from_user_id, to_user_id)
 );

 CREATE TABLE IF NOT EXISTS film_like_list
 (
     film_id INTEGER  NOT NULL REFERENCES films (film_id),
     user_id INTEGER  NOT NULL REFERENCES users (user_id)
 );
