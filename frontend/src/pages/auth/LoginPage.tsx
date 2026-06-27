import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { Mail, Lock, Eye, EyeOff } from 'lucide-react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import toast from 'react-hot-toast';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { PeekingCat } from '@/components/animations/PeekingCat';
import { FloatingPaws } from '@/components/animations/FloatingPaws';
import { PageTransition } from '@/components/layout/PageTransition';
import { authService } from '@/services/authService';
import { useAuthStore } from '@/stores/authStore';

const loginSchema = z.object({
  email: z.string().email('Please enter a valid email'),
  password: z.string().min(1, 'Password is required'),
});

type LoginFormData = z.infer<typeof loginSchema>;

export function LoginPage() {
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { setAuth } = useAuthStore();

  const { register, handleSubmit, formState: { errors } } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginFormData) => {
    setLoading(true);
    try {
      const response = await authService.login(data);
      setAuth(response.user, response.accessToken, response.refreshToken);
      toast.success(`Welcome back, ${response.user.name}! 🐱`);
      navigate('/dashboard');
    } catch (error: any) {
      const message = error.response?.data?.message || 'Invalid credentials';
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
          className="w-full max-w-md relative"
        >
          <div className="bg-white rounded-3xl shadow-soft p-8 relative overflow-hidden">
            <PeekingCat position="bottom-right" className="opacity-30" />
            
            {/* Header */}
            <div className="text-center mb-8">
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                transition={{ delay: 0.1, type: 'spring' }}
                className="w-16 h-16 bg-primary-50 rounded-2xl flex items-center justify-center mx-auto mb-4"
              >
                <span className="text-3xl">🐱</span>
              </motion.div>
              <h1 className="text-2xl font-bold text-text-primary">Welcome Back!</h1>
              <p className="text-text-secondary mt-1">Sign in to continue to Catrimonial</p>
            </div>

            {/* Form */}
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
              <div>
                <Input
                  {...register('email')}
                  type="email"
                  placeholder="Email address"
                  icon={<Mail size={18} />}
                  error={errors.email?.message}
                />
              </div>

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

              <div className="flex justify-end">
                <Link to="/forgot-password" className="text-sm text-primary font-medium hover:underline">
                  Forgot password?
                </Link>
              </div>

              <Button type="submit" className="w-full" size="lg" disabled={loading}>
                {loading ? (
                  <motion.div
                    className="flex items-center gap-2"
                    animate={{ opacity: [1, 0.5, 1] }}
                    transition={{ duration: 1, repeat: Infinity }}
                  >
                    <span>Signing in...</span>
                  </motion.div>
                ) : (
                  'Sign In'
                )}
              </Button>
            </form>

            {/* Footer */}
            <p className="text-center text-sm text-text-secondary mt-6">
              Don't have an account?{' '}
              <Link to="/register" className="text-primary font-semibold hover:underline">
                Create one
              </Link>
            </p>
          </div>
        </motion.div>
      </div>
    </PageTransition>
  );
}
