import React, { useState } from 'react'

const ShowCategories = ({ categories, auctionData }) => {

  const [selectedCategoryId, setSelectedCategoryId] = useState(null);

  return (
    <>
      {categories.length !== 0 ? <div className='w-[100vw] h-[90vh] bg-white/20 backdrop-blur-2xl overflow-scroll scrollbar-hide rounded-xl'>
        <div className="w-full  h-[30vh] px-20 text-white flex flex-col items-center justify-center">
          <h1 className='text-[10vh] font-serif'>{auctionData.auctionName.toUpperCase()}</h1>
          <h1 className='text-[7vh] font-serif text-black bg-yellow-200 px-3 rounded-sm'>Categories</h1>
          <h1 className='text-xl font-serif text-black  px-3 rounded-sm'>Select category for list players</h1>
        </div>
        <div className='w-full grid grid-cols-3  p-10  gap-5 '>
          <div className='h-[10vh] w-full bg-red-400 rounded-xl text-black flex  items-center justify-center hover:shadow-2xl hover:m-2 cursor-pointer duration-300 transition-all'>
            <div className='text-2xl font-serif    pl-2'>All</div>
          </div>
          {
            categories.map((category) => (
              <div key={category.categoryId} className='h-[10vh] w-full bg-red-400 rounded-xl text-black flex  items-center justify-center hover:shadow-2xl hover:m-2 cursor-pointer duration-300 transition-all'>
                <div className='text-2xl font-serif pl-2'>{category.categoryName.toUpperCase()}</div>
              </div>
            ))
          }
        </div>
      </div> : <h1 className='text-black p-2 rounded-lg self-center bg-yellow-400 font-serif text-2xl m-10'>This auction does't have any category</h1>}
    </>
  )
}

export default ShowCategories