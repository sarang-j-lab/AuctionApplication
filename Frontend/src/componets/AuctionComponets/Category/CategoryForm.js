import { useContext, useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import { RouteToprevBtn } from '../../Component/Button.js';
import { messageContext } from '../../../context/MessageContext';
import axiosApi from '../../../utils/axiosApi';

const CategoryForm = () => {
    window.scrollTo(0, 0);

    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
    const location = useLocation();
    const navigate = useNavigate();
    const [categoryData, setCategoryData] = useState(location.state || {
        "categoryName": "",
        "maxPlayerPerTeam": "",
        "minPlayerPerTeam": 0,
        "baseBid": "",
        "increment": ""
    })

    const auction = JSON.parse(localStorage.getItem("auction"));
    const isEditForm = categoryData.categoryId;


    const handleSubmits = async (event) => {
        event.preventDefault()
        if (!auction) {
            setErrorMessage("Auction not found")
            return;
        }
        if (parseInt(categoryData.maxPlayerPerTeam) < parseInt(categoryData.minPlayerPerTeam)) {
            setErrorMessage("Max player should be greather than min player")
            return;
        }
        const url = isEditForm ? `/update-auction-category/${event.target.id}` : `/create-auction-category/${auction.auctionId}`;

        const method = isEditForm ? "put" : "post";

        try {
            await axiosApi({
                method,
                url,
                data: categoryData,
                headers: { "Content-Type": "application/json" }
            })
            setSuccessMessage(isEditForm ? "Category edited successfully!" : "Category created successfully!")
            navigate("/auction/auction-categories")
        } catch (e) {
            setErrorMessage(e.response.data.message || "Something went wrong! please try again.");
        }
    }

    if (!auction) {
        setErrorMessage("Auction not found!")
        return <Navigate to={"/"} />
    }

    return (
        <div className='xl:w-3/4 lg:w-3/4 sm:w-full p-2 rounded-lg flex flex-col  border-2'>
            <h1 className='self-center  text-2xl my-5'>{isEditForm ? "Edit category" : "Create new Category"}</h1>
            <form className="max-w-lg mx-auto" id={categoryData.categoryId} onSubmit={handleSubmits}>
                {!isEditForm ? <span className='text-[10px]  font-bold '>Carefully fill the details category Base bid and Min player cannot be edited! You have recreate cateogry.</span> : <span className='text-[10px]  font-bold '>Base bid and min player cannot be edited you have recreate category.</span>}
                <div className="mb-5">
                    <label htmlFor="categoryName" className="block mb-2 text-sm font-medium dark:text-gray">Category name</label>
                    <input autoComplete="off" onChange={(e) => { setCategoryData({ ...categoryData, "categoryName": e.target.value.toUpperCase() }) }} value={categoryData.categoryName} type="text" id='categoryName' className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5  dark:placeholder-gray-400 dark:text-black dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:shadow-sm-light" placeholder="Ex. Golden" required />
                </div>

                <div className='flex gap-2'>
                    <div className="mb-5">
                        <label htmlFor="maxPlayerPerTeam" className="block mb-2 text-sm font-medium text-gray-900 dark:gray-white">Max player per team</label>
                        <input autoComplete="off" onChange={(e) => { setCategoryData({ ...categoryData, "maxPlayerPerTeam": e.target.value }) }} value={categoryData.maxPlayerPerTeam} type="number" id="maxPlayerPerTeam" className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5  dark:placeholder-gray-400 dark:text-black dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:shadow-sm-light" placeholder='Ex. 4' required />
                    </div>
                    {!isEditForm && <div className="mb-5">
                        <label htmlFor="minPlayerPerTeam" className="block mb-2 text-sm font-medium text-gray-900 dark:gray-white">Min player per team <sub>{"(optional)"}</sub></label>
                        <input autoComplete="off" onChange={(e) => { setCategoryData({ ...categoryData, "minPlayerPerTeam": e.target.value }) }} value={categoryData.minPlayerPerTeam} type="number" id="minPlayerPerTeam" className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5  dark:placeholder-gray-400 dark:text-black dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:shadow-sm-light" placeholder='Ex. 2' required />
                    </div>}
                </div>
                {!isEditForm && <div className="mb-5">
                    <label htmlFor="baseBid" className="block mb-2 text-sm font-medium text-gray-900 dark:gray-white">Base bid</label>
                    <input autoComplete="off" onChange={(e) => { setCategoryData({ ...categoryData, "baseBid": e.target.value }) }} value={categoryData.baseBid} type="number" id="baseBid" className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5  dark:placeholder-gray-400 dark:text-black dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:shadow-sm-light" placeholder='Ex. 2,500' required />
                </div>}
                <div className="mb-5">
                    <label htmlFor="increment" className="block mb-2 text-sm font-medium text-gray-900 dark:gray-white">Bid increaseBy</label>
                    <input autoComplete="off" onChange={(e) => { setCategoryData({ ...categoryData, "increment": e.target.value }) }} value={categoryData.increment} type="number" id="increment" className="shadow-sm bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5  dark:placeholder-gray-400 dark:text-black dark:focus:ring-blue-500 dark:focus:border-blue-500 dark:shadow-sm-light" placeholder='Ex. 1000' required />
                </div>


                <button type="submit" value={categoryData.categoryId} className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">{categoryData.categoryId ? "Edit category" : "Create Category"}</button>
            </form>
            <RouteToprevBtn onClick={() => navigate("/auction/auction-categories")} />
        </div>
    )
}

export default CategoryForm