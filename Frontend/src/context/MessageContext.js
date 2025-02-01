import React, { createContext, useState } from 'react'

export const messageContext = createContext();

const MessageProvider = ({children}) => {


    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

  return (
    <messageContext.Provider value={{successMessage, setSuccessMessage,errorMessage,setErrorMessage}}>{children}</messageContext.Provider>
  )
}

export default MessageProvider