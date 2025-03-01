import { useContext, useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { CiCalendarDate } from "react-icons/ci";
import { BiCategory } from "react-icons/bi";
import { TiGroupOutline } from "react-icons/ti";
import { BiSolidCricketBall } from "react-icons/bi";
import { RouteToprevBtn } from '../Component/Button.js';
import Confirmation from '../Component/Confirmation.js';
import PopupForm from './PopupForm';
import { messageContext } from '../../context/MessageContext';
import AuctionIncrements from './AuctionIncrements';
import axiosApi from '../../utils/axiosApi';
import { FaArrowRightToBracket } from "react-icons/fa6";



const AuctionDetail = () => {


  // -- constants
  const navigate = useNavigate()

  // messages and confirmation
  const [selectedAuctionId, setSelectedAuctionId] = useState(0);
  const [confirmation, setConfirmation] = useState(false)
  const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
  const [isOpen, setIsOpen] = useState(false);
  const auction = JSON.parse(localStorage.getItem("auction"))


  useEffect(() => {
    window.scrollTo(0, 0);
  }, [])




  //delete confirmation it will setSelectedAuctionId to auctionId and display the confirmation component.
  const deleteConfirmation = (e) => {
    setSelectedAuctionId(e.target.value);
    setConfirmation(true);
  }

  //if you confirm deleted from confimation component than this fun will deleted auction of selectAuctionId .
  const deleteAuction = async (e) => {
    try {
      await axiosApi.delete(`/auction/delete-auction/${selectedAuctionId}`);
      setSelectedAuctionId(0);
      setSuccessMessage("Auction deleted successfully!")
      navigate("/auction/my-auction")
    } catch (error) {
      setErrorMessage("Something went wrong! please try again.")
    }
  }


  if (!auction) {
    setErrorMessage("Auction not found!")
    return <Navigate to={"/"} />
  }



  return (
    <div className='w-full flex flex-col space-y-5 lg:w-3/4 sm:w-full'>
      {isOpen && <PopupForm setId={(r) => { }} purpose={"auctionIncrement"} setIsOpen={setIsOpen} />}
      {confirmation && <Confirmation setId={setSelectedAuctionId} deleteFun={deleteAuction} setConfirmation={setConfirmation} />}

      <>
        <h1 className='text-2xl text-blue-400 mx-auto lg:text-4xl sm:text-2xl'>Auction Details</h1>
        <div className="bg-white shadow-lg rounded-lg lg:max-w-2xl xl:max-w-3xl  w-[70vw] sm:w-[70vw]  mx-auto p-3 space-y-6 md:space-y-4">


          <div className="text-base lg:text-2xl font-bold text-gray-800 sm:text-base flex flex-col xl:flex-row sm:flex-col justify-center xl:items-end sm:items-start">
            {auction.auctionName.toUpperCase()}  <sub className='text-xs ml-2'>code: {auction.auctionId}</sub>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">

            {[
              { label: "Points per team", value: auction.pointsPerTeam.toLocaleString() },
              { label: "Base bid", value: auction.baseBid.toLocaleString() },
              { label: "Bid increase by", value: auction.bidIncreaseBy.toLocaleString() },
              { label: "Max player per team", value: auction.maxPlayerPerTeam },
              { label: "Min player per team", value: auction.minPlayerPerTeam },
            ].map((item, i) => (
              <div key={i} className="flex justify-between items-center">
                <span className="text-blue-400 font-medium">{item.label}</span>
                <span className="text-gray-800 font-semibold">{item.value}</span>
              </div>
            ))}
            <div className='flex space-x-4 text-sm'>
              <p className='flex items-center font-bold'><CiCalendarDate className='mx-2' />Date: {auction.auctionDate}</p>
              <p className='flex items-center font-bold text-sm '>{auction.auctionTime}</p>
            </div>
          </div>


          <div className="flex items-center justify-between flex-col lg:flex-row sm:flex-col gap-5 ">
            <div className=' flex flex-col gap-2 xl:flex-row lg:flex-row md:flex-row sm:flex-col'>
              <button onClick={() => navigate("/auction/auction-form", { state: auction })} className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
                Edit
              </button>
              <button onClick={() => setIsOpen(true)}
                className="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
                Add increment
              </button>
              <button value={auction.auctionId} onClick={deleteConfirmation} className="px-6 py-2 bg-red-500 text-white rounded-lg hover:bg-red-600">
                Delete
              </button>
            </div>
          </div>
        </div>
        <div className='flex flex-col justify-center items-center'>
          <button onClick={() => navigate("/auction-dashboard")} value={auction.auctionId} className=" flex justify-center mb-5 items-center space-x-5 w-full xl:w-3/4 self-center lg:w-full  sm:w-full px-6 py-2  border-2 rounded-lg bg-blue-600 hover:bg-blue-800 text-white">
            <span className='text-lg'> Conduct Auction</span> <FaArrowRightToBracket size={"22px"} />
          </button>
          <span className='my-4 shadow-xl bg-white px-4 py-2 rounded-lg'> Share live link : <Link className='text-blue-500' target='_blank' to={`http://localhost:3000/live/auction/${auction.auctionId}`}>{`http://localhost:3000/live/auction/${auction.auctionId}`}</Link></span>
          {auction.additionalIncrements.length > 0 && <AuctionIncrements />}
        </div>

        <div className=" bg-white shadow-lg rounded-lg w-[70vw] xl:max-w-3xl lg:max-w-2xl  sm:w-[70vw] mx-auto p-6 space-y-6 md:space-y-4">
          <div className=" flex items-center justify-between flex-col lg:flex-row sm:flex-col gap-5">
            {
              [
                { label: "Show teams", icon: <TiGroupOutline className='mr-2' />, onClick: () => navigate(`/auction/auction-teams`) },
                { label: "Show categories", icon: <BiCategory className='mr-2' />, onClick: () => navigate(`/auction/auction-categories`) },
                { label: "Show players", icon: <BiSolidCricketBall className='mr-2' />, onClick: () => navigate("/auction/auction-players") },
              ].map((button, i) => (
                <button key={i} onClick={button.onClick} className="px-6 py-2 bg-white text-black border rounded-lg hover:bg-blue-600 hover:text-white flex items-center">
                  {button.icon}
                  {button.label}
                </button>
              ))
            }
          </div>
        </div>

        <RouteToprevBtn onClick={() => { navigate("/auction/my-auction") }} />
      </>



    </div>

  )
}

export default AuctionDetail