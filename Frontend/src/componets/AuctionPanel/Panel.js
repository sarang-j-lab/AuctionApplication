import { Link } from "react-router-dom"
import LoadingBar from "../LoadingBar"

const Panel = ({ auctionData, teams }) => {

    const textShadow = {
        fontSize: "30px",
        color: "black",
        textShadow: "10px 10px 15px rgba(0, 0, 0, 2)"
    }

    return (

        <>
            <div className="h-[90vh] w-full  rounded-xl">
                <div className="h-[80vh] flex gap-2">
                    <div className="w-1/3 h-full flex items-end">
                        <div className="bg-[#e67e22] h-16 w-full m-2 rounded-md ">
                            <div className="h-2/4 flex justify-center items-center font-serif text-xl shadow-xl border-b-2 w-1/2" style={textShadow}>Manoj kumar</div>
                            <div className="font-serif ml-10 text-lg">Batsman</div>
                        </div>
                    </div>
                    <div className=" w-1/3 h-full">
                        <div className="w-full h-full flex  items-end">
                            <div className=" h-48 w-full m-2 rounded-md flex flex-col items-center justify-center">
                                <div className="h-full w-2/4 border-2 border-blue-400  bg-white rounded-full">

                                </div>
                                <div className="h-1/4 bg-white w-3/4 rounded-full flex">
                                    <div className="w-1/2 text-black text-center text-2xl font-serif">current  bid </div>
                                    <div className="w-1/2 bg-blue-500 rounded-r-full text-center text-2xl font-serif">12000</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="w-1/3 h-full flex items-end">
                        <div className="bg-[#e67e22] h-40 w-full m-2 rounded-md ">
                            <div className="h-2/4 flex justify-center items-center font-serif text-xl shadow-xl border-b-2 w-2/3" style={textShadow}>jalgoan warriers</div>
                            <div className="grid grid-cols-2 mt-4">
                            <div className="font-serif ml-10 text-lg">Balance: 9600</div>
                            <div className="font-serif ml-10 text-lg">Reserve: 10000</div>
                            <div className="font-serif ml-10 text-lg">Max bid: 3000</div>
                            <div className="font-serif ml-10 text-lg">P.  4</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="w-screen flex justify-end border-white/30 rounded-xl h-[10vh] bg-white/20 backdrop-blur-md border">
                    <div className="w-full flex flex-row justify-end items-center gap-4 mr-4">
                        <span className="border-r-2 pr-4">Player code <button className="border font-thin px-3 py-1 hover:bg-slate-50 hover:text-gray-800 rounded-lg duration-500 transition-all">1</button></span>

                        <button className="border font-thin px-2 py-1 hover:bg-slate-50 hover:text-gray-800 rounded-lg duration-500 transition-all ">Newplayer</button>
                        <button className="border font-thin px-2 py-1 hover:bg-green-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">Sold</button>
                        <button className="border font-thin px-2 py-1 hover:bg-red-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">Unsold</button>
                        <span>Teams</span>
                        {
                            teams.map((team, i) => (

                                <button className="border font-thin px-2 py-1 hover:bg-orange-500 hover:text-gray-800 rounded-lg duration-500 transition-all ">{team.shortName}</button>
                            ))
                        }
                    </div>
                </div>
            </div>

        </>
    )
}

export default Panel