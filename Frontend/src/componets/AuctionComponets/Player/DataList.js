import React from "react";

const DataList = () => {
    const players = [
        { id: 1, name: "Virat Kohli", role: "Batsman", },
        { id: 2, name: "MS Dhoni", role: "Wicketkeeper" },
        { id: 3, name: "Jasprit Bumrah", role: "Bowler" },
    ];

    return (
        <div className="max-w-lg mx-auto p-4">
            <ul className="space-y-4">
                {players.map((player) => (
                    <>
                        <li
                            key={player.id}
                            className="p-4 border border-gray-200 rounded-lg shadow hover:shadow-lg transition"
                        >
                            <div className="text-lg font-semibold">{player.name}</div>
                            <div className="text-sm text-gray-600">{player.role}</div>
                            <div className="mt-4 flex space-x-12 lg:mt-6">
                                <button 
                                    className="inline-flex border-2 border-blue-700 bg-blue-700  items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white"
                                >
                                    Edit
                                </button>
                                <button value={player.playerId}
                                    className="inline-flex border-2 border-red-500 bg-red-500 hover:bg-red-600  items-center shadow-xl rounded-lg px-4 py-2 text-center text-sm font-medium text-white"
                                >
                                    Delete
                                </button>
                            </div>
                        </li>
                    </>
                ))}
            </ul>
        </div>
    );
};

export default DataList;
