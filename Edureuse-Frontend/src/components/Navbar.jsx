import React, { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
const Navbar = () => {
  const { auth, logout } = useContext(AuthContext);
  const location = useLocation();

  const currentPath = location.pathname;

  const isAuthPage = currentPath === '/login' || currentPath === '/signup';

  return (
    <nav className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center">
      <div className="flex space-x-4">
        <Link to="/" className="font-semibold text-lg">EduReuse</Link>
        {auth?.logOut &&(<Link to={`/myBookSets/${auth.userId}`} className="hover:underline">My BookSets</Link>)}
        {auth?.logOut &&(<Link to="/swaps" className="hover:underline">Swap Requests</Link>)}
        {auth?.role === 'ADMIN' && (
          <Link to="/admin/users" className="hover:underline">User Management</Link>
        )}
      </div>

      {auth?.logOut && (
        <div className="flex items-center space-x-4">
          <span>Welcome, {auth.email}</span>
          <button onClick={logout} className="bg-white text-blue-600 px-3 py-1 rounded">Logout</button>
        </div>
      )}
    </nav>
  );
};

export default Navbar;

