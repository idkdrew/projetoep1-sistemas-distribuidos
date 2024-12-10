CREATE DATABASE sistemasDistribuidos;

CREATE TABLE IF NOT EXISTS public.users
(
    ra character varying(7) COLLATE pg_catalog."default" NOT NULL,
    nome character varying(50) COLLATE pg_catalog."default" NOT NULL,
    senha character varying(20) COLLATE pg_catalog."default" NOT NULL,
    isadmin boolean NOT NULL DEFAULT false,
    CONSTRAINT "Usuario_pkey" PRIMARY KEY (ra)
    );

INSERT INTO users(ra, nome, senha, isadmin)
VALUES ('2524090','ANDREW', 'admin', true);