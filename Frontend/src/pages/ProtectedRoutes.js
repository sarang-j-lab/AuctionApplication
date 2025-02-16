import { getRole, isTokenExpired } from '../utils/JwtConfig';
import { Navigate, Outlet} from 'react-router-dom';



const ProtectedRoutes = () => {


    const user = JSON.parse(localStorage.getItem("user"));




    const isAuthenticated = user && !isTokenExpired(user?.token);

 

    return   isAuthenticated   ?
        <>
            <Outlet/>
        </> : <Navigate to={"/authentication"} /> 
}

export default ProtectedRoutes