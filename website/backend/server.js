const express = require("express");
const cors = require("cors");

const corsOptions = {
  //origin: "https://localhost:5173/",
  origin: "*",
  method: ["POST", "GET", "PUT", "DELETE"],
};
const app = express();

app.use(cors(corsOptions));
app.use(express.json()); // Parse JSON-encoded bodies
app.use(express.urlencoded({ extended: false })); // Parse URL-encoded

app.use(express.static("public"));

const loginRouter = require("./routes/login");

app.use("/login", loginRouter);

const registerRouter = require("./routes/register");

app.use("/register", registerRouter);

const profilRouter = require("./routes/profil");

app.use("/profil", profilRouter);

const modifierProfilRouter = require("./routes/modifier-profil");

app.use("/modifier-profil", modifierProfilRouter);

const PORT = 3001;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
