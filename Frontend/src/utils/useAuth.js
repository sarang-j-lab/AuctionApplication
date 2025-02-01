import { useContext } from "react"
import { userContext } from "../context/UserContext"


const useAuth = ()=>{
    return useContext(userContext);
}

export default useAuth;