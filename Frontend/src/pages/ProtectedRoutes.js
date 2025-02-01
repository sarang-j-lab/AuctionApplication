import React, { useEffect, useLayoutEffect } from 'react'
import { isTokenExpired } from '../utils/JwtConfig';
import { Navigate, useNavigate } from 'react-router-dom';
import Dashboard from './Dashboard';
import LoadingBar from '../componets/LoadingBar';
import axios from 'axios';
import axiosApi from '../utils/axiosApi';

const ProtectedRoutes = () => {

    const user = JSON.parse(localStorage.getItem("user"));

   

    const isAuthenticated = user && !isTokenExpired(user.token);


    return isAuthenticated ?
        <>
            <Dashboard />
            
        </> : <Navigate to={"/authentication"} />
}

export default ProtectedRoutes