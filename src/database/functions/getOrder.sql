create function public."getOrder"(IN "orderid" bigint)
RETURNS TABLE (
	zamowienie_id int,
	klient_id int,
	data timestamp,
	pozycja_id int,
	produkt_id int,
	ilosc int,
	imie varchar,
	nazwisko varchar,
	adres text,
	telefon varchar
) AS 
$func$
begin
	RETURN QUERY
 		SELECT 
			sklep.public."Zamowienie".zamowienie_id,
 			sklep.public."Zamowienie".klient_id,
 			sklep.public."Zamowienie".data,
 			sklep.public."Pozycja".pozycja_id, 
 			sklep.public."Pozycja".produkt_id,
 			sklep.public."Pozycja".ilosc,
 			sklep.public."Klient".imie,
 			sklep.public."Klient".nazwisko,
 			sklep.public."Klient".adres,
 			sklep.public."Klient".telefon
			FROM sklep.public."Zamowienie" 
			INNER JOIN sklep.public."Pozycja" ON "Zamowienie".zamowienie_id = "Pozycja".zamowienie_id
			INNER JOIN sklep.public."Klient"  ON "Zamowienie".klient_id = "Klient".klient_id
		WHERE
			sklep.public."Zamowienie".zamowienie_id = orderid;
end;
$func$ LANGUAGE 'plpgsql';