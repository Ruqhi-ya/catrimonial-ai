import { motion } from 'framer-motion';
import { PageTransition } from '@/components/layout/PageTransition';
import { LoadingCat } from '@/components/animations/LoadingCat';

export function CatProfilePage() {
  return (
    <PageTransition>
      <div className="max-w-4xl mx-auto px-4 py-8 pb-24 md:pb-8">
        <LoadingCat message="Loading cat profile..." />
      </div>
    </PageTransition>
  );
}
