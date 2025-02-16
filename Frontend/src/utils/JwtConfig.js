import {jwtDecode} from "jwt-decode"; 


export const isTokenExpired = (token)=>{

    if(!token) return true;
    const {exp} = jwtDecode(token);
    return Date.now() >= exp * 1000;
}

export const getRole = (token)=>{
    if(!token ) return null;
    const {role} = jwtDecode(token)
    return role;
}