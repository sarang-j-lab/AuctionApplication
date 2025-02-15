import axios from "axios";
import React, { useContext,  useState } from "react";
import { RiAuctionFill } from "react-icons/ri";
import { messageContext } from "../../context/MessageContext";


const Signin = ({ setSignIn }) => {

    const [mobileNo, setMobileNo] = useState("");
    const [password, setPassword] = useState("");

    const { setSuccessMessage, setErrorMessage } = useContext(messageContext);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/user/login",
                { mobileNo, password },
                {
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            )
            // navigate("/auction/auction-menu")
            localStorage.setItem("user",JSON.stringify({user:response?.data?.user,token: response?.data?.token}));
            setSuccessMessage("User logged in successfully!")
        } catch (error) {
            if (error?.response) {
                let errorMessage = ""
                for (const [key, value] of Object.entries(error?.response?.data)) {
                    errorMessage =  value
                }
                setErrorMessage(errorMessage || "Wrong credencials! please provide valid details.")
            } else {
                setErrorMessage("An error occured! please try again");
            }
        }
    };





    return (
        <div className="flex items-center justify-center my-16 xl:my-10 sm:my-16 ">
            <div className="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md">
                <div className='flex flex-row justify-between'>
                    <div className="flex gap-2 ">
                        <p className='text-xl'>TrueAuction</p>
                        <RiAuctionFill size={25} />
                    </div>
                    <h2 className="text-2xl font-bold mb-6 text-gray-800 text-center">
                        Login
                    </h2>
                </div>
                <form onSubmit={handleSubmit} className="space-y-4">
                    {/* Mobile Number Field */}
                    <div>
                        <label
                            htmlFor="mobileNo"
                            className="block text-sm font-medium text-gray-700"
                        >
                            Mobile Number
                        </label>
                        <input
                            type="text"
                            id="mobileNo"
                            value={mobileNo}
                            onChange={(e) => setMobileNo(e.target.value)}
                            placeholder="Enter your mobile number"
                            className="w-full px-4 py-2 mt-1 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                            required
                        />
                    </div>

                    {/* Password Field */}
                    <div>
                        <label
                            htmlFor="password"
                            className="block text-sm font-medium text-gray-700"
                        >
                            Password
                        </label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            placeholder="Enter your password"
                            className="w-full px-4 py-2 mt-1 border rounded-md focus:ring-2 focus:ring-blue-500 focus:outline-none"
                            required
                        />
                    </div>

                    {/* Submit Button */}
                    <button type="submit" className="w-full px-4 py-2 font-bold text-white bg-blue-500 rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        Login
                    </button>
                </form>
                <div className="text-sm text-center text-gray-600">
                    Don’t have an account?{" "}
                    <button onClick={() => { setSignIn(false) }} className="text-blue-500 hover:underline">
                        Sign up
                    </button>
                </div>
            </div>
        </div>
    );
};

export default Signin;
