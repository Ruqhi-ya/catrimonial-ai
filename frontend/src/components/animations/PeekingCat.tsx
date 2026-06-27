import { motion } from 'framer-motion';

interface PeekingCatProps {
  position?: 'bottom-left' | 'bottom-right' | 'top-right';
  className?: string;
}

export function PeekingCat({ position = 'bottom-right', className = '' }: PeekingCatProps) {
  const positionClasses = {
    'bottom-left': 'bottom-0 left-0',
    'bottom-right': 'bottom-0 right-0',
    'top-right': 'top-0 right-0 rotate-180',
  };

  return (
    <motion.div
      className={`absolute ${positionClasses[position]} ${className}`}
      initial={{ y: 40 }}
      animate={{ y: [40, 10, 15, 10] }}
      transition={{ duration: 2, repeat: Infinity, repeatDelay: 3 }}
    >
      <svg width="60" height="50" viewBox="0 0 60 50">
        {/* Cat head peeking */}
        <circle cx="30" cy="25" r="16" fill="#FF6B9A" />
        {/* Ears */}
        <polygon points="18,12 22,0 28,12" fill="#FF6B9A" />
        <polygon points="32,12 38,0 42,12" fill="#FF6B9A" />
        <polygon points="20,11 23,3 27,11" fill="#FFB3D1" />
        <polygon points="33,11 37,3 41,11" fill="#FFB3D1" />
        {/* Eyes */}
        <motion.circle
          cx="24"
          cy="24"
          r="4"
          fill="white"
        />
        <motion.circle
          cx="36"
          cy="24"
          r="4"
          fill="white"
        />
        <motion.circle
          cx="24"
          cy="25"
          r="2.5"
          fill="#1E293B"
          animate={{ x: [0, 2, 0, -2, 0] }}
          transition={{ duration: 3, repeat: Infinity }}
        />
        <motion.circle
          cx="36"
          cy="25"
          r="2.5"
          fill="#1E293B"
          animate={{ x: [0, 2, 0, -2, 0] }}
          transition={{ duration: 3, repeat: Infinity }}
        />
        {/* Eye highlights */}
        <circle cx="25" cy="23" r="1" fill="white" />
        <circle cx="37" cy="23" r="1" fill="white" />
        {/* Nose */}
        <path d="M28 30 L30 32 L32 30 Z" fill="#FF3D7A" />
        {/* Paws holding edge */}
        <ellipse cx="20" cy="42" rx="6" ry="4" fill="#FFB3D1" />
        <ellipse cx="40" cy="42" rx="6" ry="4" fill="#FFB3D1" />
        {/* Paw toes */}
        <circle cx="17" cy="41" r="2" fill="#FF8FB5" />
        <circle cx="20" cy="39" r="2" fill="#FF8FB5" />
        <circle cx="23" cy="41" r="2" fill="#FF8FB5" />
        <circle cx="37" cy="41" r="2" fill="#FF8FB5" />
        <circle cx="40" cy="39" r="2" fill="#FF8FB5" />
        <circle cx="43" cy="41" r="2" fill="#FF8FB5" />
      </svg>
    </motion.div>
  );
}
