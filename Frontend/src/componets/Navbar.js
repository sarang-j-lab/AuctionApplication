
import {  useContext, useState } from 'react'
import { RiAuctionFill } from "react-icons/ri";
import { Link } from 'react-router-dom';
import UserProfileDropdown from './UserProfileDropDown';
import { messageContext } from '../context/MessageContext';


const Navbar = () => {

    const [block, setBlock] = useState(false)

    const navItems = ["Feature", "Contact Us", "About Us", "Pricing"]

    const {setErrorMessage} = useContext(messageContext);
    
    const user = JSON.parse(localStorage.getItem("user"));
    
    const handleLogout = () => {
        localStorage.removeItem("user");
        localStorage.removeItem("auction");
        setErrorMessage("User logged out successfully!");
        setBlock(!block)
    }
    
  

    return (
        <header className=" bg-white lg:pb-0">
            <div className="px-4 mx-auto max-w-7xl sm:px-6 lg:px-8">


                <nav className="flex items-center justify-between h-16 lg:h-20 bg-white ">
                    {/* App name and logo */}
                    <div className="flex-shrink-0 w-25">
                        <div className="flex gap-2  justify-center items-center">
                            <p className='text-xl'>TrueAuction</p>
                            <RiAuctionFill size={25} />
                        </div>
                    </div>

                    {/* small size navbar button */}
                    <button type="button" onClick={() => setBlock(!block)} className="inline-flex p-2 text-black transition-all duration-200 rounded-md lg:hidden focus:bg-gray-100 hover:bg-gray-100">
                        {/* <!-- Menu open: "hidden", Menu closed: "block" --> */}
                        <svg className="block w-6 h-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 8h16M4 16h16" />
                        </svg>

                        {/* <!-- Menu open: "block", Menu closed: "hidden" --> */}
                        <svg className="hidden w-6 h-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                        </svg>
                    </button>


                    {/* Navbar items in full size */}
                    <div className="hidden lg:flex lg:items-center lg:ml-auto lg:space-x-10">
                        {
                            navItems.map((item, index) => (
                                <p title="" key={index} className="text-base font-medium text-black transition-all duration-200 hover:text-blue-600 focus:text-blue-600">{item} </p>

                            ))
                        }
                    </div>
                    <div>
                        {/* <button onClick={handleLogout} className="items-center justify-center hidden px-4 py-3 ml-10 text-base font-semibold text-white transition-all duration-200 bg-blue-600 border border-transparent rounded-md lg:inline-flex hover:bg-blue-700 focus:bg-blue-700" role="button"> Logout</button> */}
                        {!user   ? <Link className='border-2 py-2 px-2 ml-2 rounded-xl bg-red-500 text-white'>Register/login</Link> : <UserProfileDropdown handleLogout={handleLogout} />  }

                    </div>
                </nav>


                {/* navbar items in small size  */}
                {block && <nav className="pt-4 pb-6 bg-white border border-gray-200 rounded-md shadow-md lg:hidden">
                    <div className="flow-root ">
                        <ul className="flex flex-col px-6 -my-2 space-y-1">
                            {
                                navItems.map((item, index) => (
                                    <li key={index} className="text-base font-medium text-black transition-all duration-200 hover:text-blue-600 focus:text-blue-600">{item} </li>

                                ))
                            }
                        </ul>
                    </div>
                </nav>}
            </div>
        </header>
    )
}

export default Navbar