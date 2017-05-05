CREATE TABLE public.apartment_list (
id serial NOT NULL,
price text,
description text,
url varchar(255),
CONSTRAINT apartment_list_pkey PRIMARY KEY (id)
) ;
