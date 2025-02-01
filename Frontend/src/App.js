import React, { useContext, useEffect } from 'react'
import { BrowserRouter as Router, Routes, Route, useNavigate } from 'react-router-dom'
import Authentication from './pages/Authentication'
import { messageContext } from './context/MessageContext'
import { ErrorToast, SuccessToast } from './componets/Toast'
import Footer from './componets/Footer';
import Navbar from './componets/Navbar'
import ProtectedRoutes from './pages/ProtectedRoutes'
import AuctionProvider from './context/AuctionContext';
import UserProvider from './context/UserContext';
import MessageProvider from './context/MessageContext';
import CategoryProvider from './context/CategoryContext';
const App = () => {

  const { successMessage, setSuccessMessage, errorMessage, setErrorMessage } = useContext(messageContext);


  useEffect(() => {
    setTimeout(() => {
      setSuccessMessage("");
      setErrorMessage("")
    }, 4000);

  }, [successMessage, errorMessage])

  return (
    <Router>
      {successMessage && (
        <SuccessToast
          message={successMessage}
          onClose={() => setSuccessMessage("")}
        />
      )}
      {errorMessage && (
        <ErrorToast
          message={errorMessage}
          onClose={() => setErrorMessage("")}
        />
      )}
 
            <Navbar />
            <Routes>
              <Route path='/authentication' element={<Authentication />} />

              <Route path='*' element={<ProtectedRoutes />} />
            </Routes>
            <Footer />

    </Router>

  )
}

export default App