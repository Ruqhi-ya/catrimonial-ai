import { Link, useLocation } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Home, Heart, PlusCircle, MessageCircle, User } from 'lucide-react';

const navItems = [
  { path: '/dashboard', icon: Home, label: 'Home' },
  { path: '/matches', icon: Heart, label: 'Matches' },
  { path: '/add-cat', icon: PlusCircle, label: 'Add Cat' },
  { path: '/messages', icon: MessageCircle, label: 'Messages' },
  { path: '/profile', icon: User, label: 'Profile' },
];

export function MobileNav() {
  const location = useLocation();

  return (
    <div className="fixed bottom-0 left-0 right-0 z-50 md:hidden">
      <div className="bg-white/90 backdrop-blur-lg border-t border-gray-100 px-2 py-2 safe-area-pb">
        <div className="flex justify-around items-center">
          {navItems.map((item) => {
            const isActive = location.pathname === item.path;
            const Icon = item.icon;
            const isAdd = item.path === '/add-cat';

            if (isAdd) {
              return (
                <Link key={item.path} to={item.path}>
                  <motion.div
                    whileHover={{ scale: 1.1 }}
                    whileTap={{ scale: 0.9 }}
                    className="relative -mt-6"
                  >
                    <div className="w-14 h-14 bg-primary rounded-full flex items-center justify-center shadow-glow">
                      <PlusCircle size={26} className="text-white" />
                    </div>
                    <motion.div
                      className="absolute inset-0 rounded-full bg-primary"
                      animate={{ scale: [1, 1.2, 1], opacity: [0.3, 0, 0.3] }}
                      transition={{ duration: 2, repeat: Infinity }}
                    />
                  </motion.div>
                </Link>
              );
            }

            return (
              <Link key={item.path} to={item.path}>
                <motion.div
                  whileTap={{ scale: 0.9 }}
                  className="flex flex-col items-center gap-0.5 px-3 py-1"
                >
                  <div className={`p-1.5 rounded-xl transition-colors ${isActive ? 'text-primary' : 'text-text-secondary'}`}>
                    <Icon size={22} />
                  </div>
                  <span className={`text-[10px] font-medium ${isActive ? 'text-primary' : 'text-text-secondary'}`}>
                    {item.label}
                  </span>
                  {isActive && (
                    <motion.div
                      layoutId="mobileActiveTab"
                      className="absolute -bottom-0.5 w-6 h-1 bg-primary rounded-full"
                    />
                  )}
                </motion.div>
              </Link>
            );
          })}
        </div>
      </div>
    </div>
  );
}
