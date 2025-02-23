

import { useContext, useEffect, useState } from "react";
import Confirmation from "../../Component/Confirmation.js";
import { RouteToprevBtn } from "../../Component/Button.js";
import { Navigate, useNavigate } from "react-router-dom";
import { messageContext } from "../../../context/MessageContext";
import axiosApi from "../../../utils/axiosApi";
import CategoryWisePlayers from "./CategoryWisePlayers";


export function AuctionPlayers() {
    
    const navigate = useNavigate()
    const [players, setPlayers] = useState([])
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
    const [id, setId] = useState(0);
    const [categories, setCategories] = useState([]);
    const auction = JSON.parse(localStorage.getItem('auction'));


    const [confirmation, setConfirmation] = useState(false);
    useEffect(() => {
        window.scrollTo(0, 0);
        fetchData();
        fetchCategories();
    }, [])

    const fetchCategories = async () => {
        try {
            const response = await axiosApi.get(`/show-auction-category/${auction?.auctionId}`);
            setCategories(response?.data);
        } catch (e) {
            setErrorMessage(e?.response?.data?.message || "Failed to load categories! please try again.");
        }
    }

    const fetchData = async () => {
        if (!auction) {
            setErrorMessage("Auction not found");
            return;
        }
        try {
            const response = await axiosApi.get(`/auction-player/${auction?.auctionId}`)
            const replacedCategoryIdPlayers = response.data.map((player) => {
                return player.categoryId !== null ? { ...player, "categoryId": player.categoryId.categoryId } : { ...player, [player.categoryId]: null }
            });
            setPlayers(replacedCategoryIdPlayers);
        } catch (err) {
            setErrorMessage(err.response.data.message || "Failed to load players! please try again.")
        }
    }

    const deletePlayer = async () => {
        try {
            await axiosApi.delete(`/delete-player/${id}/${auction?.auctionId}`)
            setId(0);
            setConfirmation(false)
            fetchData()
            setSuccessMessage("Player deleted successfully!")
        } catch (err) {
            console.log(err)
            setErrorMessage(err.response.data.message || "Something went wrong! please try again.");
        }
    }

    const deleteConfirmation = (e) => {
        setId(e.target.value)
        setConfirmation(true);
    }

    const addPlayer = () => {
        navigate("/auction/player-form", { state: { for: "newForm" } })
    }

    const editPlayer = (player) => {
        navigate("/auction/player-form", { state: { for: "editForm", player: player } })
    }


    if (!auction) {
        setErrorMessage("Auction not found!")
        return <Navigate to={"/"} />
    }


    return (

        <div className=' lg:w-3/4 flex flex-col'>

            {confirmation && <Confirmation setId={setId} deleteFun={deletePlayer} setConfirmation={setConfirmation} />}

            <div className='xl:w-[65vw] lg:w-[60vw] md:w-[60vw] sm:w-[70vw] shadow-lg rounded-xl mx-4  flex space-y-4 justify-between items-center px-4 py-2  flex-col lg:flex-row md:flex-col sm:flex-col '>
                <h1 className='text-xs text-blue-600 md:text-lg lg:text-2xl sm:text-xs'>{auction.auctionName.toUpperCase()}<span className='text-xs ml-3 lg:text-xl sm:text-xs'>Players</span></h1>

                <button onClick={addPlayer} className='rounded-md border p-2 hover:bg-blue-600 hover:text-white '>Add Player</button>
            </div>


            {categories.length > 0 && <CategoryWisePlayers categories={categories} setPlayers={setPlayers} fetchData={fetchData} />}

            {/* <------------ player card------------------------> */}
            <div className="mx-4 my-4 xl:w-[65vw] lg:w-[60vw] md:w-[60vw] sm:w-[70vw] grid xl:grid-cols-3 lg:grid-cols-2 md:grid-cols-2  gap-10 mr-4 bg-white  rounded-xl">
                {players.length !== 0 ? players.map((player) => (
                    <div key={player.playerId} className="w-full bg-white border-2 rounded-xl">
                        <span className="p-2 rounded-2xl bg-blue-900 text-white">Form no.{player.formNo}</span>
                        <div className="flex flex-col items-center pb-5">
                            <img
                                alt="Bonnie image"
                                height="96"
                                src="/playerimage.png"
                                width="96"
                                className="mb-3 rounded-full shadow-lg"
                            />
                            <h5 className="mb-1 text-xl font-medium text-gray-900 dark:text-gray-900">{player.playerName}</h5>
                            <span className="text-sm text-gray-500 dark:text-gray-400">{player.playerStyle}</span>
                            <div className="mt-4 flex space-x-3 lg:mt-6">
                                <p className="inline-flex items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-black">
                                    age: {player.playerAge}
                                </p>
                                <p className="inline-flex items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-black">
                                    jerssey no.{player.jersseyNumber}
                                </p>
                            </div>
                            <div className="mt-4 flex space-x-12 lg:mt-6">
                                <button onClick={() => { editPlayer(player) }}
                                    className="inline-flex border-2 border-blue-700 bg-blue-700  items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white"
                                >
                                    Edit
                                </button>
                                <button onClick={deleteConfirmation} value={player.playerId}
                                    className="inline-flex border-2 border-red-500 bg-red-500 hover:bg-red-600  items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white"
                                >
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                )) : <p className="text-xl ml-10 mt-5">There is no player in this auction.</p>}
            </div>

            <RouteToprevBtn onClick={() => navigate("/auction/auction-details")} />
        </div >
    );
}

export default AuctionPlayers