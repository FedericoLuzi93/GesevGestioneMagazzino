CREATE TABLE TIPO_DERRATE
(
	CODICE SERIAL PRIMARY KEY,
	DESCRIZIONE VARCHAR(256) NOT NULL -- Fresco / Carne / Congelati / Scatolati /Bevande
);

CREATE TABLE DERRATA
(
	DERRATA_ID SERIAL,
	DESCRIZIONE_DERRATA VARCHAR(256) NOT NULL,
	UNITA_MISURA VARCHAR(5),
	PREZZO DECIMAL(10,2),
	GIACENZA INT,
	DATA_AGGIORNAMENTO_GIACENZA TIMESTAMP,
	QUANTITA_MIMINA INT,
	CODICE_MENSA INT,
	TIPO_DERRATA INT REFERENCES TIPO_DERRATE(CODICE),
	PRIMARY KEY(DERRATA_ID)

);

CREATE TABLE PRELEVAMENTO_MENSA
(
	NUMERO_BUONO SERIAL PRIMARY KEY,
	DATA_PRELEVAMENTO_MENSA TIMESTAMP
);

CREATE TABLE dettaglio_prelevamento (
	numero_buono int4 NOT NULL,
	data_dettaglio_prelevamento timestamp NULL,
	derrata int4 NULL,
	quantita int4 NULL,
	dettaglio_prelevamento_id serial4 NOT NULL,
	CONSTRAINT dettaglio_prelevamento_pkey PRIMARY KEY (dettaglio_prelevamento_id)
);

ALTER TABLE dettaglio_prelevamento ADD CONSTRAINT dettaglio_prelevamento_derrata_fkey FOREIGN KEY (derrata) REFERENCES derrata(derrata_id);
ALTER TABLE dettaglio_prelevamento ADD CONSTRAINT dettaglio_prelevamento_numero_buono_fkey FOREIGN KEY (numero_buono) REFERENCES prelevamento_mensa(numero_buono);

CREATE TABLE TIPO_MOVIMENTO
(
	CODICE SERIAL PRIMARY KEY,
	TIPO_DESCRIZIONE VARCHAR(256), -- PRELEVAMENTO/APPROVIGIONAMENTO/GIACENZA
	SEGNO VARCHAR(1)
);

CREATE TABLE ente 
(
	codice_aced varchar(256) NOT NULL,
	ente_riferimento int4 NULL,
	mensa_fk int4 NULL,
	descrizione_ente varchar(256) NULL,
	id_ente serial4 NOT NULL,
	CONSTRAINT ente_pkey PRIMARY KEY (id_ente)
);

ALTER TABLE public.ente ADD CONSTRAINT ente_fk FOREIGN KEY (ente_riferimento) REFERENCES public.ente(id_ente);
ALTER TABLE public.ente ADD CONSTRAINT ente_mensa_fk_fkey FOREIGN KEY (mensa_fk) REFERENCES public.mensa(codice_mensa);

CREATE TABLE FORNITORE
(
	CODICE SERIAL PRIMARY KEY,
	DESCRIZIONE VARCHAR(256) NOT NULL
);

CREATE TABLE testata_movimento 
(
	numero_progressivo serial4 NOT NULL,
	data_testata_movimento timestamp NULL,
	tipo_movimento int4 NULL,
	codice_fornitore int4 NULL,
	codice_ente int4 NULL,
	num_ordine_lavoro int4 NULL,
	nota varchar(256) NULL,
	totale_importo numeric(10, 2) NULL,
	utente_operatore varchar(256) NULL,
	CONSTRAINT testata_movimento_pkey PRIMARY KEY (numero_progressivo)
);

ALTER TABLE public.testata_movimento ADD CONSTRAINT ente_fk FOREIGN KEY (codice_ente) REFERENCES ente(id_ente);
ALTER TABLE public.testata_movimento ADD CONSTRAINT testata_movimento_codice_fornitore_fkey FOREIGN KEY (codice_fornitore) REFERENCES fornitore(codice);
ALTER TABLE public.testata_movimento ADD CONSTRAINT testata_movimento_tipo_movimento_fkey FOREIGN KEY (tipo_movimento) REFERENCES tipo_movimento(codice);

CREATE TABLE DETTAGLIO_MOVIMENTO
(
	ID_DETTAGLIO_MOVIMENTO SERIAL PRIMARY KEY,
	NUM_PROGRESSIVO INT NOT NULL REFERENCES TESTATA_MOVIMENTO(NUMERO_PROGRESSIVO),
	DATA_DETTAGLIO_MOVIMENTO TIMESTAMP,
	DERRATA INT REFERENCES DERRATA(DERRATA_ID),
	QUANTITA_RICHIESTA DECIMAL(5,2),
	QUANTITA_EFFETTIVA DECIMAL(5,2),
	PREZZO_UNITARIO DECIMAL(5,2),
	TOTALE_VALORE DECIMAL (5,2)
);

