import axios from "axios";

const axiosApi = axios.create({
    baseURL: "http://localhost:8080"
})


axiosApi.interceptors.request.use((config)=>{
    const user = JSON.parse(localStorage.getItem("user"));
    if(user){
        config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
},(error)=> Promise.reject(error));

export default axiosApi;