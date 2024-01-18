const express = require("express");
const router = express.Router();
const connection = require("../database/database");
const { DateTime } = require("luxon");

router.route("/").post(async (req, res) => {
  const { token, typeAdhesionId, jardinId } = req.body;

  // Utilize the token to retrieve the client's ID
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

    // Set the current date as the start date
    const startDate = DateTime.now();

    // Set the end date to one month from the start date
    const endDate = startDate.plus({ months: 1 });

    // Create a new adhesion
    const insertQuery =
      "INSERT INTO `Adhesion` (Client_idClient, TypeAdhesion_idTypeAdhesion, Jardin_idJardin, debut, fin, enCours) VALUES (?, ?, ?, ?, ?, ?)";
    console.log("Query:", insertQuery);
    console.log("Values:", [
      userId,
      typeAdhesionId,
      jardinId,
      startDate,
      endDate,
      true,
    ]);

    connection.execute(
      insertQuery,
      [userId, typeAdhesionId, jardinId, startDate, endDate, true],
      async (err, result) => {
        if (err) {
          console.log(err);
          return res.status(500).send(err);
        }

        // Adhesion created successfully
        res.status(201).send("Adhesion added successfully");
      }
    );
  });
});

router.route("/").get(async (req, res) => {
  const token =
    req.headers.authorization && req.headers.authorization.split(" ")[1];

  console.log("Token:", token);

  // Utilize the token to retrieve the client's ID
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

    // Retrieve user's adhesions with garden, type, and tarif information
    const adhesionsQuery = `
        SELECT
          A.*,
          J.nomCommercial AS nomJardin,
          T.nom AS nomTypeAdhesion,
          T.tarif
        FROM
          Adhesion A
          JOIN Jardin J ON A.Jardin_idJardin = J.idJardin
          JOIN TypeAdhesion T ON A.TypeAdhesion_idTypeAdhesion = T.idTypeAdhesion
        WHERE
          A.Client_idClient = ?
      `;

    connection.execute(adhesionsQuery, [userId], async (err, adhesionRows) => {
      if (err) {
        console.log(err);
        return res.status(500).send(err);
      }
      console.log(adhesionRows[0].debut);
      console.log(DateTime.fromJSDate(adhesionRows[0].debut));
      // Transform adhesionRows into the desired format
      const formattedAdhesions = adhesionRows.map((adhesion) => {
        return {
          id: adhesion.id, // Replace with the actual field name from your database
          jardin: adhesion.nomJardin,
          type: adhesion.nomTypeAdhesion,
          prix: adhesion.tarif,
          debut: DateTime.fromJSDate(adhesion.debut)
            .setLocale("fr")
            .toLocaleString(), // Replace with the actual field name from your database
          fin: DateTime.fromJSDate(adhesion.fin)
            .setLocale("fr")
            .toLocaleString(), // Replace with the actual field name from your database
          enCours: adhesion.enCours, // Replace with the actual field name from your database
        };
      });

      console.log(formattedAdhesions);
      // Send the formatted adhesions data back to the client
      res.status(200).json(formattedAdhesions);
    });
  });
});

module.exports = router;
