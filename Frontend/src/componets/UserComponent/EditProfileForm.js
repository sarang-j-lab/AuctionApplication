import  { useContext, useState } from 'react';
import axiosApi from '../../utils/axiosApi';
import { messageContext } from "../../context/MessageContext.js"
import { Navigate, useNavigate } from 'react-router-dom';

const EditProfileForm = () => {

  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem("user"));

  // State to manage form data
  const [userData, setUserData] = useState(user?.user);

  const { setSuccessMessage, setErrorMessage } = useContext(messageContext);
  // Handle input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserData({
      ...userData,
      [name]: value,
    });
  };

  // Handle form submission
  const handleSubmit = async (event) => {
    event.preventDefault();

    if(!user){
      setErrorMessage("User not found!")
      return;
    }
    try {
      const response = await axiosApi.put(`/user/edit-profile/${user?.user?.userId}`, userData, {
        headers: {
          "Content-Type": "application/json",
        }
      })
      user.user = response?.data
      localStorage.setItem("user", JSON.stringify(user));
      setSuccessMessage("User edited successfully");
      navigate("/auction/user-profile")
    } catch (error) {
      setErrorMessage(error?.response?.data?.message || "User editing failed! please try again.")
      navigate("/auction/user-profile")
    }
  };


  if(!user){
    setErrorMessage('User not found!')
    return <Navigate to={"/authentication"}/>
  }

  return (
    <div className="w-full">
      <div className="max-w-4xl mx-auto bg-white rounded-lg shadow-lg p-8">
        <h1 className="text-3xl font-bold text-gray-800 text-center mb-8">Edit Profile</h1>

        {/* Edit Form */}
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Name Field */}
          <div>
            <label className="block text-sm font-medium text-gray-500">Name</label>
            <input
              type="text"
              name="name"
              value={userData.name}
              onChange={handleInputChange}
              className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Mobile Number Field */}
          <div>
            <label className="block text-sm font-medium text-gray-500">Mobile Number</label>
            <input
              type="number"
              name="mobileNo"
              value={userData.mobileNo}
              onChange={handleInputChange}
              className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Email Field */}
          <div>
            <label className="block text-sm font-medium text-gray-500">Email Address</label>
            <input
              type="email"
              name="email"
              value={userData.email}
              onChange={handleInputChange}
              className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* City Field */}
          <div>
            <label className="block text-sm font-medium text-gray-500">City</label>
            <input
              type="text"
              name="city"
              value={userData.city}
              onChange={handleInputChange}
              className="mt-1 block w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          {/* Submit Button */}
          <div className="flex justify-end">
            <button
              type="submit"
              className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-300"
            >
              Save Changes
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EditProfileForm;