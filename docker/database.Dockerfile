FROM mariadb:11.1.2
LABEL authors="Lukas Adrian Keller"

ENV MARIADB_USER=example
ENV MARIADB_ROOT_PASSWORD=example
ENV MYSQL_ROOT_PASSWORD=root

#copy sql files to special folder, will be executed on startup in alphabetical order
COPY ./database/__a__init.sql /docker-entrypoint-initdb.d/