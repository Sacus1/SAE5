/**
 * @swagger
 * /types-adhesion:
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
const connection = require("../database/database");

router.route("/").post(async (req, res) => {
  const query = "SELECT idTypeAdhesion, nom, tarif FROM TypeAdhesion";

  connection.execute(query, async (err, rows, columns) => {
    if (err) {
      res.status(500).send(err);
    } else {
      const typesAdhesionList = rows.map((typeAdhesion) => ({
        name: `${typeAdhesion.nom}: ${typeAdhesion.tarif}€`,
        value: typeAdhesion.idTypeAdhesion,
      }));

      res.status(200).json(typesAdhesionList);
    }
  });

  connection.unprepare(query);
});

module.exports = router;
