import { useState, ChangeEvent } from "react";
const Register = () => {
  function MySelectComponent() {
    const [tagInputVal, setTagInputVal] = useState("");

    const [selectedValue, setSelectedValue] = useState<string>("male"); // Default value with type string

    const handleChange = (event: ChangeEvent<HTMLSelectElement>) => {
      setSelectedValue(event.target.value);
    };

    function onChangeTagInput(e: ChangeEvent<HTMLInputElement>) {
      setTagInputVal(
        e.target.value.replace(
          /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/gi,
          ""
        )
      );
    }

    return (
      <form className="row g-3">
        <div className="col-md-4">
          <label htmlFor="nom" className="form-label">
            Nom
          </label>
          <input
            type="text"
            className="form-control"
            id="nom"
            required
            maxLength={127}
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="prenom" className="form-label">
            Prenom
          </label>
          <input
            type="text"
            className="form-control"
            id="prenom"
            required
            maxLength={127}
          />
        </div>
        <div className="col-md-4">
          <label htmlFor="mailAdress" className="form-label">
            Adresse Mail
          </label>
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              id="mailAdress"
              pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
              value={tagInputVal}
              onChange={(e) => onChangeTagInput(e)}
              required
            />
          </div>
        </div>
        <div className="col-md-6">
          <label htmlFor="validationDefault03" className="form-label">
            City
          </label>
          <input
            type="text"
            className="form-control"
            id="validationDefault03"
            required
          />
        </div>
        <div className="col-md-3">
          <label htmlFor="validationDefault04" className="form-label">
            Civilité
          </label>
          <select
            value={selectedValue}
            onChange={handleChange}
            className="form-select"
            id="validationDefault04"
          >
            <option value="male">Male </option>
            <option value="femelle">Femelle </option>
            <option value="autre">Autre </option>
          </select>
        </div>
        <div className="col-12">
          <div className="form-check">
            <input
              className="form-check-input"
              type="checkbox"
              value=""
              id="invalidCheck2"
              required
            />
            <label className="form-check-label" htmlFor="invalidCheck2">
              Agree to terms and conditions
            </label>
          </div>
        </div>
        <div className="col-12">
          <button className="btn btn-primary" type="submit">
            Submit form
          </button>
        </div>
      </form>
    );
  }
  return <MySelectComponent />;
};
export default Register;
