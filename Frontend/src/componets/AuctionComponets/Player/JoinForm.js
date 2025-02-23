
import { RouteToprevBtn,BlueButton } from "../../Component/Button.js";
import { useNavigate } from 'react-router-dom';
import axiosApi from "../../../utils/axiosApi";
import { useContext } from "react";
import { messageContext } from "../../../context/MessageContext";


const auctionFields = [
    { label: "Player Age", name: "playerAge", type: "number", placeholder: "Ex. 22" },
    { label: "Jerssey Number", name: "jersseyNumber", type: "number", placeholder: "Ex. 18" },
    { label: "Jerssey Name", name: "jersseyName", type: "text", placeholder: "Ex. sachin" },
]

const JoinForm = ({ categories, playerData, setPlayerData, purpose }) => {
    const navigate = useNavigate()
    const auction = JSON.parse(localStorage.getItem("auction"));
    const user = JSON.parse(localStorage.getItem("user"));
    const { setErrorMessage, setSuccessMessage } = useContext(messageContext);
    
    const handleChange = (e) => {
        const { name, value } = e.target;
        setPlayerData({ ...playerData, [name]: value === "" ? null : value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
    
        try {
                await axiosApi.post(`/join-auction/${auction.id}/${user?.user?.userId}`, playerData, {
                headers: {
                    "Content-Type": "application/json",
                }
            })
            navigate("/");
            setSuccessMessage("User joined to auction successfully!")
        } catch (error) {
            if (error?.response) {
                let serverError = "";
                for (const [key, value] of Object.entries(error?.response?.data)) {
                    serverError = value
                }
                setErrorMessage(serverError);
            }else {
                setErrorMessage("Something went wrong! please try again")
            }
        }
    }

    return (
        <>
            {auction && <div className='bg-white shadow-md rounded-lg p-3  w-full mx-4 mb-2'>
                <h1 className='text-xs text-blue-600 md:text-lg lg:text-2xl sm:text-xs'>{auction.name.toUpperCase()}<span className='text-xs ml-3 lg:text-xl sm:text-xs'>Players</span></h1>

                <button className='rounded-md p-2 '>{auction.date}</button>
            </div>}
            <form onSubmit={handleSubmit} className="bg-white shadow-md rounded-lg p-3  w-full mx-4 ">
                <h2 className="text-base font-bold m-10 mt-5 lg:text-2xl sm:text-base">{purpose}</h2>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                    {/* Map through fields */}
                    <div >
                        <label htmlFor="name" className="block text-sm font-medium text-gray-700">Enter Name</label>
                        <input disabled type="text" id='name' name="playerName" value={playerData.playerName} className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm" required />
                    </div>
                    <div >
                        <label htmlFor="mobileNo" className="block text-sm font-medium text-gray-700">Enter mobile no.</label>
                        <input disabled type="text" id="mobileNo" name='mobileNo' value={playerData.mobileNo} className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm" required />
                    </div>
                    {
                        auctionFields.map((field, index) => (
                            <div key={index}>
                                <label htmlFor={field.name} className="block text-sm font-medium text-gray-700">{field.label}</label>
                                <input autoComplete="off" type={field.type} id={field.name} name={field.name} value={playerData[field.name]} placeholder={field.placeholder} onChange={handleChange} className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm" required />
                            </div>
                        ))}
                </div>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-3">
                    <div className="">
                        <label htmlFor="tshirtSize" className="block text-gray-700 text-sm font-medium">
                            T-Shirt Size
                        </label>
                        <select
                            id="tshirtSize"
                            name="tshirtSize"
                            value={playerData.tshirtSize}
                            onChange={handleChange}
                            className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                            required
                        >
                            <option value="">Select size</option>
                            <option value="S">Small (S)</option>
                            <option value="M">Medium (M)</option>
                            <option value="L">Large (L)</option>
                            <option value="XL">Extra Large (XL)</option>
                        </select>
                    </div>
                    <div className="">
                        <label htmlFor="trouserSize" className="block text-sm text-gray-700 font-medium ">
                            Trouser Size
                        </label>
                        <select
                            id="trouserSize"
                            name="trouserSize"
                            value={playerData.trouserSize}
                            onChange={handleChange}
                            className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                            required
                        >
                            <option value="">Select size</option>
                            <option value="S">Small (S)</option>
                            <option value="M">Medium (M)</option>
                            <option value="L">Large (L)</option>
                            <option value="XL">Extra Large (XL)</option>
                        </select>
                    </div>
                    <div className="mb-4">
                        <label htmlFor="playerStyle" className="block text-gray-700 text-sm font-medium">
                            Player Style
                        </label>
                        <select
                            id="playerStyle"
                            name="playerStyle"
                            value={playerData.playerStyle}
                            onChange={handleChange}
                            className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm"
                            required
                        >
                            <option value="">Select style</option>
                            <option value="BATSMAN">BATSMAN</option>
                            <option value="BOWLER">BOWLER</option>
                            <option value="ALL_ROUNDER">ALL ROUNDER</option>
                            <option value="BATSMAN_WICKET_KEEPER">BATSMAN WICKET KEEPER</option>
                        </select>
                    </div>
                    {categories.length > 0 && <div className="mb-4">
                        <label htmlFor="category" className="block text-gray-700 text-sm font-medium">
                            Categories
                        </label>
                        <select id="category" name="categoryId" value={playerData.categoryId} onChange={handleChange} className="mt-1 p-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm">
                            <option value="">None</option>
                            {
                                categories.map((category) => (
                                    <option key={category.categoryId} value={category.categoryId}>{category.categoryName}</option>
                                ))
                            }

                        </select>
                    </div>}
                </div>

                <div className="flex flex-row justify-between items-center">
                    <RouteToprevBtn onClick={() => navigate("/")} />
                    <BlueButton>Join</BlueButton>
                </div>
            </form>
        </>
    )
}

export default JoinForm