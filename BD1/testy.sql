/* TESTY DLA FUNKCJI */

select library_id , calculte_library_used_percentage(library_id) from libraries ;
/* powinno wyjść 5/6 * 100 dla biblioteki o id 1, 1/6 * 100 dla biblioteki o id 3, 0 dla reszty*/


select useridfromname('user1') from dual;
/* powinno wyjść 1 */


/* TESTY DLA PROCEDUR */

exec userhistory(1);
/* powinno wyjść:
    Pan Tadeusz: Zwrocona
    Dziady: Zwrocona
    Lalka: Wypozyczona
*/

exec userhistory(2);
/*
    Pan Tadeusz: Rezerwacja
    Quo Vadis: Rezerwacja
*/

exec get_user_reliability(1) ;
/*
    Użytkownik o id: 1 jest bardzo nierzetelny
*/

exec get_user_reliability(2) ;
/*
    Użytkownik o id: 2 nie oddał jeszcze żadnej książki
*/

/* Testy Wyzwalaczy */

select count(*) from PENALTIES_HISTORY where user_id = 1; -- równe 2

select * from ORDERS_HISTORY;
update orders_history set status = 'Zwrocona' where order_id = 3;

select count(*) from PENALTIES_HISTORY where user_id = 1; -- równe 3

select count(*) from COPIES natural join LIBRARIES where name = 'Biblioteka Narodowa'; -- 5
select count(*) from COPIES natural join LIBRARIES where name = 'Biblioteka Miejska'; -- 5

INSERT INTO BOOKS values(null, 'Kroniki', 1, 200, 1324567890123, 1835, 'poemat');
select count(*) from COPIES natural join LIBRARIES where name = 'Biblioteka Narodowa'; -- 6
select count(*) from COPIES natural join LIBRARIES where name = 'Biblioteka Miejska'; -- 5



/* Przykładowe zapytania do bazy danych */

--statystyka wypozyczen uzytkownikow
Select u.name name, u.surname surname,
       sum(case when o.status = 'Rezerwacja' then 1 else 0 end) reserved,
       sum(case when o.status = 'Wypozyczona' then 1 else 0 end) borrowed,
       sum(case when o.status = 'Zwrocona' then 1 else 0 end) returned,
       count(order_id) sum
from users u join orders_history o on u.user_id = o.user_id
Group by u.surname, u.name;
--ilosc egzemplarzy w bibliotekach danej ksiazki
Select l.name, count(c.copy_id)
from copies c join libraries l using(library_id) join books b on (c.book_id =b.book_id)
where b.title = 'Dziady'
group by l.name;
--Zarobione pieniądze z zapłaconych kar
Select Nvl(sum(p.value),0) money_from_penalties
from penalties p join penalties_history ph using(penalty_id)
where ph.is_paid=1;