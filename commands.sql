select * from usuario;
create table autor(
      id uuid not null primary key,
      nome varchar(100) not null,
      data_nascimento date not null,
      nacionalidade varchar(50) not null,
      data_cadastro timestamp,
      data_atualizacao timestamp,
      id_usuario uuid references usuario(id)
);

CREATE TABLE client(
   id uuid not null primary key,
    client_id varchar(150) not null,
    client_secret varchar(400) not null,
    redirect_uri varchar(200) not null,
    scope varchar(50)
)

create table livro(
      id uuid not null primary key,
      isbn varchar(20) not null,
      titulo varchar(150) not null,
      genero varchar(30) not null,
      data_publicacao date not null,
      preco numeric(18,2),
      id_autor uuid references autor(id),
      data_cadastro timestamp,
      data_atualizacao timestamp,
      id_usuario uuid references usuario(id)
);



drop table usuario;

alter table usuario add column email varchar(150) not null -- não pode se já existir user

create table usuario(
    id uuid not null primary key,
    login varchar(20) not null unique,
    senha varchar(300) not null,
--     email varchar(150) not null,
    roles varchar[]
)