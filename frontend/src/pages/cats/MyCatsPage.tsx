import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import { Plus, MapPin, Calendar, Shield } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { PageTransition } from '@/components/layout/PageTransition';
import { SleepingCat } from '@/components/animations/SleepingCat';
import { calculateAge } from '@/lib/utils';

export function MyCatsPage() {
  // This would come from API
  const cats: any[] = [];

  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        {/* Header */}
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary">My Cats</h1>
            <p className="text-text-secondary mt-1">Manage your cat profiles</p>
          </div>
          <Link to="/add-cat">
            <Button>
              <Plus size={18} className="mr-2" />
              Add Cat
            </Button>
          </Link>
        </div>

        {cats.length === 0 ? (
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            className="text-center py-16"
          >
            <SleepingCat size={160} className="mx-auto mb-6" />
            <h2 className="text-xl font-bold text-text-primary mb-2">No cats yet!</h2>
            <p className="text-text-secondary mb-6 max-w-md mx-auto">
              Add your first cat to start getting AI-powered compatibility matches, 
              health tracking, and more.
            </p>
            <Link to="/add-cat">
              <Button size="lg">
                <Plus size={18} className="mr-2" />
                Add Your First Cat
              </Button>
            </Link>
          </motion.div>
        ) : (
          <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {cats.map((cat, index) => (
              <motion.div
                key={cat.id}
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ delay: index * 0.1 }}
              >
                <Link to={`/cats/${cat.id}`}>
                  <Card className="overflow-hidden hover:shadow-card-hover hover:-translate-y-1 transition-all duration-300">
                    {/* Image */}
                    <div className="h-48 bg-primary-50 relative">
                      {cat.images?.[0]?.url ? (
                        <img src={cat.images[0].url} alt={cat.name} className="w-full h-full object-cover" />
                      ) : (
                        <div className="w-full h-full flex items-center justify-center">
                          <span className="text-5xl">🐱</span>
                        </div>
                      )}
                      {cat.verificationStatus === 'VERIFIED' && (
                        <div className="absolute top-3 right-3 bg-success text-white px-2 py-1 rounded-full text-xs font-medium flex items-center gap-1">
                          <Shield size={12} />
                          Verified
                        </div>
                      )}
                    </div>
                    
                    <CardContent className="p-4">
                      <h3 className="font-bold text-text-primary">{cat.name}</h3>
                      <p className="text-sm text-text-secondary">{cat.breed} • {cat.gender === 'MALE' ? '♂' : '♀'}</p>
                      <div className="flex items-center gap-4 mt-2 text-xs text-text-secondary">
                        <span className="flex items-center gap-1">
                          <Calendar size={12} />
                          {calculateAge(cat.ageMonths)}
                        </span>
                        {cat.city && (
                          <span className="flex items-center gap-1">
                            <MapPin size={12} />
                            {cat.city}
                          </span>
                        )}
                      </div>
                    </CardContent>
                  </Card>
                </Link>
              </motion.div>
            ))}
          </div>
        )}
      </div>
    </PageTransition>
  );
}
