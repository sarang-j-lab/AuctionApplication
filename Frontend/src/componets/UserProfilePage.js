import { useContext } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { messageContext } from '../context/MessageContext';

const UserProfilePage = () => {
  // Sample user data (can be fetched from an API or state)


  const user = JSON.parse(localStorage.getItem("user"));

  const {setErrorMessage} = useContext(messageContext);
  
  if (!user) {
    setErrorMessage("user not found");
    return <Navigate to={"/authentication"} />
  }


  return (
    <div className='xl:w-3/4 lg:w-3/4 sm:w-full flex justify-center items-center'>
      <div className="xl:w-2/4 lg:w-2/4 sm:w-full p-4 bg-white rounded-lg shadow-lg ">
        {/* Profile Header */}
        <div className="flex flex-col items-center space-y-4">
          {/* <img
            src={user.profilePicture}
            alt="Profile"
            className="h-32 w-32 rounded-full border-4 border-white shadow-lg"
          /> */}
          <h1 className="text-3xl font-bold text-gray-800">{user?.user?.name}</h1>
        </div>

        {/* User Details */}
        <div className="mt-8 space-y-6">
          <div className="space-y-2">
            <label className="text-sm font-medium text-gray-500">Mobile Number</label>
            <p className="text-lg text-gray-800">{user?.user?.mobileNo}</p>
          </div>

          <div className="space-y-2">
            <label className="text-sm font-medium text-gray-500">Email Address</label>
            <p className="text-lg text-gray-800">{user?.user?.email}</p>
          </div>

          <div className="space-y-2">
            <label className="text-sm font-medium text-gray-500">City</label>
            <p className="text-lg text-gray-800">{user?.user?.city}</p>
          </div>
        </div>

        {/* Edit Profile Button (Optional) */}
        <div className="mt-8 flex justify-center">
          <Link to={"/auction/edit-profile"} className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-300">
            Edit Profile
          </Link>
        </div>
      </div>
    </div>
  );
};

export default UserProfilePage;