const express = require("express");
const app = express();

app.use(express.static("public"));
app.use(express.urlencoded({ extended: true }));

const userRouter = require("./routes/login");

app.use("/login", userRouter);

app.listen(3001, () => {
  console.log("express running on port 3001");
});
