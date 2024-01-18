const mysql = require("mysql2");

const connection = mysql.createConnection({
  host: "mysql-db",
  port: "3306",
  user: "user",
  password: "password",
  database: "SAE",
});

module.exports = connection;
