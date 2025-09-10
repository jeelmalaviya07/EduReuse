import React, { useState, useEffect, useContext } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const BookSetForm = ({ editMode = false }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { axiosAuth, auth } = useContext(AuthContext);

  const initialBook = {
    title: '',
    author: '',
    subject: '',
    book_condition: '',
    price: '',
    quantity: '1', // Default to 1
    imageUrl: ''
  };

  const [form, setForm] = useState({
    title: '',
    classLevel: '',
    board: '',
    listingType: 'SELL',
    bookCondition: '',
    imageUrl: '',
    completeSet: false,
    price: '',
    desiredClassLevel: '',
    desiredBoard: '',
    desiredCompleteSet: false,
    books: [{ ...initialBook }]
  });

  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    const loadBookSet = async () => {
      if (!editMode || !id) return;

      try {
        const res = await axiosAuth.get(`/api/users/booksets/${id}`);
        const bs = res.data;

        setForm({
          title: bs.title || '',
          classLevel: bs.classLevel?.toString() || '',
          board: bs.board || '',
          listingType: bs.listingType || 'SELL',
          bookCondition: bs.bookConditionDescription || '',
          imageUrl: bs.imageUrl || '',
          completeSet: bs.completeSet || false,
          price: bs.price?.toString() || '',
          desiredClassLevel: bs.desiredClassLevel?.toString() || '',
          desiredBoard: bs.desiredBoard || '',
          desiredCompleteSet: bs.desiredCompleteSet || false,
          books: bs.books?.length > 0
            ? bs.books.map(b => ({
                ...b,
                price: b.price?.toString() || '',
                quantity: b.quantity?.toString() || '1'
              }))
            : [{ ...initialBook }]
        });
      } catch (err) {
        console.error('Failed to load book set:', err);
        setError('Failed to load book set. Please try again.');
      }
    };

    loadBookSet();
  }, [editMode, id, axiosAuth]);

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleBookChange = (index, e) => {
    const { name, value } = e.target;
    setForm(prev => {
      const updatedBooks = [...prev.books];
      updatedBooks[index] = { ...updatedBooks[index], [name]: value };
      return { ...prev, books: updatedBooks };
    });
  };

  const addBook = () => {
    setForm(prev => ({
      ...prev,
      books: [...prev.books, { ...initialBook }]
    }));
  };

  const removeBook = index => {
    setForm(prev => ({
      ...prev,
      books: prev.books.filter((_, i) => i !== index)
    }));
  };

 const handleSubmit = async e => {
   e.preventDefault();
   setError('');
   setIsSubmitting(true);

   try {
     const errors = [];
     if (!form.title.trim()) errors.push('Title is required');
     if (!form.classLevel) errors.push('Class level is required');
     form.books.forEach((book, i) => {
       if (!book.title.trim()) errors.push(`Book ${i+1} needs a title`);
     });

     if (errors.length) throw new Error(errors.join(', '));

     const payload = {
       title: form.title.trim(),
       classLevel: parseInt(form.classLevel),
       board: form.board.trim() || null,
       listingType: form.listingType,
       bookCondition: form.bookCondition.trim() || null,
       imageUrl: form.imageUrl|| null,//changed
       completeSet: form.completeSet,
       price: form.listingType === 'SELL' ? parseFloat(form.price || 0) : null,
       desiredClassLevel: form.desiredClassLevel ? parseInt(form.desiredClassLevel) : null,
       desiredBoard: form.desiredBoard.trim() || null,
       desiredCompleteSet: form.desiredCompleteSet,
       sellerId: auth.userId,
       books: form.books.map(book => ({
         title: book.title.trim(),
         author: book.author.trim() || null,
         subject: book.subject.trim() || null,
         book_condition: book.book_condition?.trim() || null,
         price: book.price ? parseFloat(book.price) : null,
         quantity: book.quantity ? parseInt(book.quantity) : 1,
         imageUrl: book.imageUrl || null//changed
       }))
     };

     console.log('Submitting:', payload);

     const response = editMode
       ? await axiosAuth.put(`/api/users/booksets/${id}`, payload)
       : await axiosAuth.post(`/api/users/booksets`, payload);

     navigate('/booksets', { state: { success: true } });
   } catch (err) {
     console.error('Submission failed:', {
       error: err,
       response: err.response?.data
     });

     setError(
       err.response?.data?.message ||
       err.response?.data?.error ||
       err.message ||
       'Failed to save. Please check your data and try again.'
     );
   } finally {
     setIsSubmitting(false);
   }
 };

  return (
    <div className="max-w-2xl mx-auto p-6 bg-white rounded shadow mb-8">
      <h2 className="text-2xl font-bold mb-4">
        {editMode ? 'Edit Book Set' : 'Create New Book Set'}
      </h2>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        {/* Title Field */}
        <div>
          <label className="block">Title *</label>
          <input
            name="title"
            required
            className="w-full border px-3 py-2 rounded"
            value={form.title}
            onChange={handleChange}
          />
        </div>

        {/* Class Level and Board */}
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label>Class Level *</label>
            <input
              name="classLevel"
              type="number"
              required
              min="1"
              className="w-full border px-3 py-2 rounded"
              value={form.classLevel}
              onChange={handleChange}
            />
          </div>
          <div>
            <label>Board</label>
            <input
              name="board"
              className="w-full border px-3 py-2 rounded"
              value={form.board}
              onChange={handleChange}
            />
          </div>
        </div>

        {/* Listing Type */}
        <div>
          <label>Listing Type</label>
          <select
            name="listingType"
            className="w-full border px-3 py-2 rounded"
            value={form.listingType}
            onChange={handleChange}
          >
            <option value="SELL">Sell</option>
            <option value="DONATION">Donation</option>
            <option value="SWAP">Swap</option>
          </select>
        </div>

        {/* Conditional Fields Based on Listing Type */}
        {(form.listingType === 'SELL' || form.listingType === 'DONATION') && (
          <>
            <div>
              <label>Price {form.listingType === 'SELL' && '*'}</label>
              <input
                name="price"
                type="number"
                step="0.01"
                min="0"
                className="w-full border px-3 py-2 rounded"
                value={form.price}
                onChange={handleChange}
                required={form.listingType === 'SELL'}
                disabled={form.listingType !== 'SELL'}
              />
            </div>
            <div>
              <label>Book Condition</label>
              <input
                name="bookCondition"
                className="w-full border px-3 py-2 rounded"
                value={form.bookCondition}
                onChange={handleChange}
              />
            </div>
          </>
        )}

        {form.listingType === 'SWAP' && (
          <>
          <div>
            <label>Book Condition</label>
            <input
              name="bookCondition"
              className="w-full border px-3 py-2 rounded"
              value={form.bookCondition}
              onChange={handleChange}
            />
          </div>
            <div>
              <label>Desired Class Level</label>
              <input
                name="desiredClassLevel"
                type="number"
                min="1"
                className="w-full border px-3 py-2 rounded"
                value={form.desiredClassLevel}
                onChange={handleChange}
              />
            </div>
            <div>
              <label>Desired Board</label>
              <input
                name="desiredBoard"
                className="w-full border px-3 py-2 rounded"
                value={form.desiredBoard}
                onChange={handleChange}
              />
            </div>
             <div>
                 <label className="flex items-center">
                     <input
                        type="checkbox"
                        name="desiredCompleteSet"
                        checked={form.desiredCompleteSet}
                        onChange={handleChange}
                        className="mr-2"
                     />
                     Desired Complete Set
                 </label>
             </div>
          </>
        )}

        {/* Common Fields */}
        <div>
          <label>Image URL</label>
          <input
            name="imageUrl"
            type="url"
            className="w-full border px-3 py-2 rounded"
            value={form.imageUrl}
            onChange={handleChange}
          />
        </div>

        <div>
          <label className="flex items-center">
            <input
              type="checkbox"
              name="completeSet"
              checked={form.completeSet}
              onChange={handleChange}
              className="mr-2"
            />
            Complete Set
          </label>
        </div>

        {/* Books in Set */}
        <div>
          <h3 className="font-semibold">Books in Set *</h3>
          {form.books.map((book, index) => (
            <div key={index} className="border rounded p-4 mb-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label>Title *</label>
                  <input
                    name="title"
                    required
                    className="w-full border px-2 py-1 rounded"
                    value={book.title}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
                <div>
                  <label>Author</label>
                  <input
                    name="author"
                    className="w-full border px-2 py-1 rounded"
                    value={book.author}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 mt-2">
                <div>
                  <label>Subject</label>
                  <input
                    name="subject"
                    className="w-full border px-2 py-1 rounded"
                    value={book.subject}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
                <div>
                  <label>Condition</label>
                  <input
                    name="book_condition"
                    className="w-full border px-2 py-1 rounded"
                    value={book.book_condition}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4 mt-2">
                <div>
                  <label>Price</label>
                  <input
                    name="price"
                    type="number"
                    step="0.01"
                    min="0"
                    className="w-full border px-2 py-1 rounded"
                    value={book.price}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
                <div>
                  <label>Quantity *</label>
                  <input
                    name="quantity"
                    type="number"
                    min="1"
                    required
                    className="w-full border px-2 py-1 rounded"
                    value={book.quantity}
                    onChange={(e) => handleBookChange(index, e)}
                  />
                </div>
              </div>

              <div className="mt-2">
                <label>Image URL</label>
                <input
                  name="imageUrl"
                  type="url"
                  className="w-full border px-2 py-1 rounded"
                  value={book.imageUrl}
                  onChange={(e) => handleBookChange(index, e)}
                />
              </div>

              {form.books.length > 1 && (
                <button
                  type="button"
                  onClick={() => removeBook(index)}
                  className="mt-2 text-red-600 hover:underline"
                >
                  Remove Book
                </button>
              )}
            </div>
          ))}

          <button
            type="button"
            onClick={addBook}
            className="bg-gray-200 text-gray-700 px-3 py-1 rounded hover:bg-gray-300"
          >
            Add Another Book
          </button>
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 disabled:opacity-50"
          disabled={isSubmitting}
        >
          {isSubmitting ? (
            <span className="flex items-center justify-center">
              <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              Processing...
            </span>
          ) : editMode ? 'Update Book Set' : 'Create Book Set'}
        </button>
      </form>
    </div>
  );
};

export default BookSetForm;