import { motion } from 'framer-motion';

interface FloatingHeartsProps {
  count?: number;
  className?: string;
}

export function FloatingHearts({ count = 5, className = '' }: FloatingHeartsProps) {
  const hearts = Array.from({ length: count }, (_, i) => ({
    id: i,
    x: 10 + Math.random() * 80,
    size: 14 + Math.random() * 10,
    delay: Math.random() * 4,
    duration: 5 + Math.random() * 3,
  }));

  return (
    <div className={`absolute inset-0 overflow-hidden pointer-events-none ${className}`}>
      {hearts.map((heart) => (
        <motion.div
          key={heart.id}
          className="absolute bottom-0"
          style={{ left: `${heart.x}%` }}
          animate={{
            y: [0, -300, -600],
            x: [0, Math.random() > 0.5 ? 20 : -20, 0],
            opacity: [0, 0.6, 0],
            scale: [0.5, 1, 0.5],
          }}
          transition={{
            duration: heart.duration,
            repeat: Infinity,
            delay: heart.delay,
            ease: 'easeOut',
          }}
        >
          <svg width={heart.size} height={heart.size} viewBox="0 0 24 24" fill="#FF6B9A">
            <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
          </svg>
        </motion.div>
      ))}
    </div>
  );
}
