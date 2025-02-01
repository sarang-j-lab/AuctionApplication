import React, { createContext, useState } from 'react'


export const categoryContext = createContext();

const CategoryProvider= ({children}) => {

const [auctionCategories,setAuctionCategories] = useState([]);


  return (
    <categoryContext.Provider value={{auctionCategories,setAuctionCategories}}>{children}</categoryContext.Provider>
  )
}

export default CategoryProvider