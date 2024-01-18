const express = require("express");
const router = express.Router();
const multer = require("multer");
const connection = require("../database/database");
const fs = require("fs");

// Set up multer to handle file uploads
const upload = multer({ dest: "uploads/" });

/**
 * @swagger
 * /:
 *   put:
 *     summary: Endpoint to update profile image
 *     consumes:
 *       - multipart/form-data
 *     parameters:
 *       - in: formData
 *         name: image
 *         type: file
 *         description: The file to upload.
 *     responses:
 *       200:
 *         description: Profile image updated successfully
 *       401:
 *         description: Invalid token
 *       500:
 *         description: Failed to validate token or Failed to update profile image
 */
// Endpoint to update profile image
router.put("/", upload.single("image"), (req, res) => {
  const body = req.body.data;
  const token = JSON.parse(body).token;
  const fileData = req.file;

  // Check if the token is valid before proceeding
  const checkTokenQuery = "SELECT * FROM `Client` WHERE `token` = ?";
  connection.execute(checkTokenQuery, [token], (err, rows) => {
    if (err) {
      res.status(500).send("Failed to validate token");
      return;
    }

    if (rows.length === 0) {
      // Invalid token
      res.status(401).send("Invalid token");
      return;
    }

    // Token is valid, continue with image update logic
    const fileContent = fs.readFileSync(fileData.path);
    const blob = new Blob([fileContent], { type: "image/jpeg" });

    blob
      .arrayBuffer()
      .then((arrayBuffer) => {
        const buffer = Buffer.from(arrayBuffer);

        const query = "UPDATE `Client` SET imageProfil = ? WHERE token = ?";

        connection.execute(query, [buffer, token], (err) => {
          if (err) {
            console.error("Failed to update profile image:", err);
            res.status(500).send("Failed to update profile image");
          } else {
            res.status(200).send("Profile image updated successfully");
          }
          fs.unlinkSync(fileData.path);
        });
      })
      .catch((error) => {
        console.error("Error converting Blob to Buffer:", error);
        res.status(500).send("Failed to update profile image");
      });
  });
});

/**
 * @swagger
 * /:
 *   post:
 *     summary: Endpoint to retrieve profile image
 *     parameters:
 *       - in: body
 *         name: token
 *         schema:
 *           type: string
 *         required: true
 *         description: Token of the client
 *     responses:
 *       200:
 *         description: Profile image retrieved successfully
 *       401:
 *         description: Invalid token
 *       404:
 *         description: Profile image not found or Client not found
 *       500:
 *         description: Failed to validate token or Failed to get profile image
 */
// Endpoint to retrieve profile image
router.post("/", (req, res) => {
  const token = req.body.token;

  // Check if the token is valid before proceeding
  const checkTokenQuery = "SELECT * FROM `Client` WHERE `token` = ?";
  connection.execute(checkTokenQuery, [token], (err, rows) => {
    if (err) {
      res.status(500).send("Failed to validate token");
      return;
    }

    if (rows.length === 0) {
      // Invalid token
      res.status(401).send("Invalid token");
      return;
    }

    // Token is valid, continue with image retrieval logic
    const selectQuery = "SELECT imageProfil FROM `Client` WHERE token = ?";

    connection.query(selectQuery, [token], (err, results) => {
      if (err) {
        console.error("Failed to get profile image:", err);
        res.status(500).send("Failed to get profile image");
      } else {
        if (results.length > 0) {
          const imageContent = results[0].imageProfil;
          if (!imageContent) {
            res.status(404).send("Profile image not found");
          } else {
            res.setHeader("Content-Type", "image/jpeg");
            res.status(200).send(imageContent);
          }
        } else {
          res.status(404).send("Client not found");
        }
      }
    });
  });
});

module.exports = router;
