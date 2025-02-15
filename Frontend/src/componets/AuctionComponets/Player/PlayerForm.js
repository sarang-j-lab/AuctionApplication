import { useContext, useEffect, useState } from "react";
import { Navigate, useLocation } from "react-router-dom";
import Form from "./Form";
import JoinForm from "./JoinForm";
import { messageContext } from "../../../context/MessageContext";
import axiosApi from "../../../utils/axiosApi";


function PlayerForm() {

    const initialState = { playerName: "", mobileNo: "", playerAge: "", jersseyNumber: "", jersseyName: "", tshirtSize: "", trouserSize: "", playerStyle: "", categoryId: null }
    const location = useLocation();
    const { setErrorMessage } = useContext(messageContext);
    const purpose = location.state.for
    const auction = JSON.parse(localStorage.getItem('auction'));
    const [categories, setCategories] = useState([]);
    const [playerData, setPlayerData] = useState(location.state.player || initialState);


    useEffect(() => {
        window.scrollTo(0, 0);

        const fetchCategories = async () => {

            if (!auction) {
                setErrorMessage("Auction not found!");
                return;
            }
            try {
                const url = purpose === "joinForm" ? `/show-auction-category/${auction.id}` : `/show-auction-category/${auction.auctionId}`

                const response = await axiosApi.get(url);
                setCategories(response?.data);
            } catch (e) {
                setErrorMessage(e?.response?.data?.message || "Failed to load categories! please try again.");
            }
        }
        fetchCategories();
    }, [])


    if (!auction) {
        setErrorMessage("Auction not found!")
        return <Navigate to={"/"} />
    }

    return (
        <div className="flex flex-col items-center xl:w-3/4 sm:w-[90vw]  justify-center  ">

            {purpose === "newForm" && <Form purpose={"Add player"} categories={categories} playerData={playerData} setPlayerData={setPlayerData} />}
            {purpose === "editForm" && <Form purpose={"Edit player"} categories={categories} playerData={playerData} setPlayerData={setPlayerData} />}
            {purpose === "joinForm" && <JoinForm purpose={"Join auction"} categories={categories} playerData={playerData} setPlayerData={setPlayerData} />}

        </div>

    );

}
export default PlayerForm;
