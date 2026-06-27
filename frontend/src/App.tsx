import { Routes, Route, Navigate } from 'react-router-dom';
import { AnimatePresence } from 'framer-motion';
import { Toaster } from 'react-hot-toast';
import { useAuthStore } from '@/stores/authStore';
import { Navbar } from '@/components/layout/Navbar';
import { MobileNav } from '@/components/layout/MobileNav';

// Pages
import { LandingPage } from '@/pages/LandingPage';
import { LoginPage } from '@/pages/auth/LoginPage';
import { RegisterPage } from '@/pages/auth/RegisterPage';
import { DashboardPage } from '@/pages/dashboard/DashboardPage';
import { MyCatsPage } from '@/pages/cats/MyCatsPage';
import { AddCatPage } from '@/pages/cats/AddCatPage';
import { CatProfilePage } from '@/pages/cats/CatProfilePage';
import { MatchesPage } from '@/pages/matches/MatchesPage';
import { MessagesPage } from '@/pages/messaging/MessagesPage';
import { HealthDashboard } from '@/pages/health/HealthDashboard';
import { CommunityPage } from '@/pages/community/CommunityPage';
import { LostFoundPage } from '@/pages/lostfound/LostFoundPage';
import { AdoptionPage } from '@/pages/adoption/AdoptionPage';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuthStore();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  return <>{children}</>;
}

function App() {
  const { isAuthenticated } = useAuthStore();

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <AnimatePresence mode="wait">
        <Routes>
          {/* Public routes */}
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Protected routes */}
          <Route path="/dashboard" element={<ProtectedRoute><DashboardPage /></ProtectedRoute>} />
          <Route path="/my-cats" element={<ProtectedRoute><MyCatsPage /></ProtectedRoute>} />
          <Route path="/add-cat" element={<ProtectedRoute><AddCatPage /></ProtectedRoute>} />
          <Route path="/cats/:catId" element={<ProtectedRoute><CatProfilePage /></ProtectedRoute>} />
          <Route path="/matches" element={<ProtectedRoute><MatchesPage /></ProtectedRoute>} />
          <Route path="/messages" element={<ProtectedRoute><MessagesPage /></ProtectedRoute>} />
          <Route path="/health" element={<ProtectedRoute><HealthDashboard /></ProtectedRoute>} />
          <Route path="/community" element={<ProtectedRoute><CommunityPage /></ProtectedRoute>} />
          <Route path="/lost-found" element={<ProtectedRoute><LostFoundPage /></ProtectedRoute>} />
          <Route path="/adoption" element={<ProtectedRoute><AdoptionPage /></ProtectedRoute>} />

          {/* Catch all */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AnimatePresence>

      {isAuthenticated && <MobileNav />}
      <Toaster
        position="top-center"
        toastOptions={{
          duration: 3000,
          style: {
            borderRadius: '16px',
            background: '#fff',
            boxShadow: '0 4px 20px rgba(0,0,0,0.08)',
            padding: '12px 20px',
            fontFamily: 'Inter, sans-serif',
          },
        }}
      />
    </div>
  );
}

export default App;
