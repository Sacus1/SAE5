const express = require("express");
const router = express.Router();
const connection = require("../database/database");

router.post("/", (req, res) => {
  // Modify the query to retrieve both panier data and images
  const selectQuery = "SELECT idPanier, nom, prix, image FROM `Panier`";

  connection.query(selectQuery, (err, results) => {
    if (err) {
      console.error("Failed to get paniers from Panier table:", err);
      res.status(500).send("Failed to get paniers from Panier table");
    } else {
      if (results.length > 0) {
        // Assuming the column names in the result are "idPanier", "nom", "prix", "image"
        const paniersData = results.map((result) => {
          return {
            idPanier: result.idPanier,
            nom: result.nom,
            prix: result.prix,
            image: result.image.toString("base64"),
          };
        });

        res.status(200).send(paniersData);
      } else {
        console.log("No paniers found");
        res.status(404).send("No paniers found in Panier table");
      }
    }
  });
});

module.exports = router;
