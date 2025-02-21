import { useContext, useState } from 'react'

import { messageContext } from '../../context/MessageContext';
import axiosApi from '../../utils/axiosApi';
import { Navigate } from 'react-router-dom';
import LoadingBar from "../LoadingBar.js";

const AuctionIncrements = () => {

    const auction = JSON.parse(localStorage.getItem("auction"));
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
    const [loading, setLoading] = useState(false);

    const deleteAuctionIncrement = async (e) => {
        const { value } = e.target;
        if (!auction) {
            setErrorMessage("Auction not found!")
            return;
        }
        try {
            setLoading(true);
            const response = await axiosApi.delete(`/auction/delete-increment/${auction.auctionId}/${value}`)
            setSuccessMessage("Additional Increment deleted successfully!")
            localStorage.setItem("auction", JSON.stringify(response?.data));
            setLoading(false);
        } catch (e) {
            setLoading(false);
            setErrorMessage(e.response.data.message || "Something went wrong! please try again.");
        }
    }

    if (!auction) {
        setErrorMessage("User not found!");
        return <Navigate to={"/"} />
    }


    return (
        <>
            <div className='mx-auto xl:w-[25vw] sm:w-full flex justify-center flex-col items-center rounded-lg border-1 shadow-lg py-5 px-6'>
                <h1>Additional increments</h1>
                {auction.additionalIncrements.length > 0 &&
                    auction.additionalIncrements.map((increm,index) => (
                        <>
                            {!loading ? <div key={index} className='flex flex-row gap-2 justify-evenly items-center text-sm mt-1 '>
                                <p>Increment: {increm.increment}</p>
                                <p>After: {increm.after}</p>
                                <button id={auction.auctionId} value={increm.id} onClick={deleteAuctionIncrement} className='border-2 px-1 self-end hover:bg-red-400 hover:border-red-400'>X</button>
                            </div> : <LoadingBar />}
                        </>
                    ))
                }
            </div>
        </>
    )
}

export default AuctionIncrements