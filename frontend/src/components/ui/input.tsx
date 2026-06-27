import * as React from 'react';
import { cn } from '@/lib/utils';

export interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  icon?: React.ReactNode;
  error?: string;
}

const Input = React.forwardRef<HTMLInputElement, InputProps>(
  ({ className, type, icon, error, ...props }, ref) => {
    return (
      <div className="relative w-full">
        {icon && (
          <div className="absolute left-3.5 top-1/2 -translate-y-1/2 text-text-secondary/60">
            {icon}
          </div>
        )}
        <input
          type={type}
          className={cn(
            'flex w-full px-4 py-3 bg-light-gray border border-transparent rounded-2xl text-sm',
            'text-text-primary placeholder:text-text-secondary/60',
            'focus:border-primary-200 focus:bg-white focus:ring-2 focus:ring-primary-100',
            'transition-all duration-200 outline-none',
            icon && 'pl-11',
            error && 'border-error/50 focus:border-error focus:ring-error/20',
            className
          )}
          ref={ref}
          {...props}
        />
        {error && (
          <p className="mt-1.5 text-xs text-error font-medium">{error}</p>
        )}
      </div>
    );
  }
);
Input.displayName = 'Input';

export { Input };
