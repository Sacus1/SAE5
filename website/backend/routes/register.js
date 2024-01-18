/**
 * @swagger
 * /register:
 *   post:
 *     summary: Register a new client
 *     description: This endpoint allows a new client to register. It first checks if the email is already registered. If not, it proceeds with the registration process.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               email:
 *                 type: string
 *                 description: The email of the client.
 *               password:
 *                 type: string
 *                 description: The password of the client.
 *               nom:
 *                 type: string
 *                 description: The last name of the client.
 *               prenom:
 *                 type: string
 *                 description: The first name of the client.
 *               telephone:
 *                 type: string
 *                 description: The primary telephone number of the client.
 *               telephone2:
 *                 type: string
 *                 description: The secondary telephone number of the client.
 *               telephone3:
 *                 type: string
 *                 description: The tertiary telephone number of the client.
 *               dateNaissance:
 *                 type: string
 *                 description: The birth date of the client.
 *               profession:
 *                 type: string
 *                 description: The profession of the client.
 *               adresse:
 *                 type: string
 *                 description: The address of the client.
 *               ville:
 *                 type: string
 *                 description: The city of the client.
 *               codePostal:
 *                 type: string
 *                 description: The postal code of the client.
 *               civilite:
 *                 type: string
 *                 description: The civil status of the client.
 *     responses:
 *       201:
 *         description: Registration successful. Returns the token.
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 token:
 *                   type: string
 *                   description: The token of the client.
 *       400:
 *         description: Date de naissance incorrecte.
 *       409:
 *         description: Email is already registered.
 *       500:
 *         description: An error occurred.
 */
const express = require("express");
const router = express.Router();
const { v4: uuidv4 } = require("uuid");
const connection = require("../database/database");
const argon2 = require("argon2");

router.route("/").post((req, res) => {
  const {
    email,
    password,
    nom,
    prenom,
    telephone,
    telephone2,
    telephone3,
    dateNaissance,
    profession,
    adresse,
    ville,
    codePostal,
    civilite,
  } = req.body;
  if (!dateNaissance) {
    res.status(400).send("Date de naissance incorrecte");
  } else {
    const formattedDateNaissance = new Date(dateNaissance)
      .toISOString()
      .split("T")[0];

    // Check if the email is already registered
    const checkEmailQuery = "SELECT * FROM `Client` WHERE `mail` = ?";
    connection.execute(checkEmailQuery, [email], (err, rows, columns) => {
      if (err) {
        res.status(500).send(err);
      } else {
        if (rows.length > 0) {
          // Email is already registered
          res.status(409).send("Email is already registered");
        } else {
          // If email is not registered, proceed with registration
          const token = uuidv4();

          // Insert the address first
          const insertAddressQuery =
            "INSERT INTO `Adresse` (adresse, ville, codePostal) VALUES (?, ?, ?)";
          connection.execute(
            insertAddressQuery,
            [adresse, ville, codePostal],
            async (err, addressResult) => {
              if (err) {
                res.status(500).send(err);
              } else {
                // Get the generated `idAdresse`
                const Adresse_idAdresse = addressResult.insertId;

                // Insert the client with the generated `idAdresse` and token
                const insertClientQuery =
                  "INSERT INTO `Client` (mail, motDePasse, nom, prenom, telephone, telephone2, telephone3, dateNaissance, profession, Adresse_idAdresse, token, civilite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                const hashedPassword = await argon2.hash(password);
                connection.execute(
                  insertClientQuery,
                  [
                    email,
                    hashedPassword,
                    nom,
                    prenom,
                    telephone,
                    telephone2,
                    telephone3,
                    formattedDateNaissance,
                    profession,
                    Adresse_idAdresse,
                    token,
                    civilite, // Include the civilite field
                  ],
                  (err, result) => {
                    if (err) {
                      res.status(500).send(err);
                    } else {
                      // Registration successful
                      res.status(201).json({ token });
                    }
                  }
                );
              }
            }
          );
        }
      }
    });
  }
});

module.exports = router;
