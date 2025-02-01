import React, { useState } from 'react'
import Signup from '../componets/Authentication/Signup.js'
import Signin from '../componets/Authentication/SignIn.js'
import { Navigate } from 'react-router-dom'

const Authentication = () => {

  const [signIn, setSignIn] = useState(true)

  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
      {!user ? signIn ? <Signin setSignIn={setSignIn} /> : <Signup setSignIn={setSignIn} /> : <Navigate to={"/"} />}
    </>
  )
}

export default Authentication