import React, { useContext, useState } from 'react'
import { FaDownload } from "react-icons/fa6";
import { messageContext } from '../../../context/MessageContext';
import axiosApi from '../../../utils/axiosApi';
import { RouteToprevBtn } from '../../Component/Button';
import { useNavigate } from 'react-router-dom';

const BulkPlayerAdd = () => {
    const fileURL = "/sampleplayerlist.xlsx"
    const [file, setFile] = useState(null);
    const { setErrorMessage, setSuccessMessage } = useContext(messageContext);
    const auction = JSON.parse(localStorage.getItem("auction"))
    const navigate = useNavigate();

    const handleFileChange = (event) => {
        const selectedFile = event?.target?.files[0];

        // Check if file is selected
        if (!selectedFile) {
            setErrorMessage("Please select a file!");
            return;
        }
        // Allowed Excel MIME types
        const allowedTypes = [
            "application/vnd.ms-excel", // .xls
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
        ];

        // Check file type
        if (!allowedTypes.includes(selectedFile.type)) {
            setErrorMessage("Invalid file type! Please upload an Excel file (.xls or .xlsx).");
            setFile(null)
            return;
        }

        // Check file extension (additional security)
        const allowedExtensions = [".xls", ".xlsx"];
        const fileExtension = selectedFile.name.slice(
            ((selectedFile.name.lastIndexOf(".") - 1) >>> 0) + 2
        );

        if (!allowedExtensions.includes("." + fileExtension)) {
            setErrorMessage("Invalid file extension! Only .xls or .xlsx allowed.");
            setFile(null)
            return;
        }

        setFile(selectedFile);
    }
    console.log(file)

    const handleFileSubmit = async (event) => {
        event.preventDefault();

        if (!file) {
            setErrorMessage("Select file first!")
            return;
        }
        const formData = new FormData();
        formData.append("file", file);

        try {
            const response = await axiosApi.post(`/player-file-upload/${auction?.auctionId}`, formData)
            setSuccessMessage("Player added successfully");
        } catch (error) {
            setErrorMessage(error?.response?.data?.message || "Failed to upload file please try again!.")
        }
    }


    return (
        <div className='flex w-3/4 flex-col items-center'>
            <h1 className='xl:text-3xl sm:text-2xl text-blue-400'>IMPORT PLAYER FROM EXCEL</h1>
            <div className='flex flex-col xl:flex-row sm:flex-col w-screen xl:w-full sm:w-screen shadow-lg h-[50vh] rounded-xl justify-center items-center gap-10 '>
                <div className='flex flex-col   w-2/3 h-2/3 shadow-lg rounded-xl ml-4'>
                    <h1 className='px-4 py-4 text-gray-600'>Your Excel file must follow a predefined format!!</h1>
                    <h1 className='px-4 text-gray-600'> Need help? Check out this sample Excel to understand the required structure.</h1>
                    <a href={fileURL} download='Sample.xlsx' className='ml-3'>
                        <button className='flex gap-2  mt-5 bg-purple-500 px-4 py-2 rounded-lg text-white hover:bg-purple-600 self-center'>Sample Excel <FaDownload className='mt-1' /></button>
                    </a>
                </div>
                <div className='flex flex-col w-2/3 h-2/3 shadow-lg rounded-xl mr-4'>
                    <form onSubmit={handleFileSubmit} className='p-2 pl-10 m-2'>
                        <input type='file' accept=".xls,.xlsx" className='w-auto border-2 rounded-lg p-2' onChange={handleFileChange} />
                        <div className='flex gap-2'>
                            <button className='flex gap-2  mt-5 bg-purple-500 px-4 py-2 rounded-lg text-white hover:bg-purple-600 self-center'>Sumbit</button>
                      
                        </div>
                    </form>
                </div>
            </div>
                <RouteToprevBtn onClick={()=>navigate('/auction/auction-players')}/>
        </div>
    )
}

export default BulkPlayerAdd