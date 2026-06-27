import { motion } from 'framer-motion';
import { Users } from 'lucide-react';
import { PageTransition } from '@/components/layout/PageTransition';
import { Button } from '@/components/ui/button';

export function CommunityPage() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
              <Users className="text-primary" size={24} />
              Community
            </h1>
            <p className="text-text-secondary mt-1">Connect with cat lovers</p>
          </div>
          <Button size="sm">Create Post</Button>
        </div>

        {/* Category Tabs */}
        <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
          {categories.map((cat) => (
            <button
              key={cat}
              className="px-4 py-2 rounded-full text-sm font-medium whitespace-nowrap bg-light-gray text-text-secondary hover:bg-primary-50 hover:text-primary transition-colors"
            >
              {cat}
            </button>
          ))}
        </div>

        {/* Feed placeholder */}
        <div className="text-center py-16">
          <motion.div
            animate={{ y: [0, -5, 0] }}
            transition={{ duration: 2, repeat: Infinity }}
            className="text-5xl mb-4"
          >
            📝
          </motion.div>
          <h2 className="text-xl font-bold text-text-primary mb-2">Community Feed</h2>
          <p className="text-text-secondary max-w-md mx-auto">
            Share stories, ask questions, and connect with fellow cat owners.
          </p>
        </div>
      </div>
    </PageTransition>
  );
}

const categories = ['All', 'Questions', 'Success Stories', 'Tips', 'Events', 'Discussion'];
