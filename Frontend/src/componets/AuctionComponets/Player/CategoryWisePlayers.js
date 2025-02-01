import React, { useContext, useState } from 'react'
import axiosApi from '../../../utils/axiosApi';
import { messageContext } from '../../../context/MessageContext';

const CategoryWisePlayers = ({ setPlayers,categories,fetchData }) => {

    const [categoryId, setCategoryId] = useState("");
    const {setErrorMessage} = useContext(messageContext);

    const handleCategoryChange = (e) => {
        setCategoryId(e.target.value === "" ? null : e.target.value);
    }

    const handleCategorySearch = async (event) => {
        event.preventDefault();
        if (categoryId === null) {
            fetchData();
            return;
        }
        try {
            const response = await axiosApi.get(`/show-category-players/${categoryId}`)
            const replaceCategoryId = response.data.map((player) => {
                return player.categoryId !== null ? { ...player, "categoryId": player.categoryId.categoryId }
                    :
                    { ...player, [player.categoryId]: null }

            });
            setPlayers(replaceCategoryId);
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Something went wrong! please try again.")
        }
    }




    return (
        <div className='xl:w-[65vw] lg:w-[60vw] md:w-[60vw] sm:w-[70vw] shadow-lg rounded-xl mx-4  flex space-y-4 justify-center items-center px-4 py-2  flex-col lg:flex-row md:flex-col sm:flex-col '>

            {categories.length > 0 && <div className="mb-4 mt-3 flex flex-row justify-center items-center gap-3">
                <form onSubmit={handleCategorySearch} className="mb-4 flex flex-row justify-center items-center gap-3">
                    <label htmlFor="category" className="block text-gray-700 text-sm font-medium">
                        Categories
                    </label>
                    <select id="category" name="categoryId" value={categoryId} onChange={handleCategoryChange} className="mt-1 px-3 py-3 block w-full rounded-md border-2 border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm">
                        <option value="">None</option>
                        {
                            categories.map((category) => (
                                <option key={category.categoryId} value={category.categoryId}>{category.categoryName}</option>
                            ))
                        }
                    </select>
                    <button className="bg-blue-500 text-white px-4 py-2  rounded-md hover:bg-blue-700 transition duration-300">Search</button>
                </form>

            </div>}
        </div>
    )
}

export default CategoryWisePlayers