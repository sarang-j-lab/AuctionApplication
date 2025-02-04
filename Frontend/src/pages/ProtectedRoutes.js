import { isTokenExpired } from '../utils/JwtConfig';
import { Navigate, Outlet, Route, Routes } from 'react-router-dom';
import Dashboard from './Dashboard';
import Panel from '../componets/AuctionPanel/Panel';


const ProtectedRoutes = () => {

    const user = JSON.parse(localStorage.getItem("user"));



    const isAuthenticated = user && !isTokenExpired(user.token);


    return isAuthenticated ?
        <>
            {/* <Dashboard /> */}
            <Outlet/>
        </> : <Navigate to={"/authentication"} />
}

export default ProtectedRoutes