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
