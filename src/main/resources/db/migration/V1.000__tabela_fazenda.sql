create table fazenda (
    id uuid primary key,
    nome varchar(200) unique,
    cnpj varchar(18),
    cidade varchar(30),
    estado varchar(2),
    logradouro varchar(50)
);