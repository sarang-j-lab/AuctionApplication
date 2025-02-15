import React, { useContext, useEffect, useState } from 'react'
import { useLocation, useParams } from 'react-router-dom'
import axiosApi from '../../utils/axiosApi';
import { messageContext } from '../../context/MessageContext';
import { ImCross } from "react-icons/im";
import LoadingBar from '../LoadingBar';

const ShowTeamPlayers = ({ fetchAuctionPlayers, fetchAuctionTeam, category }) => {

    const location = useLocation();
    const { teamId, auctionData, teamName } = location.state;

    const [loading, setLoading] = useState(false);

    const [teamPlayers, setTeamPlayers] = useState(null);
    const { setErrorMessage, setSuccessMessage } = useContext(messageContext);
    useEffect(() => {
        fetchTeamPlayer();
    }, [])
    const fetchTeamPlayer = async () => {
        try {
            const response = await axiosApi.get(`/show-team-players/${teamId}`)
            setTeamPlayers(response?.data);
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Failed to fetch team player please try again.");
        }
    }




    const reauctionPlayer = async (bid) => {

        const url = bid.category === null ? `/reauction-none-category-player/${auctionData?.auctionId}` : `/reauction-category-player`;
        try {
            setLoading(true)
            const response = await axiosApi({
                method: "put", url, data: bid, headers: {
                    "Content-Type": "application/json",
                }
            })
            setSuccessMessage(response?.data);
            fetchTeamPlayer();
            fetchAuctionTeam();
            fetchAuctionPlayers();
            setLoading(false)
        } catch (error) {
            setLoading(false)
            setErrorMessage(error?.response?.data?.message || "Can't able to delete player from this team");
        }


    }

    return (
        <>
            {teamPlayers ? <div className='w-[100vw] h-[90vh] bg-white/20 backdrop-blur-2xl overflow-scroll scrollbar-hide rounded-xl'>
                <div className="w-full  h-[30vh] px-20 text-white flex flex-col items-center justify-center">
                    <h1 className='text-[10vh] font-serif'>{auctionData.auctionName.toUpperCase()}</h1>
                    <h1 className='text-[5vh] font-serif text-black bg-yellow-200 px-3 rounded-sm'>{teamName} players</h1>
                </div>

                {!loading ? <div className='w-full grid grid-cols-3  p-10  gap-7 gap-y-11'>
                    {teamPlayers.map((player) => (
                        <div key={player.playerId} className='h-[21vh]  bg-white rounded-xl text-black flex flex-col items-center'>
                            <div className='flex flex-row justify-between  text-xl font-serif h-2/4 bg-green-400 w-full pl-2'><span>{player.playerName.toUpperCase()}</span><span onClick={() => reauctionPlayer(player.bid)} className='mr-3 mt-1 cursor-pointer'><ImCross /></span></div>
                            <div className='flex flex-col h-full w-full justify-center p-2  font-serif '>
                                <div className='flex justify-between'>
                                    <p><span className='text-lg'>Form no.{player.formNo}</span></p>
                                    <p><span>Category: {player?.categoryId?.categoryName ? player?.categoryId?.categoryName : "None"}</span></p>
                                </div>
                                <p className='w-full'><span className='text-sm w-2/3'></span>Style: {player.playerStyle}</p>
                                <div >Sold for {player?.bid?.amount} to {teamName}</div>
                            </div>
                        </div>
                    ))

                    }

                </div > : <LoadingBar />}
            </div> : <h1 className='text-black p-2 rounded-lg self-center bg-yellow-400 font-serif text-2xl m-10'>This auction does'nt have any player.</h1>}
        </>
    )
}

export default ShowTeamPlayers