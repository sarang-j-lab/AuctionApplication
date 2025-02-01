import  { createContext, useEffect,  useState } from 'react'


import { useNavigate } from 'react-router-dom';

export const userContext = createContext();



const UserProvider = ({ children }) => {

    const navigate = useNavigate();
    const [user, setUser] = useState({})

    



    return (
        <userContext.Provider value={{ user, setUser }}>{children}</userContext.Provider>
    )
}

export default UserProvider