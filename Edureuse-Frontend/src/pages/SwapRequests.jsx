import React, { useState, useEffect, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';

const SwapRequestsPage = () => {
  const { axiosAuth, auth } = useContext(AuthContext);
  const [madeRequests, setMadeRequests] = useState([]);
  const [receivedRequests, setReceivedRequests] = useState([]);
  const [error, setError] = useState('');

  const userId = auth.userId;

  useEffect(() => {
    if (userId) {
      fetchRequests();
    }

  }, [userId]);

  const fetchRequests = async () => {
    try {
      const resMade = await axiosAuth.get(`/api/swaps/made/${userId}`);
      const resReceived = await axiosAuth.get(`/api/swaps/received/${userId}`);
      console.log(resReceived);
      setMadeRequests(resMade.data);
      setReceivedRequests(resReceived.data);
    } catch (err) {
      setError('Failed to fetch swap requests.');
    }
  };

  const handleAccept = async (reqId) => {
    try {
      await axiosAuth.post(`/api/swaps/${reqId}/accept/${userId}`);
      fetchRequests();
    } catch (err) {
      setError('Failed to accept request.');
    }
  };

  const handleReject = async (reqId) => {
    try {
      await axiosAuth.post(`/api/swaps/${reqId}/reject/${userId}`);
      fetchRequests();
    } catch (err) {
      setError('Failed to reject request.');
    }
  };

  const handleWithdraw = async (reqId) => {
    try {
      await axiosAuth.post(`/api/swaps/${reqId}/withdraw/${userId}`);
      fetchRequests();
    } catch (err) {
      setError('Failed to withdraw request.');
    }
  };

  return (
    <div className="p-8">
      <h2 className="text-2xl font-bold mb-4">Swap Requests</h2>
      {error && <div className="text-red-600 mb-4">{error}</div>}

      <div className="mb-6">
        <h3 className="text-xl font-semibold">Requests You Made</h3>
        {madeRequests.length === 0 && <p>No requests made.</p>}
        {madeRequests.map(req => (
          <div key={req.id} className="bg-white rounded shadow p-4 mb-2">
            <p><strong>To Swap:</strong> {req.requestedBookSetId?.title || req.requestedBookSetId}</p>
            <p><strong>You Offered:</strong> {req.offeredBookSetId?.title || req.offeredBookSetId}</p>
            <p><strong>Status:</strong> {req.swapStatus}</p>
            {req.swapStatus === 'PENDING' && (
              <button
                onClick={() => handleWithdraw(req.id)}
                className="mt-2 bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
              >
                Withdraw
              </button>
            )}
            {req.swapStatus === 'ACCEPTED' && (
              <div className="mt-2 text-green-600 font-semibold">
                ✅ Swap Completed
              </div>
            )}
          </div>
        ))}
      </div>

      <div>
        <h3 className="text-xl font-semibold">Requests You Received</h3>
        {receivedRequests.length === 0 && <p>No requests received.</p>}
        {receivedRequests.map(req => (
          <div key={req.id} className="bg-white rounded shadow p-4 mb-2">
            <p><strong>From:</strong> {req.requesterId?.fullName || req.requesterId}</p>
            <p><strong>Requested Your:</strong> {req.offeredBookSetId?.title || req.offeredBookSetId}</p>
            <p><strong>They Offered:</strong> {req.requestedBookSetId?.title || req.requestedBookSetId}</p>
            <p><strong>Status:</strong> {req.swapStatus}</p>

            {req.swapStatus === 'PENDING' && (
              <div className="mt-2 space-x-2">
                <button
                  onClick={() => handleAccept(req.id)}
                  className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                >
                  Accept
                </button>
                <button
                  onClick={() => handleReject(req.id)}
                  className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                >
                  Reject
                </button>
              </div>
            )}

            {req.swapStatus === 'ACCEPTED' && (
              <div className="mt-2 text-green-600 font-semibold">
                ✅ Swap Completed
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default SwapRequestsPage;
