

import { useContext, useEffect, useState } from "react";
import Confirmation from "../../Confirmation";
import { RouteToprevBtn } from "../../Button";
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
                                src="data:image/png;base64,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"
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