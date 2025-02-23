import { useContext, useState } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { messageContext } from "../../context/MessageContext.js"
import { getRole } from '../../utils/JwtConfig.js';

const UserProfileDropdown = ({ handleLogout }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const { setErrorMessage } = useContext(messageContext);


  const user = JSON.parse(localStorage.getItem("user"));

  if (!user) {
    setErrorMessage("user not found");
    return <Navigate to={"/authentication"} />
  }

  return (
    <div className="relative border-2 py-2 px-2 ml-2 rounded-xl">
      <button
        onClick={toggleDropdown}
        className="flex items-center text-sm font-medium text-gray-700 hover:text-gray-900 focus:outline-none"
      >
        <span
          className="h-8 w-8 ml-2 rounded-full bg-gray-600 flex  justify-center  text-white text-xl"
        >{user?.user?.name.charAt(0)}</span>
        <span className='ml-1'>{user?.user?.name}</span>
        <svg
          className="ml-2 h-4 w-4"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            d="M19 9l-7 7-7-7"
          />
        </svg>
      </button>

      {isOpen && (
        <div className="absolute  right-0 mt-5 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
          <div className="py-1">
            <Link to={"/auction/user-profile"} onClick={() => setIsOpen(!isOpen)}

              className="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Profile
            </Link>
            
           {getRole(user?.token) === "ADMIN" &&  <Link to={"/admin"} 
              className="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Admin panel
            </Link>}
            <Link to={"/authentication"} onClick={handleLogout}
              className="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
            >
              Sign out
            </Link>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserProfileDropdown;