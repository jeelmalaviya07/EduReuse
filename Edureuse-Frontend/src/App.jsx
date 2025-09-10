import React, { useContext } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/Login';
import SignupPage from './pages/Signup';
import HomePage from './pages/Home';
import BookSetsPage from './pages/BookSets';
import BookSetForm from './pages/BookSetForm';
import SwapRequestsPage from './pages/SwapRequests';
import AdminUsersPage from './pages/AdminUsers';
import AdminUserEditPage from './pages/AdminUserEdit';
import Navbar from './components/Navbar';
import MyBookSetsPage from './pages/MyBookSets'
import { AuthContext } from './context/AuthContext';
import SwapMatchSelector from './pages/SwapMatchSelector';

function App() {
  const { auth } = useContext(AuthContext);
  const isAuthenticated = !!auth.token;

  return (
    <div className="min-h-screen flex flex-col">
      { <Navbar />}
      <div className="flex-grow">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/myBookSets/:id" element={<MyBookSetsPage/>}/>
          <Route path="/swap/select-match/:requestedId" element={<SwapMatchSelector />} />
          <Route path="/" element={
            isAuthenticated ? <HomePage /> : <Navigate to="/login" />
          } />
          <Route path="/booksets" element={
            isAuthenticated ? <BookSetsPage /> : <Navigate to="/login" />
          } />
          <Route path="/booksets/new" element={
            isAuthenticated ? <BookSetForm /> : <Navigate to="/login" />
          } />


          <Route path="/booksets/edit/:id" element={
            isAuthenticated ? <BookSetForm editMode={true} /> : <Navigate to="/login" />
          } />
          <Route path="/swaps" element={
            isAuthenticated ? <SwapRequestsPage /> : <Navigate to="/login" />
          } />
          {auth.role === 'ADMIN' && (
            <>
              <Route path="/admin/users" element={<AdminUsersPage />} />
              <Route path="/admin/users/edit/:id" element={<AdminUserEditPage />} />
            </>
          )}
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </div>
    </div>
  );
}

export default App;
