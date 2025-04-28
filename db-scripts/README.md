# DBO_SAMPLE DOCKER AND DB SCRIPTS READ ME

# Running MySQL locally

## Prerequisites

1. Docker installed
2. mysql installed
3. maven installed

## Run the MySQL container (Docker)

Run the script located in the db-scripts directory:

```bash
./start-local-db.sh
```

This will:

1. Setup variables for the MySQL
2. Start up MySQL container
3. Create master user `DBO_SAMPLE_USER` for `DBO_SAMPLE` schema
4. Create user `SAMPLE_API_SM` with read-write privileges
5. Run liquibase container to apply any changesets

## Liquibase

Put your liquibase scripts under `db-scripts/src/main/schema` directory

Run the run-liquibase script in the db-scrips directory:

```bash
./run-liquibase.sh
```

## Clean up

If you want to perform the full import again and apply liquibase changes again,
you will need to delete the MySQL container and the volume that it created.

```bash
docker-compose down -v
```

## View the logs

To quickly view the logs of the running containers, you can use the following command:

```bash
docker logs --tail=100 local-mysql-db
```

## General Conventions

#### WHAT BELONGS TO DBO_SAMPLE SCHEMA

* Tables in the DBO_SAMPLE schema should be self-managed ( only by SAMPLE_API_SM through api's or through db-scripts ).
* Read Only permission is granted to others ( API's and DB Users thru SAMPLE_RO)

-- ONLY DBO_SAMPLE_USER AND SAMPLE_API_SM ARE ALLOWED TO WRITE DATA TO THE DBO_SAMPLE TABLES

```mysql
GRANT SELECT, INSERT, UPDATE, DELETE on DBO_SAMPLE.SAMPLE to DBO_SAMPLE_RW;
GRANT SELECT on DBO_SAMPLE.SAMPLE to DBO_SAMPLE_SELECT_ROLE;
```

#### JAVA MAPPING TYPES:

* Refer to the JDBC types mappings https://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html

| MySQL TYPE          | JDBC/JAVA TYPE            |
|---------------------|---------------------------|
| VARCHAR             | java.lang.String          |
| TINYINT(1), BOOLEAN | java.lang.Boolean         |
| DECIMAL(10,0)       | java.lang.BigDecimal      |
| DOUBLE(19,4)        | java.lang.Double          |
| INT, BIGINT         | java.lang.Long            |
| INT                 | java.lang.Integer         |
| BIT( >1 )           | java.lang.Byte            |
| TIMESTAMP           | java.util.Date            |
| -----------------   | ------------------------- |

## Liquibase General Conventions

* Add SQL files under the `db-scripts/src/main/schema` folder
* Add an entry referencing the SQL files into `db-scripts/src/main/changelog.xml`