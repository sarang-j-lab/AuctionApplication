import { useContext, useState } from "react";
import axiosApi from "../../utils/axiosApi";
import { messageContext } from "../../context/MessageContext";
import { Navigate } from "react-router-dom";
import LoadingBar from "../Component/LoadingBar";

const PopupForm = ({ purpose, id, setId, setIsOpen, fetchCategories }) => {


  const [incrementData, setIncrementData] = useState({
    increment: "",
    after: ""
  })
  const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
  const auction = JSON.parse(localStorage.getItem("auction"));
  const [loading, setLoading] = useState(false);

  // Close popup
  const closePopup = () => {
    setId(0)
    setIsOpen(false);
    setIncrementData({ increment: "", after: "" });
  };


  /**
   * Handles the form submission for adding increments.
   * 
   * @param {Event} event - The form submission event.
   * @returns {Promise<void>} - A promise that resolves when the form submission is complete.
   * 
   * @async
   * @function handleSubmit
   * 
   * @description
   * This function handles the form submission for adding increments to either an auction or a category.
   * It sends a POST request to the appropriate endpoint based on the `purpose` parameter.
   * 
   * If the purpose is "auctionIncrement", it updates the auction with additional increments and stores the updated auction in local storage.
   * If the purpose is not "auctionIncrement", it fetches the updated categories and resets the form state.
   * 
   * In case of an error, it sets an error message.
   * 
   * @throws Will throw an error if the POST request fails.
   */
  const handleSubmit = async (event) => {
    event.preventDefault();

    const url = purpose === "auctionIncrement" ? `/auction/add-increments/${auction.auctionId}` : `/add-category-increments/${id}`;

    try {
      setLoading(true)
      const response = await axiosApi({
        method: "post", url, data: incrementData, headers: {
          "Content-Type": "application/json",
        }
      })

      if (purpose === "auctionIncrement") {
        setSuccessMessage("Additional increments added in auction successfully!")
        const updatedAuction = response?.data;
        localStorage.setItem("auction", JSON.stringify({ ...auction, additionalIncrements: updatedAuction.additionalIncrements }));
        setIncrementData({ increment: "", after: "" })
        setIsOpen(false);
      } else {
        fetchCategories();
        setId(0);
        setIncrementData({ increment: "", after: "" })
        setIsOpen(false);
        setSuccessMessage("Category increment added successfully!")
      }

    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "Something went wrong! please try again.");
    } finally {
      setLoading(false)
    }
  }


  if (!auction) {
    setErrorMessage("Auction not found!")
    return <Navigate to={"/"} />
  }

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
        <div className="bg-white p-6 rounded-lg shadow-lg w-96">

          <h2 className="text-xl font-semibold mb-4 text-gray-800">Add increment for category</h2>
          <form onSubmit={handleSubmit}>

            <div className="mb-4">
              <label htmlFor={`increment`} className="block text-sm font-medium text-gray-700"> Increment</label>
              <input
                type="number"
                id={`increment`}
                value={incrementData.increment}
                onChange={(event) => setIncrementData({ ...incrementData, increment: event.target.value })}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder={`Ex. 1000`} />
            </div>

            <div className="mb-4">
              <label htmlFor={`after`} className="block text-sm font-medium text-gray-700"> After</label>
              <input
                type="number"
                id={`after`}
                value={incrementData.after}
                onChange={(event) => setIncrementData({ ...incrementData, after: event.target.value })}
                className="w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder={`Ex. 5000`}
              />
            </div>

            {/* Action Buttons */}
            <div className="flex justify-end space-x-3">
              <button type="button" onClick={closePopup} className="px-4 py-2 text-sm text-gray-600 border rounded-lg hover:bg-gray-100">
                Close
              </button>
              {loading && <LoadingBar />}
              <button disabled={loading} type="submit" className="px-4 py-2 text-sm text-white bg-blue-600 rounded-lg hover:bg-blue-700">
                Submit
              </button>
            </div>

          </form>
        </div>
      </div>

    </div>
  );
};

export default PopupForm;
