-- Base de Datos Microservicio Usuarios y Autenticación

CREATE DATABASE IF NOT EXISTS deviaje_users_auth;

USE deviaje_users_auth;

-- Tabla de Usuarios
CREATE TABLE users (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50),
                       last_name VARCHAR(50),
                       phone VARCHAR(20),
                       birth_date DATE,
                       active BOOLEAN DEFAULT TRUE,
                       avatar_url VARCHAR(255),
                       created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_user INT,
                       last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       last_updated_user INT
);

CREATE TABLE passports (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           user_id INT NOT NULL,
                           passport_number VARCHAR(20) NOT NULL,
                           expiry_date DATE,
                           issuance_country CHAR(2),
                           nationality CHAR(2),
                           created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                           created_user INT,
                           last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           last_updated_user INT,
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           CONSTRAINT unique_passport_number UNIQUE (passport_number) -- Para evitar pasaportes duplicados
);

-- Tabla de Roles
CREATE TABLE roles (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       description VARCHAR(50) NOT NULL UNIQUE,
                       created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_user INT,
                       last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       last_updated_user INT
);

-- Tabla de relación Usuario-Rol (muchos a muchos)
CREATE TABLE user_roles (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            user_id INT NOT NULL,
                            role_id INT NOT NULL,
                            created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                            created_user INT,
                            last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            last_updated_user INT,
                            UNIQUE KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users(id),
                            FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Tabla de Tokens de refresco para JWT
CREATE TABLE refresh_tokens (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                user_id INT NOT NULL,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                expiry_date TIMESTAMP NOT NULL,
                                created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                                created_user INT,
                                last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                last_updated_user INT,
                                FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla de Niveles de Membresía
CREATE TABLE memberships (
                             id INT PRIMARY KEY AUTO_INCREMENT,
                             description VARCHAR(50) NOT NULL UNIQUE,
                             cost DECIMAL(10,2),
                             discount_percentage DECIMAL(5,2),
                             created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                             created_user INT,
                             last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             last_updated_user INT
);

-- Tabla de Membresías de Usuarios
CREATE TABLE user_memberships (
                                  id INT PRIMARY KEY AUTO_INCREMENT,
                                  user_id INT NOT NULL UNIQUE,
                                  membership_id INT NOT NULL,
                                  current_points INT DEFAULT 0,
                                  start_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  end_date DATETIME,
                                  created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  created_user INT,
                                  last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  last_updated_user INT,
                                  FOREIGN KEY (user_id) REFERENCES users(id),
                                  FOREIGN KEY (membership_id) REFERENCES memberships(id)
);

-- Inserción de datos iniciales para roles
INSERT INTO roles (description) VALUES
                                    ('ADMINISTRADOR'),
                                    ('AGENTE'),
                                    ('CLIENTE');