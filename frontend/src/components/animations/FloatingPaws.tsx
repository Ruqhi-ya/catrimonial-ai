import { motion } from 'framer-motion';

interface FloatingPawsProps {
  count?: number;
  className?: string;
}

export function FloatingPaws({ count = 6, className = '' }: FloatingPawsProps) {
  const paws = Array.from({ length: count }, (_, i) => ({
    id: i,
    x: Math.random() * 100,
    y: Math.random() * 100,
    size: 16 + Math.random() * 12,
    delay: Math.random() * 3,
    duration: 4 + Math.random() * 3,
    rotation: Math.random() * 360,
  }));

  return (
    <div className={`absolute inset-0 overflow-hidden pointer-events-none ${className}`}>
      {paws.map((paw) => (
        <motion.div
          key={paw.id}
          className="absolute"
          style={{
            left: `${paw.x}%`,
            top: `${paw.y}%`,
          }}
          animate={{
            y: [0, -30, 0],
            opacity: [0.15, 0.3, 0.15],
            rotate: [paw.rotation, paw.rotation + 20, paw.rotation],
          }}
          transition={{
            duration: paw.duration,
            repeat: Infinity,
            delay: paw.delay,
            ease: 'easeInOut',
          }}
        >
          <svg width={paw.size} height={paw.size} viewBox="0 0 24 24" fill="#FF6B9A" opacity="0.2">
            <path d="M12 10c-1.5 0-2.5 1.5-2.5 3s1 3 2.5 3 2.5-1.5 2.5-3-1-3-2.5-3zM7 7c-1 0-1.8 1-1.8 2.2s.8 2.2 1.8 2.2 1.8-1 1.8-2.2S8 7 7 7zm10 0c-1 0-1.8 1-1.8 2.2s.8 2.2 1.8 2.2 1.8-1 1.8-2.2S18 7 17 7zM9 15c-1 0-1.5.8-1.5 1.8s.5 1.8 1.5 1.8 1.5-.8 1.5-1.8S10 15 9 15zm6 0c-1 0-1.5.8-1.5 1.8s.5 1.8 1.5 1.8 1.5-.8 1.5-1.8S16 15 15 15z" />
          </svg>
        </motion.div>
      ))}
    </div>
  );
}
