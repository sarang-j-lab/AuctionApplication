import  { createContext, useState } from 'react'

export const auctionContext = createContext();


const AuctionProvider = ({ children }) => {


    const [auctionData, setAuctionData] = useState([])
    const [currentAuction, setCurrentAuction] = useState({})


    return (
        <auctionContext.Provider value={{ auctionData, setAuctionData,  currentAuction, setCurrentAuction }}>{children}</auctionContext.Provider>
    )
}

export default AuctionProvider