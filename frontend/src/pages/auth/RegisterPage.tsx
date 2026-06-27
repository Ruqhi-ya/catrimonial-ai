import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Mail, Lock, User, Phone, MapPin, Eye, EyeOff } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import toast from 'react-hot-toast';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { FloatingPaws } from '@/components/animations/FloatingPaws';
import { PageTransition } from '@/components/layout/PageTransition';
import { authService } from '@/services/authService';
import { useAuthStore } from '@/stores/authStore';

const registerSchema = z.object({
  name: z.string().min(2, 'Name must be at least 2 characters'),
  email: z.string().email('Please enter a valid email'),
  password: z.string()
    .min(8, 'Password must be at least 8 characters')
    .regex(/[A-Z]/, 'Must contain an uppercase letter')
    .regex(/[a-z]/, 'Must contain a lowercase letter')
    .regex(/[0-9]/, 'Must contain a number')
    .regex(/[@$!%*?&]/, 'Must contain a special character'),
  confirmPassword: z.string(),
  phone: z.string().optional(),
  city: z.string().optional(),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Passwords don't match",
  path: ['confirmPassword'],
});

type RegisterFormData = z.infer<typeof registerSchema>;

export function RegisterPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { setAuth } = useAuthStore();

  const { register, handleSubmit, formState: { errors }, watch } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  const password = watch('password', '');
  const passwordStrength = getPasswordStrength(password);

  const onSubmit = async (data: RegisterFormData) => {
    setLoading(true);
    try {
      const response = await authService.register({
        name: data.name,
        email: data.email,
        password: data.password,
        phone: data.phone,
        city: data.city,
      });
      setAuth(response.user, response.accessToken, response.refreshToken);
      toast.success('Welcome to Catrimonial! 🎉🐱');
      navigate('/dashboard');
    } catch (error: any) {
      const message = error.response?.data?.message || 'Registration failed';
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageTransition>
      <div className="min-h-[calc(100vh-64px)] flex items-center justify-center relative gradient-bg px-4 py-12">
        <FloatingPaws count={5} />
        
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 0.3 }}
          className="w-full max-w-md"
        >
          <div className="bg-white rounded-3xl shadow-soft p-8">
            {/* Header */}
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.1, type: 'spring' }}
                className="w-16 h-16 bg-primary-50 rounded-2xl flex items-center justify-center mx-auto mb-4"
              >
                <span className="text-3xl">🐾</span>
              </motion.div>
              <h1 className="text-2xl font-bold text-text-primary">Join Catrimonial</h1>
              <p className="text-text-secondary mt-1">Create your account & start connecting</p>
            </div>

            {/* Form */}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <Input
                {...register('name')}
                placeholder="Full name"
                icon={<User size={18} />}
                error={errors.name?.message}
              />

              <Input
                {...register('email')}
                type="email"
                placeholder="Email address"
                icon={<Mail size={18} />}
                error={errors.email?.message}
              />

              <div className="relative">
                <Input
                  {...register('password')}
                  type={showPassword ? 'text' : 'password'}
                  placeholder="Password"
                  icon={<Lock size={18} />}
                  error={errors.password?.message}
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3.5 top-3 text-text-secondary/60 hover:text-text-secondary"
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>

              {/* Password strength indicator */}
              {password.length > 0 && (
                <div className="flex gap-1">
                  {[1, 2, 3, 4].map((level) => (
                    <div
                      key={level}
                      className={`h-1 flex-1 rounded-full transition-colors ${
                        level <= passwordStrength
                          ? passwordStrength <= 1 ? 'bg-error' 
                            : passwordStrength <= 2 ? 'bg-warning'
                            : passwordStrength <= 3 ? 'bg-primary'
                            : 'bg-success'
                          : 'bg-gray-200'
                      }`}
                    />
                  ))}
                </div>
              )}

              <Input
                {...register('confirmPassword')}
                type="password"
                placeholder="Confirm password"
                icon={<Lock size={18} />}
                error={errors.confirmPassword?.message}
              />

              <div className="grid grid-cols-2 gap-3">
                <Input
                  {...register('phone')}
                  placeholder="Phone (optional)"
                  icon={<Phone size={18} />}
                />
                <Input
                  {...register('city')}
                  placeholder="City (optional)"
                  icon={<MapPin size={18} />}
                />
              </div>

              <Button type="submit" className="w-full" size="lg" disabled={loading}>
                {loading ? 'Creating account...' : 'Create Account'}
              </Button>
            </form>

            <p className="text-center text-sm text-text-secondary mt-6">
              Already have an account?{' '}
              <Link to="/login" className="text-primary font-semibold hover:underline">
                Sign in
              </Link>
            </p>
          </div>
        </motion.div>
      </div>
    </PageTransition>
  );
}

function getPasswordStrength(password: string): number {
  let strength = 0;
  if (password.length >= 8) strength++;
  if (/[A-Z]/.test(password)) strength++;
  if (/[0-9]/.test(password)) strength++;
  if (/[@$!%*?&]/.test(password)) strength++;
  return strength;
}
