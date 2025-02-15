import React, { useContext, useEffect, useState } from "react";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import { messageContext } from "../../context/MessageContext";
import axiosApi from "../../utils/axiosApi";

/**
 * a NewAuctionForm component renders a form to create a new auction.
 * It manages the form state and handles form submission.
 *
 * @component
 * @example
 * return (
 *   <NewAuctionForm />
 * )
 *
 * @returns {JSX.Element} The rendered NewAuctionForm component.
 *
 * @function
 * @name NewAuctionForm
 *
 * @description
 * The form includes fields for auction name, date, season, time, points per team, base bid, bid increase, max players per team, and min players per team.
 * It uses the `useState` hook to manage form data and updates the state on input change.
 * On form submission, it prevents the default form submission behavior.
 */






const AuctionForm = () => {

  const location = useLocation()
  const initialState = {
    auctionName: "",
    auctionDate: "",
    season: "",
    auctionTime: "",
    pointsPerTeam: "",
    baseBid: "",
    bidIncreaseBy: "",
    maxPlayerPerTeam: "",
    minPlayerPerTeam: "",
    playerRegistration: false
  }
  const [auction, setAuction] = useState(location.state || initialState);
  const isEditing = Boolean(auction.auctionId);

  const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
  const user = JSON.parse(localStorage.getItem("user"));
  const navigate = useNavigate()


  const auctionFields = !isEditing ? [
    { label: "Auction Name", name: "auctionName", type: "text", placeholder: "Ex.  indian premier league", },
    { label: "Auction Date", name: "auctionDate", type: "date", },
    { label: "Season", name: "season", type: "number", placeholder: "Ex. 2", },
    { label: "Auction Time", name: "auctionTime", type: "time", },
    { label: "Points Per Team", name: "pointsPerTeam", type: "number", placeholder: "120000", },
    { label: "Base Bid", name: "baseBid", type: "number", placeholder: "Ex. 3000", },
    { label: "Bid Increase By", name: "bidIncreaseBy", type: "number", placeholder: "Ex. 1500", },
    { label: "Max Players Per Team", name: "maxPlayerPerTeam", type: "number", placeholder: "Ex. 14", },
    { label: "Min Players Per Team", name: "minPlayerPerTeam", type: "number", placeholder: "Ex. 11", },
  ] : [
    { label: "Auction Name", name: "auctionName", type: "text", placeholder: "Ex.  indian premier league", },
    { label: "Auction Date", name: "auctionDate", type: "date", },
    { label: "Season", name: "season", type: "number", placeholder: "Ex. 2", },
    { label: "Auction Time", name: "auctionTime", type: "time", },
    { label: "Bid Increase By", name: "bidIncreaseBy", type: "number", placeholder: "Ex. 1500", },
    { label: "Max Players Per Team", name: "maxPlayerPerTeam", type: "number", placeholder: "Ex. 14", },
  ]

  //initial state for auction if purpose is create form location.state will undefined else purpose is edit auction than auction state will taken from location.state


  //here we converting the form of date for edit which coming from server
  useEffect(() => {
    window.scrollTo(0, 0);
    const [day, month, year] = auction.auctionDate.split("-");
    const formattedDate = `${year}-${month}-${day}`;
    setAuction((prev) => ({ ...prev, auctionDate: formattedDate }));
  }, [])





  const handleChange = (event) => {
    const { name, value } = event.target;
    setAuction({ ...auction, [name]: value });
  };

  const handleToggle = () => {
    setAuction({ ...auction, playerRegistration: !auction.playerRegistration });
  }



  // handling form with condition of auction.auctionId if purpose is create auction than auction is undifiend else purpose is edit auction than auction have auctionId
  const handleForm = async (event) => {
    event.preventDefault();
    if (parseInt(auction.maxPlayerPerTeam) < parseInt(auction.minPlayerPerTeam)) {
      setErrorMessage("Max players should be greater than Min players");
      return;
    }
    if (parseInt(auction.baseBid * auction.minPlayerPerTeam) > parseInt(auction.pointsPerTeam)) {
      setErrorMessage("The Team's points are insufficient to meet the minimum player requirement for this base bid.")
      return;
    }

    if (!user) {
      setErrorMessage("User not found!")
      return;
    }
    const url = isEditing ? `/auction/edit-auction/${auction.auctionId}` : `/auction/new-auction/${user?.user?.userId}`;
    const method = auction.auctionId ? "put" : "post";
    try {
      const response = await axiosApi({
        method, url,
        data: auction,
        headers: {
          "Content-Type": "application/json",
        }
      })
      if (isEditing) {
        const updatedAuction = response?.data
        localStorage.setItem("auction", JSON.stringify({ additionalIncrements: updatedAuction.additionalIncrements, auctionId: updatedAuction.auctionId, auctionName: updatedAuction.auctionName, auctionDate: updatedAuction.auctionDate, auctionTime: updatedAuction.auctionTime, baseBid: updatedAuction.baseBid, bidIncreaseBy: updatedAuction.bidIncreaseBy, maxPlayerPerTeam: updatedAuction.maxPlayerPerTeam, minPlayerPerTeam: updatedAuction.minPlayerPerTeam, pointsPerTeam: updatedAuction.pointsPerTeam, reserve: updatedAuction.reserve, season: updatedAuction.season }))
        setSuccessMessage("Auction edited successfully!")
        navigate("/auction/auction-details");
      } else {
        navigate("/")
        setSuccessMessage("Form submitted successfully!");
      }
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
    }
  }


  if (!user) {
    setErrorMessage("User not found!")
    return <Navigate to={"/authentication"} />
  }


  return (
    <div className="flex flex-col   rounded-lg border-2 items-center xl:w-3/4 lg:w-3/4 sm:w-[90vw]  justify-center ">

      <form onSubmit={handleForm} className="flex flex-col bg-white shadow-md rounded-lg p-3  w-full mx-4 ">

        <h1 className="text-2xl  text-center mb-4"> {auction.auctionId ? "Edit Auction" : "Create Auction"}</h1>

        {!isEditing && <p className="mb-8 self-center text-xs text-gray-800 sm:text-xs">Carefully fill the details Base bid, Min player, Points cannot be edited again! you have to recreate auction.</p>}

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">

          {auctionFields.map(({ label, name, type, placeholder }) => (
            <div key={name} className="mb-5">
              <label htmlFor={name} className="block mb-2 text-sm font-medium">
                {label}
              </label>
              <input
                autoComplete="off"
                type={type}
                id={name}
                name={name}
                value={auction[name]}
                onChange={handleChange}
                placeholder={placeholder || ""}
                className="shadow-sm bg-gray-50 border border-gray-300 text-sm rounded-lg w-full p-2.5 focus:ring-blue-500 focus:border-blue-500"
                required
              />
            </div>
          ))}
          <div className="inline-flex items-center cursor-pointer">
            <label className="flex mb-5">
              <span className="mr-3 text-md font-medium text-gray-900 ">Player self Registration</span>
              <input type="checkbox" onChange={handleToggle} className="sr-only peer" checked={auction.playerRegistration} />
              <div className="relative w-11 h-6 bg-gray-200 rounded-full peer peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 dark:bg-gray-700 peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-0.5 after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600 dark:peer-checked:bg-blue-600"></div>
            </label>
          </div>
        </div>
        <button
          type="submit"
          className="w-full text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
        >
          {auction.auctionId ? "Save Changes" : "Create Auction"}
        </button>
      </form>

      <button
        onClick={() => navigate(auction.auctionId ? "/auction/auction-details" : "/")}
        className="mt-4 w-full text-blue-700 border border-blue-700 hover:bg-blue-100 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
      >
        Cancel
      </button>
    </div>
  );
}

export default AuctionForm;
