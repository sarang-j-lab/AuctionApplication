import React, { useContext, useEffect, useRef, useState } from 'react'
import { Link, Route, Routes, useParams } from 'react-router-dom';
import { messageContext } from '../../../context/MessageContext';

import LiveAuctionPanel from './LiveAuctionPanel';
import LiveAuctionTeams from './LiveAuctionTeams';
import LiveAuctionPlayers from './LiveAuctionPlayers';



const LiveAuction = () => {

    return (
        <Routes>
            <Route path='/:auctionId' element={<LiveAuctionPanel />} />
            <Route path="/teams" element={<LiveAuctionTeams />} />
            <Route path="/players" element={<LiveAuctionPlayers />} />
        </Routes>
    )
}

export default LiveAuction