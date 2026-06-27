import { motion } from 'framer-motion';

interface SleepingCatProps {
  size?: number;
  className?: string;
}

export function SleepingCat({ size = 100, className = '' }: SleepingCatProps) {
  return (
    <div className={`relative ${className}`}>
      <motion.svg
        width={size}
        height={size * 0.6}
        viewBox="0 0 100 60"
        animate={{ y: [0, -2, 0] }}
        transition={{ duration: 2, repeat: Infinity, ease: 'easeInOut' }}
      >
        {/* Curled body */}
        <ellipse cx="50" cy="40" rx="30" ry="18" fill="#FF6B9A" />
        {/* Head resting */}
        <circle cx="30" cy="32" r="12" fill="#FF6B9A" />
        {/* Ears */}
        <polygon points="22,22 25,12 30,22" fill="#FF6B9A" />
        <polygon points="35,22 38,14 40,23" fill="#FF6B9A" />
        <polygon points="23,21 26,14 29,21" fill="#FFB3D1" />
        <polygon points="36,21 38,15 39,22" fill="#FFB3D1" />
        {/* Closed eyes (sleeping) */}
        <path d="M24 32 Q27 30 30 32" stroke="#1E293B" fill="none" strokeWidth="1.5" strokeLinecap="round" />
        <path d="M32 31 Q35 29 38 31" stroke="#1E293B" fill="none" strokeWidth="1.5" strokeLinecap="round" />
        {/* Nose */}
        <circle cx="30" cy="36" r="1.5" fill="#FF3D7A" />
        {/* Tail curled */}
        <motion.path
          d="M78 35 Q85 25 80 20 Q75 15 72 20"
          stroke="#FF6B9A"
          strokeWidth="5"
          fill="none"
          strokeLinecap="round"
        />
        {/* Paw */}
        <ellipse cx="25" cy="45" rx="5" ry="3" fill="#FFB3D1" />
      </motion.svg>
      
      {/* Zzz animation */}
      <div className="absolute -top-2 right-4">
        <motion.span
          className="text-primary text-sm font-bold"
          animate={{ opacity: [0, 1, 0], y: [0, -10, -20] }}
          transition={{ duration: 2, repeat: Infinity, delay: 0 }}
        >
          z
        </motion.span>
        <motion.span
          className="text-primary text-base font-bold ml-1"
          animate={{ opacity: [0, 1, 0], y: [0, -10, -20] }}
          transition={{ duration: 2, repeat: Infinity, delay: 0.3 }}
        >
          Z
        </motion.span>
        <motion.span
          className="text-primary text-lg font-bold ml-1"
          animate={{ opacity: [0, 1, 0], y: [0, -10, -20] }}
          transition={{ duration: 2, repeat: Infinity, delay: 0.6 }}
        >
          Z
        </motion.span>
      </div>
    </div>
  );
}
