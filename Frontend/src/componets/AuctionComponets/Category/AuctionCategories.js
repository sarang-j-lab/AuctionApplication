import { use, useContext, useEffect, useState } from 'react'
import { RouteToprevBtn } from '../../Component/Button';
import { Navigate, useNavigate } from 'react-router-dom';
import Confirmation from '../../Component/Confirmation';
import PopupForm from '../PopupForm';
import { messageContext } from '../../../context/MessageContext';
import axiosApi from '../../../utils/axiosApi';
import LoadingBar from '../../Component/LoadingBar.js';

const AuctionCategories = () => {
    window.scrollTo(0, 0);

    const navigate = useNavigate()
    const [categories, setCategories] = useState([]);
    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
    const auction = JSON.parse(localStorage.getItem("auction"));
    const [loading, setLoading] = useState(false);
    const [categoryLoading,setCategoryLoading] = useState(false)

    const [confirmation, setConfirmation] = useState(false);
    const [selectedCategoryId, setSelectedCategoryId] = useState(0)

    const [isOpen, setIsOpen] = useState(false);

    useEffect(() => {
        fetchCategories();
    }, [])

    const fetchCategories = async () => {
        if (!auction) {
            setErrorMessage("Auction not found")
            return;
        }
        try {
            setCategoryLoading(true)
            const response = await axiosApi.get(`/show-auction-category/${auction?.auctionId}`);
            setCategories(response?.data);
        } catch (e) {
            setErrorMessage(e?.response?.data?.message || "Something went wrong! please try again.");
        }finally{
            setCategoryLoading(false);
        }
    }

    const addCateogry = () => {
        navigate("/auction/category-form")
    }
    const editCategory = (category) => {
        navigate("/auction/category-form", { state: category })
    }

    const deleteConfirmation = (e) => {
        setSelectedCategoryId(e.target.value)
        setConfirmation(true);
    }

    const deleteCategory = async () => {
        try {
            setCategoryLoading(true)
            await axiosApi.delete(`/delete-auction-category/${selectedCategoryId}/${auction.auctionId}`);
            setConfirmation(false);
            setCategories((prev) => prev.filter(category => category.categoryId !== selectedCategoryId));
            setSelectedCategoryId(0);
            setSuccessMessage("Category deleted successfully!");
        } catch (e) {
            if (e.response.data.message === "Cannot delete or update a parent row: a foreign key constraint fails (`auction_db`.`auction_player_tbl`, CONSTRAINT `FKq8hika4e7jaepdlxwih24l13o` FOREIGN KEY (`player_id`) REFERENCES `player_tbl` (`player_id`))") {
                setErrorMessage("First you have to edit player category which belongs to this category!");
                navigate('/auction/auction-players')
            } else {
                setErrorMessage(e.response.data.message || "Something went wrong! please try again.");
            }
        }finally{
            setCategoryLoading(false)
        }
    }

    const popUp = (e) => {
        setSelectedCategoryId(e.target.value)
        setIsOpen(true)
    }

    const deleteCategoryIncrement = async (e) => {
        const { id, value } = e.target
        try {
            setLoading(true);
            setCategoryLoading(true)
            await axiosApi.delete(`/delete-category-increment/${id}/${value}`)
            fetchCategories();
            setSuccessMessage("Increment deleted successfully!")
        } catch (e) {
            setErrorMessage(e.response.data.message || "Something went wrong! please try again.")
        }finally{
            setLoading(false);
            setCategoryLoading(false);
        }
    }

    if (!auction) {
        setErrorMessage("Auction not found!")
        return <Navigate to={"/"} />
    }

    return (
        <div className='xl:w-3/4 lg:w-3/4 sm:w-full'>

            {isOpen && <PopupForm purpose={"categoryIncrement"} id={selectedCategoryId} setId={setSelectedCategoryId} setIsOpen={setIsOpen} fetchCategories={fetchCategories} />}
            {confirmation && <Confirmation setConfirmation={setConfirmation} setId={setSelectedCategoryId} deleteFun={deleteCategory} />}

            <div className='xl:w-[65vw] lg:w-[60vw] text-xl md:w-full sm:w-full shadow-lg rounded-xl mx-4  flex space-y-4 justify-center items-center px-4 py-2  flex-col lg:flex-row md:flex-row sm:flex-col '>
                <h1 className='text-xs text-blue-600 md:text-lg lg:text-2xl sm:text-xs mx-auto'>{auction.auctionName.toUpperCase()}<span className='text-xs ml-3 lg:text-xl sm:text-xs'>Categories</span></h1>

                <button onClick={addCateogry} className='rounded-md border text-sm p-2 mx-auto hover:bg-blue-600 hover:text-white '>Add Category</button>
            </div>
            {categoryLoading && <LoadingBar/>}

            <div className=" my-4 xl:w-[65vw] lg:w-[60vw] md:w-full sm:w-full grid xl:grid-cols-3 lg:grid-cols-2 md:grid-cols-2  gap-10 mr-2 bg-white  rounded-xl">
                {categories.length !== 0 ? categories.map((category) => (
                    <div key={category.categoryId} className="xl:w-[22vw]  px-4 bg-white border-2 flex flex-col items-center rounded-xl pt-5 pb-5">
                        <h5 className="mb-1 text-xl font-medium text-gray-900 dark:text-gray-900">{category.categoryName}</h5>
                        <span className="text-sm text-gray-500 dark:text-gray-400">Base Bid : {category.baseBid}</span>
                        <span className="text-sm text-gray-500 dark:text-gray-400">IncreaseBy : {category.increment}</span>
                        <div className="mt-4 flex flex-col gap-2 lg:mt-6">
                            <p className=" items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-black">
                                Max player per team: {category.maxPlayerPerTeam}
                            </p>
                            <p className=" items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-black">
                                min player per team: {category.minPlayerPerTeam}
                            </p>
                        </div>
                        <div className="mt-4 flex space-x-2 lg:mt-6">
                            <button disabled={categoryLoading} onClick={() => { editCategory(category) }}
                                className="inline-flex border-2 border-blue-500 bg-blue-500 hover:bg-blue-600 items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white">
                                Edit
                            </button>
                            <button disabled={categoryLoading} onClick={popUp} value={category.categoryId}
                                className="inline-flex text-xs border-1 border-blue-500 bg-blue-500 hover:bg-blue-600  items-center shadow-xl rounded-lg px-1 py-2 text-center  font-medium text-white">
                                Add increment
                            </button>
                            <button disabled={categoryLoading} onClick={deleteConfirmation} value={category.categoryId}
                                className="inline-flex border-2 border-red-500 bg-red-500 hover:bg-red-600  items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white">
                                Delete
                            </button>
                        </div>
                        <div className='flex flex-col mt-3'>
                            {category.categoryAdditionalIncrements.length > 0 &&
                                <>
                                    <p className='text-sm mr-10'>Additional category increments</p>
                                    {category.categoryAdditionalIncrements.length > 0 &&
                                        category.categoryAdditionalIncrements.map(increm => (
                                            <>
                                                {!loading ? <div key={increm.id} className='flex flex-row gap-2 justify-evenly items-center text-sm mt-1 '>
                                                    <p>Increment: {increm.increment}</p>
                                                    <p>After: {increm.after}</p>
                                                    <button id={category.categoryId} value={increm.id} onClick={deleteCategoryIncrement} className='border-2 px-1 self-end hover:bg-red-400 hover:border-red-400'>X</button>
                                                </div> : <LoadingBar />}
                                            </>
                                        ))
                                    }
                                </>}
                        </div>
                    </div>
                )) : <p className="text-xl ml-10 mt-5">There is no category in this auction.</p>}
            </div>

            <RouteToprevBtn onClick={() => navigate("/auction/auction-details")} />
        </div>
    )
}

export default AuctionCategories