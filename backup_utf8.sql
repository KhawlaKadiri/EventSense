--
-- PostgreSQL database dump
--

-- Dumped from database version 17.6
-- Dumped by pg_dump version 17.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: events; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.events (
    id integer NOT NULL,
    title character varying(255) NOT NULL,
    description text,
    category character varying(100),
    location character varying(255),
    event_date timestamp without time zone NOT NULL,
    duration_minutes integer,
    price numeric(8,2),
    age_min integer,
    age_max integer,
    total_seats integer,
    available_seats integer,
    organizer_id integer,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    actors text,
    budget numeric(10,2)
);


ALTER TABLE public.events OWNER TO postgres;

--
-- Name: events_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.events_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.events_id_seq OWNER TO postgres;

--
-- Name: events_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.events_id_seq OWNED BY public.events.id;


--
-- Name: ticket; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ticket (
    id integer NOT NULL,
    eventid integer NOT NULL,
    pricepaid double precision NOT NULL,
    purchasedate timestamp without time zone,
    qrcode character varying(1000),
    seatnumber character varying(255),
    status character varying(255),
    userid integer NOT NULL
);


ALTER TABLE public.ticket OWNER TO postgres;

--
-- Name: ticket_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.ticket_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ticket_id_seq OWNER TO postgres;

--
-- Name: ticket_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.ticket_id_seq OWNED BY public.ticket.id;


--
-- Name: tickets; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tickets (
    id integer NOT NULL,
    event_id integer,
    user_id integer,
    purchase_date timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    seat_number character varying(10),
    qr_code character varying(255),
    status character varying(50) DEFAULT 'active'::character varying,
    price_paid numeric(8,2),
    user_name character varying(100),
    eventid integer,
    pricepaid double precision,
    purchasedate timestamp without time zone,
    qrcode character varying(255),
    seatnumber character varying(255),
    userid integer,
    username character varying(255)
);


ALTER TABLE public.tickets OWNER TO postgres;

--
-- Name: tickets_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tickets_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.tickets_id_seq OWNER TO postgres;

--
-- Name: tickets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tickets_id_seq OWNED BY public.tickets.id;


--
-- Name: user_event_interactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_event_interactions (
    id integer NOT NULL,
    user_id integer,
    event_id integer,
    action character varying(20),
    created_at timestamp without time zone DEFAULT now()
);


ALTER TABLE public.user_event_interactions OWNER TO postgres;

--
-- Name: user_event_interactions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_event_interactions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_event_interactions_id_seq OWNER TO postgres;

--
-- Name: user_event_interactions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_event_interactions_id_seq OWNED BY public.user_event_interactions.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    firstname character varying(255),
    lastname character varying(255),
    birthdaydate date,
    email text,
    cin character varying(255),
    role character varying(255),
    telephone character varying(255),
    pass_word character varying(255),
    preferred_categ text,
    preferred_locati text,
    budget_max integer,
    preferred_actors text,
    preferred_categories character varying(255),
    preferred_locations character varying(255),
    username character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: events id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events ALTER COLUMN id SET DEFAULT nextval('public.events_id_seq'::regclass);


--
-- Name: ticket id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket ALTER COLUMN id SET DEFAULT nextval('public.ticket_id_seq'::regclass);


--
-- Name: tickets id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets ALTER COLUMN id SET DEFAULT nextval('public.tickets_id_seq'::regclass);


--
-- Name: user_event_interactions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_event_interactions ALTER COLUMN id SET DEFAULT nextval('public.user_event_interactions_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: events; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.events (id, title, description, category, location, event_date, duration_minutes, price, age_min, age_max, total_seats, available_seats, organizer_id, created_at, updated_at, actors, budget) FROM stdin;
2	Jazz Night Updated	Concert jazz mis ├á jour	Music	Casablanca	2026-01-20 00:00:00	190	150.00	18	60	300	111	3	2026-01-06 12:02:43.918263	2026-01-06 12:02:43.918263	Local Jazz Band	20000.00
5	Soir├⌐e Th├⌐├ótre Jeune Public	Pi├¿ce de th├⌐├ótre interactive pour enfants.	Th├⌐├ótre	Maison de la Culture	2026-06-10 00:00:00	60	15.00	5	13	150	149	10	2026-01-06 12:02:43.918263	2026-01-06 12:02:43.918263	Children Theater Actors	35000.00
1	Festival Jazz d'Automne	Un festival de jazz avec des artistes internationaux.	Musique	Th├⌐├ótre National	2026-10-15 00:00:00	180	50.00	18	90	500	87	1	2026-01-06 12:02:43.918263	2026-01-06 12:02:43.918263	Various Jazz Artists	10000.00
4	Concert Pop Updated	Updated concert description	Music	Casablanca	2026-01-20 00:00:00	150	180.00	18	60	300	197	1	2026-01-06 12:02:43.918263	2026-01-06 12:02:43.918263	Ariana	0.00
6	Tech Conference 2026	Big tech event in Morocco	Technology	Rabat	2026-09-10 10:00:00	240	200.00	16	60	500	497	1	2026-05-13 19:07:37.908186	2026-05-13 19:07:37.908186	Elon Musk, AI Speakers	\N
\.


--
-- Data for Name: ticket; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ticket (id, eventid, pricepaid, purchasedate, qrcode, seatnumber, status, userid) FROM stdin;
\.


--
-- Data for Name: tickets; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tickets (id, event_id, user_id, purchase_date, seat_number, qr_code, status, price_paid, user_name, eventid, pricepaid, purchasedate, qrcode, seatnumber, userid, username) FROM stdin;
3	1	4	2026-01-13 08:52:31.959431	B03	QR-2026-EVT1-USER4-B03	active	100.00	\N	\N	\N	\N	\N	\N	\N	\N
8	4	7	2026-04-18 23:05:21.774813	A05	QR-2026-EVT4-USER7-A05	404	450.00	\N	\N	\N	\N	\N	\N	\N	\N
4	2	5	2026-01-13 08:52:31.959431	B15	QR-2026-EVT2-USER4-C04	RESERVED	125.00	\N	\N	\N	\N	\N	\N	\N	\N
9	1	1	2026-04-20 09:53:08.010947	A45	QR-2026-EVT1-USER1-G04	PAID	100.00	\N	\N	\N	\N	\N	\N	\N	\N
26	1	1	2026-04-21 14:49:03.485003	B12	QR-2026-EVT1-USER1-B12	PAID	50.00	\N	\N	\N	\N	\N	\N	\N	\N
31	2	1	2026-05-10 22:57:54.323919	A06	QR-2026-EVT2-USERUSER1-A06	PAID	600.00	\N	\N	\N	\N	\N	\N	\N	\N
32	5	1	2026-05-10 23:01:12.668804	A-23	QR-2026-EVT5-USERSALMA-A-23	PAID	15.00	Salma	\N	\N	\N	\N	\N	\N	\N
34	\N	\N	2026-05-11 09:26:36.840386	\N	\N	PAID	\N	\N	1	50	2026-05-11 09:26:36.759402	QR-2026-EVT1-USER2-A-28	A-28	2	2
37	2	1	2026-05-11 09:47:04.893457	A-28	QR-2026-EVT2-USERYOUSSEF-A-28	PAID	150.00	Youssef	\N	\N	\N	\N	\N	\N	\N
39	2	1	2026-05-11 12:07:06.167536	A76	QR-2026-EVT2-USERKHAWLA-A76	PAID	800.00	Khawla	\N	\N	\N	\N	\N	\N	\N
38	1	1	2026-05-11 09:51:02.446208	A-77	QR-2026-EVT1-USERYOUSSEF-A-34	PAID	50.00	Youssef	\N	\N	\N	\N	\N	\N	\N
42	1	3	2026-05-15 10:47:29.415798	A2	QR-2026-EVT1-USERSARA-A2	NOT_FULLY_PAID	20.00	Sara	\N	\N	\N	\N	\N	\N	\N
44	1	3	2026-06-03 10:46:55.823534	A72	QR-2026-EVT1-USERSARABENALI-A72	PAID	50.00	Sara Benali	\N	\N	\N	\N	\N	\N	\N
45	4	8	2026-06-03 10:56:30.848055	A08	QR-2026-EVT4-USERUSER8-A08	NOT_FULLY_PAID	0.00	USER8	\N	\N	\N	\N	\N	\N	\N
46	4	4	2026-06-03 11:15:03.468185	B-456	QR-2026-EVT4-USERSIHAMBOUZAGRAR-B-456	PAID	180.00	Siham Bouzagrar	\N	\N	\N	\N	\N	\N	\N
47	6	4	2026-06-03 12:25:41.048784	K-100	QR-2026-EVT6-USERSIHAMBOUZAGRAR-K-100	PAID	200.00	Siham Bouzagrar	\N	\N	\N	\N	\N	\N	\N
48	6	4	2026-06-03 13:19:32.755667	Y-901	QR-2026-EVT6-USERSIHAMBOUZAGRAR-Y-901	PAID	200.00	Siham Bouzagrar	\N	\N	\N	\N	\N	\N	\N
49	6	9	2026-06-03 13:29:45.037103	S-003	QR-2026-EVT6-USERSALMAOUMARI-S-003	PAID	200.00	Salma Oumari	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Data for Name: user_event_interactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_event_interactions (id, user_id, event_id, action, created_at) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, firstname, lastname, birthdaydate, email, cin, role, telephone, pass_word, preferred_categ, preferred_locati, budget_max, preferred_actors, preferred_categories, preferred_locations, username) FROM stdin;
8	khawla	kadiri	\N	khawlakadiri@gmail.com	AA000000	ROLE_ADMIN	0600000000	$2a$10$HYDBxreBdPwfzQMLSVhlAurAnRrxLVn8iGCgSu0yQ.sxmQRIRdOYC	\N	\N	\N	\N	\N	\N	\N
4	Siham	Bouzagrar	2000-06-10	SihamBouzagrar@gmail.com	A12345678	ROLE_USER	0667895432	$2a$10$2OUcr3zD/lftCTwgSGtqbepbnUpacD3canuqirACYDmlIti9H1oTu	Rap	\N	500	Eminem	Rap	\N	Siham.Bouzagrar
3	Sara	Benali	1990-01-01	sara@test.com	CD98765	ROLE_USER	0611223344	$2a$10$wnYjTaNhVrcHGCfBMM99Def5m7SfDNijkT1EoPqjA1TOuabvpiCle	Jazz	\N	500	Gracie A brams	Jazz	Casablanca	Sara.Benali
9	Salma	Oumari	\N	salmaomari@gmail.com	A123457711	ROLE_USER	0754329421	$2a$10$7Qym17Fs5hflOL4i4e2/hegQrswfkg26E0d8WXvddvk5B4j2zC6je	\N	\N	\N	\N	\N	\N	\N
\.


--
-- Name: events_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.events_id_seq', 8, true);


--
-- Name: ticket_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.ticket_id_seq', 1, false);


--
-- Name: tickets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tickets_id_seq', 49, true);


--
-- Name: user_event_interactions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_event_interactions_id_seq', 1, false);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 9, true);


--
-- Name: events events_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.events
    ADD CONSTRAINT events_pkey PRIMARY KEY (id);


--
-- Name: ticket ticket_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_qr_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_qr_code_key UNIQUE (qr_code);


--
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: user_event_interactions user_event_interactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_event_interactions
    ADD CONSTRAINT user_event_interactions_pkey PRIMARY KEY (id);


--
-- Name: users users_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: tickets tickets_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tickets
    ADD CONSTRAINT tickets_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id);


--
-- Name: user_event_interactions user_event_interactions_event_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_event_interactions
    ADD CONSTRAINT user_event_interactions_event_id_fkey FOREIGN KEY (event_id) REFERENCES public.events(id);


--
-- PostgreSQL database dump complete
--

