INSERT INTO "role" ("id", "name") VALUES
('13fbb108-1b6e-46f5-9e0d-d78f4bca1efc', 'ROLE_EMPLOYEE'),
('f376a0e2-d26c-4c66-beb9-e5d5d63cd687', 'ROLE_USER'),
('ca704dd5-067d-4847-a9b2-06c91c4b8744', 'ROLE_MANAGER');

INSERT INTO "user" ("id", "address_line1", "day_of_birth", "email", "last_name", "name", "phone", "postal_code", "town", "role_id") VALUES
(1, 'Biblioteczna', '1996-01-19', 'admin@biblioteka.com', 'admin', 'admin', '512155211', '41-800', 'Zabrze', '13fbb108-1b6e-46f5-9e0d-d78f4bca1efc'),
(2, 'Cisowa 8B/8', '1998-12-20', 'rudziak555@gmail.com', 'Podstada', 'Ewa', '', '41-800', 'Zabrze', 'f376a0e2-d26c-4c66-beb9-e5d5d63cd687'),
(3, 'Jagodowa', '1983-02-18', 'jan@biblioteka.com', 'Kowalski', 'Jan', '721507857', '44-100', 'Gliwice', '13fbb108-1b6e-46f5-9e0d-d78f4bca1efc'),
(4, 'Porzeczkowa 2/5', '1977-04-12', 'andrzej@biblioteka.com', 'Cuber', 'Andrzej', '721573821', '41-800', 'Zabrze', '13fbb108-1b6e-46f5-9e0d-d78f4bca1efc'),
(5, 'Malinowa8/5', '1995-06-20', 'paulina172@onet.pl', 'Stefańczyk', 'Paulina', '', '41-800', 'Zabrze', 'f376a0e2-d26c-4c66-beb9-e5d5d63cd687'),
(6, 'Wolnośc 323/7', '1973-08-15', 'stefan73@gmail.com', 'Zatylny', 'Stefan', '', '41-800', 'Zabrze', 'f376a0e2-d26c-4c66-beb9-e5d5d63cd687'),
(7, 'Brzoskwiniowa 14/3', '1999-09-18', 'artur55555@o2.pl', 'Kopek', 'Artur', '', '41-800', 'Zabrze', 'f376a0e2-d26c-4c66-beb9-e5d5d63cd687'),
(8, 'Arbuzowa 15/9', '1949-01-19', 'porzeczka721@wp.pl', 'Czerwony', 'Celina', '', '41-800', 'Zabrze', 'f376a0e2-d26c-4c66-beb9-e5d5d63cd687'),
(9, 'Biblioteczna', '1996-01-19', 'manager@biblioteka.com', 'manager', 'manager', '512155211', '41-800', 'Zabrze', 'ca704dd5-067d-4847-a9b2-06c91c4b8744');
