const express = require("express");
const router = express.Router();
const multer = require("multer"); // Import multer for handling file uploads
const connection = require("../database/database");
const fs = require("fs");

// Set up multer to handle file uploads
const upload = multer({ dest: "uploads/" });

// Endpoint to update profile image
router.put("/", upload.single("image"), (req, res) => {
  const body = req.body.data;
  const token = JSON.parse(body).token;
  const fileData = req.file;

  // Read the file content
  const fileContent = fs.readFileSync(fileData.path);
  const blob = new Blob([fileContent], { type: "image/jpeg" }); // Adjust the type based on your image format

  // Convert the Blob to a Buffer
  blob
    .arrayBuffer()
    .then((arrayBuffer) => {
      const buffer = Buffer.from(arrayBuffer);

      // Implement your logic to update the profile image in the database
      const query = "UPDATE `Client` SET imageProfil = ? WHERE token = ?";

      connection.execute(query, [buffer, token], (err) => {
        if (err) {
          console.error("Failed to update profile image:", err);
          res.status(500).send("Failed to update profile image");
        } else {
          // Remove the file after reading its content
          fs.unlinkSync(fileData.path);

          res.status(200).send("Profile image updated successfully");
        }
      });
    })
    .catch((error) => {
      console.error("Error converting Blob to Buffer:", error);
      res.status(500).send("Failed to update profile image");
    });
});

// Add closing brace for router.put here

// Endpoint to retrieve profile image
router.post("/", (req, res) => {
  const token = req.body.token;

  // Implement your logic to retrieve the profile image from the database
  const selectQuery = "SELECT imageProfil FROM `Client` WHERE token = ?";

  connection.query(selectQuery, [token], (err, results) => {
    if (err) {
      console.error("Failed to get profile image:", err);
      res.status(500).send("Failed to get profile image");
    } else {
      if (results.length > 0) {
        const imageContent = results[0].imageProfil;

        // Set the appropriate Content-Type header based on your image type
        res.setHeader("Content-Type", "image/jpeg"); // Adjust based on your image type

        // Send the profile image to the frontend
        res.status(200).send(imageContent);
      } else {
        res.status(404).send("Profile image not found");
      }
    }
  });
});

module.exports = router;
