import { useEffect, useState, useContext } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import Confirmation from '../../Component/Confirmation.js';
import { RouteToprevBtn } from '../../Component/Button.js';
import { messageContext } from '../../../context/MessageContext';
import axiosApi from '../../../utils/axiosApi';
import LoadingBar from '../../Component/LoadingBar.js';

const AuctionTeams = () => {
    window.scrollTo(0, 0);

    // Navigation
    const navigate = useNavigate();

    // States
    const [auctionTeams, setAuctionTeams] = useState(null);
    const [confirmation, setConfirmation] = useState(false);
    const [selectedTeamId, setSelectedTeamId] = useState(null);
    const [loading,setLoading] = useState(false);


    const auction = JSON.parse(localStorage.getItem("auction"))

    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);

    // Fetch auction teams on component load
    useEffect(() => {
        const fetchAuctionTeams = async () => {
            try {
                setLoading(true)
                const response = await axiosApi.get(`/auction-teams/${auction?.auctionId}`);
                setAuctionTeams(response.data);
            } catch (error) {
                setErrorMessage(error.response?.data?.message || 'Failed to fetch teams. Please try again.');
            }finally{
                setLoading(false)
            }
        };
        fetchAuctionTeams();
    }, []);

    // Add a new team
    const addTeam = () => {
        if(auction?.teamSize <= auctionTeams.length){
            setErrorMessage("You have to upgrade your plan!");
            navigate("/pricing")
            return;
        }
        navigate('/auction/team-form', { state: { newTeamForm: true } });
    };

    // Edit an existing team
    const editTeam = (team) => {
        navigate('/auction/team-form', { state: { newTeamForm: false, team } });
    };

    // Confirm team deletion
    const confirmDeletion = (teamId) => {
        setSelectedTeamId(teamId);
        setConfirmation(true);
    };

    // Delete a team
    const deleteTeam = async () => {
        try { 
            setLoading(true)
            await axiosApi.delete(`/delete-team/${selectedTeamId}`);
            setSuccessMessage('Team deleted successfully!');
            setAuctionTeams((prevTeams) => prevTeams.filter((team) => team.teamId !== selectedTeamId));
        } catch (error) {
            setErrorMessage(error.response?.data?.message || 'Failed to delete team. Please try again.');
        } finally {
            setLoading(false)
            setConfirmation(false);
            setSelectedTeamId(null);
        }
    };

    if (!auction) {
        return <Navigate to={"/"}/>
      }

    return (
        <>
            {/* Confirmation Modal */}
            {confirmation && (<Confirmation setId={setSelectedTeamId} deleteFun={deleteTeam} setConfirmation={setConfirmation} />)}
            <div className="lg:w-3/4 flex flex-col">
                {/* Header Section */}
                {auction.auctionId && <div className="xl:w-[65vw] lg:w-[60vw] md:w-[60vw] sm:w-[70vw] shadow-lg rounded-xl mx-4 flex justify-between items-center px-4 py-2 flex-col lg:flex-row">
                    <h1 className="text-xs text-blue-600 md:text-lg lg:text-2xl sm:text-xs">
                        {auction.auctionName.toUpperCase()} <span className="text-xs ml-3 lg:text-xl sm:text-xs">Teams</span>
                    </h1>
                    <button onClick={addTeam} className="rounded-md border p-2 hover:bg-blue-600 hover:text-white">
                        Add Team
                    </button>
                </div>}
                {/* Teams Section */}
                {loading && <LoadingBar/>}
                {auctionTeams  && (
                    <div className="mt-5 mx-4 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 p-4">
                        {auctionTeams.length > 0 ? auctionTeams.map((team) => (
                            <div
                                key={team.teamId}
                                className="border border-gray-200 shadow-lg hover:shadow-xl transition-shadow duration-300 ease-in-out rounded-lg overflow-hidden hover:scale-105 transform"
                            >
                                <img
                                    src="https://thumbs.dreamstime.com/b/cricket-ball-crossed-clubs-center-golden-wreath-shield-sport-logo-any-team-white-cricket-ball-135886828.jpg"
                                    alt="Team"
                                    className="w-full h-48 object-cover object-bottom transition-transform duration-300 ease-in-out md:h-30 sm:h-30"
                                />
                                <div className="p-4 space-y-2">
                                    <h3 className="font-semibold text-lg text-blue-500">
                                        <span className="font-bold text-black text-xl">{team.teamName}</span>
                                    </h3>
                                    <p className="text-sm text-blue-500 flex justify-between">
                                        Short name <span className="font-bold text-black">{team.shortName}</span>
                                    </p>
                                 
                                    <button onClick={() => editTeam(team)} className="bg-blue-500 text-white px-4 py-1 rounded hover:bg-blue-600 transition-colors duration-300 ease-in-out mr-2">
                                        Edit
                                    </button>
                                    <button onClick={() => confirmDeletion(team.teamId)} disabled={loading} className="bg-transparent border border-red-500 text-red-500 px-2 py-1 rounded hover:bg-red-500 hover:text-white transition-colors duration-300 ease-in-out">
                                        Delete
                                    </button>
                                </div>
                            </div>
                        )) : <div className="text-xl ml-10 mt-5 ">Oops! There are no teams in this auction.</div> }
                    </div>
                ) }

                {/* Navigation Button */}
                <RouteToprevBtn onClick={() => navigate('/auction/auction-details')} />


            </div>
        </>
    );
};

export default AuctionTeams;
