import React, { useContext } from 'react'
import { auctionContext } from '../../context/AuctionContext'
import axios from 'axios';
import { messageContext } from '../../context/MessageContext';
import axiosApi from '../../utils/axiosApi';

const AuctionIncrements = () => {

    const auction = JSON.parse(localStorage.getItem("auction"));
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);

    const deleteAuctionIncrement = async (e) => {
        const { value } = e.target
        try {
            const response = await axiosApi.delete(`/auction/delete-increment/${auction.auctionId}/${value}`)
            setSuccessMessage("Additional Increment deleted successfully!")
            const updatedAuction = response?.data
            localStorage.setItem("auction",JSON.stringify({ additionalIncrements: updatedAuction.additionalIncrements,auctionId: updatedAuction.auctionId, auctionName: updatedAuction.auctionName, auctionDate: updatedAuction.auctionDate, auctionTime: updatedAuction.auctionTime, baseBid: updatedAuction.baseBid, bidIncreaseBy: updatedAuction.bidIncreaseBy, maxPlayersPerTeam: updatedAuction.maxPlayersPerTeam, minPlayerPerTeam: updatedAuction.minPlayerPerTeam, pointsPerTeam:updatedAuction.pointsPerTeam,reserve:updatedAuction.reserve ,season:updatedAuction.season }));
        } catch (e) {
            setErrorMessage(e.response.data.message || "Something went wrong! please try again.");
        }
    }
    

    return (
        <div className='mx-auto w-[25vw] flex justify-center flex-col items-center rounded-lg border-1 shadow-lg py-5 px-6'>
            <h1>Additional increments</h1>
            {auction.additionalIncrements.length > 0 &&
                auction.additionalIncrements.map(increm => (
                    <div key={increm.id} className='flex flex-row gap-2 justify-evenly items-center text-sm mt-1 '>
                        <p>Increment: {increm.increment}</p>
                        <p>After: {increm.after}</p>
                        <button id={auction.auctionId} value={increm.id} onClick={deleteAuctionIncrement} className='border-2 px-1 self-end hover:bg-red-400 hover:border-red-400'>X</button>
                    </div>
                ))
            }
        </div>
    )
}

export default AuctionIncrements