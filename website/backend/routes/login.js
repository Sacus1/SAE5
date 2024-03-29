const express = require("express");
const router = express.Router();
const { v4: uuidv4 } = require("uuid");
const connection = require("../database/database");
const argon2 = require("argon2");

/**
 * @swagger
 * /login:
 *   post:
 *     summary: Logs in a user
 *     description: This endpoint logs in a user by verifying the provided email and password. If the login is successful, a UUID token is generated and sent in the response.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             required:
 *               - mail
 *               - password
 *             properties:
 *               mail:
 *                 type: string
 *                 description: The user's email
 *               password:
 *                 type: string
 *                 description: The user's password
 *     responses:
 *       200:
 *         description: Login successful. Returns a token.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 token:
 *                   type: string
 *                   description: The token generated for the session
 *       401:
 *         description: Invalid email or password
 *       500:
 *         description: Internal Server Error
 */
router.route("/").post(async (req, res) => {
  const { mail, password } = req.body;

  const query = "SELECT * FROM `Client` WHERE `mail` = ?";

  connection.execute(query, [mail], async (err, rows, columns) => {
    if (err) {
      res.status(500).send(err);
    } else {
      if (rows.length > 0) {
        const hashedPasswordFromDB = rows[0].motDePasse;

        try {
          // Verify the password using argon2.verify
          const isPasswordValid = await argon2.verify(
            hashedPasswordFromDB,
            password
          );

          if (isPasswordValid) {
            // If login is successful, generate a UUID token
            const token = uuidv4();

            // You may want to save the token in the database or use a different method to manage sessions
            const userId = rows[0].idClient;
            const queryToken =
              "UPDATE `Client` SET token = ? WHERE idClient = ?";

            connection.execute(queryToken, [token, userId], (err) => {
              if (err) {
                console.log(
                  "The token wasn't saved to the database. RIP BOZO."
                );
              }
            });

            // Send the token in the response
            res.status(200).json({ token });
          } else {
            res.status(401).send("Invalid email or password");
          }
        } catch (error) {
          console.error("Error verifying password:", error);
          res.status(500).send("Internal Server Error");
        }
      } else {
        res.status(401).send("Invalid email or password");
      }
    }
  });

  connection.unprepare(query);
});

module.exports = router;
