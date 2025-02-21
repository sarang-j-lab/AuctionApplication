import React, { useContext, useEffect, useRef, useState } from 'react'
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { Link, Routes, useParams } from 'react-router-dom';
import { messageContext } from '../../../context/MessageContext';
import axios from 'axios';
import Confetti from 'react-confetti'
import { Fireworks } from "fireworks-js";


const textShadow = {
    textShadow: "10px 10px 15px rgba(0, 0, 0, 2)"
}

const LiveAuctionPanel = () => {
    const fireworksRef = useRef(null);

    const { auctionId } = useParams();
    const [sold, setSold] = useState(false)
    const [auctionData, setAuctionData] = useState(null)
    const { setErrorMessage, setSuccessMessage } = useContext(messageContext);
    const [bid, setBid] = useState(null);

    const [time, setTime] = useState(0);
    const user = JSON.parse(localStorage.getItem("user"))
    const token = user?.token
    const client = new Client({
        webSocketFactory: () => new SockJS("http://localhost:8080/ws-auction"),
        connectHeaders: {
            Authorization: `Bearer ${token}`,  // Send JWT in headers
        },
        reconnectDelay: 5000,
    })

    useEffect(() => {
        if (time <= 0) return;

        const timer = setInterval(() => {
            setTime((prevTime) => prevTime - 1);
        }, 1000);

        return () => clearInterval(timer);
    }, [time]);

    useEffect(() => {
        client.onConnect = () => {
            client.subscribe(`/live/bid/${auctionId}`, (message) => {
                setTime(10);
                setBid(JSON.parse(message.body));
            });
            client.subscribe(`/live/player/${auctionId}`, (message) => {
                setBid(JSON.parse(message.body))
            })
            client.subscribe(`/live/unsold/${auctionId}`, (message) => {
                setBid({ player: null, team: null, amount: 0, category: null });
                setSuccessMessage(message.body);
            })
            client.subscribe(`/live/sold/${auctionId}`, (message) => {
                setSold(true);
                setTime(0)
                setSuccessMessage(message.body)
                const fireworks = new Fireworks(fireworksRef.current, { speed: 2, particles: 150, opacity: 0.7, });
                fireworks.start();
                setTimeout(() => {
                    setBid({ player: null, team: null, amount: 0, category: null });
                    fireworksRef.current.removeChild(fireworksRef.current.firstChild);
                    setSold(false);
                    fireworks.stop();
                }, 5000);
            })
        };

        fetchAuction();

        client.activate();

        return () => {
            client.deactivate();
        };
    }, []);

    const fetchAuction = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/auction/get-auction/${auctionId}`)
            setAuctionData(response?.data);
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Something went wrong");
        }
    }


    return (
        <>
            <div className="h-screen w-screen bg-center bg-contain flex  text-white flex-col" style={{ backgroundImage: 'url("https://cdn.vectorstock.com/i/500p/56/47/cricket-stadium-wallpaper-vector-20455647.jpg")', }}>
                {/* <div className="z-10 p-3  bg-white/20 backdrop-blur-md border border-white/30 text-xl text-white text-center flex justify-between items-center rounded-xl px-10" >

                    <div className="flex flex-row gap-6 z-10">
                        {[{ title: "Panel", path: "/auction-dashboard" }, { title: "Teams", path: "/live/auction/teams" }, { title: "Players", path: "/live/auction/players" }].map(({ title, path }, i) => (
                            <Link to={path} key={i} className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">{title}</Link>
                        ))}
                    </div>
                </div> */}
                <div className="h-[87vh] w-full  rounded-xl flex flex-col">
                    {sold && <Confetti width={window.innerWidth} height={window.innerHeight} />}

                    {auctionData && <div className="flex flex-col justify-center mt-10 self-center font-serif  bg-black  px-8 text-[40px]    items-center text-sky-200 border-2 rounded-lg border-sky-200 shadow-[0_0_2px_#fff,inset_0_0_2px_#fff,0_0_5px_#08f,0_0_15px_#08f,0_0_30px_#08f]">
                        <p >{auctionData?.auctionName?.toUpperCase()}</p>
                        <p className="w-1/2 text-[25px] px-8 self-center rounded-lg text-wrap">Players Auction</p>
                    </div>}
                    <div className="h-[80vh] flex gap-2 " >
                        <div className="w-1/3 h-full flex items-end ">
                            {bid?.player && <div className="bg-[#e67e22] h-20 w-full m-2  rounded-md " >
                                <div className="h-2/4 flex justify-center items-center font-serif text-xl shadow-xl border-b-2   text-black font-extrabold text-[30px]" style={textShadow} >{bid?.player?.playerName}</div>
                                <div className="font-serif ml-10 text-lg">{bid?.player?.playerStyle}</div>
                            </div>}
                        </div>
                        <div className=" w-1/3 h-full">

                            <div className="w-full h-full flex flex-col justify-end">
                                {bid && <div className=" h-48 w-full m-2 rounded-md flex flex-col items-center justify-center">
                                    <div className="h-full w-2/4 border-2 border-blue-400  bg-white rounded-full">
                                        {bid?.team && <div>
                                            <h1 className="text-black text-[100px] font-serif flex justify-center items-center"> {time} </h1>
                                        </div>}
                                    </div>
                                    <div className="h-1/4 bg-white w-3/4 rounded-full flex">
                                        <div className="w-1/2 text-black text-center text-2xl font-serif">current  bid </div>
                                        <div className="w-1/2 bg-blue-500 rounded-r-full text-center text-2xl font-serif">{bid.amount}</div>
                                    </div>
                                </div>}
                            </div>
                        </div>
                        <div className="w-1/3 h-full flex items-end">
                            {bid?.team && <div className="bg-[#e67e22] h-40 w-full m-2 rounded-md ">
                                <div className="h-2/4 flex justify-center items-center font-serif text-[30px] font-extrabold shadow-xl border-b-2  text-black" style={textShadow}>{bid?.team?.teamName}</div>
                                <div className="grid grid-cols-2 mt-4">
                                    <div className="font-serif ml-10 text-lg">Balance: {bid?.team?.totalPoints.toLocaleString()}</div>
                                    <div className="font-serif ml-10 text-lg">Reserve: {bid?.team.reserve.toLocaleString()}</div>
                                    <div className="font-serif ml-10 text-lg">Max bid: {(bid?.team?.maxBid).toLocaleString()}</div>
                                </div>
                            </div>}
                        </div>
                    </div>
                </div>
                <div ref={fireworksRef} className="absolute inset-0 z-0 w-[90vw] h-[90vh]"></div>
            </div >
        </>
    )
}

export default LiveAuctionPanel