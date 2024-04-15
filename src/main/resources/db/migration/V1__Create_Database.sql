CREATE TABLE accounts (
                          id SERIAL PRIMARY KEY,
                          account_number BIGINT UNIQUE NOT NULL
);

CREATE TABLE currencies (
                            id SERIAL PRIMARY KEY,
                            symbol VARCHAR(7) NOT NULL,
                            rate NUMERIC(10, 2) NOT NULL
);

CREATE TABLE limits (
                        id SERIAL PRIMARY KEY,
                        limit_amount NUMERIC(10, 2) NOT NULL,
                        limit_balance NUMERIC(10, 2) NOT NULL,
                        limit_datetime TIMESTAMP NOT NULL,
                        currency_short_name VARCHAR(3) NOT NULL,
                        category VARCHAR(20) NOT NULL,
                        account_id BIGINT REFERENCES accounts(id),
                        is_active BOOLEAN NOT NULL
);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              account_from BIGINT REFERENCES accounts(id),
                              account_to BIGINT REFERENCES accounts(id),
                              currency_shortname VARCHAR(3) NOT NULL,
                              sum NUMERIC(10, 2) NOT NULL,
                              expense_category VARCHAR(20) NOT NULL,
                              datetime TIMESTAMP NOT NULL,
                              limit_exceeded BOOLEAN NOT NULL
);