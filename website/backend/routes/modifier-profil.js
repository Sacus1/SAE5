const express = require("express");
const router = express.Router();
const connection = require("../database/database");
const { DateTime } = require("luxon");

router.route("/").post((req, res) => {
  const { token } = req.body;

  // Check if the token is valid
  const checkTokenQuery = "SELECT idClient FROM `Client` WHERE `token` = ?";
  connection.execute(checkTokenQuery, [token], (err, rows) => {
    if (err) {
      res.status(500).send(err);
    } else {
      if (rows.length === 0) {
        // Invalid token
        res.status(401).send("Invalid token");
      } else {
        // Token is valid, fetch client data
        const clientId = rows[0].idClient;

        // Join the Client and Adresse tables to get all client data
        const getClientDataQuery = `
          SELECT 
            C.mail as email,
            C.nom,
            C.prenom,
            C.telephone,
            C.telephone2,
            C.telephone3,
            dateNaissance,
            C.profession,
            A.adresse,
            A.ville,
            A.codePostal
          FROM 
            Client C
          JOIN 
            Adresse A ON C.Adresse_idAdresse = A.idAdresse
          WHERE 
            C.idClient = ?
        `;

        connection.execute(getClientDataQuery, [clientId], (err, dataRows) => {
          if (err) {
            res.status(500).send(err);
          } else {
            // Send the fetched client data
            res.status(200).json(dataRows[0]);
          }
        });
      }
    }
  });
});

router.route("/").put(async (req, res) => {
  const { token, clientData } = req.body;
  const {
    email,
    password,
    nom,
    prenom,
    telephone,
    telephone2,
    telephone3,
    profession,
    adresse,
    ville,
    codePostal,
  } = clientData;

  const dateNaissance = DateTime.fromISO(clientData.dateNaissance).toISODate();

  // Check if the token is valid
  const checkTokenQuery =
    "SELECT idClient, Adresse_idAdresse FROM `Client` WHERE `token` = ?";
  connection.execute(checkTokenQuery, [token], async (err, rows) => {
    if (err) {
      res.status(500).send(err);
    } else {
      if (rows.length === 0) {
        // Invalid token
        res.status(401).send("Invalid token");
      } else {
        // Token is valid, update client data
        const clientId = rows[0].idClient;
        const hashedPassword = password ? await argon2.hash(password) : null;

        // Build the SET part of the update query for client data
        let updateClientQuerySet = "";
        const updateClientQueryValues = [];

        if (email) {
          updateClientQuerySet += "mail = ?, ";
          updateClientQueryValues.push(email);
        }
        if (password) {
          updateClientQuerySet += "motDePasse = ?, ";
          updateClientQueryValues.push(hashedPassword);
        }
        if (nom) {
          updateClientQuerySet += "nom = ?, ";
          updateClientQueryValues.push(nom);
        }
        if (prenom) {
          updateClientQuerySet += "prenom = ?, ";
          updateClientQueryValues.push(prenom);
        }
        if (telephone) {
          updateClientQuerySet += "telephone = ?, ";
          updateClientQueryValues.push(telephone);
        }
        if (telephone2) {
          updateClientQuerySet += "telephone2 = ?, ";
          updateClientQueryValues.push(telephone2);
        }
        if (telephone3) {
          updateClientQuerySet += "telephone3 = ?, ";
          updateClientQueryValues.push(telephone3);
        }
        if (dateNaissance) {
          updateClientQuerySet += "dateNaissance = ?, ";
          updateClientQueryValues.push(dateNaissance);
        }
        if (profession) {
          updateClientQuerySet += "profession = ?, ";
          updateClientQueryValues.push(profession);
        }

        // Remove trailing comma and space
        updateClientQuerySet = updateClientQuerySet.replace(/,\s*$/, "");

        // Update the client data
        const updateClientQuery = `UPDATE Client SET ${updateClientQuerySet} WHERE idClient = ?`;

        connection.execute(
          updateClientQuery,
          [...updateClientQueryValues, clientId],
          async (err) => {
            if (err) {
              res.status(500).send(err);
            } else {
              // Update successful, now update the Adresse data
              const adresseId = rows[0].Adresse_idAdresse;

              // Build the SET part of the update query for Adresse data
              let updateAdresseQuerySet = "";
              const updateAdresseQueryValues = [];

              if (adresse) {
                updateAdresseQuerySet += "adresse = ?, ";
                updateAdresseQueryValues.push(adresse);
              }
              if (ville) {
                updateAdresseQuerySet += "ville = ?, ";
                updateAdresseQueryValues.push(ville);
              }
              if (codePostal) {
                updateAdresseQuerySet += "codePostal = ?, ";
                updateAdresseQueryValues.push(codePostal);
              }

              // Remove trailing comma and space
              updateAdresseQuerySet = updateAdresseQuerySet.replace(
                /,\s*$/,
                ""
              );
              const updateAdresseQuery = `UPDATE Adresse SET ${updateAdresseQuerySet} WHERE idAdresse = ?`;
              connection.execute(
                updateAdresseQuery,
                [...updateAdresseQueryValues, adresseId],
                (err) => {
                  if (err) {
                    res.status(500).send(err);
                  } else {
                    // Adresse update successful
                    res.status(200).send("Profile updated successfully");
                  }
                }
              );
            }
          }
        );
      }
    }
  });
});

module.exports = router;
