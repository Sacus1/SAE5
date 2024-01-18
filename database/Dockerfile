FROM mysql:8.2
# Set the root password
ENV MYSQL_ROOT_PASSWORD=password
COPY ./database.sql /tmp/database.sql
# Expose the port 3306
EXPOSE 3306
# create volume
VOLUME /var/lib/mysql
# Run the mariadb service and initialize the database when the container starts
CMD ["mysqld", "--init-file=/tmp/database.sql"]
