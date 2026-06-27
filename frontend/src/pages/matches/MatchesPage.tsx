import { motion } from 'framer-motion';
import { Heart, Sparkles } from 'lucide-react';
import { PageTransition } from '@/components/layout/PageTransition';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { YarnPlayingCat } from '@/components/animations/YarnPlayingCat';
import { FloatingHearts } from '@/components/animations/FloatingHearts';

export function MatchesPage() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8 relative">
        <FloatingHearts count={3} className="opacity-30" />

        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
              <Heart className="text-primary fill-primary" size={24} />
              AI Matches
            </h1>
            <p className="text-text-secondary mt-1">AI-powered compatibility recommendations</p>
          </div>
          <Button variant="secondary" size="sm">
            <Sparkles size={16} className="mr-2" />
            Find Matches
          </Button>
        </div>

        {/* Empty State */}
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-center py-16"
        >
          <YarnPlayingCat size={200} className="mx-auto mb-6" />
          <h2 className="text-xl font-bold text-text-primary mb-2">No matches yet</h2>
          <p className="text-text-secondary max-w-md mx-auto mb-6">
            Add your cat's profile and our AI will find compatible companions 
            based on breed, age, health, temperament, and location.
          </p>

          {/* How it works */}
          <Card className="max-w-lg mx-auto">
            <CardContent className="p-6">
              <h3 className="font-bold text-text-primary mb-4">How AI Matching Works</h3>
              <div className="space-y-3 text-left">
                {matchFactors.map((factor) => (
                  <div key={factor.label} className="flex items-center gap-3">
                    <div className="w-12 h-2 rounded-full bg-light-gray overflow-hidden">
                      <div className="h-full bg-primary rounded-full" style={{ width: `${factor.weight}%` }} />
                    </div>
                    <span className="text-sm text-text-primary font-medium">{factor.label}</span>
                    <span className="text-xs text-text-secondary ml-auto">{factor.weight}%</span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </PageTransition>
  );
}

const matchFactors = [
  { label: 'Breed Compatibility', weight: 40 },
  { label: 'Age Compatibility', weight: 20 },
  { label: 'Health Status', weight: 20 },
  { label: 'Distance', weight: 10 },
  { label: 'Temperament', weight: 10 },
];
