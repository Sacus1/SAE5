import Button from "./components/Button";
import Alert from "./components/Alert";
import { useState } from "react";
import { Route, Routes } from "react-router-dom";

export default function App() {
  const [showAlert, setShowAlert] = useState(false);
  return (
    <div>
      {showAlert && (
        <Alert onCloseClick={() => setShowAlert(false)}>This is an error</Alert>
      )}
      <Button
        color="success"
        onClick={() => {
          setShowAlert(true);
          console.log("Clicked");
        }}
      >
        This is a button
      </Button>
    </div>
  );
}
