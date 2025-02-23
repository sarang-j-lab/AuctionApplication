import React from 'react'
import { Link, Outlet, Route, Routes, useNavigate } from 'react-router-dom'

import Navbar from './Navbar'
import Footer from './Footer'
import SideMenu from './SideMenu'

const Main = () => {
    window.scrollTo(0, 0);
    const menu = [{ title: "Home", path: "/" }, { title: "Self Auction", path: "/auction/my-auction" }, { title: "join Auction", path: "/auction/join-auction" }, { title: "New Auction", path: "/auction/auction-form" }, { title: "Auction Panel", path: "/auction/auction-panel" }]

    return (
        <>
            <Navbar />
            <main className="border-2  mx-5 flex flex-col lg:flex-row space-x-0 lg:space-x-1 rounded-lg border-gray-200  justify-center sm:items-center lg:items-start px-5 py-5">
                <Outlet />
                <SideMenu />
            </main>
            <Footer />
        </>
    )
}

export default Main