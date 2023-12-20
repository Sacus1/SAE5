const express = require("express");
const router = express.Router();
const connection = require("../database/database");

router
  .route("/")
  .get((req, res) => {
    res.status(200).send("Get request received");
    // This is a prepared statement
  })
  .post((req, res) => {
    mail = req.body.mail;
    password = req.body.password;
    query = "SELECT * FROM `Client` WHERE `mail` = ? AND `motDePasse` = ?";
    connection.execute(query, [mail, password], (err, rows, columns) => {
      if (err) res.status(500).send(err);
      else {
        res.status(200).send(rows);
      }
    });

    connection.unprepare(query);
  });

module.exports = router;
