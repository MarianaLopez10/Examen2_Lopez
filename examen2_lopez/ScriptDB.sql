-- ================================================================
--  Script MySQL — examen2_lopez
--
--  Estrategia ORM: JOINED TABLE  (InheritanceType.JOINED)
--  Una tabla por clase → sin repetición de columnas → normalizado.
--
--  Mapeo clase → tabla:
--  ┌──────────────────────────────────────────────────────────────────┐
--  │  Clase Java                   │  Tabla MySQL       │  Relación   │
--  ├──────────────────────────────────────────────────────────────────┤
--  │  Propietario                  │  propietario       │  —          │
--  │  Inmueble  (abstracta)        │  inmueble          │  —          │
--  │  Apartamento  extends Inmueble│  inmueble_apto     │  1:1 FK     │
--  │  Casa extends Inmueble        │  inmueble_casa     │  1:1 FK     │
--  └──────────────────────────────────────────────────────────────────┘
--
--  Relaciones:
--    inmueble.propietario_id   → propietario.id     (@ManyToOne  * → 1)
--    apartamento.numero        → inmueble.numero    (@OneToOne   1 → 1)
--    casa.numero               → inmueble.numero    (@OneToOne   1 → 1)
-- ================================================================

CREATE DATABASE IF NOT EXISTS examen2_lopez
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE examen2_lopez;

-- ================================================================
-- TABLA 1 — propietario
-- Clase: Propietario
-- @Entity  @Table(name = "propietario")
-- ================================================================
CREATE TABLE IF NOT EXISTS propietario (

	-- @Id
    id      VARCHAR(50)  NOT NULL  COMMENT '@Id',
    
    -- @Column
    nombre  VARCHAR(100) NOT NULL  COMMENT '@Column',

    PRIMARY KEY (id)

) ENGINE = InnoDB
  COMMENT = '@Entity Propietario';

-- ================================================================
-- TABLA 2 — inmueble
-- Clase: Inmueble (clase base)
-- @Entity  @Inheritance(strategy = InheritanceType.JOINED)
-- ================================================================
  CREATE TABLE IF NOT EXISTS inmueble (

	-- @Id - PK compartida con subclases
    numero        VARCHAR(20) NOT NULL COMMENT '@Id — PK compartida con subclases',
    
    -- @Column
    fecha_compra  VARCHAR(20) NOT NULL COMMENT '@Column heredado — formato MM/AAAA',
    
    -- @Column  true= alquilado | false= vendido
    estado        TINYINT(1)  NOT NULL DEFAULT 1 COMMENT '@Column heredado — 1 alquilado / 0 vendido',

	-- @ManyToOne @JoinColumn
    -- Cardinalidad: N inmuebles → 1 propietario  (diagrama: * → 1)
    propietario_id VARCHAR(50) NOT NULL  COMMENT '@ManyToOne FK → propietario.id',

    PRIMARY KEY (numero),

    CONSTRAINT fk_inmueble_propietario
        FOREIGN KEY (propietario_id)
        REFERENCES propietario (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT

) ENGINE = InnoDB
  COMMENT = '@Entity Inmueble — tabla base JOINED';
  
-- ================================================================
-- TABLA 3 — inmueble_apto
-- Clase: Apartamento extends Inmueble
-- @Entity  @Table(name = "inmueble_apto")
-- @PrimaryKeyJoinColumn(name = "numero")
-- ================================================================
CREATE TABLE IF NOT EXISTS inmueble_apto (

    -- @PrimaryKeyJoinColumn
    -- PK de esta tabla = FK @OneToOne → inmueble.numero
    numero       VARCHAR(20) NOT NULL
                        COMMENT '@PrimaryKeyJoinColumn — FK 1:1 → inmueble.numero',

    -- @Column (campo propio de Apartamento)
    numero_piso  INT NOT NULL
                        COMMENT '@Column propio Apartamento',

    PRIMARY KEY (numero),

    CONSTRAINT fk_apto_inmueble
        FOREIGN KEY (numero)
        REFERENCES inmueble (numero)
        ON UPDATE CASCADE
        ON DELETE CASCADE

) ENGINE = InnoDB
  COMMENT = '@Entity Apartamento extends Inmueble';

-- ================================================================
-- TABLA 4 — inmueble_casa
-- Clase: Casa extends Inmueble
-- @Entity  @Table(name = "inmueble_casa")
-- @PrimaryKeyJoinColumn(name = "numero")
-- ================================================================
CREATE TABLE IF NOT EXISTS inmueble_casa (

    -- @PrimaryKeyJoinColumn
    numero          VARCHAR(20) NOT NULL
                        COMMENT '@PrimaryKeyJoinColumn — FK 1:1 → inmueble.numero',

    -- @Column (campo propio de Casa)
    cantidad_pisos  INT NOT NULL
                        COMMENT '@Column propio Casa',

    PRIMARY KEY (numero),

    CONSTRAINT fk_casa_inmueble
        FOREIGN KEY (numero)
        REFERENCES inmueble (numero)
        ON UPDATE CASCADE
        ON DELETE CASCADE

) ENGINE = InnoDB
  COMMENT = '@Entity Casa extends Inmueble';
  
-- ================================================================
-- DATOS DE PRUEBA
-- ================================================================

INSERT INTO propietario (id, nombre) VALUES
    ('P001', 'Carlos Pérez'),
    ('P002', 'Ana Gómez');

INSERT INTO inmueble (numero, fecha_compra, estado, propietario_id) VALUES
    ('I001', '01/01/2024', 1, 'P001'),
    ('I002', '15/03/2023', 1, 'P001'),
    ('I003', '20/06/2022', 0, 'P002'),
    ('I004', '10/10/2021', 1, 'P002');

INSERT INTO inmueble_apto (numero, numero_piso) VALUES
    ('I001', 5),
    ('I003', 2);

INSERT INTO inmueble_casa (numero, cantidad_pisos) VALUES
    ('I002', 2),
    ('I004', 3);