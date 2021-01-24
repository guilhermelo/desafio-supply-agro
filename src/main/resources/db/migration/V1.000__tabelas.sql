create table fazenda (
    id uuid primary key,
    nome varchar(200) unique not null,
    cnpj varchar(18) not null,
    cidade varchar(30),
    estado varchar(2),
    logradouro varchar(50)
);

create table talhao (
    id uuid primary key,
    codigo varchar(10) not null,
    area_em_hectare numeric(5,2) not null,
    safra integer not null,
    estimativa_produtividade numeric(5,2) not null,
    fazenda_id uuid not null,
    constraint fazenda_fk foreign key (fazenda_id) references fazenda(id)
);

create table evento (
    id uuid primary key,
    data timestamp with time zone not null,
    area_em_hectare numeric(5,2) not null,
    tipo varchar(12) not null,
    talhao_id uuid not null,
    constraint talhao_fk foreign key (talhao_id) references talhao(id)
);

create table colheita (
    id uuid primary key,
    data timestamp with time zone not null,
    area_colhida_em_hectare numeric(5,2) not null,
    peso_colhido_em_kg numeric(5,2) not null,
    talhao_id uuid not null,
    constraint talhao_fk foreign key (talhao_id) references talhao(id)
);

create table plantio (
    id uuid primary key,
    data timestamp with time zone not null,
    area_plantada_em_hectare numeric(5,2) not null,
    variedade varchar(50) not null,
    talhao_id uuid not null,
    constraint talhao_fk foreign key (talhao_id) references talhao(id)
);


