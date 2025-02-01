import React, { useEffect, useState } from 'react'
import AuctionCard from './AuctionCard';

import { useContext } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { RouteToprevBtn } from '../Button';

import axiosApi from '../../utils/axiosApi';
import { messageContext } from '../../context/MessageContext';
import LoadingBar from '../LoadingBar';

const MyAuction = () => {
    const [userAuctions, setUserAuctions] = useState([]);
    const [loading, setLaoding] = useState(true);
    const navigate = useNavigate()
    const { setErrorMessage } = useContext(messageContext);
    const user = JSON.parse(localStorage.getItem("user"));


    useEffect(() => {
        const fetchData = async () => {
            if (!user?.user?.userId) return;

            try {
                const response = await axiosApi.get(`/auction/my-auction/${user?.user?.userId}`)
                setUserAuctions(response?.data)
            } catch (error) {
                setErrorMessage(error?.response?.data?.message || "Failed to load auction data! please try again.")
            } finally {
                setLaoding(false)
            }
        }
        fetchData();
    }, [])


    if (!user) {
        <Navigate to={"/authentication"} />
    }


    return (
        <>
            <div className='w-full lg:w-3/4'>
                {loading && <LoadingBar />}
                {!loading && userAuctions.length === 0  && <div className='text-xl'>Ooop! you not have any auction </div>}
                {userAuctions.length > 0 && <AuctionCard userAuctions={userAuctions} />}
                <RouteToprevBtn onClick={() => navigate('/')} />
            </div>
        </>

    )
}

export default MyAuction