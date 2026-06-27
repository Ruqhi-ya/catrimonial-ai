import { Link } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Heart, Shield, Search, MapPin, Stethoscope, Users, ArrowRight, Star } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { AnimatedCat } from '@/components/animations/AnimatedCat';
import { FloatingPaws } from '@/components/animations/FloatingPaws';
import { FloatingHearts } from '@/components/animations/FloatingHearts';
import { YarnPlayingCat } from '@/components/animations/YarnPlayingCat';

export function LandingPage() {
  return (
    <div className="overflow-hidden">
      {/* Hero Section */}
      <section className="relative min-h-[90vh] flex items-center gradient-bg">
        <FloatingPaws count={8} />
        <FloatingHearts count={4} />
        
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
          <div className="grid lg:grid-cols-2 gap-12 items-center">
            {/* Left content */}
            <motion.div
              initial={{ opacity: 0, x: -50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.6 }}
            >
              <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: 0.2 }}
                className="inline-flex items-center gap-2 bg-white px-4 py-2 rounded-full shadow-soft mb-6"
              >
                <span className="text-lg">🐱</span>
                <span className="text-sm font-medium text-text-secondary">AI-Powered Cat Owner Network</span>
              </motion.div>
              
              <h1 className="text-4xl sm:text-5xl lg:text-6xl font-bold text-text-primary leading-tight mb-6">
                Connect, Care &<br />
                <span className="text-gradient">Find the Perfect</span><br />
                Companion
              </h1>
              
              <p className="text-lg text-text-secondary max-w-md mb-8 leading-relaxed">
                The intelligent ecosystem for responsible cat owners. AI-powered matching, 
                health management, and a loving community for your feline friends.
              </p>
              
              <div className="flex flex-wrap gap-4">
                <Link to="/register">
                  <Button size="xl" className="group">
                    Get Started Free
                    <ArrowRight size={20} className="ml-2 group-hover:translate-x-1 transition-transform" />
                  </Button>
                </Link>
                <Link to="/login">
                  <Button variant="secondary" size="xl">
                    Sign In
                  </Button>
                </Link>
              </div>

              {/* Trust indicators */}
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                transition={{ delay: 0.8 }}
                className="flex items-center gap-6 mt-10"
              >
                <div className="flex -space-x-2">
                  {[1, 2, 3, 4, 5].map((i) => (
                    <div key={i} className="w-8 h-8 rounded-full bg-primary-100 border-2 border-white flex items-center justify-center">
                      <span className="text-xs">🐱</span>
                    </div>
                  ))}
                </div>
                <div>
                  <div className="flex items-center gap-1">
                    {[1, 2, 3, 4, 5].map((i) => (
                      <Star key={i} size={14} className="fill-warning text-warning" />
                    ))}
                  </div>
                  <p className="text-xs text-text-secondary">Loved by 10,000+ cat owners</p>
                </div>
              </motion.div>
            </motion.div>

            {/* Right - Animated Cat */}
            <motion.div
              initial={{ opacity: 0, x: 50 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ duration: 0.6, delay: 0.3 }}
              className="hidden lg:flex justify-center items-center relative"
            >
              <div className="relative">
                {/* Glow circle */}
                <motion.div
                  className="absolute inset-0 rounded-full bg-primary-100"
                  animate={{ scale: [1, 1.1, 1] }}
                  transition={{ duration: 3, repeat: Infinity }}
                  style={{ width: 300, height: 300, left: '50%', top: '50%', transform: 'translate(-50%, -50%)' }}
                />
                <AnimatedCat size={250} />
              </div>
            </motion.div>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-24 bg-white relative">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <motion.div
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-center mb-16"
          >
            <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-4">
              Everything for your <span className="text-gradient">feline family</span>
            </h2>
            <p className="text-text-secondary max-w-2xl mx-auto text-lg">
              From AI-powered compatibility matching to health tracking, we've built
              the complete platform for responsible cat ownership.
            </p>
          </motion.div>

          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
            {features.map((feature, index) => (
              <motion.div
                key={feature.title}
                initial={{ opacity: 0, y: 30 }}
                whileInView={{ opacity: 1, y: 0 }}
                viewport={{ once: true }}
                transition={{ delay: index * 0.1 }}
                whileHover={{ y: -5, shadow: '0 8px 30px rgba(0,0,0,0.08)' }}
                className="card-interactive relative overflow-hidden group"
              >
                <div className={`w-12 h-12 rounded-2xl flex items-center justify-center mb-4 ${feature.bgColor}`}>
                  <feature.icon size={24} className={feature.iconColor} />
                </div>
                <h3 className="text-lg font-bold text-text-primary mb-2">{feature.title}</h3>
                <p className="text-text-secondary text-sm leading-relaxed">{feature.description}</p>
                
                {/* Hover accent */}
                <div className="absolute top-0 right-0 w-20 h-20 bg-gradient-to-bl from-primary-50 to-transparent rounded-bl-3xl opacity-0 group-hover:opacity-100 transition-opacity" />
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      {/* AI Section */}
      <section className="py-24 bg-light-gray relative overflow-hidden">
        <FloatingPaws count={4} />
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid lg:grid-cols-2 gap-16 items-center">
            <motion.div
              initial={{ opacity: 0, x: -30 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
            >
              <div className="inline-flex items-center gap-2 bg-primary-50 px-3 py-1.5 rounded-full mb-4">
                <span className="text-xs font-semibold text-primary">AI POWERED</span>
              </div>
              <h2 className="text-3xl sm:text-4xl font-bold text-text-primary mb-6">
                Smart matching with <span className="text-gradient">real intelligence</span>
              </h2>
              <p className="text-text-secondary text-lg mb-8">
                Our AI considers breed compatibility, age, health status, temperament, 
                and location to find the perfect companion for your cat.
              </p>
              
              <div className="space-y-4">
                {aiFeatures.map((item, idx) => (
                  <motion.div
                    key={item}
                    initial={{ opacity: 0, x: -20 }}
                    whileInView={{ opacity: 1, x: 0 }}
                    viewport={{ once: true }}
                    transition={{ delay: idx * 0.1 }}
                    className="flex items-center gap-3"
                  >
                    <div className="w-6 h-6 rounded-full bg-success/10 flex items-center justify-center">
                      <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
                        <path d="M2 7l3.5 3.5L12 4" stroke="#10B981" strokeWidth="2" strokeLinecap="round" />
                      </svg>
                    </div>
                    <span className="text-text-primary font-medium">{item}</span>
                  </motion.div>
                ))}
              </div>
            </motion.div>

            <motion.div
              initial={{ opacity: 0, x: 30 }}
              whileInView={{ opacity: 1, x: 0 }}
              viewport={{ once: true }}
              className="flex justify-center"
            >
              <YarnPlayingCat size={350} />
            </motion.div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="py-24 bg-gradient-to-r from-primary-500 to-lavender-400 relative overflow-hidden">
        <div className="max-w-4xl mx-auto px-4 text-center relative z-10">
          <motion.h2
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            className="text-3xl sm:text-4xl font-bold text-white mb-6"
          >
            Join the responsible cat owner community today
          </motion.h2>
          <motion.p
            initial={{ opacity: 0, y: 20 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true }}
            transition={{ delay: 0.1 }}
            className="text-white/80 text-lg mb-8 max-w-2xl mx-auto"
          >
            Connect with thousands of cat lovers, manage your cat's health, 
            and find the perfect companions - all powered by AI.
          </motion.p>
          <Link to="/register">
            <Button size="xl" className="bg-white text-primary hover:bg-gray-50 shadow-lg">
              Get Started Free
              <ArrowRight size={20} className="ml-2" />
            </Button>
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-white py-12 border-t border-gray-100">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex flex-col md:flex-row justify-between items-center gap-4">
            <div className="flex items-center gap-2">
              <span className="text-lg font-bold">🐱 Catrimonial AI</span>
            </div>
            <p className="text-sm text-text-secondary">
              Made with ❤️ for cats everywhere
            </p>
          </div>
        </div>
      </footer>
    </div>
  );
}

const features = [
  {
    title: 'AI Compatibility',
    description: 'Multi-factor matching algorithm considering breed, age, health, distance, and temperament.',
    icon: Heart,
    bgColor: 'bg-primary-50',
    iconColor: 'text-primary',
  },
  {
    title: 'Health Management',
    description: 'Track vaccinations, medical history, and get AI-powered health alerts.',
    icon: Stethoscope,
    bgColor: 'bg-success/10',
    iconColor: 'text-success',
  },
  {
    title: 'Responsible Breeding',
    description: 'AI-guided breeding recommendations ensuring the health and welfare of cats.',
    icon: Shield,
    bgColor: 'bg-secondary-100',
    iconColor: 'text-secondary-600',
  },
  {
    title: 'Smart Search',
    description: 'Natural language search - just type "White Persian female near Bangalore".',
    icon: Search,
    bgColor: 'bg-lavender-100',
    iconColor: 'text-lavender-600',
  },
  {
    title: 'Nearby Discovery',
    description: 'Find cats, vets, pet stores, and adoption centers in your area.',
    icon: MapPin,
    bgColor: 'bg-warning/10',
    iconColor: 'text-warning',
  },
  {
    title: 'Community',
    description: 'Connect with cat owners, share stories, ask questions, and celebrate milestones.',
    icon: Users,
    bgColor: 'bg-primary-50',
    iconColor: 'text-primary',
  },
];

const aiFeatures = [
  'Breed compatibility analysis (40% weight)',
  'Age & health assessment (20% each)',
  'Natural language cat search',
  'Photo-based breed detection',
  'AI health assistant with RAG',
  'Smart "You may also like" recommendations',
];
