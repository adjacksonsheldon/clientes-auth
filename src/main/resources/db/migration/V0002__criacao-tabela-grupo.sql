CREATE TABLE public.grupos (
	id int8 NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	nome varchar(120) NOT NULL,
	CONSTRAINT grupos_pkey PRIMARY KEY (id)
);

CREATE TABLE public.usuarios_grupos (
	usuario_id int8 NOT NULL,
	grupo_id int8 NOT NULL,
	CONSTRAINT usuarios_grupos_pkey PRIMARY KEY (usuario_id, grupo_id)
);

ALTER TABLE public.usuarios_grupos ADD CONSTRAINT usuarios_grupos_grupo_id_fk FOREIGN KEY (grupo_id) REFERENCES public.grupos(id);
ALTER TABLE public.usuarios_grupos ADD CONSTRAINT usuarios_grupos_usuario_id_fk_ FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id);