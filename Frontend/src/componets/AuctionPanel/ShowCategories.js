import React, { useContext, useEffect, useState } from 'react'
import axiosApi from '../../utils/axiosApi';
import { messageContext } from '../../context/MessageContext';
import { Navigate, useNavigate } from 'react-router-dom';

const ShowCategories = ({ categories, auctionData, setPlayers, fetchAuctionPlayers, setCategory }) => {

  const { setErrorMessage } = useContext(messageContext);

  const navigate = useNavigate();


  const changeCategory = async (event) => {
    if (event?.target?.value === "") {
      fetchAuctionPlayers()
      navigate("/auction-dashboard");
      return;
    }
    try {
      const response = await axiosApi.get(`/show-category-players/${event?.target?.value}`)
      const categoryPlayers = response?.data.filter((player)=> player?.status === "PENDING");
      console.log(categoryPlayers)
      setPlayers(categoryPlayers)
      setCategory(response?.data[0]?.categoryId)
      navigate('/auction-dashboard');
    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "Not able to fetch category players! please try again.")
    }
  }
  
  return (
    <>
      {categories.length !== 0 ? <div className='w-[100vw] h-[90vh] bg-white/20 backdrop-blur-2xl overflow-scroll scrollbar-hide rounded-xl'>
        <div className="w-full  h-[30vh] px-20 text-white flex flex-col items-center justify-center">
          <h1 className='text-[10vh] font-serif'>{auctionData.auctionName.toUpperCase()}</h1>
          <h1 className='text-[7vh] font-serif text-black bg-yellow-200 px-3 rounded-sm'>Categories</h1>
          <h1 className='text-xl font-serif text-black  px-3 rounded-sm'>Select category for list players</h1>
        </div>
        <div className='w-full grid grid-cols-3  p-10  gap-5 '>
          <button value=""  onClick={changeCategory}  className='text-2xl font-serif pl-2 h-[10vh] w-full bg-red-400 rounded-xl text-black flex  items-center justify-center hover:shadow-2xl hover:bg-red-500 cursor-pointer duration-300 transition-all'>
            None category players
          </button>
          {
            categories.map((category) => (
              <button value={category?.categoryId} onClick={changeCategory} key={category.categoryId} className='text-2xl font-serif pl-2 h-[10vh] w-full bg-red-400 rounded-xl text-black flex  items-center justify-center hover:shadow-2xl hover:bg-red-500 cursor-pointer duration-300 transition-all'>
                {category.categoryName.toUpperCase()}
              </button>
            ))
          }
        </div>
      </div> : <h1 className='text-black p-2 rounded-lg self-center bg-yellow-400 font-serif text-2xl m-10'>This auction does't have any category</h1>}
    </>
  )
}

export default ShowCategories