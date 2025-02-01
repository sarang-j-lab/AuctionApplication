import React from 'react'
import { Link } from "react-router-dom"

import { RiAuctionFill } from "react-icons/ri"
import { BiDollar } from "react-icons/bi";//my auction
import { BiMaleSign } from "react-icons/bi";//join auction

import { MdOutlineJoinInner } from "react-icons/md";

// array contain title, content, and icon in nested array of every feature
const menu = [
  {
    title: "New auction",
    description: "Create new auction and add player, teams.",
    icon: <RiAuctionFill />,
    path: "/auction-form"
  },
  {
    title: "Join Auction",
    description: "Join auction by auction ID.",
    icon: <MdOutlineJoinInner />,
    path: "/join-auction"
  },
  {
    title: "Self Auction",
    description: "Checkout your auctions.",
    icon: <BiMaleSign />,
    path: "/my-auction"
  },
  {
    title: "Auction Panel",
    description: "Join auction by auction ID.",
    icon: <BiDollar />,
    path: "/auction-panel"
  }
];


const Menu = () => {


  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-2 xl:grid-cols-2 gap-6 rounded-lg p-5 w-full  lg:w-3/4">

      {
        menu.map((menu, index) => (
          <Link key={index} to={menu.path}><div className="button flex flex-col gap-2 items-center justify-center rounded-2xl text-lg sm:text-xl md:text-2xl lg:text-3xl xl:text-4xl  hover:text-white hover:bg-blue-700  shadow-2xl p-5   sm:h-[200px] lg:h-[250px] xl:h-[150px]">

            <span className="flex cursor-pointer  w-full items-center justify-center hover:text-white text-gray-700 h-[full] gap-4"><span className='mt-2'>{menu.icon}</span>{menu.title}</span>
            <p className="text-xs sm:text-xs md:text-xs lg:text-sm">{menu.description}</p>
          </div></Link>
        ))
      }

     </div>
  )
}

export default Menu