DELETE FROM penalties_history;
DELETE FROM penalties;
DELETE FROM orders_history;
DELETE FROM orders;
DELETE FROM USERS;
DELETE FROM users_data;
DELETE FROM copies;
DELETE FROM books;
DELETE FROM authors;
DELETE FROM work_times;
DELETE FROM libraries;
DELETE FROM addresses;


INSERT INTO addresses VALUES (1, 'Piotrkowska', 100, 'Lodz', 51.776832, 19.456764);
INSERT INTO addresses VALUES (2,'Warszawska', 50, 'Warszawa', 52.229841, 21.012229);
INSERT INTO addresses VALUES (3,'Lubelska', 25, 'Bialystok', 53.132489, 23.168899);
INSERT INTO addresses VALUES (4,'Krakowska', 110, 'Krakow', 50.049683, 19.944544);
INSERT INTO addresses VALUES (5,'Gdanska', 35, 'Gdansk', 54.352025, 18.646638);
INSERT INTO addresses VALUES (6,'Piotrkowska', 20, 'Lodz', 51.78, 19.456764);
INSERT INTO addresses VALUES (7,'Warszawska', 10, 'Warszawa', 52.23, 21.012229);
INSERT INTO addresses VALUES (8,'Lubelska', 100, 'Bialystok', 53.14, 23.168899);
INSERT INTO addresses VALUES (9,'Krakowska', 5, 'Krakow', 50.05, 19.944544);
INSERT INTO addresses VALUES (10,'Gdanska', 75, 'Gdansk', 54.36, 18.646638);

INSERT INTO libraries VALUES (1, 'Biblioteka Miejska', 1, 123456789);
INSERT INTO libraries VALUES (2, 'Biblioteka Narodowa', 2, 987654321);
INSERT INTO libraries VALUES (3, 'Biblioteka Uniwersytecka', 3, 741852963);
INSERT INTO libraries VALUES (4, 'Biblioteka Publiczna', 4, 369258147);
INSERT INTO libraries VALUES (5, 'Biblioteka Szkolna', 5, 963258741);


INSERT INTO work_times VALUES (1, 1, 1, '9:00', '17:00');
INSERT INTO work_times VALUES (2, 1, 2, '9:00', '17:00');
INSERT INTO work_times VALUES (3, 1, 3, '9:00', '15:00');
INSERT INTO work_times VALUES (4, 1, 4, '9:00', '17:00');
INSERT INTO work_times VALUES (5, 1, 5, '9:00', '17:00');
INSERT INTO work_times VALUES (6, 2, 1, '10:00', '18:00');
INSERT INTO work_times VALUES (7, 2, 2, '10:00', '18:00');
INSERT INTO work_times VALUES (8, 2, 3, '10:00', '16:00');
INSERT INTO work_times VALUES (9, 2, 4, '10:00', '18:00');
INSERT INTO work_times VALUES (10, 2, 5, '10:00', '18:00');
INSERT INTO work_times VALUES (11, 3, 1, '9:00', '17:00');
INSERT INTO work_times VALUES (12, 3, 2, '9:00', '17:00');
INSERT INTO work_times VALUES (13, 3, 3, '9:00', '15:00');
INSERT INTO work_times VALUES (14, 3, 4, '9:00', '17:00');
INSERT INTO work_times VALUES (15, 3, 5, '9:00', '17:00');
INSERT INTO work_times VALUES (16, 4, 1, '10:00', '18:00');
INSERT INTO work_times VALUES (17, 4, 2, '10:00', '18:00');
INSERT INTO work_times VALUES (18, 4, 3, '10:00', '16:00');
INSERT INTO work_times VALUES (19, 4, 4, '10:00', '18:00');
INSERT INTO work_times VALUES (20, 4, 5, '10:00', '18:00');
INSERT INTO work_times VALUES (21, 5, 1, '7:00', '16:00');
INSERT INTO work_times VALUES (22, 5, 2, '7:00', '16:00');
INSERT INTO work_times VALUES (23, 5, 3, '7:00', '16:00');
INSERT INTO work_times VALUES (24, 5, 4, '7:00', '16:00');
INSERT INTO work_times VALUES (25, 5, 5, '7:00', '14:00');


INSERT INTO authors VALUES (1, 'Adam', 'Mickiewicz', '1798-12-24', 'Polska');
INSERT INTO authors VALUES (2, 'Boleslaw', 'Prus', '1847-08-20', 'Polska');
INSERT INTO authors VALUES (3, 'Henryk', 'Sienkiewicz', '1846-05-05', 'Polska');


INSERT INTO books VALUES (1, 'Pan Tadeusz', 1, 400, 1234567890123, 1834, 'poemat');
INSERT INTO books VALUES (2, 'Dziady', 1, 450, 2345678901234, 1823, 'dramat');
INSERT INTO books VALUES (3, 'Lalka', 2, 600, 3456789012345, 1890, 'powiesc');
INSERT INTO books VALUES (4, 'Quo vadis', 3, 550, 4567890123456, 1895, 'powiesc');
INSERT INTO books VALUES (5, 'W pustyni i w puszczy', 3, 250, 5678901234567, 1911, 'powiesc przygodowa');


INSERT INTO copies VALUES (1, 1, 1, 1);
INSERT INTO copies VALUES (2, 1, 2, 1);
INSERT INTO copies VALUES (3, 1, 3, 1);
INSERT INTO copies VALUES (4, 1, 4, 1);
INSERT INTO copies VALUES (5, 1, 5, 1);
INSERT INTO copies VALUES (6, 2, 1, 1);
INSERT INTO copies VALUES (7, 2, 2, 1);
INSERT INTO copies VALUES (8, 2, 3, 1);
INSERT INTO copies VALUES (9, 2, 4, 1);
INSERT INTO copies VALUES (10, 2, 5, 1);
INSERT INTO copies VALUES (11, 3, 1, 1);
INSERT INTO copies VALUES (12, 3, 2, 1);
INSERT INTO copies VALUES (13, 3, 3, 1);
INSERT INTO copies VALUES (14, 3, 4, 1);
INSERT INTO copies VALUES (15, 3, 5, 1);
INSERT INTO copies VALUES (16, 4, 1, 1);
INSERT INTO copies VALUES (17, 4, 2, 1);
INSERT INTO copies VALUES (18, 4, 3, 1);
INSERT INTO copies VALUES (19, 4, 4, 1);
INSERT INTO copies VALUES (20, 4, 5, 1);
INSERT INTO copies VALUES (21, 5, 1, 1);
INSERT INTO copies VALUES (22, 5, 2, 1);
INSERT INTO copies VALUES (23, 5, 3, 1);
INSERT INTO copies VALUES (24, 5, 4, 1);
INSERT INTO copies VALUES (25, 5, 5, 1);


INSERT INTO users_data VALUES (1, 'user1', 'password1', 0);
INSERT INTO users_data VALUES (2, 'user2', 'password2', 0);
INSERT INTO users_data VALUES (3, 'user3', 'password3', 0);
INSERT INTO users_data VALUES (4, 'admin1', 'admin1', 1);
INSERT INTO users_data VALUES (5, 'admin2', 'admin2', 1);


INSERT INTO USERS VALUES (1, 1, 'Jan', 'Kowalski', 6);
INSERT INTO USERS VALUES (2, 2, 'Anna', 'Nowak', 7);
INSERT INTO USERS VALUES (3, 3, 'Tomasz', 'Wisniewski', 8);
INSERT INTO USERS VALUES (4, 4, 'Joanna', 'Kwiatkowska', 9);
INSERT INTO USERS VALUES (5, 5, 'Piotr', 'Lewandowski', 10);


INSERT INTO penalties VALUES (1, 'Nieodebranie ksiazki', 'Nieodebranie ksiazki z biblioteki po uplywie terminu', 2);
INSERT INTO penalties VALUES(2, 'Opoznienie w zwrocie', 'Opoznienie w zwrocie ksiazki', 5);
INSERT INTO penalties VALUES(3, 'Zniszczenie ksiazki', 'Zniszczenie ksiazki podczas wypozyczenia', 20);
INSERT INTO penalties VALUES(4, 'Brak zwrotu ksiazki', 'Ksiazka nie zostala zwrocona po uplywie 2 tygodni od terminu oddania', 50);


COMMIT;
