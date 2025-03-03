import axios from 'axios';
import  { useContext, useState } from 'react'
import { RiAuctionFill } from 'react-icons/ri';
import { useNavigate } from 'react-router-dom';
import { messageContext } from '../../context/MessageContext';
const API_URL = process.env.REACT_APP_API_URL
const Signup = ({ setSignIn }) => {

    const navigate = useNavigate();


    const [formData, setFormData] = useState({
        name: "",
        mobileNo: "",
        email: "",
        city: "",
        password: "",
    });

    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);



    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post(`${API_URL}/user/user-registration`,
                formData,
                {
                    headers: {
                        "Content-Type": "application/json"
                    },
                })
            localStorage.setItem("user",JSON.stringify({user:{ name: response?.data?.user?.name, city: response?.data?.user?.city, email: response?.data?.user?.email, mobileNo: response?.data?.user?.mobileNo,userId:response?.data?.user?.userId },token: response?.data?.token}));
            setSuccessMessage("User registered successfully!")
        } catch (error) {
            if (error.response) {
                let errorMessage = ""
                for (const [key, value] of Object.entries(error.response.data)) {
                    errorMessage =  value
                }
                setErrorMessage(errorMessage || "Signup failed. Please try again.");
            } else {
                setErrorMessage("An error occurred. Please try again later.");
            }
        }
    };



    return (

        <div className="my-16 xl:my-0 sm:my-16 flex items-center justify-center  ">
            <div className="bg-white shadow-lg rounded-lg px-8 py-5 w-full max-w-md">
                <div className='flex flex-row justify-between'>

                    <div title="" className="flex gap-2 ">
                        <p className='text-xl'>TrueAuction</p>
                        <RiAuctionFill size={25} />
                    </div>
                    <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
                        Sign Up
                    </h2>

                </div>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {/* Name Field */}
                    <div>
                        <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name</label>
                        <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm p-2" placeholder="Enter your name" required />
                    </div>

                    {/* Phone Number Field */}
                    <div>
                        <label htmlFor="mobileNo" className="block text-sm font-medium text-gray-700">Phone Number</label>
                        <input type="number" id="mobileNo" name="mobileNo" value={formData.mobileNo} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm" placeholder="Enter your phone number" required />
                    </div>

                    {/* Email Field */}
                    <div className='flex gap-2'>
                        <div>
                            <label htmlFor="email" className="block text-sm font-medium text-gray-700"  > Email</label>
                            <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm" placeholder="Enter your email" required />
                        </div>
                        <div>
                            <label htmlFor="city" className="block text-sm font-medium text-gray-700" > City</label>
                            <input type="text" id="city" name="city" value={formData.city} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm" placeholder="Enter your city" required />
                        </div>
                    </div>

                    {/* City Field */}


                    {/* Password Field */}
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                        <input type="password" id="password" name="password" value={formData.password} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm" placeholder="Enter your password" required />
                    </div>

                    {/* Submit Button */}
                    <div>
                        <button type="submit" className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2">
                            Sign Up
                        </button>
                        <p className="text-sm text-center text-gray-600 mt-5">
                            Already have an account then!{" "}
                            <button onClick={() => { setSignIn(true) }} className="text-blue-500 hover:underline">
                                Sign In
                            </button>
                        </p>
                    </div>
                </form>
            </div>


        </div>

    )
}

export default Signup