const express = require("express");
const cors = require("cors");
const swaggerUi = require("swagger-ui-express");
const swaggerJsdoc = require("swagger-jsdoc");
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
// Swagger definition
const swaggerOptions = {
  swaggerDefinition: {
    info: {
      title: 'My API',
      version: '1.0.0',
      description: 'API documentation',
    },
  },
  apis: ['./routes/*.js'], // Path to the API docs
};

// Initialize swagger-jsdoc
const swaggerDocs = swaggerJsdoc(swaggerOptions);

// Use swagger-ui-express for your app's documentation endpoint
app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));

const loginRouter = require("./routes/login");

app.use("/login", loginRouter);

const registerRouter = require("./routes/register");

app.use("/register", registerRouter);

const profilRouter = require("./routes/profil");

app.use("/profil", profilRouter);

const modifierProfilRouter = require("./routes/modifier-profil");

app.use("/modifier-profil", modifierProfilRouter);

const jardinsRouter = require("./routes/jardins");

app.use("/jardins", jardinsRouter);

const typesAdhesionRouter = require("./routes/types-adhesion");

app.use("/types-adhesion", typesAdhesionRouter);

const adhesionsRouter = require("./routes/adhesions");

app.use("/adhesions", adhesionsRouter);

const PORT = 3001;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});
