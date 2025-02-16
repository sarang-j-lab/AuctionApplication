import React from 'react';
import { getRole } from '../utils/JwtConfig';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';
import AdminAllUsers from '../componets/AdminComponent/AdminAllUsers';
import AdminUserAuctions from '../componets/AdminComponent/AdminUserAuctions';
import AdminAucitonDetails from '../componets/AdminComponent/AdminAuctionDetails';

const AdminPage = () => {


  const user = JSON.parse(localStorage.getItem("user"));



  return (
    <>

      {getRole(user?.token) === "ADMIN" ? <Routes>
        <Route path='/' element={<AdminAllUsers />} />
        <Route path='/user-auctions/:userId' element={<AdminUserAuctions />} />
        <Route path='/auction-details' element={<AdminAucitonDetails/>}/>
      </Routes> : <Navigate to={"/"} />}
    </>
  )
}

export default AdminPage 