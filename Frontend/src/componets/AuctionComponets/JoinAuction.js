import { useContext, useEffect, useState } from 'react'
import { Navigate, useNavigate } from 'react-router-dom';
import { RouteToprevBtn } from '../Component/Button.js';
import { messageContext } from '../../context/MessageContext.js';
import axiosApi from '../../utils/axiosApi.js';

const JoinAuction = () => {


    const navigate = useNavigate();
    const [auctions, setAuctions] = useState([])
    const [code, setCode] = useState()
    const { setErrorMessage } = useContext(messageContext);


    const user = JSON.parse(localStorage.getItem("user"));

    useEffect(() => {
        window.scrollTo(0, 0);
        const fetchUserJoinedAuctions = async () => {
            if (!user) {
                setErrorMessage("User not found!")
                return;
            }
            try {
                const response = await axiosApi.get(`user/user-joined-auctions/${user?.user?.userId}`)
                setAuctions(response?.data)
            } catch (error) {
                setErrorMessage(error?.response?.data?.message || "Something went wrong please try again!");
            }
        }
        fetchUserJoinedAuctions()
    }, [])


    const searchAuction = async (e) => {
        e.preventDefault();

        const checkJoined = auctions.filter((auction) => auction.auctionId === code)
        if (checkJoined.length) {
            setErrorMessage("You have already joined this auction");
            return;
        }

        try {
            const response = await axiosApi.get(`/auction/auction-details/${code}`)

            if (!response?.data?.playerRegistration) {
                setErrorMessage("Player registration is not allowed in this auction");
                return;
            }

            const auction = response?.data

            //here auction is storing in localStorage with different keys for security reasons because we are using auctionId 
            // in auction panel if someone redirect to auction-dashboard route for join auction he will able to conduct auction
            localStorage.setItem("auction", JSON.stringify({ id: auction.auctionId, name: auction.auctionName, date: auction.auctionDate }));
            navigate("/auction/player-form", { state: { for: "joinForm", player: { playerName: user.user.name, mobileNo: user.user.mobileNo, playerAge: "", jersseyNumber: "", jersseyName: "", tshirtSize: "", trouserSize: "", playerStyle: "", categoryId: null } } })
        } catch (error) {
            if (error.response) {
                setErrorMessage("Auction not found with this auction code");
            } else {
                setErrorMessage("Something went wrong! please try again.")
            }
        }
    }

    const handleChange = (e) => {
        setCode(e.target.value.trim());
    }


    if (!user) {
        setErrorMessage('User not found!')
        return <Navigate to={"/authentication"} />
    }

    return (
        <div className='xl:w-3/4  lg:w-3/4 sm:w-full md:w-full flex flex-col'>
            <div className='xl:w-full lg:w-full  md:w-full sm:w-full shadow-lg rounded-xl   flex space-y-4 justify-between items-center px-4 py-2 lg:flex-row md:flex-row sm:flex-row '>
                <h1 className='text-2xl py-2 px-2 text-blue-600 md:text-lg lg:text-2xl sm:text-xl'>Join auction</h1>
            </div>
            <div className='h-40 w-full shadow-lg rounded-xl flex space-y-4 justify-center items-center px-4 py-2 text-sm sm:text-xs xl:flex-col lg:flex-col flex-col  '>
                <h1 className='xl:w-2/4 sm:w-full   xl:text-2xl lg:text-2xl md:text-xl  text-blue-600 mx-auto  sm:text-xs '>Enter auction code to join auction</h1>
                <form onSubmit={searchAuction} className='flex flex-row justify-center gap-5 mx-auto mt-3'>
                    <input type="text" name="code" placeholder="Enter code" onChange={handleChange} value={code} className="  p-3 h-10 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm" required />

                    <button className='rounded-md border px-4  hover:bg-blue-600 mx-auto hover:text-white '>Join</button>
                </form>
            </div>
            <h1 className='font-bold text-lg mt-5 ml-5 mb-3'>Joined Auctions</h1>
            {auctions.length > 0 && auctions.map((auction) => (
                <div key={auction.auctionId} className='bg-white shadow-md rounded-lg p-3  w-full  mb-2'>
                    <h1 className='text-xs text-blue-600 md:text-lg lg:text-2xl sm:text-xs'>{auction.auctionName.toUpperCase()}<span className='text-xs'> Code: {auction.auctionId}</span></h1>

                    <button className='rounded-md p-2 '>Auction Date: {auction.auctionDate}</button>
                </div>
            ))}
            <RouteToprevBtn onClick={() => navigate("/")} />
        </div>
    )
}

export default JoinAuction