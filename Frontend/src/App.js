import { useContext, useEffect } from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import Authentication from './pages/Authentication'
import { messageContext } from './context/MessageContext'
import { ErrorToast, SuccessToast } from "../src/componets/Component/Toast.js"

import ProtectedRoutes from './pages/ProtectedRoutes'
import UserProfilePage from './componets/UserComponent/UserProfilePage.js'
import EditProfileForm from './componets/UserComponent/EditProfileForm.js'
import AuctionForm from './componets/AuctionComponets/AuctionForm'
import MyAuction from './componets/AuctionComponets/MyAuction'
import AuctionDetail from './componets/AuctionComponets/AuctionDetail'
import AuctionTeams from './componets/AuctionComponets/Team/AuctionTeams'
import AuctionPlayers from './componets/AuctionComponets/Player/AuctionPlayers'
import TeamForm from './componets/AuctionComponets/Team/TeamForm'
import PlayerForm from './componets/AuctionComponets/Player/PlayerForm'
import AuctionCategories from './componets/AuctionComponets/Category/AuctionCategories'
import CategoryForm from './componets/AuctionComponets/Category/CategoryForm'
import Main from './componets/Component/Main.js'
import JoinAuction from './componets/AuctionComponets/JoinAuction.js'
import Menu from './componets/Component/Menu.js'
import AuctionPanel from './pages/AuctionPanel'
import NotFound from './componets/Component/NotFound.js'
import LiveAuction from './componets/AuctionPanel/LiveAuctionComponents/LiveAuction'
import AdminPage from './pages/AdminPage'
import Pricing from './componets/Component/Pricing.js'
import BulkPlayerAdd from './componets/AuctionComponets/Player/BulkPlayerAdd'

import AuctionPanelMenu from './pages/AuctionPanelMenu.js'
import HowToUse from './componets/Component/HowToUser.js'








const App = () => {

  const { successMessage, setSuccessMessage, errorMessage, setErrorMessage } = useContext(messageContext);

  const user = JSON.parse(localStorage.getItem("user"))

  useEffect(() => {
    setTimeout(() => {
      setSuccessMessage("");
      setErrorMessage("")
    }, 6000);

  }, [successMessage, errorMessage])

  return (
    <Router>
      {successMessage && (
        <SuccessToast
          message={successMessage}
          onClose={() => setSuccessMessage("")}
        />
      )}
      {errorMessage && (
        <ErrorToast
          message={errorMessage}
          onClose={() => setErrorMessage("")}
        />
      )}

      <Routes>
        <Route path='/authentication' element={<Authentication />} />

        <Route element={<ProtectedRoutes />} >
          <Route element={<Main />}>
            <Route path='/' element={<Menu />} />
            <Route path='/auction/user-profile' element={<UserProfilePage />} />
            <Route path='/auction/edit-profile' element={<EditProfileForm />} />
            <Route path='/auction/auction-form' element={<AuctionForm />} />
            <Route path='/auction/my-auction' element={<MyAuction />} />
            <Route path='/auction/auction-details' element={<AuctionDetail />} />
            <Route path='/auction/auction-teams' element={<AuctionTeams />} />
            <Route path='/auction/auction-players' element={<AuctionPlayers />} />
            <Route path='/auction/bulk-player-add' element={<BulkPlayerAdd />} />
            <Route path='/auction/team-form' element={<TeamForm />} />
            <Route path='/auction/player-form' element={<PlayerForm />} />
            <Route path='/auction/join-auction' element={<JoinAuction />} />
            <Route path='/auction/auction-categories' element={<AuctionCategories />} />
            <Route path='/auction/category-form' element={<CategoryForm />} />
            <Route path='/pricing' element={<Pricing />} />
            <Route path='/how-to-use' element={<HowToUse />} />
          </Route>
          <Route path='/Auction-menu' element={<AuctionPanelMenu />} />
          <Route path="/auction-dashboard/*" element={<AuctionPanel />} />
        </Route>

        <Route path="/live/auction/*" element={<LiveAuction />} />

        <Route path='/admin/*' element={<AdminPage />} />
        <Route path='*' element={<NotFound />} />

      </Routes>

    </Router>

  )
}

export default App