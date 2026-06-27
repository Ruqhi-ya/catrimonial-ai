import { motion } from 'framer-motion';
import { MessageCircle } from 'lucide-react';
import { PageTransition } from '@/components/layout/PageTransition';
import { SleepingCat } from '@/components/animations/SleepingCat';

export function MessagesPage() {
  return (
    <PageTransition>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        <div className="flex items-center gap-2 mb-8">
          <MessageCircle className="text-primary" size={24} />
          <h1 className="text-2xl font-bold text-text-primary">Messages</h1>
        </div>

        <div className="grid lg:grid-cols-3 gap-6 h-[70vh]">
          {/* Conversation List */}
          <div className="bg-white rounded-3xl shadow-card p-4 overflow-y-auto">
            <div className="text-center py-12">
              <SleepingCat size={100} className="mx-auto mb-4" />
              <p className="text-sm text-text-secondary">No conversations yet</p>
              <p className="text-xs text-text-secondary mt-1">
                Match with cat owners to start chatting!
              </p>
            </div>
          </div>

          {/* Chat Area */}
          <div className="lg:col-span-2 bg-white rounded-3xl shadow-card flex items-center justify-center">
            <div className="text-center">
              <motion.div
                animate={{ y: [0, -5, 0] }}
                transition={{ duration: 2, repeat: Infinity }}
                className="text-5xl mb-4"
              >
                💬
              </motion.div>
              <h3 className="font-bold text-text-primary">Select a conversation</h3>
              <p className="text-sm text-text-secondary mt-1">
                Choose a chat or start a new one
              </p>
            </div>
          </div>
        </div>
      </div>
    </PageTransition>
  );
}
