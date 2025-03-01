import React, { useContext, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import { messageContext } from '../context/MessageContext';
import axiosApi from '../utils/axiosApi';
import { RouteToprevBtn } from '../componets/Component/Button';
import LoadingBar from '../componets/Component/LoadingBar';

const AuctionPanelMenu = () => {

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

    if(!user){
        return navigate("/authentication")
    }


    const selectAuction = (auction) => {
        localStorage.setItem("auction", JSON.stringify(auction));
        navigate("/auction-dashboard")
    }

    return (
        <>
            <div className="h-screen w-screen bg-center bg-contain flex flex-col text-white p-10 gap-10" style={{ backgroundImage: 'url("/auctionpanelimage.jpg")', }}>
            <h1 className='bg-white/20 backdrop-blur-md flex justify-center font-serif text-[4vw] py-4 rounded-lg'>{user?.user?.name}'s Auctions</h1>
                {userAuctions && <div className='flex gap-10'>
                    {userAuctions.map((auction) => (
                        <div onClick={() => selectAuction(auction)} className='cursor-pointer bg-white/20 backdrop-blur-md w-[30vw] h-[20vh] hover:w-[35vw] hover:h-[25vh] transition-all duration-500 rounded-xl flex flex-col'>
                            <p className='h-1/2 w-full flex justify-center items-center text-[2vw] font-serif'>{auction?.auctionName}</p>
                            <p className='h-1/2 w-full flex justify-center font-serif gap-3 text-[1vw]'><span>{auction?.auctionDate}</span><span>{auction?.auctionTime}</span></p>
                        </div>
                    ))}
                </div> }
                <RouteToprevBtn onClick={() => { navigate("/") }} />
            </div>
        </>
    )
}

export default AuctionPanelMenu