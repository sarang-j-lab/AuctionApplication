import { useContext, useState } from "react";
import { RouteToprevBtn, BlueButton } from "../../Component/Button.js";
import { Navigate, useNavigate } from 'react-router-dom';
import { messageContext } from "../../../context/MessageContext";
import axiosApi from "../../../utils/axiosApi";
import LoadingBar from "../../Component/LoadingBar.js";


const auctionFields = [
    { label: "Player Name", name: "playerName", type: "text", placeholder: "Ex.  Sachin tendulkar" },
    { label: "Mobile No", name: "mobileNo", type: "number", placeholder: "Ex.9090909090" },
    { label: "Player Age", name: "playerAge", type: "number", placeholder: "Ex. 22" },
    { label: "Jerssey Number", name: "jersseyNumber", type: "number", placeholder: "Ex. 18" },
    { label: "Jerssey Name", name: "jersseyName", type: "text", placeholder: "Ex. sachin" },
]

const Form = ({ categories, setPlayerData, playerData, purpose }) => {
    const navigate = useNavigate();
    const auction = JSON.parse(localStorage.getItem('auction'));
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPlayerData({ ...playerData, [name]: value === "" ? null : value });
    };


    const handleForm = async (event) => {
        event.preventDefault();

        if (!auction) {
            setErrorMessage('Auction not found!')
            return;
        }

        const isEditForm = playerData.playerId;
        const url = isEditForm ? `/edit-auction-player/${playerData.playerId}` : `/add-auction/${auction.auctionId}`;
        const method = isEditForm ? "put" : "post";
        setLoading(true);
        try {
            await axiosApi({
                method,
                url,
                data: playerData,
                headers: {
                    "Content-Type": "application/json",
                }
            })
            navigate("/auction/auction-players")
            setSuccessMessage(isEditForm ? "Player edited successfully" : "Player added successfully!");
        } catch (error) {
            if (error?.response) {
                let serverError = "";
                for (const [key, value] of Object.entries(error?.response?.data)) {
                    serverError = value
                }
                setErrorMessage(serverError);
            } else {
                setErrorMessage("Something went wrong! please try again")
            }
        } finally {
            setLoading(false)
        }
    }

    if (!auction) {
        setErrorMessage("Auction not found!")
        return <Navigate to={"/"} />
    }


    return (
        <form onSubmit={handleForm} className="bg-white shadow-md rounded-lg p-3  w-full mx-4 ">
            <h2 className="text-base font-bold m-10 mt-5 lg:text-2xl sm:text-base">{purpose}</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {/* Map through fields */}
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
                {loading && <LoadingBar/>}
                <RouteToprevBtn onClick={() => navigate("/auction/auction-players")} />
                {purpose === "Add player" ? 
                <button type="submit" disabled={loading} className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300">Submit</button> 
                : <button type="submit" disabled={loading} className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300">Edit</button>}
            </div>
        </form>
    )
}

export default Form