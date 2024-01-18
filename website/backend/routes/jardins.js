/**
 * @swagger
 * /jardins:
 *   post:
 *     summary: Retrieve a list of gardens that the client has not joined
 *     description: This endpoint retrieves a list of gardens that the client, identified by a token sent in the request body, has not joined.
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             type: object
 *             properties:
 *               token:
 *                 type: string
 *                 description: The token of the client.
 *     responses:
 *       200:
 *         description: Returns a JSON array of garden objects. Each garden object has a "name" and "value" property.
 *         content:
 *           application/json:
 *             schema:
 *               type: array
 *               items:
 *                 type: object
 *                 properties:
 *                   name:
 *                     type: string
 *                     description: The commercial name of the garden.
 *                   value:
 *                     type: integer
 *                     description: The ID of the garden.
 *       401:
 *         description: Invalid token.
 *       500:
 *         description: An error occurred.
 */
const express = require("express");
const router = express.Router();
const connection = require("../database/database");

router.route("/").post(async (req, res) => {
  const { token } = req.body;

  // Utilisez le token pour récupérer l'userId
  const queryUserId = "SELECT idClient FROM `Client` WHERE token = ?";
  connection.execute(queryUserId, [token], async (err, userRows) => {
    if (err) {
      console.log(err);
      return res.status(500).send(err);
    }

    if (userRows.length === 0) {
      return res.status(401).send("Invalid token");
    }

    const userId = userRows[0].idClient;

    // Requête pour récupérer les jardins que le client n'a pas rejoint
    const query =
      "SELECT idJardin, nomCommercial FROM `Jardin` WHERE idJardin NOT IN (SELECT Jardin_idJardin FROM `Adhesion` WHERE Client_idClient = ?)";

    connection.execute(query, [userId], async (err, rows) => {
      if (err) {
        console.log(err);
        return res.status(500).send(err);
      }

      // Construire la liste d'éléments
      const jardinsList = rows.map((jardin) => ({
        name: jardin.nomCommercial,
        value: jardin.idJardin,
      }));

      // Envoyer la liste en réponse
      res.status(200).json(jardinsList);
    });
  });
});

module.exports = router;
