import React, { use, useContext, useDeferredValue, useEffect, useState } from 'react'
import axiosApi from '../../utils/axiosApi'
import { messageContext } from '../../context/MessageContext'
import { Link } from 'react-router-dom'

const ShowPlayers = ({ auctionData, fetchAuctionPlayers, categories }) => {

  const [players, setPlayers] = useState(null)
  const [copyPlayers, setCopyPlayers] = useState(null);
  const [showUnsold, setShowUnsold] = useState(false)
  const { setErrorMessage, setSuccessMessage } = useContext(messageContext);

  useEffect(() => {

    AuctionPlayer();
  }, [])

  const AuctionPlayer = async () => {
    try {
      setShowUnsold(false)
      const response = await axiosApi.get(`/auction-player/${auctionData.auctionId}`);
      setPlayers(response?.data);
      setCopyPlayers(response?.data);
    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "Failed to fetch players");
    }
  }

  console.log(players)

  const reauctionUnsolds = async () => {
    try {
      setShowUnsold(false)
      await axiosApi.put(`/reauction-unsold/${auctionData.auctionId}`)
      setSuccessMessage("Unsold player added Re-auction!");
      AuctionPlayer();
      fetchAuctionPlayers();
    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "Please try again!")
    }
  }

  const fetchAvaliablePlayers = () => {
    setShowUnsold(false)
    const avaliablePlayers = copyPlayers.filter((player) => player?.status === "PENDING")
    setPlayers(avaliablePlayers);
  }
  const fetchSoldPlayers = () => {
    setShowUnsold(false)
    const soldPlayers = copyPlayers.filter((player) => player?.status === "SOLD")
    setPlayers(soldPlayers);
  }
  const fetchUnsoldPlayers = () => {
    setShowUnsold(true)
    const unsoldPlayers = copyPlayers.filter((player) => player?.status === "UNSOLD");
    setPlayers(unsoldPlayers);
  }

  const handleCategoryChange = (event)=>{
    if(event?.target?.value === ""){
      setShowUnsold(false);
      const categoryPlayers = copyPlayers.filter((player)=> player?.categoryId === null);
      setPlayers(categoryPlayers);
      return;
    }
    setShowUnsold(false);
    const categoryPlayers = copyPlayers.filter((player)=> player?.categoryId?.categoryId === event?.target?.value);
    setPlayers(categoryPlayers);
  }

  return (
    <>
      {players ? <div className='w-[100vw] h-[90vh] bg-white/20 backdrop-blur-2xl overflow-scroll scrollbar-hide rounded-xl'>
        <div className="w-full  h-[30vh] px-20 text-white flex flex-col items-center justify-center">
          <h1 className='text-[10vh] font-serif'>{auctionData.auctionName.toUpperCase()}</h1>
          <h1 className='text-[7vh] font-serif text-black bg-yellow-200 px-3 rounded-sm'>Players</h1>
        </div>
        <div className="w-screen h-[10vh]  backdrop-blur-md border border-white/30 text-xl text-white text-center flex justify-between items-center rounded-xl px-10" >
          <div className="flex flex-row gap-6">
            {[{ title: "All", onClick: AuctionPlayer }, { title: "Avaliable", onClick: fetchAvaliablePlayers }, { title: "Sold", onClick: fetchSoldPlayers }, { title: "Unsold", onClick: fetchUnsoldPlayers }].map(({ title, onClick }, i) => (
              <button onClick={onClick} key={i} className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">{title}</button>
            ))}
            <select onChange={handleCategoryChange}  className='border font-thin px-2 py-1 bg-white/10 hover:backdrop-blur-2xl text-gray-800 rounded-lg  '>
              <option className=' border font-thin px-2 py-1  rounded-lg ' value="">None category</option>
              {
                categories.map((category) => (
                  <option className='border font-thin px-2 py-1 bg-white/10 hover:backdrop-blur-2xl text-gray-800 rounded-lg ' value={category?.categoryId}>{category?.categoryName}</option>

                ))
              }
            </select>
          </div>

          {showUnsold && players.length ? <button onClick={reauctionUnsolds} className="border font-thin px-2 py-1  hover:backdrop-blur-2xl hover:text-gray-800 rounded-lg duration-500 transition-all ">Reauction all unsold</button> : <></>}
        </div>
        <div className='w-full grid grid-cols-3  p-10  gap-5 '>

          {players.map((player) => (
            <div key={player.playerId} className='h-[17vh]  bg-white rounded-xl text-black flex flex-col items-center'>
              <div className='flex flex-row text-xl font-serif h-2/4 bg-green-400 w-full pl-2'>{player.playerName.toUpperCase()}</div>
              <div className='flex flex-col h-full w-full justify-center p-2  font-serif '>
                <div className='flex justify-between'>
                  <p><span className='text-lg'>Form no.{player.formNo}</span></p>
                  <p><span>Category: {player?.categoryId?.categoryName ? player?.categoryId?.categoryName : "None"}</span></p>
                </div>
                <p className='w-full'><span className='text-sm w-2/3'></span>Style: {player.playerStyle}</p>
              </div>
            </div>
          ))}

        </div>
      </div> : <h1 className='text-black p-2 rounded-lg self-center bg-yellow-400 font-serif text-2xl m-10'>This auction does'nt have any player.</h1>}
    </>
  )
}

export default ShowPlayers