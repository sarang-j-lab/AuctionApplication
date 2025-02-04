import React from 'react'
import { Link, Outlet, Route, Routes, useNavigate } from 'react-router-dom'
import Menu from '../componets/Menu'
import AuctionForm from '../componets/AuctionComponets/AuctionForm'
import MyAuction from '../componets/AuctionComponets/MyAuction'
import AuctionDetail from '../componets/AuctionComponets/AuctionDetail'
import AuctionTeams from '../componets/AuctionComponets/Team/AuctionTeams'
import TeamForm from '../componets/AuctionComponets/Team/TeamForm'
import AuctionPlayers from '../componets/AuctionComponets/Player/AuctionPlayers'
import PlayerForm from '../componets/AuctionComponets/Player/PlayerForm'
import JoinAuction from '../componets/JoinAuction'
import AuctionCategories from '../componets/AuctionComponets/Category/AuctionCategories'
import CategoryForm from '../componets/AuctionComponets/Category/CategoryForm'
import { useEffect } from 'react'
import axiosApi from '../utils/axiosApi'
import UserProfilePage from '../componets/UserProfilePage'
import EditProfileForm from '../componets/EditProfileForm'
import Panel from '../componets/AuctionPanel/Panel'
import Navbar from './Navbar'
import Footer from './Footer'

const Main = () => {
    const menu =  [{title:"Home",path:"/"},{title:"Self Auction",path:"/auction/my-auction"},{title:"join Auction",path:"/auction/join-auction"},{title:"New Auction",path:"/auction/auction-form"},{title:"Auction Panel",path:"/auction/auction-panel"}]

    return (
        <>
            <Navbar />
            <main className="border-2  mx-5 flex flex-col lg:flex-row space-x-0 lg:space-x-1 rounded-lg border-gray-200  justify-center sm:items-center lg:items-start px-5 py-5">
                {/* <Routes>
                <Route path='/' element={<Menu />} />
                <Route path='auction-dashboard' element={<Panel />} />
                <Route path='/user-profile' element={<UserProfilePage />} />
                <Route path='/edit-profile' element={<EditProfileForm />} />
                <Route path='/auction-form' element={<AuctionForm />} />
                <Route path='/my-auction' element={<MyAuction />} />
                <Route path='/auction-details' element={<AuctionDetail />} />
                <Route path='/auction-teams' element={<AuctionTeams />} />
                <Route path='/auction-players' element={<AuctionPlayers />} />
                <Route path='/team-form' element={<TeamForm />} />
                <Route path='/player-form' element={<PlayerForm />} />
                <Route path='/join-auction' element={<JoinAuction />} />
                <Route path='/auction-categories' element={<AuctionCategories />} />
                <Route path='/category-form' element={<CategoryForm />} />
            </Routes> */}
                <Outlet />
                    
                <div className="m-2 w-full h-full  sm:w-full md:w-full lg:w-[20vw] xl:w-[22vw]  rounded-lg border-gray-300 shadow-2xl py-5">
                    <ul className=" mx-6 mt-7 flex flex-col items-center justify-center gap-8 rounded-3xl">
                    {menu.map((item,i)=>(
                        <Link key={i} to={item.path}><li className="button font-medium  cursor-pointer rounded-xl text-lg transition-all hover:text-xl  hover:bg-blue-700 hover:text-white border-2 border-grey-500 px-8 py-2 lg:text-xl xl:text-lg">{item.title}</li></Link>
                    ))
                    
                }
                </ul>
                </div>

            </main>
            <Footer />
        </>
    )
}

export default Main