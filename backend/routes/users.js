const express = require("express");
const router = express.Router();
const connection = require("../database/database");

router
  .route("/")
  .get((req, res) => {
    console.log("Get request");

    // This is a prepared statement
    connection.execute(
      "SELECT * FROM `Client` WHERE `nom` = ? AND `civilite` = ?",
      ["ClientNom", "Male"],
      (err, rows, columns) => {
        if (err) res.status(500).send(err);
        else res.status(200).send(rows);
      }
    );
  })
  .post((req, res) => {
    console.log("Post request");
    res.status(200).send("Post request");
  });

/* 
connection.unprepare(
    "SELECT * FROM `Client` WHERE `nom` = ? AND `civilite` = ?"
    );
*/
// Quand il n'y a plus besoin du prepared statement

module.exports = router;
