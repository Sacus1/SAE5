import { ReactNode } from "react";

interface PropsButton {
  children: string;
  color?: "primary" | "secondary" | "danger" | "success";
  onClick: () => void;
}

const Alert = ({ children, onClick, color = "primary" }: PropsButton) => {
  return (
    <button type="button" className={"btn btn-" + color} onClick={onClick}>
      {children}
    </button>
  );
};

export default Alert;
