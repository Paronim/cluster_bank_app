CREATE SCHEMA app_dbi;

CREATE TABLE app_dbi.account (
    id bigint NOT NULL,
    currency text NOT NULL,
    balance numeric DEFAULT 0.00 NOT NULL,
    client_id bigint NOT NULL,
    name text NOT NULL,
    type text NOT NULL
);

ALTER TABLE app_dbi.account OWNER TO postgres;

ALTER TABLE app_dbi.account ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME app_dbi.accounts_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE app_dbi.client (
    id bigint NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL
);

ALTER TABLE app_dbi.client OWNER TO postgres;

ALTER TABLE app_dbi.client ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME app_dbi.clients_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

CREATE TABLE app_dbi.transaction (
    id bigint NOT NULL,
    amount numeric NOT NULL,
    transaction_type text NOT NULL,
    created_at timestamp without time zone NOT NULL,
    main_account_id bigint NOT NULL,
    recipient_account_id bigint
);

ALTER TABLE app_dbi.transaction OWNER TO postgres;

ALTER TABLE app_dbi.transaction ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME app_dbi.transactions_id_seq
    START WITH 10000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);

ALTER TABLE ONLY app_dbi.account
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (id);

ALTER TABLE app_dbi.account
    ADD CONSTRAINT check_type CHECK ((type = ANY (ARRAY['main'::text, 'secondary'::text]))) NOT VALID;

ALTER TABLE app_dbi.account
    ADD CONSTRAINT check_type_currency CHECK ((currency = ANY (ARRAY['RUB'::text, 'USD'::text]))) NOT VALID;

ALTER TABLE ONLY app_dbi.client
    ADD CONSTRAINT clients_pkey PRIMARY KEY (id);

ALTER TABLE app_dbi.transaction
    ADD CONSTRAINT transaction_type_check CHECK ((transaction_type = ANY (ARRAY['DEPOSIT'::text, 'WITHDRAW'::text, 'TRANSFER'::text]))) NOT VALID;

ALTER TABLE ONLY app_dbi.transaction
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (id);

ALTER TABLE ONLY app_dbi.account
    ADD CONSTRAINT client_id FOREIGN KEY (client_id) REFERENCES app_dbi.client(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;

ALTER TABLE ONLY app_dbi.transaction
    ADD CONSTRAINT main_account_id FOREIGN KEY (main_account_id) REFERENCES app_dbi.account(id) ON UPDATE CASCADE ON DELETE CASCADE NOT VALID;

ALTER TABLE ONLY app_dbi.transaction
    ADD CONSTRAINT recipient_account_id FOREIGN KEY (recipient_account_id) REFERENCES app_dbi.account(id) ON DELETE CASCADE NOT VALID;
