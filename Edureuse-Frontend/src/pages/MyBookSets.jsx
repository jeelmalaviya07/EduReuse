import React, { useState, useEffect, useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const MyBookSetsPage = () => {
  const { axiosAuth, auth } = useContext(AuthContext);
  const [bookSets, setBookSets] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    if (auth?.userId) {
      fetchBookSets();
    }
  }, [auth]);

  const fetchBookSets = async () => {
    try {
      const res = await axiosAuth.get(`/api/users/myBookSets/${auth.userId}`);
      setBookSets(res.data);
    } catch (err) {
      console.error('Failed to load book sets:', err);
      setError('Failed to load your book sets. Please try again.');
    }
  };

  const handleDelete = async (setId) => {
    if (!window.confirm('Are you sure you want to delete this book set?')) return;

    try {
      await axiosAuth.delete(`/api/users/booksets/${setId}`);
      fetchBookSets(); // Refresh list
    } catch (err) {
      console.error('Delete failed:', err);
      setError('Failed to delete the book set. Please try again.');
    }
  };

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <h2 className="text-2xl font-bold mb-6 text-center">Manage Your Book Sets</h2>

      {error && <p className="text-center text-red-600 mb-4">{error}</p>}

      {bookSets.length === 0 ? (
        <div className="text-center text-gray-600">
          <p>You haven&apos;t listed any book sets yet.</p>
          <Link to="/booksets/new" className="inline-block mt-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
            Add Book Set
          </Link>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {bookSets.map((bs) => (
            <div key={bs.setId} className="bg-white border rounded shadow p-4">
              <h3 className="font-semibold text-xl mb-2">{bs.title}</h3>
              <p><strong>Class:</strong> {bs.classLevel}</p>
              <p><strong>Board:</strong> {bs.board || 'N/A'}</p>
              <p><strong>Type:</strong> {bs.listingType}</p>
              {bs.price && <p><strong>Price:</strong> ₹{bs.price}</p>}
              <div className="mt-4 space-x-2">
                <Link
                  to={`/booksets/edit/${bs.setId}`}
                  className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                >
                  Edit
                </Link>
                <button
                  onClick={() => handleDelete(bs.setId)}
                  className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyBookSetsPage;
