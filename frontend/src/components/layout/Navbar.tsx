import { Link, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Bell, MessageCircle, Search, Menu, X } from 'lucide-react';
import { useState } from 'react';
import { useAuthStore } from '@/stores/authStore';
import { Button } from '@/components/ui/button';

export function Navbar() {
  const { isAuthenticated, user, logout } = useAuthStore();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const location = useLocation();

  return (
    <motion.nav
      className="sticky top-0 z-50 bg-white/80 backdrop-blur-lg border-b border-gray-100"
      initial={{ y: -100 }}
      animate={{ y: 0 }}
      transition={{ duration: 0.3 }}
    >
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to="/" className="flex items-center gap-2 group">
            <motion.div
              whileHover={{ rotate: [0, -10, 10, 0] }}
              transition={{ duration: 0.5 }}
            >
              <svg width="32" height="32" viewBox="0 0 64 64">
                <path d="M32 8c-2 0-4 1-6 3l-4-6c-1-1.5-3-1-3 1v8c-4 3-7 8-7 14 0 10 9 18 20 18s20-8 20-18c0-6-3-11-7-14v-8c0-2-2-2.5-3-1l-4 6c-2-2-4-3-6-3z" fill="#FF6B9A" />
                <circle cx="25" cy="26" r="3" fill="white" />
                <circle cx="39" cy="26" r="3" fill="white" />
                <circle cx="25" cy="27" r="1.5" fill="#1E293B" />
                <circle cx="39" cy="27" r="1.5" fill="#1E293B" />
                <path d="M28 35c2 2 6 2 8 0" stroke="#1E293B" fill="none" strokeWidth="1" strokeLinecap="round" />
              </svg>
            </motion.div>
            <span className="text-xl font-bold text-text-primary">
              Catri<span className="text-primary">monial</span>
            </span>
          </Link>

          {/* Desktop Navigation */}
          {isAuthenticated && (
            <div className="hidden md:flex items-center gap-1">
              <NavLink to="/dashboard" current={location.pathname}>Dashboard</NavLink>
              <NavLink to="/my-cats" current={location.pathname}>My Cats</NavLink>
              <NavLink to="/matches" current={location.pathname}>Matches</NavLink>
              <NavLink to="/community" current={location.pathname}>Community</NavLink>
              <NavLink to="/adoption" current={location.pathname}>Adopt</NavLink>
            </div>
          )}

          {/* Right actions */}
          <div className="flex items-center gap-2">
            {isAuthenticated ? (
              <>
                <motion.button
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className="hidden md:flex p-2.5 rounded-xl hover:bg-light-gray transition-colors relative"
                >
                  <Search size={20} className="text-text-secondary" />
                </motion.button>
                <Link to="/messages">
                  <motion.button
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    className="hidden md:flex p-2.5 rounded-xl hover:bg-light-gray transition-colors relative"
                  >
                    <MessageCircle size={20} className="text-text-secondary" />
                  </motion.button>
                </Link>
                <Link to="/notifications">
                  <motion.button
                    whileHover={{ scale: 1.05 }}
                    whileTap={{ scale: 0.95 }}
                    className="hidden md:flex p-2.5 rounded-xl hover:bg-light-gray transition-colors relative"
                  >
                    <Bell size={20} className="text-text-secondary" />
                    <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-primary rounded-full" />
                  </motion.button>
                </Link>
                <Link to="/profile">
                  <motion.div
                    whileHover={{ scale: 1.05 }}
                    className="w-9 h-9 rounded-full bg-primary-100 flex items-center justify-center overflow-hidden ml-2"
                  >
                    {user?.profileImage ? (
                      <img src={user.profileImage} alt="" className="w-full h-full object-cover" />
                    ) : (
                      <span className="text-sm font-bold text-primary">
                        {user?.name?.charAt(0).toUpperCase()}
                      </span>
                    )}
                  </motion.div>
                </Link>
              </>
            ) : (
              <div className="hidden md:flex items-center gap-3">
                <Link to="/login">
                  <Button variant="ghost" size="sm">Login</Button>
                </Link>
                <Link to="/register">
                  <Button size="sm">Get Started</Button>
                </Link>
              </div>
            )}

            {/* Mobile menu button */}
            <button
              className="md:hidden p-2 rounded-xl hover:bg-light-gray"
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            >
              {mobileMenuOpen ? <X size={24} /> : <Menu size={24} />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <motion.div
          initial={{ opacity: 0, y: -10 }}
          animate={{ opacity: 1, y: 0 }}
          exit={{ opacity: 0, y: -10 }}
          className="md:hidden bg-white border-t border-gray-100 px-4 py-4 space-y-2"
        >
          {isAuthenticated ? (
            <>
              <MobileNavLink to="/dashboard" onClick={() => setMobileMenuOpen(false)}>Dashboard</MobileNavLink>
              <MobileNavLink to="/my-cats" onClick={() => setMobileMenuOpen(false)}>My Cats</MobileNavLink>
              <MobileNavLink to="/matches" onClick={() => setMobileMenuOpen(false)}>Matches</MobileNavLink>
              <MobileNavLink to="/messages" onClick={() => setMobileMenuOpen(false)}>Messages</MobileNavLink>
              <MobileNavLink to="/community" onClick={() => setMobileMenuOpen(false)}>Community</MobileNavLink>
              <MobileNavLink to="/health" onClick={() => setMobileMenuOpen(false)}>Health</MobileNavLink>
              <MobileNavLink to="/adoption" onClick={() => setMobileMenuOpen(false)}>Adoption</MobileNavLink>
              <button
                onClick={() => { logout(); setMobileMenuOpen(false); }}
                className="w-full text-left px-4 py-3 text-error font-medium rounded-xl hover:bg-red-50"
              >
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" onClick={() => setMobileMenuOpen(false)}>
                <Button variant="ghost" className="w-full justify-start">Login</Button>
              </Link>
              <Link to="/register" onClick={() => setMobileMenuOpen(false)}>
                <Button className="w-full">Get Started</Button>
              </Link>
            </>
          )}
        </motion.div>
      )}
    </motion.nav>
  );
}

function NavLink({ to, children, current }: { to: string; children: React.ReactNode; current: string }) {
  const isActive = current === to || current.startsWith(to + '/');
  return (
    <Link
      to={to}
      className={`relative px-3 py-2 text-sm font-medium rounded-xl transition-colors ${
        isActive ? 'text-primary bg-primary-50' : 'text-text-secondary hover:text-text-primary hover:bg-light-gray'
      }`}
    >
      {children}
      {isActive && (
        <motion.div
          layoutId="activeTab"
          className="absolute bottom-0 left-1/2 -translate-x-1/2 w-1 h-1 rounded-full bg-primary"
        />
      )}
    </Link>
  );
}

function MobileNavLink({ to, children, onClick }: { to: string; children: React.ReactNode; onClick: () => void }) {
  return (
    <Link
      to={to}
      onClick={onClick}
      className="block px-4 py-3 text-text-primary font-medium rounded-xl hover:bg-light-gray transition-colors"
    >
      {children}
    </Link>
  );
}
