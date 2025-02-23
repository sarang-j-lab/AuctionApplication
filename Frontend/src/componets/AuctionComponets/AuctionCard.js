import { useNavigate } from "react-router-dom";
const AuctionCard = ({ userAuctions }) => {
  window.scrollTo(0, 0);

  const navigate = useNavigate();


  const showDetails = async (event) => {
    const auction = userAuctions.filter((auction) => String(auction.auctionId) === event.target.value)
    localStorage.setItem("auction", JSON.stringify(...auction));
    navigate("/auction/auction-details")
  }




  return (
    <>
      <div className='grid lg:grid-cols-2  md:grid-cols-2 gap-10 sm:grid-cols-1'>
        {
          userAuctions.length > 0 && userAuctions.map((auction) => (
            < div key={auction.auctionId} className="m-2 flex items-center justify-center ">
              <div className="px-4  max-w-sm w-full bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-all">
                <div className="relative">
                  <img
                    src="/auctionimage.jpg"
                    alt="Auction"
                    className="w-full h-52 object-cover"
                  />
                  <span className="absolute top-3 right-3 bg-black text-white px-3 py-1 rounded-full text-sm font-medium">
                    Incomplete
                  </span>
                </div>

                <div className="p-5 space-y-4">
                  <div>
                    <h3 className="text-xl font-bold text-gray-900">{auction.auctionName}</h3>
                    <p className=" mt-1 text-blue-500">Bid Increase By <span className="text-gray-600">&#8377;{auction.bidIncreaseBy.toLocaleString()}</span></p>
                    <p className="text-blue-500 mt-1">Base Bid <span className="text-gray-600">&#8377;{auction.baseBid.toLocaleString()}</span></p>
                  </div>


                  <div className="space-y-1">
                    <p className=" font-bold text-blue-900 text-xl">Points Per Team <span className="text-gray-600 text-xl">&#8377;{auction.pointsPerTeam.toLocaleString()}</span></p>
                    <p className="text-sm text-blue-500 ">Max player <span className="text-gray-600">{auction.maxPlayers}</span></p>
                  </div>


                  <button value={auction.auctionId} onClick={showDetails} className="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-lg transition-colors">
                    Show details
                  </button>

                </div>
              </div>

            </div>
          ))
        }
      </div>
    </>
  );
};


export default AuctionCard;
