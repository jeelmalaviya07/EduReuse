import React, { useEffect, useContext } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const HomePage = () => {
  const { auth } = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (!auth?.token) {
      navigate('/login', { replace: true });
      return;
    }

    const handleBackButton = (e) => {
      window.history.pushState(null, null, window.location.href);
    };

    window.history.pushState(null, null, window.location.href);
    window.addEventListener('popstate', handleBackButton);

    return () => {
      window.removeEventListener('popstate', handleBackButton);
    };
  }, [auth, navigate]);

  return (
    <div className="p-8 text-center">
      <h1 className="text-3xl font-bold mb-4">Welcome to EduReuse</h1>
      <p className="mb-6">Share and reuse educational book sets efficiently!</p>
      <div className="space-x-4">
        <Link to="/booksets" className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
          View Book Sets
        </Link>
        <Link to="/booksets/new" className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">
          Create New Book Set
        </Link>
      </div>
    </div>
  );
};

export default HomePage;