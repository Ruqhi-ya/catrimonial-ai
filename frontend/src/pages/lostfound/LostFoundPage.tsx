import { motion } from 'framer-motion';
import { AlertCircle, MapPin } from 'lucide-react';
import { PageTransition } from '@/components/layout/PageTransition';
import { Button } from '@/components/ui/button';

export function LostFoundPage() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
              <AlertCircle className="text-warning" size={24} />
              Lost & Found
            </h1>
            <p className="text-text-secondary mt-1">Help reunite cats with owners</p>
          </div>
          <Button size="sm">Report Missing Cat</Button>
        </div>

        <div className="grid sm:grid-cols-2 gap-4 mb-8">
          <motion.div
            whileHover={{ scale: 1.02 }}
            className="p-6 rounded-3xl bg-error/5 border border-error/10 cursor-pointer"
          >
            <h3 className="font-bold text-error mb-1">Lost Cats</h3>
            <p className="text-sm text-text-secondary">Report and find missing cats</p>
          </motion.div>
          <motion.div
            whileHover={{ scale: 1.02 }}
            className="p-6 rounded-3xl bg-success/5 border border-success/10 cursor-pointer"
          >
            <h3 className="font-bold text-success mb-1">Found Cats</h3>
            <p className="text-sm text-text-secondary">Report cats you've found</p>
          </motion.div>
        </div>

        <div className="text-center py-12">
          <motion.div animate={{ y: [0, -5, 0] }} transition={{ duration: 2, repeat: Infinity }} className="text-5xl mb-4">
            🔍
          </motion.div>
          <p className="text-text-secondary">No active reports in your area</p>
        </div>
      </div>
    </PageTransition>
  );
}
