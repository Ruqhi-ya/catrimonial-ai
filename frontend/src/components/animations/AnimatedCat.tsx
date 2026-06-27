import { motion } from 'framer-motion';

interface AnimatedCatProps {
  size?: number;
  className?: string;
}

export function AnimatedCat({ size = 120, className = '' }: AnimatedCatProps) {
  return (
    <motion.svg
      width={size}
      height={size}
      viewBox="0 0 120 120"
      className={className}
      animate={{ y: [0, -8, 0] }}
      transition={{ duration: 3, repeat: Infinity, ease: 'easeInOut' }}
    >
      {/* Body */}
      <motion.ellipse cx="60" cy="75" rx="25" ry="20" fill="#FF6B9A" />
      {/* Head */}
      <motion.circle cx="60" cy="45" r="18" fill="#FF6B9A" />
      {/* Left Ear */}
      <motion.polygon points="45,30 48,15 55,30" fill="#FF6B9A" />
      <motion.polygon points="47,28 49,18 54,28" fill="#FFB3D1" />
      {/* Right Ear */}
      <motion.polygon points="65,30 72,15 75,30" fill="#FF6B9A" />
      <motion.polygon points="66,28 71,18 73,28" fill="#FFB3D1" />
      {/* Eyes */}
      <motion.ellipse
        cx="53"
        cy="43"
        rx="4"
        ry="4.5"
        fill="white"
      />
      <motion.ellipse
        cx="67"
        cy="43"
        rx="4"
        ry="4.5"
        fill="white"
      />
      <motion.circle
        cx="53"
        cy="44"
        r="2.5"
        fill="#1E293B"
        animate={{ scaleY: [1, 0.1, 1] }}
        transition={{ duration: 3, repeat: Infinity, repeatDelay: 2 }}
      />
      <motion.circle
        cx="67"
        cy="44"
        r="2.5"
        fill="#1E293B"
        animate={{ scaleY: [1, 0.1, 1] }}
        transition={{ duration: 3, repeat: Infinity, repeatDelay: 2 }}
      />
      {/* Eye highlights */}
      <circle cx="54.5" cy="42.5" r="1" fill="white" />
      <circle cx="68.5" cy="42.5" r="1" fill="white" />
      {/* Nose */}
      <motion.path d="M58 50 L60 52 L62 50 Z" fill="#FF3D7A" />
      {/* Mouth */}
      <motion.path
        d="M56 54 Q60 57 64 54"
        stroke="#1E293B"
        fill="none"
        strokeWidth="1.2"
        strokeLinecap="round"
      />
      {/* Whiskers */}
      <motion.line x1="40" y1="48" x2="50" y2="49" stroke="#1E293B" strokeWidth="0.8" />
      <motion.line x1="40" y1="52" x2="50" y2="51" stroke="#1E293B" strokeWidth="0.8" />
      <motion.line x1="70" y1="49" x2="80" y2="48" stroke="#1E293B" strokeWidth="0.8" />
      <motion.line x1="70" y1="51" x2="80" y2="52" stroke="#1E293B" strokeWidth="0.8" />
      {/* Tail */}
      <motion.path
        d="M85 75 Q95 60 90 50"
        stroke="#FF6B9A"
        strokeWidth="5"
        fill="none"
        strokeLinecap="round"
        animate={{ d: ['M85 75 Q95 60 90 50', 'M85 75 Q100 65 95 55', 'M85 75 Q95 60 90 50'] }}
        transition={{ duration: 1.5, repeat: Infinity, ease: 'easeInOut' }}
      />
      {/* Paws */}
      <circle cx="45" cy="92" r="5" fill="#FFB3D1" />
      <circle cx="55" cy="94" r="5" fill="#FFB3D1" />
      <circle cx="65" cy="94" r="5" fill="#FFB3D1" />
      <circle cx="75" cy="92" r="5" fill="#FFB3D1" />
    </motion.svg>
  );
}
