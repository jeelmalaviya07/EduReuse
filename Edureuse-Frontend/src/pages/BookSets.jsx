import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const BookSetsPage = () => {
  const { axiosAuth, auth } = useContext(AuthContext);
  const [bookSets, setBookSets] = useState([]);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetchBookSets();
  }, []);

  const fetchBookSets = async () => {
    try {
      const res = await axiosAuth.get('/api/users/booksets/withSellerId');

      const filteredPairs = res.data.filter(pair => pair.b !== auth.userId); // pair.b is sellerId

      const promises = filteredPairs.map(async pair => {
        const bookSet = pair.a;
        const sellerId = pair.b;

        try {
          const sellerRes = await axiosAuth.get(`/api/users/${sellerId}`);
          const seller = sellerRes.data;
          return { ...bookSet, seller }; // attach seller
        } catch (e) {
          console.error(`Failed to fetch seller with ID ${sellerId}`, e);
          return { ...bookSet, seller: { name: 'Unknown', city: '-', state: '-' } };
        }
      });

      const completeBookSets = await Promise.all(promises);
      setBookSets(completeBookSets);
    } catch (err) {
      console.error(err);
      setError('Failed to load book sets.');
    }
  };

  const handleBuy = (id) => {
    navigate(`/payment/${id}`);
  };

  const handleSwap = (id) => {
    navigate(`/swap/select-match/${id}`);
  };

  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold mb-4">Available Book Sets</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {bookSets.map((bs) => (
          <div key={bs.setId} className="bg-white rounded shadow p-4">
            <h3 className="font-semibold text-xl mb-2">{bs.title}</h3>

            <p><strong>Class Level:</strong> {bs.classLevel}</p>
            <p><strong>Board:</strong> {bs.board}</p>
            <p><strong>Listing Type:</strong> {bs.listingType}</p>
            {bs.price && <p><strong>Price:</strong> ₹{bs.price}</p>}

            {/* Seller Info */}
            {bs.seller && (
              <>
                <p className="mt-2"><strong>Seller:</strong> {bs.seller.fullName}</p>
                <p><strong>Location:</strong> {bs.seller.city}, {bs.seller.state}</p>
              </>
            )}

            {/* Book List */}
            <p className="mt-2 font-semibold">Books in this set:</p>
            <ul className="list-disc list-inside text-sm text-gray-700 mb-2">
              {bs.books?.map(book => (
                <li key={book.bookId}>
                  {book.name} ({book.subject}) – <em>Condition:</em> {book.book_condition}
                </li>
              ))}
            </ul>

            {/* Action Buttons */}
            <div className="mt-4 space-x-2">
              {bs.listingType === 'SELL' && (
                <button
                  type="button"
                  onClick={() => handleBuy(bs.setId)}
                  className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                >
                  Buy
                </button>
              )}

              {bs.listingType === 'SWAP' && (
                <button
                  type="button"
                  onClick={() => handleSwap(bs.setId)}
                  className="bg-blue-600 text-white px-3 py-1 rounded hover:bg-blue-700"
                >
                  Swap
                </button>
              )}

              {bs.listingType === 'DONATION' && (
                <button
                  type="button"
                  disabled
                  className="bg-gray-500 text-white px-3 py-1 rounded cursor-not-allowed"
                >
                  Free
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {bookSets.length === 0 && (
        <p className="text-gray-600 mt-4">No book sets available for you at the moment.</p>
      )}
    </div>
  );
};

export default BookSetsPage;
