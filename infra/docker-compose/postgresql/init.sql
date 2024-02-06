CREATE TABLE public.estabelecimento
(
    id   varchar(36) NOT NULL,
    nome varchar(255) NULL,
    CONSTRAINT estabelecimento_pk PRIMARY KEY (id)
);

CREATE TABLE public.device
(
    id   varchar(36) NULL,
    nome varchar(255) NULL,
    CONSTRAINT device_pk PRIMARY KEY (id)
);

CREATE TABLE public.device_estabelecimento
(
    id                 varchar(36) NULL,
    id_device          varchar(36) NULL,
    id_estabelecimento varchar(36) NULL,
    CONSTRAINT device_estabelecimento_pk PRIMARY KEY (id),
    CONSTRAINT device_device_fk FOREIGN KEY (id_device) REFERENCES public.device (id),
    CONSTRAINT device_estabelecimento_fk FOREIGN KEY (id_estabelecimento) REFERENCES public.estabelecimento (id)
);
