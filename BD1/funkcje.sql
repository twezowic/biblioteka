/* userhistory  wyypisuje wszystkie ksi¹¿ki które wypo¿yczy³ odda³ lub zarezerwowa³*/

create or replace procedure user_history(p_userid NUMBER) as 
BEGIN
FOR r_ord IN (SELECT * 
FROM orders_history natural join orders natural join copies natural join books )
LOOP
if r_ord.user_id=p_userid Then
dbms_output.put_line( r_ord.title || ': '|| r_ord.status );
END IF;
END LOOP;
END;
/
/*
wyswietla jak bardzo rzetelny jest uzytkownik - ile punktow karnych / ilosc oddanych, wyrzuca blad jesli dany uzytkownik nie oddal jeszcze zadnej ksiazki.
*/
CREATE OR replace PROCEDURE get_user_reliability(p_user_id NUMBER)
AS
    v_reliable NUMBER := 0.2;
    v_unreliable NUMBER := 1;
    v_very_unreliable NUMBER := 2;
    v_penalties_sum NUMBER;
    v_count_returned NUMBER;

    v_penalties_returned_ratio NUMBER;
     NOT_ENOUGH_DATA EXception;
BEGIN
    SELECT count(*) into v_count_returned from orders_history where p_user_id = user_id and status='Zwrocona';
 
    SELECT sum(p.VALUE) INTO v_penalties_sum from PENALTIES_HISTORY ph join PENALTIES p on (p.penalty_id = ph.penalty_id)
    where ph.USER_ID = p_user_id;
    IF v_count_returned = 0 THEN
        raise NOT_ENOUGH_DATA;
    end if;

    v_penalties_returned_ratio := v_penalties_sum / v_count_returned;

    IF v_penalties_returned_ratio > v_very_unreliable THEN
        dbms_output.put_line ('Uzytkownik o id: ' || p_user_id || ' jest bardzo nierzetelny');
    ELSIF v_penalties_returned_ratio > v_very_unreliable THEN
        dbms_output.put_line ('Uzytkownik o id: ' || p_user_id || ' jest nierzetelny');
    ELSE
        dbms_output.put_line ('Uzytkownik o id: ' || p_user_id || ' jest rzetelny');
    END IF;
    EXCEPTION
        WHEN NOT_ENOUGH_DATA then
            dbms_output.put_line ('Uzytkownik o id: ' || p_user_id || ' nie oddal jeszcze zadnej ksiazki');
        Raise;
END;

/
/*zwraca id u¿ytkownika maj¹c jego nazwe*/
create or replace function userid_from_name(username varchar2) return Number as 
userid NUMBER;
BEGIN

select user_id into userid from users_data join users using(user_data_id)
                     where login=username ;
return userid;

END;
/
/*oblicza procent ruchu(ksi¹zek wypozyczonych) w danej bibliotece ze wszystkich bibliotek*/
create or replace function calculte_library_used_percentage(p_lib_id Number) return Number as 
v_all_books_ordered NUMBER;
v_our_books_ordered Number;
BEGIN

select count(*) into v_all_books_ordered from orders ;
select count(*) into v_our_books_ordered from orders natural join copies where library_id=p_lib_id;
        return v_our_books_ordered/v_all_books_ordered*100;

END;



/



 /*dodaje kare za przetrzymanie przy oddaniu ksiazki*/
CREATE OR REPLACE TRIGGER automatic_penalties
AFTER UPDATE OF status ON orders_history
For each row
when (old.status='Wypozyczona' and new.status='Zwrocona')
DECLARE
v_date_start DATE ;
v_time_of_borowing Number :=14;
PRAGMA AUTONOMOUS_TRANSACTION;
BEGIN
select date_borrow into v_date_start from orders where order_id=:old.order_id;
if SYSDATE-v_date_start>=v_time_of_borowing then
 INSERT INTO Penalties_history Values(null,1,:old.user_ID,0);
end if;
commit;
END automatic_penalties;
/ /*dodaje jedna kopie do biblioteki narodowej dla ka¿dej nowej ksiazki*/
CREATE OR REPLACE TRIGGER add_book
AFTER insert ON Books
for each row
DECLARE
l_id Number;
BEGIN
 select library_id into l_id from libraries where name='Biblioteka Narodowa';
 INSERT INTO Copies Values(null,l_id,:new.book_id,1);
END add_book;