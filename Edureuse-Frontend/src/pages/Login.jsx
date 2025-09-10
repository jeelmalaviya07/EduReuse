import React, { useState, useContext , useEffect} from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';


const LoginPage = () => {
  const { login } = useContext(AuthContext);
  const { auth } = useContext(AuthContext);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();


  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await login({ email, password });
    } catch (err) {
      setError('Invalid credentials. Please try again.');
    }
  };

  useEffect(() => {
    if(!auth) auth.logOut=false;

  }, []);

  return (
    <div className="max-w-md mx-auto mt-20 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block">Email</label>
          <input
            type="email"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={email}
            onChange={e => setEmail(e.target.value)}
          />
        </div>
        <div>
          <label className="block">Password</label>
          <input
            type="password"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
        </div>
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Log In
        </button>
      </form>
      <p className="mt-4 text-center">
        Don't have an account? <Link to="/signup" className="text-blue-600 underline">Sign up</Link>
      </p>
    </div>
  );
};

export default LoginPage;
