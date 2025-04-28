FROM mysql:8.0.32

RUN chown -R mysql:root /var/lib/mysql/

ARG MYSQL_DATABASE
ARG MYSQL_ROOT_PASSWORD

ENV MYSQL_DATABASE=$MYSQL_DATABASE
ENV MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD

ADD scripts/set-up-master-roles.sql /etc/mysql/set-up-master-roles.sql

RUN sed -i 's/MYSQL_DATABASE/'$MYSQL_DATABASE'/g' /etc/mysql/set-up-master-roles.sql
RUN cp /etc/mysql/set-up-master-roles.sql /docker-entrypoint-initdb.d

#EXPOSE 3306