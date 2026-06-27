import { motion } from 'framer-motion';
import { Stethoscope, Syringe, Calendar, Activity, AlertCircle } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { PageTransition } from '@/components/layout/PageTransition';
import { Button } from '@/components/ui/button';

export function HealthDashboard() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
              <Stethoscope className="text-success" size={24} />
              Health Dashboard
            </h1>
            <p className="text-text-secondary mt-1">Track your cats' health and wellness</p>
          </div>
          <Button size="sm">
            <Calendar size={16} className="mr-2" />
            Schedule Appointment
          </Button>
        </div>

        {/* Health Summary Cards */}
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {healthStats.map((stat, index) => (
            <motion.div
              key={stat.label}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.1 }}
            >
              <Card className={`border-l-4 ${stat.borderColor}`}>
                <CardContent className="p-4">
                  <div className="flex items-center gap-3">
                    <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${stat.bgColor}`}>
                      <stat.icon size={20} className={stat.iconColor} />
                    </div>
                    <div>
                      <p className="text-2xl font-bold text-text-primary">{stat.value}</p>
                      <p className="text-xs text-text-secondary">{stat.label}</p>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </motion.div>
          ))}
        </div>

        {/* Main Content */}
        <div className="grid lg:grid-cols-2 gap-6">
          {/* Vaccination Tracker */}
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="text-lg flex items-center gap-2">
                <Syringe size={18} className="text-primary" />
                Vaccination Tracker
              </CardTitle>
              <Button variant="ghost" size="sm">View All</Button>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <div className="w-16 h-16 bg-success/10 rounded-full flex items-center justify-center mx-auto mb-3">
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
                    <path d="M4 12l5 5L20 6" stroke="#10B981" strokeWidth="3" strokeLinecap="round" />
                  </svg>
                </div>
                <p className="font-medium text-text-primary">All up to date!</p>
                <p className="text-sm text-text-secondary mt-1">No pending vaccinations</p>
              </div>
            </CardContent>
          </Card>

          {/* Upcoming Appointments */}
          <Card>
            <CardHeader className="flex flex-row items-center justify-between">
              <CardTitle className="text-lg flex items-center gap-2">
                <Calendar size={18} className="text-secondary-600" />
                Upcoming Appointments
              </CardTitle>
              <Button variant="ghost" size="sm">Schedule</Button>
            </CardHeader>
            <CardContent>
              <div className="text-center py-8">
                <div className="text-4xl mb-3">📋</div>
                <p className="font-medium text-text-primary">No appointments</p>
                <p className="text-sm text-text-secondary mt-1">Schedule a vet visit</p>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </PageTransition>
  );
}

const healthStats = [
  { label: 'Vaccinations', value: '0', icon: Syringe, bgColor: 'bg-primary-50', iconColor: 'text-primary', borderColor: 'border-primary' },
  { label: 'Health Records', value: '0', icon: Activity, bgColor: 'bg-success/10', iconColor: 'text-success', borderColor: 'border-success' },
  { label: 'Appointments', value: '0', icon: Calendar, bgColor: 'bg-secondary-100', iconColor: 'text-secondary-600', borderColor: 'border-secondary' },
  { label: 'Alerts', value: '0', icon: AlertCircle, bgColor: 'bg-warning/10', iconColor: 'text-warning', borderColor: 'border-warning' },
];
