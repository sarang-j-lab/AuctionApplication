import ReactDOM from 'react-dom/client';
import './input.css';
import App from './App';
import MessageProvider from './context/MessageContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <MessageProvider>
    <App />
  </MessageProvider>

);

