CREATE SEQUENCE sale.PRODUCT_SEQ;
CREATE SEQUENCE sale.CATEGORY_SEQ;

CREATE TABLE sale.TB_CATEGORY(
                  id INTEGER NOT NULL,
                  description varchar(45) NOT NULL,
                  cod varchar(10) NOT NULL,
                  status boolean,
                  CONSTRAINT category_pk PRIMARY KEY (id)
);

CREATE TABLE sale.TB_PRODUCT (
                id INTEGER NOT NULL,
                id_category INTEGER NOT NULL,
                description varchar(45) not null,
                manufacturer varchar(60) not null,
                price decimal(18,2),
                CONSTRAINT product_pk PRIMARY KEY (id),
                CONSTRAINT category_fk FOREIGN KEY (id_category) REFERENCES sale.TB_CATEGORY (id)
);