import { Link, Navigate, Route, Routes, useNavigate } from "react-router-dom"
import { useContext, useEffect,  useState } from "react";
import axiosApi from "../utils/axiosApi";
import { messageContext } from "../context/MessageContext";
import ShowTeams from "../componets/AuctionPanel/ShowTeams";
import ShowPlayers from "../componets/AuctionPanel/ShowPlayers";
import ShowCategories from "../componets/AuctionPanel/ShowCategories";
import LoadingBar from "../componets/Component/LoadingBar.js";
import CategoryAuctionPanel from "../componets/AuctionPanel/CategoryAuctionPanel";
import NoneCategoryAuctionPanel from "../componets/AuctionPanel/NoneCategoryAuctionPanel";
import ShowTeamPlayers from "../componets/AuctionPanel/ShowTeamPlayers";
import { Client } from "@stomp/stompjs"
import SockJS from "sockjs-client"

const AuctionPanel = () => {

  const navigate = useNavigate();

  const auction = JSON.parse(localStorage.getItem("auction"))

  const [auctionData, setAuctionData] = useState(null);
  const [categories, setCategories] = useState(null);
  const [category, setCategory] = useState(null);
  const [teams, setTeams] = useState(null);
  const [players, setPlayers] = useState(null);
  const { setErrorMessage } = useContext(messageContext);
  const [stompClient, setStompClient] = useState(null)


  useEffect(() => {

    fetchAuctionTeam();
    fetchAuctionPlayers();
    fetchAuctionData();
  }, [])

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws-auction",null,{
        withCredentials: true,
      }),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("connected");
      }
    });


    client.activate();
    setStompClient(client)

    return () => {
      client.deactivate();
    };
  }, []);

  const fetchAuctionData = async () => {
    try {
      const response = await axiosApi.get(`/auction/get-detailed-auction/${auction?.auctionId}`);
      const auctionResponse = response?.data;
      setAuctionData(auctionResponse?.auction);
      setCategories(auctionResponse?.auctionCategories);
    } catch (error) {
      navigate("/")
      setErrorMessage(error?.response?.data?.message || "Something went wrong! please try again.")
    }
  }

  const fetchAuctionPlayers = async () => {
    try {
      const response = await axiosApi.get(`/auction-player/${auction?.auctionId}`)
      const noneCategoryPlayers = response?.data.filter((player) => player?.categoryId === null && player?.status === "PENDING");
      setPlayers(noneCategoryPlayers);
      setCategory(null)
    } catch (error) {
      navigate("/")
      setErrorMessage(error?.response?.data?.message || "Something went wrong! please try again.")
    }
  }

  const fetchAuctionTeam = async () => {
    try {
      const response = await axiosApi.get(`/auction-teams/${auction?.auctionId}`)
      setTeams(response?.data);
    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "Not able to fetch teams! please try again.");
    }
  }

  if (!auction) {
    setErrorMessage("Auction not found!")
    return <Navigate to={"/"} />
  }

  return (
    <>
      {auctionData && teams && players ? <div className="h-screen w-screen bg-center bg-contain flex  text-white flex-col" style={{ backgroundImage: 'url("/auctionpanelimage.jpg")', }}>
        <div className="z-10 w-screen h-[10vh] bg-white/20 backdrop-blur-md border border-white/30 text-xl text-white text-center flex justify-between items-center rounded-xl px-10" >
          <div className="flex flex-row gap-4 items-center">
            <Link to={"/"} className=" flex border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">
              <svg className="w-5 mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                <path fillRule="evenodd" d="M7.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l2.293 2.293a1 1 0 010 1.414z" clipRule="evenodd"></path>
              </svg>
              Home
            </Link>
          </div>
          <div className="flex flex-row gap-6 z-10">
            {[{ title: "Panel", path: "/auction-dashboard" }, { title: "Teams", path: "/auction-dashboard/teams" }, { title: "Players", path: "/auction-dashboard/players" }].map(({ title, path }, i) => (
              <Link to={path} key={i} className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">{title}</Link>
            ))}
              {categories.length > 0 && <Link to={"/auction-dashboard/categories"}  className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">Categories</Link>}
          </div>
        </div>
        <Routes>

          {category ? <Route path="/" element={<CategoryAuctionPanel stompClient={stompClient} fetchAuctionPlayers={fetchAuctionPlayers} setPlayers={setPlayers} category={category} teams={teams} players={players} auctionData={auctionData} fetchAuctionTeam={fetchAuctionTeam} />} />
            : <Route path="/" element={<NoneCategoryAuctionPanel stompClient={stompClient} fetchAuctionPlayers={fetchAuctionPlayers} category={category} teams={teams} players={players} auctionData={auctionData} fetchAuctionTeam={fetchAuctionTeam} />} />}
          <Route path="/teams" element={<ShowTeams categories={categories} teams={teams} auctionData={auctionData} />} />
          <Route path="/categories" element={<ShowCategories categories={categories} setCategory={setCategory} auctionData={auctionData} setPlayers={setPlayers} fetchAuctionPlayers={fetchAuctionPlayers} />} />
          <Route path="/players" element={<ShowPlayers players={players} categories={categories} auctionData={auctionData} fetchAuctionPlayers={fetchAuctionPlayers} />} />
          <Route path="/show-team-players" element={<ShowTeamPlayers category={category} fetchAuctionPlayers={fetchAuctionPlayers} fetchAuctionTeam={fetchAuctionTeam} />} />
        </Routes>
      </div> : <LoadingBar />}
    </>
  )
}

export default AuctionPanel