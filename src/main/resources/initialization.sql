create table currencies
(
    id        INTEGER
        primary key autoincrement,
    code      TEXT,
    full_name TEXT,
    sign      TEXT
);

create unique index code_index
    on currencies (code);

create table exchange_rates
(
    id                 INTEGER
        primary key autoincrement,
    base_currency_id   INTEGER not null
        references currencies,
    target_currency_id INTEGER not null
        references currencies,
    rate               REAL
);

create unique index unique_pair
    on exchange_rates (base_currency_id, target_currency_id);

insert into currencies (code, full_name, sign)
values  ('USD', 'US Dollar', '$'),
        ('AUD', 'Australian Dollar', 'A$'),
        ('EUR', 'Euro', '€'),
        ('RUB', 'Russian Ruble', '₽'),
        ('XOF', 'CFA Franc BCEAO', '₣'),
        ('JPY', 'Yen', '¥'),
        ('XCD', 'Eastern Caribbean Dollar', 'EC$');

insert into exchange_rates (base_currency_id, target_currency_id, rate)
values  ( 1, 3, 0.8532),
        (1, 4, 75.53),
        (1, 2, 1.4),
        (2, 3, 0.6102),
        (5, 4, 0.1349),
        (1, 5, 559.82),
        ( 1, 12, 159.36),
        (13, 4, 27.97),
        (13, 3, 0.316),
        (12, 1, 0.006275);