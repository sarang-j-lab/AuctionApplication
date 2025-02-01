import React from 'react';
import ReactDOM from 'react-dom/client';
import './input.css';
import App from './App';
import AuctionProvider from './context/AuctionContext';
import UserProvider from './context/UserContext';
import MessageProvider from './context/MessageContext';
import CategoryProvider from './context/CategoryContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <MessageProvider>
    <App />
  </MessageProvider>

);

