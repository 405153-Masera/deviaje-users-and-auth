-- Base de Datos Microservicio Usuarios y Autenticación

CREATE DATABASE IF NOT EXISTS deviaje_users_auth;

USE deviaje_users_auth;

-- Tabla de Tipos de Documentos
CREATE TABLE dni_types (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           description VARCHAR(50) NOT NULL,
                           created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                           created_user INT,
                           last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           last_updated_user INT
);

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
                       dni VARCHAR(20),
                       dni_type_id INT,
                       active BOOLEAN DEFAULT TRUE,
                       avatar_url VARCHAR(255),
                       created_datetime DATETIME DEFAULT CURRENT_TIMESTAMP,
                       created_user INT,
                       last_updated_datetime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       last_updated_user INT,
                       FOREIGN KEY (dni_type_id) REFERENCES dni_types(id)
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

-- Tabla de Auditoría para dni_types
CREATE TABLE dni_types_audit (
                                 version_id INT AUTO_INCREMENT PRIMARY KEY,
                                 id INT,
                                 version INT,
                                 description VARCHAR(50),
                                 created_datetime DATETIME,
                                 created_user INT,
                                 last_updated_datetime DATETIME,
                                 last_updated_user INT
);

-- Tabla de Auditoría para users
CREATE TABLE users_audit (
                             version_id INT AUTO_INCREMENT PRIMARY KEY,
                             id INT,
                             version INT,
                             username VARCHAR(50),
                             email VARCHAR(100),
                             password VARCHAR(255),
                             first_name VARCHAR(50),
                             last_name VARCHAR(50),
                             phone VARCHAR(20),
                             birth_date DATE,
                             dni VARCHAR(20),
                             dni_type_id INT,
                             active BOOLEAN,
                             avatar_url VARCHAR(255),
                             created_datetime DATETIME,
                             created_user INT,
                             last_updated_datetime DATETIME,
                             last_updated_user INT
);

-- Tabla de Auditoría para roles
CREATE TABLE roles_audit (
                             version_id INT AUTO_INCREMENT PRIMARY KEY,
                             id INT,
                             version INT,
                             description VARCHAR(50),
                             created_datetime DATETIME,
                             created_user INT,
                             last_updated_datetime DATETIME,
                             last_updated_user INT
);

-- Tabla de Auditoría para user_roles
CREATE TABLE user_roles_audit (
                                  version_id INT AUTO_INCREMENT PRIMARY KEY,
                                  id INT,
                                  version INT,
                                  user_id INT,
                                  role_id INT,
                                  created_datetime DATETIME,
                                  created_user INT,
                                  last_updated_datetime DATETIME,
                                  last_updated_user INT
);

-- Tabla de Auditoría para refresh_tokens
CREATE TABLE refresh_tokens_audit (
                                      version_id INT AUTO_INCREMENT PRIMARY KEY,
                                      id INT,
                                      version INT,
                                      user_id INT,
                                      token VARCHAR(255),
                                      expiry_date TIMESTAMP,
                                      created_datetime DATETIME,
                                      created_user INT,
                                      last_updated_datetime DATETIME,
                                      last_updated_user INT
);

-- Tabla de Auditoría para memberships
CREATE TABLE memberships_audit (
                                   version_id INT AUTO_INCREMENT PRIMARY KEY,
                                   id INT,
                                   version INT,
                                   description VARCHAR(50),
                                   cost DECIMAL(10,2),
                                   discount_percentage DECIMAL(5,2),
                                   created_datetime DATETIME,
                                   created_user INT,
                                   last_updated_datetime DATETIME,
                                   last_updated_user INT
);

-- Tabla de Auditoría para user_memberships
CREATE TABLE user_memberships_audit (
                                        version_id INT AUTO_INCREMENT PRIMARY KEY,
                                        id INT,
                                        version INT,
                                        user_id INT,
                                        membership_id INT,
                                        current_points INT,
                                        start_date DATETIME,
                                        end_date DATETIME,
                                        created_datetime DATETIME,
                                        created_user INT,
                                        last_updated_datetime DATETIME,
                                        last_updated_user INT
);

-- Ahora creamos los triggers de auditoría
DELIMITER $$

-- Triggers para dni_types
CREATE TRIGGER trg_dni_types_insert
    AFTER INSERT ON dni_types
    FOR EACH ROW
BEGIN
    INSERT INTO dni_types_audit (id, version, description, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.description, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_dni_types_update
    AFTER UPDATE ON dni_types
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM dni_types_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO dni_types_audit (id, version, description, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.description, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para users
CREATE TRIGGER trg_users_insert
    AFTER INSERT ON users
    FOR EACH ROW
BEGIN
    INSERT INTO users_audit (id, version, username, email, password, first_name, last_name, phone, birth_date, dni, dni_type_id, active, avatar_url, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.username, NEW.email, NEW.password, NEW.first_name, NEW.last_name, NEW.phone, NEW.birth_date, NEW.dni, NEW.dni_type_id, NEW.active, NEW.avatar_url, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_users_update
    AFTER UPDATE ON users
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM users_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO users_audit (id, version, username, email, password, first_name, last_name, phone, birth_date, dni, dni_type_id, active, avatar_url, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.username, NEW.email, NEW.password, NEW.first_name, NEW.last_name, NEW.phone, NEW.birth_date, NEW.dni, NEW.dni_type_id, NEW.active, NEW.avatar_url, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para roles
CREATE TRIGGER trg_roles_insert
    AFTER INSERT ON roles
    FOR EACH ROW
BEGIN
    INSERT INTO roles_audit (id, version, description, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.description, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_roles_update
    AFTER UPDATE ON roles
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM roles_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO roles_audit (id, version, description, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.description, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para user_roles
CREATE TRIGGER trg_user_roles_insert
    AFTER INSERT ON user_roles
    FOR EACH ROW
BEGIN
    INSERT INTO user_roles_audit (id, version, user_id, role_id, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.user_id, NEW.role_id, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_user_roles_update
    AFTER UPDATE ON user_roles
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM user_roles_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO user_roles_audit (id, version, user_id, role_id, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.user_id, NEW.role_id, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para refresh_tokens
CREATE TRIGGER trg_refresh_tokens_insert
    AFTER INSERT ON refresh_tokens
    FOR EACH ROW
BEGIN
    INSERT INTO refresh_tokens_audit (id, version, user_id, token, expiry_date, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.user_id, NEW.token, NEW.expiry_date, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_refresh_tokens_update
    AFTER UPDATE ON refresh_tokens
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM refresh_tokens_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO refresh_tokens_audit (id, version, user_id, token, expiry_date, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.user_id, NEW.token, NEW.expiry_date, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para memberships
CREATE TRIGGER trg_memberships_insert
    AFTER INSERT ON memberships
    FOR EACH ROW
BEGIN
    INSERT INTO memberships_audit (id, version, description, cost, discount_percentage, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.description, NEW.cost, NEW.discount_percentage, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_memberships_update
    AFTER UPDATE ON memberships
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM memberships_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO memberships_audit (id, version, description, cost, discount_percentage, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.description, NEW.cost, NEW.discount_percentage, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

-- Triggers para user_memberships
CREATE TRIGGER trg_user_memberships_insert
    AFTER INSERT ON user_memberships
    FOR EACH ROW
BEGIN
    INSERT INTO user_memberships_audit (id, version, user_id, membership_id, current_points, start_date, end_date, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, 1, NEW.user_id, NEW.membership_id, NEW.current_points, NEW.start_date, NEW.end_date, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

CREATE TRIGGER trg_user_memberships_update
    AFTER UPDATE ON user_memberships
    FOR EACH ROW
BEGIN
    DECLARE latest_version INT;
    SELECT MAX(version) INTO latest_version FROM user_memberships_audit WHERE id = NEW.id;
    SET latest_version = IFNULL(latest_version, 0) + 1;

    INSERT INTO user_memberships_audit (id, version, user_id, membership_id, current_points, start_date, end_date, created_datetime, created_user, last_updated_datetime, last_updated_user)
    VALUES (NEW.id, latest_version, NEW.user_id, NEW.membership_id, NEW.current_points, NEW.start_date, NEW.end_date, NEW.created_datetime, NEW.created_user, NEW.last_updated_datetime, NEW.last_updated_user);
END $$

DELIMITER ;

-- Inserción de datos iniciales para roles
INSERT INTO roles (description) VALUES
                                    ('SUPERADMIN'),
                                    ('CLIENTE'),
                                    ('AGENTE');

-- Inserción de datos iniciales para tipos de documento
INSERT INTO dni_types (description) VALUES
                                        ('DNI'),
                                        ('PASAPORTE'),
                                        ('CUIT/CUIL');

-- Inserción de datos iniciales para niveles de membresía
INSERT INTO memberships (description, discount_percentage) VALUES
                                                               ('BASIC', 0.00),
                                                               ('PREMIUM', 5.00);