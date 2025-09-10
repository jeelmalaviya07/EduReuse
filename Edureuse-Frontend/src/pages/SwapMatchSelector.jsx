import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const SwapMatchSelector = () => {
  const { requestedId } = useParams();
  const { axiosAuth, auth } = useContext(AuthContext);
  const [matchingSets, setMatchingSets] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    fetchData();
  }, [requestedId]);

  const fetchData = async () => {
    setLoading(true);
    try {
      const [matchRes, sentRes] = await Promise.all([
        axiosAuth.get(`/api/swaps/match/${requestedId}?requesterId=${auth.userId}`),
        axiosAuth.get(`/api/swaps/${auth.userId}/sent`)
      ]);

      const sentRequests = sentRes.data.filter(
        req => req.offeredBookSetId && req.requestedBookSetId
      );

      const updatedMatches = matchRes.data.map(set => {
        const alreadySent = sentRequests.some(req =>
          req.offeredBookSetId?.setId === parseInt(requestedId) &&
          req.requestedBookSetId?.setId === set.setId
        );
        console.log(set.setId);
        console.log(sentRes);
        return { ...set, alreadySentRequest: alreadySent };
      });

      setMatchingSets(updatedMatches);
    } catch (err) {
      console.error(err);
      setError('Failed to fetch matching book sets or sent requests.');
    } finally {
      setLoading(false);
    }
  };

  const handleSendRequest = async (offeredSetId) => {
    try {
      await axiosAuth.post(`/api/swaps/${auth.userId}/create/${requestedId}/${offeredSetId}`);
      alert('Swap request sent successfully.');
      navigate('/my-swap-requests');
    } catch (err) {
      console.error(err);
      alert('Failed to send swap request.');
    }
  };

  if (loading) return <div className="p-8 text-gray-600">Loading matching book sets...</div>;

  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold mb-6">Select One of Your Matching Book Sets</h2>

{/*      {error && <div className="text-red-600 mb-4">{error}</div>} */}

      {matchingSets.length === 0 ? (
        <div className="text-gray-700">
          You don’t have any matching book sets to swap with this one.
          <br />
          Please add a book set with matching <strong>Class Level</strong>, <strong>Board</strong>, and <strong>Complete Set</strong>.
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {matchingSets.map(bs => (
            <div key={bs.setId} className="bg-white rounded-lg shadow p-4">
              <h3 className="text-xl font-semibold mb-2">{bs.title}</h3>
              <p><strong>Class Level:</strong> {bs.classLevel}</p>
              <p><strong>Board:</strong> {bs.board}</p>
              <p><strong>Complete Set:</strong> {bs.completeSet ? 'Yes' : 'No'}</p>

              {bs.books?.length > 0 && (
                <ul className="list-disc text-sm mt-2 text-gray-700 ml-4">
                  {bs.books.map(book => (
                    <li key={book.bookId}>
                      {book.name} ({book.subject}) – <em>{book.condition}</em>
                    </li>
                  ))}
                </ul>
              )}

              {bs.alreadySentRequest ? (
                <div className="mt-4 text-sm bg-yellow-100 border border-yellow-300 text-yellow-800 px-3 py-2 rounded">
                  ✅ You’ve already sent a swap request with this book set.
                </div>
              ) : (
                <button
                  onClick={() => handleSendRequest(bs.setId)}
                  className="mt-4 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
                >
                  Swap with this Set
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SwapMatchSelector;
