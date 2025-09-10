import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';

const SignupPage = () => {
  const navigate = useNavigate();
  //const showLogout=useContext(Login)
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    mobile: '',
    city: '',
    state: ''
  });
  const [error, setError] = useState('');

  const handleChange = e => {
    setForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await axios.post('/api/users', {
        ...form,
        role: 'USER',
        createdAt: new Date()
      });
      navigate('/login');
    } catch (err) {
      setError('Failed to sign up. Maybe user already exists.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded shadow">
      <h2 className="text-2xl font-bold mb-6 text-center">Sign Up</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block">Full Name</label>
          <input
            type="text"
            name="fullName"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.fullName}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Email</label>
          <input
            type="email"
            name="email"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.email}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Password</label>
          <input
            type="password"
            name="password"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.password}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Mobile</label>
          <input
            type="text"
            name="mobile"
            required
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.mobile}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">City</label>
          <input
            type="text"
            name="city"
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.city}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">State</label>
          <input
            type="text"
            name="state"
            className="w-full border border-gray-300 px-3 py-2 rounded"
            value={form.state}
            onChange={handleChange}
          />
        </div>
        <button
          type="submit"
          className="w-full bg-green-600 text-white py-2 rounded hover:bg-green-700"
        >
          Sign Up
        </button>
      </form>
      <p className="mt-4 text-center">
        Already have an account? <Link to="/login" className="text-blue-600 underline">Log in</Link>
      </p>
    </div>
  );
};

export default SignupPage;
