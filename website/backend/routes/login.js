const express = require("express");
const router = express.Router();
const { v4: uuidv4 } = require("uuid"); // Import the uuid library
const connection = require("../database/database");

router
  .route("/")
  .get((req, res) => {
    res.status(200).send("Get request received");
  })
  .post((req, res) => {
    const mail = req.body.mail;
    const password = req.body.password;
    console.log("mail:", mail);
    console.log("password:", password);
    const query =
      "SELECT * FROM `Client` WHERE `mail` = ? AND `motDePasse` = ?";

    connection.execute(query, [mail, password], (err, rows, columns) => {
      if (err) {
        res.status(500).send(err);
      } else {
        if (rows.length > 0) {
          // If login is successful, generate a UUID token
          const token = uuidv4();

          // You may want to save the token in the database or use a different method to manage sessions
          const userId = rows[0].idClient;
          const queryToken = "UPDATE `Client` SET token = ? WHERE idClient = ?";
          connection.execute(queryToken, [token, userId], (err) => {
            if (err) {
              console.log("The token wasn't saved to the database. RIP BOZO.");
            }
          });
          // Send the token in the response
          res.status(200).json({ token });
        } else {
          res.status(401).send("Invalid email or password");
        }
      }
    });

    connection.unprepare(query);
  });

module.exports = router;
