import { useContext, useEffect, useRef, useState } from "react"
import { messageContext } from "../../context/MessageContext"

import axiosApi from "../../utils/axiosApi"
import Confetti from 'react-confetti'
import { Fireworks } from "fireworks-js";
import LoadingBar from "../Component/LoadingBar";

const textShadow = {
    textShadow: "10px 10px 15px rgba(0, 0, 0, 2)"
}


const CategoryAuctionPanel = ({ stompClient,players, auctionData, teams, category, setPlayers, fetchAuctionTeam}) => {
    const auction = JSON.parse(localStorage.getItem("auction"));
    const fireworksRef = useRef(null);
    const [showPanel, setShowPanel] = useState(true);

    const [count, setCount] = useState(0)

    const [bid, setBid] = useState({ id: 1, player: null, team: null, amount: 0, category: null })
    const [sold, setSold] = useState(false)
    const [currentPlayer, setCurrentPlayer] = useState(null)
    const [currentTeam, setCurrentTeam] = useState(null)

    const [maxBid, setMaxBid] = useState(null);
    const [reserve, setReserve] = useState(null)

    const [loading, setLoading] = useState(false);
    const { setErrorMessage, setSuccessMessage } = useContext(messageContext);

    const [time, setTime] = useState(10);




    useEffect(() => {
        if (time <= 0) return;

        const timer = setInterval(() => {
            setTime((prevTime) => prevTime - 1);
        }, 1000);

        return () => clearInterval(timer);
    }, [time]);



    const placeNewPlayer = () => {
        if (!players.length) {
            setErrorMessage("This category player does'nt have player!");
            return; // Avoid unnecessary execution if no players exist
        }
        const nextCount = count === players.length ? 1 : count + 1;
        const nextPlayer = players[nextCount - 1];


        setCurrentTeam(null);
        setBid({ player: null, team: null, amount: 0, category: null });
        setCurrentPlayer(nextPlayer);
        setCount(nextCount);

        // Publish update only if Stomp client is connected
        if (stompClient?.connected) {
            stompClient.publish({
                destination: `/app/player/${auctionData.auctionId}`,
                body: JSON.stringify({ player: nextPlayer, team: null, amount: 0 }),
            });
        }

    }


    const handleTeamBid = async (team) => {
        if (!currentPlayer) {
            setErrorMessage('First select the player which you want to bid');
            return;
        }


        let noneCategoryReserve = team.noneCategoryPlayerReserve;
        let categoryReserve = team?.playerRequirement.map((requirement) => { return requirement?.reserve }).reduce((acc, num) => acc + num,0);



        const categoryRequir = team?.playerRequirement.find(req => req?.category === category?.categoryId);

        if (!categoryRequir || categoryRequir.bought >= categoryRequir.maxPlayerRequired) {
            setErrorMessage("This team's limit is full to buy from this category");
            return;
        }

       
        let finalReserve = noneCategoryReserve + categoryReserve - (categoryRequir.reserve >= category?.baseBid ? category.baseBid : 0);
        setReserve(finalReserve);
        setMaxBid(team.totalPoints - finalReserve);



        let baseBid = category?.baseBid
        let bidIncreaseBy = category?.increment
        const increments = category?.categoryAdditionalIncrements;

        increments.sort((a, b) => a.after - b.after);

        for (let index = 0; index < increments.length; index++) {
            if (bid.amount >= increments[index].after) {
                bidIncreaseBy = increments[index].increment
            }
        }


        let updatedBid = bid.amount === 0 ? { id: 1, player: currentPlayer, team: team, amount: bid.amount + baseBid, category: category?.categoryId }
            : { id: 1, player: currentPlayer, team: team, amount: bid.amount + bidIncreaseBy, category: category?.categoryId };

        if (updatedBid.amount > (team.totalPoints - finalReserve)) {
            setErrorMessage("Maximun bid amount is reached of this team");
            return;
        }
        setTime(10);
        setCurrentTeam(team);
        setBid(updatedBid)
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: `/app/bid/${auctionData.auctionId}`,
                body: JSON.stringify(updatedBid),
            });
        }

    }


    const cancleBid = () => {
        if (stompClient && stompClient.connected) {
            stompClient.publish({
                destination: `/app/player/${auctionData.auctionId}`,
                body: JSON.stringify({ player: currentPlayer, team: null, amount: 0 }),
            });
        }
        setBid({ id: 1, player: currentPlayer, team: null, amount: 0, category: null })
        setCurrentTeam(null)
    }

    const onSold = async () => {
        setShowPanel(false);
        if (bid.player === null || bid.team === null || bid.amount === 0) {
            setShowPanel(true);
            setErrorMessage("Select player and team bid for that player first!")
            return;
        }
        setTime(0)
        try {
            setLoading(true)
            await axiosApi.post(`/panel/category-sold/${auction.auctionId}`, { player: bid.player.playerId, team: bid.team.teamId, amount: bid.amount, category: bid.category }, {
                headers: {
                    "Content-Type": "application/json",
                }
            })
            setSold(true);
            setTime(0)
            const fireworks = new Fireworks(fireworksRef.current, { speed: 2, particles: 150, opacity: 0.7, });
            fireworks.start();

            setTimeout(() => {
                setCurrentPlayer(null);
                setCurrentTeam(null);
                setBid({ player: null, team: null, amount: 0, category: null });
                setSold(false);
                setShowPanel(true);
                if(fireworksRef.current){
                    fireworksRef.current.removeChild(fireworksRef.current.firstChild);
                }
                fireworks.stop();
            }, 5000);
            try {
                const response = await axiosApi.get(`/show-category-players/${category?.categoryId}`)
                const categoryPlayers = response?.data.filter((player) => player?.status === "PENDING");
                setPlayers(categoryPlayers)
            } catch (error) {
                setErrorMessage(error?.response?.data?.message || "Not able to fetch category players! please try again.")
            }
            fetchAuctionTeam();
            setSuccessMessage("Player sold successfully!");
        } catch (error) {
            setShowPanel(true);
            setErrorMessage(error?.response?.data?.message || "Bid is not executed! please try again.")
        }finally{
            setLoading(false)
        }
    }
    const onUnsold = async () => {
        setShowPanel(false);
        if (bid.team !== null || bid.amount > 0) {
            setErrorMessage("The player can't remain unsold after a bid is executed.")
            return;
        }
        if (!currentPlayer) {
            setErrorMessage("Select the player first!");
            return;
        }

        try {
            setLoading(true)
            const response = await axiosApi.put(`/add-unsold-player/${auctionData.auctionId}`, currentPlayer, {
                headers: { "Content-Type": "application/json" },
            })
            setShowPanel(true);
            setSuccessMessage(response?.data);
            setCurrentPlayer(null);
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Failed to unsold player pleae try again!");
        }finally{
            setLoading(false);
        }

    }

    return (

        <>
            <div className="h-[90vh] w-full  rounded-xl flex flex-col z-10">
                {loading && <LoadingBar />}
                {sold && <Confetti width={window.innerWidth} height={window.innerHeight} />}
                <div className="flex flex-col justify-center mt-10 self-center font-serif  bg-black  px-8 text-[40px] items-center text-sky-200 border-2 rounded-lg border-sky-200 shadow-[0_0_2px_#fff,inset_0_0_2px_#fff,0_0_5px_#08f,0_0_15px_#08f,0_0_30px_#08f]">
                    <p >{auctionData.auctionName.toUpperCase()}</p>
                    <p className="w-full text-[25px] px-8 self-center rounded-lg text-wrap">Players Auction</p>
                </div>
                <div className="self-center bg-yellow-400 px-3 py-2 text-black">{category.categoryName} Player </div>
                <div className="h-[80vh] flex gap-2 " >
                    <div className="w-1/3 h-full flex items-end ">
                        {currentPlayer && <div className="bg-[#e67e22] h-20 w-full m-2  rounded-md " >
                            <div className="h-2/4 flex justify-center items-center font-serif text-xl shadow-xl border-b-2   text-black font-extrabold text-[30px]" style={textShadow} >{currentPlayer?.playerName}</div>
                            <div className="font-serif ml-10 text-lg">{currentPlayer?.playerStyle}</div>
                        </div>}
                    </div>
                    <div className=" w-1/3 h-full">

                        <div className="w-full h-full flex flex-col justify-end">
                            <div className=" h-48 w-full m-2 rounded-md flex flex-col items-center justify-center">
                                <div className="flex justify-center items-center  h-full w-2/4 border-2 border-blue-400  bg-white rounded-full">
                                    {currentTeam && <div>
                                        <h1 className="text-black text-[100px] font-serif"> {time} </h1>
                                    </div>}
                                </div>
                                <div className="h-1/4 bg-white w-3/4 rounded-full flex">
                                    <div className="w-1/2 text-black text-center text-2xl font-serif">current  bid</div>
                                    <div className="w-1/2 bg-blue-500 rounded-r-full text-center text-2xl font-serif">{bid.amount}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="w-1/3 h-full flex items-end">
                        {currentTeam && <div className="bg-[#e67e22] h-40 w-full m-2 rounded-md ">
                            <div className="w-full h-2/4 flex justify-center items-center font-serif text-[30px] font-extrabold shadow-xl border-b-2  text-black" style={textShadow}>{currentTeam.teamName}</div>
                            <div className="grid grid-cols-2 mt-4">
                                <div className="font-serif ml-10 text-lg">Balance: {currentTeam.totalPoints.toLocaleString()}</div>
                                <div className="font-serif ml-10 text-lg">Reserve: {reserve}</div>
                                {category ? <div className="font-serif ml-10 text-lg">Max bid: {maxBid}</div> : <div className="font-serif ml-10 text-lg">Max bid: {maxBid}</div>}
                                <div className="font-serif ml-10 text-lg">{
                                    currentTeam.playerRequirement.map((req) => (req.bought)).reduce((acc, num) => acc + num) + currentTeam.noneCategoryPlayerBought
                                }</div>
                            </div>
                        </div>}
                    </div>
                </div>
                <div className="w-screen flex justify-end border-white/30 rounded-xl h-[10vh] bg-white/20 backdrop-blur-md border">
                    {showPanel && <div className="w-full flex flex-row justify-end items-center gap-4 mr-4">

                        {currentTeam && <button onClick={cancleBid} className="border font-thin px-2 py-1 hover:bg-slate-50 hover:text-gray-800 rounded-lg duration-500 transition-all ">Cancle bid</button>}
                        <button onClick={placeNewPlayer} className="border font-thin px-2 py-1 hover:bg-slate-50 hover:text-gray-800 rounded-lg duration-500 transition-all ">Newplayer</button>
                        <button onClick={onSold} className="border font-thin px-2 py-1 hover:bg-green-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">Sold</button>
                        <button onClick={onUnsold} className="border font-thin px-2 py-1 hover:bg-red-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">Unsold</button>
                        <span>Teams</span>
                        {
                            teams.map((team, i) => (

                                <button key={i} onClick={() => handleTeamBid(team)} className="border font-thin px-2 py-1 hover:bg-orange-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">{team.shortName}</button>
                            ))
                        }
                    </div>}
                </div>
            </div>
            <div ref={fireworksRef} className="absolute inset-0 z-0 w-[90vw] h-[90vh]"></div>
        </>
    )
}

export default CategoryAuctionPanel;