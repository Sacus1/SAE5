const Login = () => {
  return (
    <form className="w-50 form-inline mx-auto">
      <label className="sr-only" htmlFor="inlineFormInputName2">
        Email
      </label>
      <input
        type="text"
        className="form-control mb-2 mr-sm-2"
        id="inlineFormInputName2"
        placeholder="Email"
      />

      <label className="sr-only" htmlFor="inlineFormInputGroupUsername2">
        Password
      </label>
      <div className="input-group mb-2 mr-sm-2">
        <input
          type="text"
          className="form-control"
          id="inlineFormInputGroupUsername2"
          placeholder="Password"
        />
      </div>

      <div className="form-check mb-2 mr-sm-2">
        <input
          className="form-check-input"
          type="checkbox"
          id="inlineFormCheck"
        />
        <label className="form-check-label" htmlFor="inlineFormCheck">
          Remember me
        </label>
      </div>
      
      <button type="submit" className="btn btn-primary mb-2">
        Submit
      </button>
    </form>
  );
};

export default Login;
