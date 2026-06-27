import { motion } from 'framer-motion';

interface LoadingCatProps {
  message?: string;
  className?: string;
}

export function LoadingCat({ message = 'Loading...', className = '' }: LoadingCatProps) {
  return (
    <div className={`flex flex-col items-center justify-center gap-4 ${className}`}>
      <motion.div
        animate={{ rotate: [0, 10, -10, 0] }}
        transition={{ duration: 1.5, repeat: Infinity }}
      >
        <svg width="80" height="80" viewBox="0 0 80 80">
          {/* Cat face */}
          <circle cx="40" cy="40" r="24" fill="#FF6B9A" />
          {/* Ears */}
          <polygon points="22,20 27,6 34,20" fill="#FF6B9A" />
          <polygon points="46,20 53,6 58,20" fill="#FF6B9A" />
          <polygon points="24,18 28,8 33,18" fill="#FFB3D1" />
          <polygon points="47,18 52,8 57,18" fill="#FFB3D1" />
          {/* Eyes - yarn-chasing look */}
          <circle cx="33" cy="38" r="5" fill="white" />
          <circle cx="47" cy="38" r="5" fill="white" />
          <motion.circle
            cx="34"
            cy="39"
            r="3"
            fill="#1E293B"
            animate={{ cx: [34, 36, 34, 32, 34] }}
            transition={{ duration: 1, repeat: Infinity }}
          />
          <motion.circle
            cx="48"
            cy="39"
            r="3"
            fill="#1E293B"
            animate={{ cx: [48, 50, 48, 46, 48] }}
            transition={{ duration: 1, repeat: Infinity }}
          />
          {/* Nose */}
          <path d="M38 46 L40 48 L42 46 Z" fill="#FF3D7A" />
          {/* Smile */}
          <path d="M36 50 Q40 53 44 50" stroke="#1E293B" fill="none" strokeWidth="1.5" strokeLinecap="round" />
        </svg>
      </motion.div>

      {/* Bouncing dots */}
      <div className="flex gap-1.5">
        {[0, 1, 2].map((i) => (
          <motion.div
            key={i}
            className="w-2.5 h-2.5 rounded-full bg-primary"
            animate={{ y: [0, -8, 0] }}
            transition={{
              duration: 0.6,
              repeat: Infinity,
              delay: i * 0.15,
            }}
          />
        ))}
      </div>

      <motion.p
        className="text-text-secondary text-sm font-medium"
        animate={{ opacity: [0.5, 1, 0.5] }}
        transition={{ duration: 1.5, repeat: Infinity }}
      >
        {message}
      </motion.p>
    </div>
  );
}
