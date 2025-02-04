import { Link, Links, Route, Routes, useLocation, useNavigate } from "react-router-dom"
import Panel from "../componets/AuctionPanel/Panel"
import { useContext, useEffect, useLayoutEffect, useState } from "react";
import axiosApi from "../utils/axiosApi";
import { messageContext } from "../context/MessageContext";
import ShowTeams from "../componets/AuctionPanel/ShowTeams";
import ShowPlayers from "../componets/AuctionPanel/ShowPlayers";
import ShowCategories from "../componets/AuctionPanel/ShowCategories";
import LoadingBar from "../componets/LoadingBar";

const AuctionPanel = () => {

  const navigate = useNavigate();
  const location = useLocation();
  const path = location.pathname
  const auction = JSON.parse(localStorage.getItem("auction"))

  const [auctionData, setAuctionData] = useState(null);
  const [categories, setCategories] = useState(null);
  const [teams, setTeams] = useState(null);
  const [players, setPlayer] = useState(null);
  const [additionalIncrement, setAdditionalIncrement] = useState(null);
  const [categoryIncrements, setCategoryIncrements] = useState(null);
  const { setErrorMessage } = useContext(messageContext);

  useEffect(() => {
    const fetchAuctionData = async () => {
      try {
        const response = await axiosApi.get(`/auction/get-detailed-auction/${auction?.auctionId}`);
        const auctionResponse = response?.data;
        setAuctionData(auctionResponse?.auction);
        setCategories(auctionResponse?.auctionCategories);
        setTeams(auctionResponse?.auctionTeams);
        setPlayer(auctionResponse?.auctionPlayers);
        setAdditionalIncrement(auctionResponse?.auction?.additionalIncrement)
        setCategoryIncrements(categories?.categoryIncrements)
      } catch (error) {
        navigate("/")
        setErrorMessage(error?.response?.data?.message || "Something went wrong! please try again.")
      }
    }
    fetchAuctionData();

  }, [])


  return (
    <>
      {auctionData ? <div className="h-screen w-screen bg-center bg-contain flex  text-white flex-col" style={{ backgroundImage: 'url("https://cdn.vectorstock.com/i/500p/56/47/cricket-stadium-wallpaper-vector-20455647.jpg")', }}>
        <div className="w-screen h-[10vh] bg-white/20 backdrop-blur-md border border-white/30 text-xl text-white text-center flex justify-between items-center rounded-xl px-10" >
          <div className="flex flex-row">
            <p>{auctionData.auctionName.toUpperCase()}</p>
          </div>
          <div className="flex flex-row gap-6">
            {[{ title: "Home", path: "/" },{ title: "Panel", path: "/auction-dashboard" },{ title: "Teams", path: "/auction-dashboard/teams" }, { title: "Players", path: "/auction-dashboard/players" }, { title: "Categories", path: "/auction-dashboard/categories" }].map(({ title, path }, i) => (
              <Link to={path} key={i} className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">{title}</Link>
            ))}
          </div>
        </div>
        <Routes>
          <Route path="/" element={<Panel teams={teams} auctionData={auctionData} />} />
          <Route path="/teams" element={<ShowTeams teams={teams} auctionData={auctionData} />} />
          <Route path="/categories" element={<ShowCategories categories={categories} auctionData={auctionData} />} />
          <Route path="/players" element={<ShowPlayers players={players} auctionData={auctionData} />} />
        </Routes>
      </div> : <LoadingBar/>}
    </>
  )
}

export default AuctionPanel