CREATE TABLE device_estabelecimento
(
    id                 varchar(36) NULL,
    id_device          varchar(36) NULL,
    id_estabelecimento varchar(36) NULL,
    CONSTRAINT device_estabelecimento_pk PRIMARY KEY (id),
    CONSTRAINT device_device_fk FOREIGN KEY (id_device) REFERENCES device (id),
    CONSTRAINT device_estabelecimento_fk FOREIGN KEY (id_estabelecimento) REFERENCES estabelecimento (id)
);
