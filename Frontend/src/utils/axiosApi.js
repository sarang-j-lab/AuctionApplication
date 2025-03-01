import axios from "axios";
const API_URL = process.env.REACT_APP_API_URL

const axiosApi = axios.create({
    baseURL: API_URL
})


axiosApi.interceptors.request.use((config)=>{
    const user = JSON.parse(localStorage.getItem("user"));
    if(user){
        config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
},(error)=> Promise.reject(error));

export default axiosApi;