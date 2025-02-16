import React, { useContext, useEffect, useState } from 'react'
import axiosApi from '../../utils/axiosApi'
import { messageContext } from '../../context/MessageContext';
import { useNavigate } from 'react-router-dom';
import { RouteToprevBtn } from '../Button';

const AdminAllUsers = () => {


    const [users, setUsers] = useState(null);
    const { setErrorMessage } = useContext(messageContext);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAllUsers = async () => {
            try {
                const response = await axiosApi.get("/admin/get-all-user")
                setUsers(response?.data);
            } catch (error) {
                setErrorMessage(error?.response?.data?.message || "Not able to fetch all users please try agian!");
            }
        }
        fetchAllUsers();
    }, [])

    const showAuctions = (event)=>{
        const {value} = event?.target;
        navigate(`/admin/user-auctions/${value}`);
    }


    return (
        <>
            <RouteToprevBtn onClick={()=> navigate("/")}/>
            <h1 className='m-4 text-2xl font-bold'>All Users</h1>
            <div className='grid xl:grid-cols-4 sm:grid-cols-1 lg:grid-cols-3 md:grid-cols-2 gap-2 m-4'>
                {
                    users && users.map((user) => (
                        <div className="w-full max-w-sm bg-white border border-gray-200 rounded-lg shadow-sm ">

                            <div className="flex flex-col items-center pb-10">
                                <div className=" flex justify-center items-center w-24 h-24 mb-3 rounded-full shadow-lg text-3xl">
                                    {user?.name.charAt(0).toUpperCase()}
                                </div>
                                <h5 className="mb-1 text-xl font-medium ">
                                    {user?.name}
                                </h5>
                                <span className="text-sm text-gray-500 dark:text-gray-400">
                                    Mobile No. {user?.mobileNo}
                                </span>
                                <div className="flex mt-4 md:mt-6">
                                    <button onClick={showAuctions} value={user?.userId} className="inline-flex items-center px-4 py-2 text-sm font-medium text-center text-white bg-blue-700 rounded-lg hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">
                                        User Auctions
                                    </button>
                                    <button className="bg-red-500 py-2 px-4 ms-2 text-sm font-medium text-gray-900 focus:outline-none  rounded-lg border border-red-500 hover:bg-red-600     focus:z-10 focus:ring-4 ">
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))
                }
            </div>
        </>
    )
}

export default AdminAllUsers


