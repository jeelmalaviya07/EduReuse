import React, { useState, useEffect, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const AdminUserEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { axiosAuth } = useContext(AuthContext);
  const [form, setForm] = useState({
    fullName: '',
    email: '',
    password: '',
    mobile: '',
    city: '',
    state: '',
    role: 'USER'
  });
  const [error, setError] = useState('');

  useEffect(() => {
    axiosAuth.get(`/api/users/${id}`)
      .then(res => {
        const u = res.data;
        setForm({
          fullName: u.fullName || '',
          email: u.email || '',
          password: '', // blank means no change unless filled
          mobile: u.mobile || '',
          city: u.city || '',
          state: u.state || '',
          role: u.role || 'USER'
        });
      })
      .catch(err => setError('Failed to load user data.'));
  }, [id]);

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async e => {
    e.preventDefault();
    try {
      await axiosAuth.put(`/api/users/${id}`, {
        ...form,
        createdAt: new Date()
      });
      navigate('/admin/users');
    } catch (err) {
      setError('Failed to update user.');
    }
  };

  return (
    <div className="max-w-md mx-auto mt-8 bg-white p-6 rounded shadow">
      <h2 className="text-2xl font-bold mb-4">Edit User</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block">Full Name</label>
          <input
            name="fullName"
            className="w-full border px-3 py-2 rounded"
            value={form.fullName}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Email</label>
          <input
            type="email"
            name="email"
            className="w-full border px-3 py-2 rounded"
            value={form.email}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Password (leave blank to keep)</label>
          <input
            type="password"
            name="password"
            className="w-full border px-3 py-2 rounded"
            value={form.password}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Mobile</label>
          <input
            name="mobile"
            className="w-full border px-3 py-2 rounded"
            value={form.mobile}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">City</label>
          <input
            name="city"
            className="w-full border px-3 py-2 rounded"
            value={form.city}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">State</label>
          <input
            name="state"
            className="w-full border px-3 py-2 rounded"
            value={form.state}
            onChange={handleChange}
          />
        </div>
        <div>
          <label className="block">Role</label>
          <select
            name="role"
            className="w-full border px-3 py-2 rounded"
            value={form.role}
            onChange={handleChange}
          >
            <option value="USER">USER</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </div>
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Update User
        </button>
      </form>
    </div>
  );
};

export default AdminUserEditPage;
