
const BlueButton = ({ children }) => {
  return (
    <div className="mt-6 flex justify-end">
      <button
        type="submit"
        className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-700 transition duration-300"
      >
        {children}
      </button>
    </div>
  )
}
const RedButton = ({ children }) => {
  return (
    <div className="mt-6 flex justify-end">
      <button
        type="submit"
        className="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-700 transition duration-300"
      >
        {children}
      </button>
    </div>
  )
}
const GreenButton = ({ children }) => {
  return (
    <div className="mt-6 flex justify-end">
      <button
        type="submit"
        className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-700 transition duration-300"
      >
        {children}
      </button>
    </div>
  )
}

const PurpuleBigButton = ({ children }) => {
  return (
    <button class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-lg transition-colors">
      Show details
    </button>
  )
}


const RouteToprevBtn = ({ onClick }) => {
  return (
    <div className="ite flex align-middle max-h-10 mt-6">
      <button type='button' onClick={() => { onClick() }} className='flex justify-center items-center px-3 py-2 border-2 rounded-lg hover:bg-gray-800 hover:text-white '>
        <svg className="w-5 mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
          <path fillRule="evenodd" d="M7.707 14.707a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 1.414L5.414 9H17a1 1 0 110 2H5.414l2.293 2.293a1 1 0 010 1.414z" clipRule="evenodd"></path>
        </svg>
        <p className="ml-2">Prev</p>
      </button>
    </div>
  )
}


export { BlueButton, RedButton, GreenButton, PurpuleBigButton ,RouteToprevBtn}