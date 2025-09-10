import { createContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const navigate = useNavigate();
  const [auth, setAuth] = useState({
    userId: null,
    email: null,
    password: null,
    role: null,
    token: null,
    logOut: false,
  });

 const login = async ({ email, password }) => {
   const token = btoa(`${email}:${password}`);
   try {
     const res = await axios.get('/api/users/me', {
       headers: { Authorization: `Basic ${token}` }
     });

     const user = res.data;

     const authUser = {
       userId: user.userId,
       email: user.email,
       role: user.role,
       token,
       logOut: true,
     };

     setAuth(authUser);
     localStorage.setItem('authUser', JSON.stringify(authUser));
     navigate('/');
   } catch (err) {
     console.error("Login failed", err);
     throw new Error("Invalid credentials");
   }
 };

  const logout = () => {
    setAuth({ userId: null, email: null, password: null, role: null, token: null });
    navigate('/login');
  };

  const axiosAuth = axios.create();
  axiosAuth.interceptors.request.use(config => {
    if (auth.token) {
      config.headers.Authorization = `Basic ${auth.token}`;
    }
    return config;
  });

  return (
    <AuthContext.Provider value={{ auth, login, logout, axiosAuth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};
