import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { Heart, PawPrint, Calendar, MessageCircle, Bell, TrendingUp, Plus, Search, Stethoscope } from 'lucide-react';
import { useAuthStore } from '@/stores/authStore';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { PageTransition } from '@/components/layout/PageTransition';
import { FloatingPaws } from '@/components/animations/FloatingPaws';
import { SleepingCat } from '@/components/animations/SleepingCat';

export function DashboardPage() {
  const { user } = useAuthStore();

  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8 relative">
        <FloatingPaws count={3} className="opacity-30" />

        {/* Welcome Header */}
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl sm:text-3xl font-bold text-text-primary">
                Hey, {user?.name?.split(' ')[0]}! 🐱
              </h1>
              <p className="text-text-secondary mt-1">Here's what's happening with your cats today</p>
            </div>
            <Link to="/add-cat">
              <Button size="sm" className="hidden sm:flex">
                <Plus size={18} className="mr-2" />
                Add Cat
              </Button>
            </Link>
          </div>
        </motion.div>

        {/* Quick Stats */}
        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {quickStats.map((stat, index) => (
            <motion.div
              key={stat.label}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <Card className="hover:shadow-card-hover transition-shadow">
                <CardContent className="p-4 flex items-center gap-3">
                  <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${stat.bgColor}`}>
                    <stat.icon size={20} className={stat.iconColor} />
                  </div>
                  <div>
                    <p className="text-2xl font-bold text-text-primary">{stat.value}</p>
                    <p className="text-xs text-text-secondary">{stat.label}</p>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          ))}
        </div>

        {/* Main Content Grid */}
        <div className="grid lg:grid-cols-3 gap-6">
          {/* Left Column - Quick Actions */}
          <div className="lg:col-span-2 space-y-6">
            {/* Quick Actions */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
            >
              <Card>
                <CardHeader>
                  <CardTitle className="text-lg">Quick Actions</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="grid grid-cols-2 sm:grid-cols-3 gap-3">
                    {quickActions.map((action) => (
                      <Link key={action.path} to={action.path}>
                        <motion.div
                          whileHover={{ scale: 1.02, y: -2 }}
                          whileTap={{ scale: 0.98 }}
                          className={`p-4 rounded-2xl text-center transition-all cursor-pointer ${action.bgColor} hover:shadow-soft`}
                        >
                          <action.icon size={24} className={`mx-auto mb-2 ${action.iconColor}`} />
                          <span className="text-xs font-medium text-text-primary">{action.label}</span>
                        </motion.div>
                      </Link>
                    ))}
                  </div>
                </CardContent>
              </Card>
            </motion.div>

            {/* AI Matches Preview */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.4 }}
            >
              <Card>
                <CardHeader className="flex flex-row items-center justify-between">
                  <CardTitle className="text-lg">AI Recommendations</CardTitle>
                  <Link to="/matches" className="text-sm text-primary font-medium hover:underline">
                    View All
                  </Link>
                </CardHeader>
                <CardContent>
                  <div className="flex flex-col items-center py-8">
                    <SleepingCat size={120} className="mb-4" />
                    <p className="text-text-secondary text-center">
                      Add your first cat to get AI-powered match recommendations!
                    </p>
                    <Link to="/add-cat">
                      <Button variant="secondary" size="sm" className="mt-4">
                        <Plus size={16} className="mr-2" />
                        Add Your Cat
                      </Button>
                    </Link>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </div>

          {/* Right Column */}
          <div className="space-y-6">
            {/* Profile Completion */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.5 }}
            >
              <Card className="bg-gradient-to-br from-primary-50 to-white">
                <CardContent className="p-6">
                  <div className="flex items-center justify-between mb-3">
                    <span className="text-sm font-medium text-text-primary">Profile Completion</span>
                    <span className="text-sm font-bold text-primary">65%</span>
                  </div>
                  <div className="w-full h-2 bg-primary-100 rounded-full overflow-hidden">
                    <motion.div
                      className="h-full bg-primary rounded-full"
                      initial={{ width: 0 }}
                      animate={{ width: '65%' }}
                      transition={{ duration: 1, delay: 0.5 }}
                    />
                  </div>
                  <p className="text-xs text-text-secondary mt-3">
                    Complete your profile to get better recommendations
                  </p>
                </CardContent>
              </Card>
            </motion.div>

            {/* Upcoming */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.6 }}
            >
              <Card>
                <CardHeader>
                  <CardTitle className="text-lg flex items-center gap-2">
                    <Calendar size={18} className="text-primary" />
                    Upcoming
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="text-center py-4">
                    <p className="text-sm text-text-secondary">No upcoming appointments</p>
                    <Link to="/health">
                      <Button variant="ghost" size="sm" className="mt-2 text-primary">
                        Schedule One
                      </Button>
                    </Link>
                  </div>
                </CardContent>
              </Card>
            </motion.div>

            {/* Vaccination Alerts */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.7 }}
            >
              <Card>
                <CardHeader>
                  <CardTitle className="text-lg flex items-center gap-2">
                    <Stethoscope size={18} className="text-success" />
                    Health Status
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="flex items-center gap-3 p-3 bg-success/5 rounded-xl">
                    <div className="w-8 h-8 bg-success/10 rounded-lg flex items-center justify-center">
                      <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M2 8l4 4L14 4" stroke="#10B981" strokeWidth="2.5" strokeLinecap="round" />
                      </svg>
                    </div>
                    <div>
                      <p className="text-sm font-medium text-text-primary">All caught up!</p>
                      <p className="text-xs text-text-secondary">No overdue vaccinations</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          </div>
        </div>
      </div>
    </PageTransition>
  );
}

const quickStats = [
  { label: 'My Cats', value: '0', icon: PawPrint, bgColor: 'bg-primary-50', iconColor: 'text-primary' },
  { label: 'Matches', value: '0', icon: Heart, bgColor: 'bg-error/10', iconColor: 'text-error' },
  { label: 'Messages', value: '0', icon: MessageCircle, bgColor: 'bg-secondary-100', iconColor: 'text-secondary-600' },
  { label: 'Notifications', value: '0', icon: Bell, bgColor: 'bg-lavender-100', iconColor: 'text-lavender-600' },
];

const quickActions = [
  { label: 'Add Cat', path: '/add-cat', icon: Plus, bgColor: 'bg-primary-50', iconColor: 'text-primary' },
  { label: 'Find Match', path: '/matches', icon: Heart, bgColor: 'bg-error/5', iconColor: 'text-error' },
  { label: 'Search', path: '/search', icon: Search, bgColor: 'bg-lavender-50', iconColor: 'text-lavender-600' },
  { label: 'Health', path: '/health', icon: Stethoscope, bgColor: 'bg-success/5', iconColor: 'text-success' },
  { label: 'Community', path: '/community', icon: MessageCircle, bgColor: 'bg-secondary-50', iconColor: 'text-secondary-600' },
  { label: 'Trending', path: '/community?sort=popular', icon: TrendingUp, bgColor: 'bg-warning/5', iconColor: 'text-warning' },
];
