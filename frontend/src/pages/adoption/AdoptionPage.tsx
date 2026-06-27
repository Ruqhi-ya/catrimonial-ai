import { motion } from 'framer-motion';
import { Heart, Home } from 'lucide-react';
import { PageTransition } from '@/components/layout/PageTransition';
import { Button } from '@/components/ui/button';
import { FloatingHearts } from '@/components/animations/FloatingHearts';

export function AdoptionPage() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8 relative">
        <FloatingHearts count={3} className="opacity-20" />

        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
              <Home className="text-primary" size={24} />
              Adoption
            </h1>
            <p className="text-text-secondary mt-1">Give a cat a loving home</p>
          </div>
          <Button size="sm">List for Adoption</Button>
        </div>

        <div className="text-center py-16">
          <motion.div
            animate={{ scale: [1, 1.1, 1] }}
            transition={{ duration: 2, repeat: Infinity }}
            className="text-6xl mb-4"
          >
            🏠
          </motion.div>
          <h2 className="text-xl font-bold text-text-primary mb-2">Adoption Center</h2>
          <p className="text-text-secondary max-w-md mx-auto mb-6">
            Browse cats looking for their forever homes, or list your cat for adoption.
          </p>
          <Button variant="secondary">
            <Heart size={16} className="mr-2" />
            Browse Available Cats
          </Button>
        </div>
      </div>
    </PageTransition>
  );
}
