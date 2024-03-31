create table if not exists RATINGMPA
(
    MPA_ID   INTEGER auto_increment,
    MPA_NAME CHARACTER VARYING(10) not null,
    constraint MPA_PK
    primary key (MPA_ID)
    );

create table if not exists GENRES
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint GENRES_PK
    primary key (GENRE_ID)
    );

create table if not exists FILMS
(
    FILM_ID           BIGINT auto_increment,
    FILM_TITLE        CHARACTER VARYING(50) not null,
    FILM_DESCRIPTION  CHARACTER VARYING(200),
    FILM_RELEASE_DATE DATE                  not null,
    FILM_DURATION     BIGINT               not null,
    FILM_MPA_ID       INTEGER,
    constraint FILMS_PK
    primary key (FILM_ID),
    constraint FILMS_MPA_MPA_ID_FK
    foreign key (FILM_MPA_ID) references RATINGMPA
    );

create table if not exists USERS
(
    USER_ID       BIGINT auto_increment,
    USER_EMAIL    CHARACTER VARYING(50) not null,
    USER_LOGIN    CHARACTER VARYING(50) not null,
    USER_NAME     CHARACTER VARYING(50) not null,
    USER_BIRTHDAY DATE                  not null,
    constraint USERS_PK
    primary key (USER_ID)
    );

create table if not exists FILMGENRE
(
    FILM_ID  BIGINT,
    GENRE_ID INTEGER,
    constraint FILMGENRE_FILMS_FILM_ID_FK
    foreign key (FILM_ID) references FILMS,
    constraint FILMGENRE_GENRES_GENRE_ID_FK
    foreign key (GENRE_ID) references GENRES
    );

create table if not exists FRIENDSHIPS
(
    USER_ID   BIGINT,
    FRIEND_ID BIGINT,
    constraint FRIENDSHIP_USERS_USER_ID_FK
    foreign key (USER_ID) references USERS,
    constraint FRIENDSHIP_USERS_USER_ID_FK_2
    foreign key (FRIEND_ID) references USERS
    );

create table if not exists LIKES
(
    USER_ID BIGINT,
    FILM_ID BIGINT,
    constraint LIKES_FILMS_FILM_ID_FK
    foreign key (FILM_ID) references FILMS,
    constraint LIKES_USERS_USER_ID_FK
    foreign key (USER_ID) references USERS
    );