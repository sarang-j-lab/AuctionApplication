import React, { createContext, useState } from 'react'


export const auctionPanelContext = createContext();


const AuctionPanelProvider = ({ children }) => {

    const [auctionData, setAuctionData] = useState([]);
    const [categories, setCategories] = useState([]);
    const [teams, setTeams] = useState([]);
    const [players, setPlayer] = useState([]);
    const [additionalIncrement, setAdditionalIncrement] = useState([]);
    const [categoryIncrements, setCategoryIncrements] = useState([]);


    return (
        <auctionPanelContext.Provider value={{auctionData,setAuctionData,categories,setCategories,teams,
            setTeams,players,setPlayer,additionalIncrement,setAdditionalIncrement,
            categoryIncrements,setCategoryIncrements}}>{children}</auctionPanelContext.Provider>
    )
}

export default AuctionPanelProvider