import { useContext, useEffect } from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Authentication from './pages/Authentication'
import { messageContext } from './context/MessageContext'
import { ErrorToast, SuccessToast } from './componets/Toast'

import ProtectedRoutes from './pages/ProtectedRoutes'
import UserProfilePage from './componets/UserProfilePage'
import EditProfileForm from './componets/EditProfileForm'
import AuctionForm from './componets/AuctionComponets/AuctionForm'
import MyAuction from './componets/AuctionComponets/MyAuction'
import AuctionDetail from './componets/AuctionComponets/AuctionDetail'
import AuctionTeams from './componets/AuctionComponets/Team/AuctionTeams'
import AuctionPlayers from './componets/AuctionComponets/Player/AuctionPlayers'
import TeamForm from './componets/AuctionComponets/Team/TeamForm'
import PlayerForm from './componets/AuctionComponets/Player/PlayerForm'
import AuctionCategories from './componets/AuctionComponets/Category/AuctionCategories'
import CategoryForm from './componets/AuctionComponets/Category/CategoryForm'
import Main from './componets/Main'
import JoinAuction from './componets/JoinAuction'
import Panel from './componets/AuctionPanel/CategoryAuctionPanel'
import Menu from './componets/Menu'
import AuctionPanel from './pages/AuctionPanel'
import NotFound from './componets/NotFound'
import LiveAuction from './componets/AuctionPanel/LiveAuction'








const App = () => {

  const { successMessage, setSuccessMessage, errorMessage, setErrorMessage } = useContext(messageContext);


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
            <Route path='/auction/team-form' element={<TeamForm />} />
            <Route path='/auction/player-form' element={<PlayerForm />} />
            <Route path='/auction/join-auction' element={<JoinAuction />} />
            <Route path='/auction/auction-categories' element={<AuctionCategories />} />
            <Route path='/auction/category-form' element={<CategoryForm />} />
          </Route>
          <Route path="/auction-dashboard/*" element={<AuctionPanel />} />
          <Route path='*' element={<NotFound />} />
        </Route>
          <Route path="/live/auction/:auctionId" element={<LiveAuction />} />
      </Routes>

    </Router>

  )
}

export default App