import React from 'react'

const ShowTeams = ({ teams, auctionData }) => {
    return (
        <>
            {teams.length !== 0 ? <div className='w-[100vw] h-[90vh] bg-white/20 backdrop-blur-2xl overflow-scroll scrollbar-hide rounded-xl'>
                <div className="w-full  h-[30vh] px-20 text-white flex flex-col items-center justify-center">
                    <h1 className='text-[10vh] font-serif'>{auctionData.auctionName.toUpperCase()}</h1>
                    <h1 className='text-[7vh] font-serif text-black bg-yellow-200 px-3 rounded-sm'>Teams</h1>
                </div>

                <div className='w-full grid grid-cols-3  p-10  gap-5 '>
                    {teams.map((team) => (
                        <div key={team.teamId} className='h-[20vh] bg-white rounded-xl text-black flex flex-col items-center hover:shadow-2xl hover:h-[22vh] cursor-pointer transition-all duration-300'>
                            <div className='text-xl font-serif h-1/4 bg-green-400 w-full pl-2'>{team.teamName.toUpperCase()}</div>
                            <div className='grid grid-cols-2 h-full w-full justify-center p-2 items-end font-serif text-xl'>
                                <p><span className='text-lg'>Players.</span> 1</p>
                                <p><span className='text-lg'>Points.</span> {team.totalPoints}</p>
                                <p><span className='text-lg'>Max</span> {team.maxBid}</p>
                                <p><span className='text-lg'>Res.</span> {team.reserve}</p>
                            </div>
                        </div>
                    ))

                    }

                </div>
            </div> : <h1 className='text-black p-2 rounded-lg self-center bg-yellow-400 font-serif text-2xl m-10'>This auction does't have any team</h1>}
        </>
    )
}

export default ShowTeams