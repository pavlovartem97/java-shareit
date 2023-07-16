DROP TABLE IF EXISTS sh_user CASCADE;
DROP TABLE IF EXISTS sh_item CASCADE;
DROP TABLE IF EXISTS sh_booking CASCADE;

CREATE TABLE IF NOT EXISTS sh_user (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL UNIQUE,
  CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS sh_item (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(5000) NOT NULL,
  available BOOLEAN DEFAULT TRUE,
  user_id BIGINT NOT NULL,
  CONSTRAINT item_pk PRIMARY KEY (id),
  CONSTRAINT item_user_fk foreign key (user_id) references sh_user
);

CREATE TABLE IF NOT EXISTS sh_booking (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME NOT NULL,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  status VARCHAR(15) NOT NULL,
  CONSTRAINT booking_pk PRIMARY KEY (id),
  CONSTRAINT booking_user_fk foreign key (booker_id) references sh_user,
  CONSTRAINT booking_item_fk foreign key (item_id) references sh_item
);