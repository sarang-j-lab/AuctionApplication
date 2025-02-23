import { useState } from 'react'
import Signup from '../componets/Authentication/Signup.js'
import Signin from '../componets/Authentication/SignIn.js'
import { Navigate } from 'react-router-dom'
import Navbar from '../componets/Component/Navbar.js'
import Footer from '../componets/Component/Footer.js'

const Authentication = () => {

  const [signIn, setSignIn] = useState(true)

  const user = JSON.parse(localStorage.getItem("user"));
  return (
    <>
    <Navbar/>
      {!user ? signIn ? <Signin setSignIn={setSignIn} /> : <Signup setSignIn={setSignIn} /> : <Navigate to={"/"} />}
    <Footer />
    </>
  )
}

export default Authentication