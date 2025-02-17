import React, { useRef } from 'react'

const Pricing = () => {

    const pricing = [
        { title: "Free Lite", description: "It's total free", price: 0, teams: 2, btnText: "Current plan" },
        { title: "Silver", description: "Plan 1", price: 3000, teams: 4, btnText: "Get the plan" },
        { title: "Golden", description: "Plan 2", price: 4000, teams: 7, btnText: "Get the plan" },
        { title: "Platinum", description: "Plan 3", price: 5000, teams: 12, btnText: "Get the plan" }
    ]

    const bottomRef = useRef();

    const scrollToBottom = () => {
        // window.scrollTo({ top: document.body.scrollHeight, behavior: "smooth" });
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
      };
      

      

    return (
        <div className='w-3/4 flex flex-col'>
            <div className='xl:w-full lg:w-full grid xl:grid-cols-3 lg:grid-cols-2 md:grid-cols-2  gap-5 sm:grid-cols-1 sm:w-screen'>
                {
                    pricing.map((item) => (
                        <div className=' bg-white shadow-xl rounded-xl w-60 h-60'>
                            <div className='h-1/3  rounded-xl flex flex-col'>
                                <p className='mx-6 mt-10 text-gray-700 text-lg'>{item.title}</p>
                                <p className='ml-6 text-sm text-gray-500'>{item.description}</p>
                            </div>
                            <div className='flex flex-col justify-center items-center mt-2'>
                                <p className='text-3xl text-gray-800  font-bold'>{item.price} ₹</p>
                                <p className='mt-2'>Total teams - upto {item.teams}</p>
                                <button onClick={()=> scrollToBottom()} className='w-3/4 mt-4 flex justify-center mb-5 items-center space-x-5  xl:w-3/4 self-center lg:w-full  sm:w-full px-6 py-2  border-2 rounded-lg border-purple-600 bg-purple-600 hover:bg-purple-800 text-white'>{item.btnText}</button>
                            </div>
                        </div>
                    ))
                }

            </div>
            <div ref={bottomRef} className='w-full  m-5 rounded-xl py-4 shadow-2xl flex flex-col justify-center items-center gap-3'>
                <p className='text-2xl font-bold'>Contact US</p>
                <p className='sm:text-sm m-3'>Call or Whatsapp on any of these numbers, and we will guide you through the process to put your auction online.</p>
                <div className='flex xl:flex-row lg:flex-row md:flex-row  flex-col sm:flex-col  gap-2 '>
                    <p className='bg-green-500 px-4 py-2 text-xl text-white'>+91-9272222200</p>
                    <p className='bg-green-500 px-4 py-2 text-xl text-white'>+91-9356684802</p>
                </div>

            </div>
        </div>
    )
}

export default Pricing