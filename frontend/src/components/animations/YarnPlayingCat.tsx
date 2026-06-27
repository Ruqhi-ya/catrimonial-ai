import { motion } from 'framer-motion';

interface YarnPlayingCatProps {
  size?: number;
  className?: string;
}

export function YarnPlayingCat({ size = 150, className = '' }: YarnPlayingCatProps) {
  return (
    <motion.svg
      width={size}
      height={size * 0.7}
      viewBox="0 0 150 105"
      className={className}
    >
      {/* Yarn ball */}
      <motion.g
        animate={{ x: [0, 15, 0, -15, 0] }}
        transition={{ duration: 3, repeat: Infinity, ease: 'easeInOut' }}
      >
        <circle cx="110" cy="75" r="14" fill="#DCC6FF" />
        <path d="M100 70 Q110 60 120 70" stroke="#B280FF" fill="none" strokeWidth="1.5" />
        <path d="M103 78 Q110 85 117 78" stroke="#B280FF" fill="none" strokeWidth="1.5" />
        <path d="M108 63 Q110 75 112 87" stroke="#B280FF" fill="none" strokeWidth="1.5" />
        {/* Yarn string */}
        <motion.path
          d="M96 75 Q80 70 70 80"
          stroke="#DCC6FF"
          strokeWidth="2"
          fill="none"
          strokeLinecap="round"
          animate={{
            d: ['M96 75 Q80 70 70 80', 'M96 75 Q85 65 70 75', 'M96 75 Q80 70 70 80'],
          }}
          transition={{ duration: 3, repeat: Infinity }}
        />
      </motion.g>

      {/* Cat body (playful pose) */}
      <ellipse cx="45" cy="70" rx="22" ry="16" fill="#FF6B9A" />
      {/* Head */}
      <motion.g
        animate={{ rotate: [-5, 5, -5] }}
        transition={{ duration: 1.5, repeat: Infinity }}
        style={{ transformOrigin: '55px 45px' }}
      >
        <circle cx="55" cy="45" r="14" fill="#FF6B9A" />
        {/* Ears */}
        <polygon points="44,34 47,22 52,34" fill="#FF6B9A" />
        <polygon points="58,34 63,22 66,34" fill="#FF6B9A" />
        <polygon points="45,33 48,24 51,33" fill="#FFB3D1" />
        <polygon points="59,33 62,24 65,33" fill="#FFB3D1" />
        {/* Excited eyes */}
        <circle cx="50" cy="44" r="3.5" fill="white" />
        <circle cx="60" cy="44" r="3.5" fill="white" />
        <circle cx="51" cy="44" r="2" fill="#1E293B" />
        <circle cx="61" cy="44" r="2" fill="#1E293B" />
        <circle cx="51.5" cy="43" r="0.8" fill="white" />
        <circle cx="61.5" cy="43" r="0.8" fill="white" />
        {/* Happy mouth */}
        <path d="M52 51 Q55 54 58 51" stroke="#1E293B" fill="none" strokeWidth="1.2" strokeLinecap="round" />
      </motion.g>

      {/* Paw reaching for yarn */}
      <motion.path
        d="M65 72 Q72 68 78 72"
        stroke="#FFB3D1"
        strokeWidth="6"
        fill="none"
        strokeLinecap="round"
        animate={{
          d: ['M65 72 Q72 68 78 72', 'M65 72 Q75 64 82 68', 'M65 72 Q72 68 78 72'],
        }}
        transition={{ duration: 1.5, repeat: Infinity }}
      />

      {/* Back paws */}
      <circle cx="28" cy="82" r="4" fill="#FFB3D1" />
      <circle cx="38" cy="84" r="4" fill="#FFB3D1" />

      {/* Tail */}
      <motion.path
        d="M23 68 Q15 55 20 45"
        stroke="#FF6B9A"
        strokeWidth="5"
        fill="none"
        strokeLinecap="round"
        animate={{
          d: ['M23 68 Q15 55 20 45', 'M23 68 Q12 58 18 48', 'M23 68 Q15 55 20 45'],
        }}
        transition={{ duration: 1, repeat: Infinity }}
      />
    </motion.svg>
  );
}
