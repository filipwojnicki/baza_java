PGDMP                         z           sklep    14.2 (Debian 14.2-1.pgdg110+1)    14.1                 0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16384    sklep    DATABASE     Y   CREATE DATABASE sklep WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.utf8';
    DROP DATABASE sklep;
                postgres    false            �            1259    16400    Klient    TABLE     �   CREATE TABLE public."Klient" (
    klient_id integer NOT NULL,
    imie character varying(255) NOT NULL,
    nazwisko character varying(255) NOT NULL,
    adres text,
    telefon character varying(20)
);
    DROP TABLE public."Klient";
       public         heap    postgres    false            �            1259    16399    Klient_klient_id_seq    SEQUENCE     �   CREATE SEQUENCE public."Klient_klient_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public."Klient_klient_id_seq";
       public          postgres    false    210                       0    0    Klient_klient_id_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public."Klient_klient_id_seq" OWNED BY public."Klient".klient_id;
          public          postgres    false    209            �            1259    16419    Pozycja    TABLE     �   CREATE TABLE public."Pozycja" (
    pozycja_id integer NOT NULL,
    produkt_id integer NOT NULL,
    ilosc integer DEFAULT 0 NOT NULL,
    zamowienie_id integer NOT NULL
);
    DROP TABLE public."Pozycja";
       public         heap    postgres    false            �            1259    16418    Pozycja_pozycja_id_seq    SEQUENCE     �   CREATE SEQUENCE public."Pozycja_pozycja_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public."Pozycja_pozycja_id_seq";
       public          postgres    false    214                       0    0    Pozycja_pozycja_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public."Pozycja_pozycja_id_seq" OWNED BY public."Pozycja".pozycja_id;
          public          postgres    false    213            �            1259    16427    Produkt    TABLE     �   CREATE TABLE public."Produkt" (
    produkt_id integer NOT NULL,
    nazwa character varying(255) NOT NULL,
    opis character varying(255) NOT NULL,
    cena numeric(10,2) NOT NULL,
    ilosc integer DEFAULT 0
);
    DROP TABLE public."Produkt";
       public         heap    postgres    false            �            1259    16426    Produkt_produkt_id_seq    SEQUENCE     �   CREATE SEQUENCE public."Produkt_produkt_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public."Produkt_produkt_id_seq";
       public          postgres    false    216                       0    0    Produkt_produkt_id_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public."Produkt_produkt_id_seq" OWNED BY public."Produkt".produkt_id;
          public          postgres    false    215            �            1259    16409 
   Zamowienie    TABLE     �   CREATE TABLE public."Zamowienie" (
    zamowienie_id integer NOT NULL,
    klient_id integer NOT NULL,
    data timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);
     DROP TABLE public."Zamowienie";
       public         heap    postgres    false            �            1259    16408    Zamowienie_zamowienie_id_seq    SEQUENCE     �   CREATE SEQUENCE public."Zamowienie_zamowienie_id_seq"
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 5   DROP SEQUENCE public."Zamowienie_zamowienie_id_seq";
       public          postgres    false    212                       0    0    Zamowienie_zamowienie_id_seq    SEQUENCE OWNED BY     a   ALTER SEQUENCE public."Zamowienie_zamowienie_id_seq" OWNED BY public."Zamowienie".zamowienie_id;
          public          postgres    false    211            n           2604    16403    Klient klient_id    DEFAULT     x   ALTER TABLE ONLY public."Klient" ALTER COLUMN klient_id SET DEFAULT nextval('public."Klient_klient_id_seq"'::regclass);
 A   ALTER TABLE public."Klient" ALTER COLUMN klient_id DROP DEFAULT;
       public          postgres    false    210    209    210            q           2604    16422    Pozycja pozycja_id    DEFAULT     |   ALTER TABLE ONLY public."Pozycja" ALTER COLUMN pozycja_id SET DEFAULT nextval('public."Pozycja_pozycja_id_seq"'::regclass);
 C   ALTER TABLE public."Pozycja" ALTER COLUMN pozycja_id DROP DEFAULT;
       public          postgres    false    213    214    214            s           2604    16430    Produkt produkt_id    DEFAULT     |   ALTER TABLE ONLY public."Produkt" ALTER COLUMN produkt_id SET DEFAULT nextval('public."Produkt_produkt_id_seq"'::regclass);
 C   ALTER TABLE public."Produkt" ALTER COLUMN produkt_id DROP DEFAULT;
       public          postgres    false    216    215    216            o           2604    16412    Zamowienie zamowienie_id    DEFAULT     �   ALTER TABLE ONLY public."Zamowienie" ALTER COLUMN zamowienie_id SET DEFAULT nextval('public."Zamowienie_zamowienie_id_seq"'::regclass);
 I   ALTER TABLE public."Zamowienie" ALTER COLUMN zamowienie_id DROP DEFAULT;
       public          postgres    false    211    212    212            	          0    16400    Klient 
   TABLE DATA           M   COPY public."Klient" (klient_id, imie, nazwisko, adres, telefon) FROM stdin;
    public          postgres    false    210   �#                 0    16419    Pozycja 
   TABLE DATA           Q   COPY public."Pozycja" (pozycja_id, produkt_id, ilosc, zamowienie_id) FROM stdin;
    public          postgres    false    214   �#                 0    16427    Produkt 
   TABLE DATA           I   COPY public."Produkt" (produkt_id, nazwa, opis, cena, ilosc) FROM stdin;
    public          postgres    false    216   �#                 0    16409 
   Zamowienie 
   TABLE DATA           F   COPY public."Zamowienie" (zamowienie_id, klient_id, data) FROM stdin;
    public          postgres    false    212   �#                  0    0    Klient_klient_id_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public."Klient_klient_id_seq"', 1, false);
          public          postgres    false    209                       0    0    Pozycja_pozycja_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public."Pozycja_pozycja_id_seq"', 1, false);
          public          postgres    false    213                       0    0    Produkt_produkt_id_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public."Produkt_produkt_id_seq"', 1, false);
          public          postgres    false    215                       0    0    Zamowienie_zamowienie_id_seq    SEQUENCE SET     M   SELECT pg_catalog.setval('public."Zamowienie_zamowienie_id_seq"', 1, false);
          public          postgres    false    211            v           2606    16407    Klient Klient_pkey 
   CONSTRAINT     [   ALTER TABLE ONLY public."Klient"
    ADD CONSTRAINT "Klient_pkey" PRIMARY KEY (klient_id);
 @   ALTER TABLE ONLY public."Klient" DROP CONSTRAINT "Klient_pkey";
       public            postgres    false    210            z           2606    16425    Pozycja Pozycja_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public."Pozycja"
    ADD CONSTRAINT "Pozycja_pkey" PRIMARY KEY (pozycja_id);
 B   ALTER TABLE ONLY public."Pozycja" DROP CONSTRAINT "Pozycja_pkey";
       public            postgres    false    214            |           2606    16435    Produkt Produkt_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public."Produkt"
    ADD CONSTRAINT "Produkt_pkey" PRIMARY KEY (produkt_id);
 B   ALTER TABLE ONLY public."Produkt" DROP CONSTRAINT "Produkt_pkey";
       public            postgres    false    216            x           2606    16416    Zamowienie Zamowienie_pkey 
   CONSTRAINT     g   ALTER TABLE ONLY public."Zamowienie"
    ADD CONSTRAINT "Zamowienie_pkey" PRIMARY KEY (zamowienie_id);
 H   ALTER TABLE ONLY public."Zamowienie" DROP CONSTRAINT "Zamowienie_pkey";
       public            postgres    false    212            	      x������ � �            x������ � �            x������ � �            x������ � �     