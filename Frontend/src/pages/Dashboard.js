
import { Link, Route, Routes, useNavigate } from 'react-router-dom'
import Menu from '../componets/Menu'
import AuctionForm from '../componets/AuctionComponets/AuctionForm'
import MyAuction from '../componets/AuctionComponets/MyAuction'
import AuctionDetail from '../componets/AuctionComponets/AuctionDetail'
import AuctionTeams from '../componets/AuctionComponets/Team/AuctionTeams'
import TeamForm from '../componets/AuctionComponets/Team/TeamForm'
import AuctionPlayers from '../componets/AuctionComponets/Player/AuctionPlayers'
import PlayerForm from '../componets/AuctionComponets/Player/PlayerForm'
import JoinAuction from '../componets/JoinAuction'
import AuctionCategories from '../componets/AuctionComponets/Category/AuctionCategories'
import CategoryForm from '../componets/AuctionComponets/Category/CategoryForm'
import { useEffect } from 'react'
import axiosApi from '../utils/axiosApi'
import UserProfilePage from '../componets/UserProfilePage'
import EditProfileForm from '../componets/EditProfileForm'
import Panel from '../componets/AuctionPanel/Panel'
import Main from '../componets/Main'
import Navbar from '../componets/Navbar'
import Footer from '../componets/Footer'





const Dashboard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const checkServerHealth = async () => {
      try {
        await axiosApi.get("health-check")
      } catch (error) {
        localStorage.removeItem("user");
        localStorage.removeItem("auction");
        navigate("/authentication")
      }
    }
    checkServerHealth();
  }, [])

  useEffect(() => {
    window.scrollTo(0, 0);
  }, []);

  return (

    <>
      
      <Routes>
        <Route path="/" element={<Main />}>
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
        <Route path="/auction-dashboard" element={<Panel />} />
      
      </Routes>

    </>
  )
}

export default Dashboard