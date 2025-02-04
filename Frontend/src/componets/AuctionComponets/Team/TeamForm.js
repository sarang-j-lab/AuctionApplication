import { useEffect, useState, useContext } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { messageContext } from '../../../context/MessageContext'
import axiosApi from "../../../utils/axiosApi";

const TeamForm = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const newTeamForm = location.state.newTeamForm;
    const initialTeamData = location.state.team || { teamName: "", shortName: "" };

    const [alphabets, setAlphabets] = useState([]);
    const [teamData, setTeamData] = useState(initialTeamData);

    
    const auction = JSON.parse(localStorage.getItem("auction"));
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTeamData((prevData) => ({
            ...prevData,
            [name]: name === "shortName" || name === "shortcutKey" ? value.toUpperCase() : value,
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const url = newTeamForm
            ? `/add-team/${auction.auctionId}`
            : `/edit-team/${teamData.teamId}`;

        const method = newTeamForm ? "post" : "put";

        try {
            await axiosApi({
                method,
                url,
                data: teamData,
                headers: { "Content-Type": "application/json" },
            });
            navigate(`/auction/auction-teams`);
            setSuccessMessage(newTeamForm ? "Team added successfully!" : "Team edited successfully!");
        } catch (e) {
            setErrorMessage(e.response?.data?.message || "Something went wrong! Please try again.");
        }
    };

    return (
        <div className="max-w-sm mx-auto p-4 rounded-lg border-2">
            <h1 className="text-2xl my-5 text-center">
                {newTeamForm ? "Create New Team" : "Edit Team"}
            </h1>

            <form onSubmit={handleSubmit}>
                <div className="mb-5">
                    <label htmlFor="teamName" className="block mb-2 text-sm font-medium">
                        Team Name
                    </label>
                    <input
                        type="text"
                        id="teamName"
                        name="teamName"
                        value={teamData.teamName}
                        onChange={handleChange}
                        className="shadow-sm bg-gray-50 border border-gray-300 text-sm rounded-lg w-full p-2.5 focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Ex. Joshi Warriors"
                        required
                    />
                </div>

                <div className="mb-5">
                    <label htmlFor="shortName" className="block mb-2 text-sm font-medium">
                        Short Name
                    </label>
                    <input
                        type="text"
                        id="shortName"
                        name="shortName"
                        value={teamData.shortName}
                        onChange={handleChange}
                        className="shadow-sm bg-gray-50 border border-gray-300 text-sm rounded-lg w-full p-2.5 focus:ring-blue-500 focus:border-blue-500"
                        placeholder="Ex. JW"
                        required
                    />
                </div>

                

                <button type="submit" className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                    {newTeamForm ? "Create Team" : "Save Changes"}
                </button>
            </form>

            <button onClick={() => navigate("/auction/auction-teams")} className="mt-4 w-full text-blue-700 border border-blue-700 hover:bg-blue-100 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center">
                Cancel
            </button>
        </div>
    );
};

export default TeamForm;
