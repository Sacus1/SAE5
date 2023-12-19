import { ReactNode } from "react";

interface PropsAlert {
  children: ReactNode;
  onCloseClick: () => void;
}

const Alert = ({ children, onCloseClick }: PropsAlert) => {
  return (
    <div className="alert-warning alert-dismissible fade show" role="alert">
      {children}
      <button
        type="button"
        className="btn-close"
        data-bs-dismiss="alert"
        aria-label="Close"
        onClick={onCloseClick}
      ></button>
    </div>
  );
};

export default Alert;
