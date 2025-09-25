-- Schema belirle
SET search_path TO public;

-- Roller
CREATE TABLE public.roles (
  id SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
);

-- Kullanıcılar
CREATE TABLE public.users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE,
  enabled BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT now()
);

-- Kullanıcı-Roller ilişkisi
CREATE TABLE public.user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE
);

-- Örnek roller
INSERT INTO public.roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');
