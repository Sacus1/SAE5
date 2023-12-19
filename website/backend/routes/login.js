const express = require("express");
const router = express.Router();
const connection = require("../database/database");

router
  .route("/")
  .get((req, res) => {
    console.log("Get request");

    // This is a prepared statement
  })
  .post((req, res) => {
    console.log("Login post request received");
    mail = req.mail;
    password = req.password;
    query = "SELECT * FROM `Client` WHERE `mail` = ? AND `password` = ?";
    connection.execute(query, [mail, password], (err, rows, columns) => {
      if (err) res.status(500).send(err);
      else res.status(200).send(rows);
    });

    connection.unprepare(query);
  });

module.exports = router;
