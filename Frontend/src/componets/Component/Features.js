import React from 'react'
import { MdDescription } from 'react-icons/md'

const Features = () => {
    const step2Des = <p> Here fill details of you tournment or auction carefully because some of the fields are Immutable! After auction creation go to <span className='font-bold'>Self Auction</span> page and select auction.</p>
    const step4Des = <p>Here you can manage you auction create teams, create categories, add players, add increments, edit and delete auction! <span className='font-bold'>Conduct live auction where teams can bid on category player ,none category players.</span></p>
    const step6Des = <p>Create categories for different types of players! <span className='font-bold'>Add increments for category over increaseBy price!</span></p>
    const step7Des = <p>Add player in auction there two option to add players <span className='font-bold'>1.Add player manually by player form 2.Add bulk player by excel file!. </span></p>


    const steps = [
        { step: 1, description: "Click to new auction button, it will navigate you to create auction form!", image: "/step1.png" },
        { step: 2, description: step2Des, image: "/step2.png" },
        { step: 3, description: "Select the auction which you want to manage and conduct live auction!", image: "/step3.png" },
        { step: 4, description: step4Des, image: "/step4.png" },
        { step: 5, description: "Add teams in auction!", image: "/step5.png" },
        { step: 6, description: step6Des, image: "/step6.png" },
        { step: 7, description: step7Des, image: "/step7.png" },
        { step: 8, description: "Auction panel dashboard page where you can start bid to none category players see sold, unsold player, see teams points status, change category, start bidding , sold player, unsold player!", image: "/step8.png" },
        { step: 9, description: "Bidding started! after bidding start you can see player, bid appiled team, team points, team max bid, team reserve points for min player requirment, current bid.!", image: "/step9.png" }]

    return (
        <>

            <div className='w-3/4 h-full  mx-4'>
                {steps.map((step) => (
                    step.step % 2 == 0 ? <div className='flex flex-col xl:flex-row lg:flex-row md:flex-row sm:flex-col  w-full h-[40vh]  gap-5  border-2  '>
                        <div className='self-center'>
                            <h1 className='text-xl font-bold'>Step {step.step}:</h1>
                            <p>{step.description}</p>
                        </div>
                        <img src={step.image} className='xl:w-1/2 w-full' />
                    </div>
                        : <div className='flex flex-col xl:flex-row lg:flex-row md:flex-row sm:flex-col w-full h-[40vh]  gap-5 border-2  bg-gray-400 text-white'>
                            <img src={step.image} className='xl:w-1/2 w-full' />
                            <div className='self-center'>
                                <h1 className='text-xl font-bold'>Step {step.step}:</h1>
                                <p>{step.description}</p></div>
                        </div>
                ))}
            </div>
        </>
    )
}

export default Features