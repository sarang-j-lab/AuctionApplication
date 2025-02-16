import React, { useContext, useState } from 'react'
import { CiCalendarDate } from 'react-icons/ci'
import { FaArrowRightToBracket } from 'react-icons/fa6'
import AuctionIncrements from '../AuctionComponets/AuctionIncrements'
import { TiGroupOutline } from 'react-icons/ti'
import { BiCategory, BiSolidCricketBall } from 'react-icons/bi'
import { useLocation, useNavigate } from 'react-router-dom'
import { RouteToprevBtn } from '../Button'
import axiosApi from '../../utils/axiosApi'
import { messageContext } from '../../context/MessageContext'

const AdminAucitonDetails = () => {

    const location = useLocation();
    const auction = location.state;
    const navigate = useNavigate()

    const [size,setSize] = useState(auction?.teamSize);
    const {setErrorMessage} = useContext(messageContext);

    const handleTeamUpdate = async(event)=>{
        event.preventDefault();
        try {
            const response = await axiosApi.put(`/admin/update-team-size/${auction?.auctionId}`,{teamSize: size},{headers:{
                "Content-Type":"application/json"
            }});
            console.log(response);
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Not able to update team");
        }
    }


    return (
        <>
            {auction && <div className='w-full h-full  flex flex-col   space-y-5 lg:w-3/4 sm:w-full'>

                <>
                    <h1 className='text-2xl text-blue-400 mx-auto lg:text-4xl sm:text-2xl'>Auction Details</h1>
                    <div className="bg-white shadow-lg rounded-lg lg:max-w-2xl xl:max-w-3xl  w-[70vw] sm:w-[70vw]  mx-auto p-3 space-y-6 md:space-y-4">


                        <div className="text-base lg:text-2xl font-bold text-gray-800 sm:text-base flex flex-col xl:flex-row sm:flex-col justify-center xl:items-end sm:items-start">
                            {auction.auctionName.toUpperCase()}  <sub className='text-xs ml-2'>code: {auction.auctionId}</sub>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

                            {[
                                { label: "Points per team", value: auction.pointsPerTeam.toLocaleString() },
                                { label: "Base bid", value: auction.baseBid.toLocaleString() },
                                { label: "Bid increase by", value: auction.bidIncreaseBy.toLocaleString() },
                                { label: "Max player per team", value: auction.maxPlayerPerTeam },
                                { label: "Min player per team", value: auction.minPlayerPerTeam },
                            ].map((item, i) => (
                                <div key={i} className="flex justify-between items-center">
                                    <span className="text-blue-400 font-medium">{item.label}</span>
                                    <span className="text-gray-800 font-semibold">{item.value}</span>
                                </div>
                            ))}
                            <div className='flex space-x-4 text-sm'>
                                <p className='flex items-center font-bold'><CiCalendarDate className='mx-2' />Date: {auction.auctionDate}</p>
                                <p className='flex items-center font-bold text-sm '>{auction.auctionTime}</p>
                            </div>
                        </div>


                        <div className="flex items-center justify-between flex-col lg:flex-row sm:flex-col gap-5 ">
                            <form onSubmit={handleTeamUpdate}>
                                <label htmlFor='teamsize'>Update Team size</label>
                                 <input id='teamsize' onChange={(event)=> setSize(event?.target?.value)} value={size} type='number' className='w-20 border-2 ml-4 border-gray-700 px-2' />
                                 <button className='ml-4 w-20 px-2 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600'>Update</button>
                            </form>
                        </div>
                    </div>
                    <div className='flex flex-col justify-center '>

                        {auction.additionalIncrements.length > 0 && <AuctionIncrements />}
                    </div>

                    <div className=" bg-white shadow-lg rounded-lg w-[70vw] xl:max-w-3xl lg:max-w-2xl  sm:w-[70vw] mx-auto p-6 space-y-6 md:space-y-4">
                        <div className=" flex items-center justify-between flex-col lg:flex-row sm:flex-col gap-5">
                            {
                                [
                                    { label: "Show teams", icon: <TiGroupOutline className='mr-2' />, onClick: () => navigate(`/auction/auction-teams`) },
                                    { label: "Show categories", icon: <BiCategory className='mr-2' />, onClick: () => navigate(`/auction/auction-categories`) },
                                    { label: "Show players", icon: <BiSolidCricketBall className='mr-2' />, onClick: () => navigate("/auction/auction-players") },
                                ].map((button, i) => (
                                    <button key={i} onClick={button.onClick} className="px-6 py-2 bg-white text-black border rounded-lg hover:bg-blue-600 hover:text-white flex items-center">
                                        {button.icon}
                                        {button.label}
                                    </button>
                                ))
                            }
                        </div>
                    </div>

                    <RouteToprevBtn onClick={() => { navigate("/auction/my-auction") }} />
                </>



            </div>}
        </>
    )
}

export default AdminAucitonDetails