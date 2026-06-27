import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import toast from 'react-hot-toast';
import { Camera, PawPrint } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { PageTransition } from '@/components/layout/PageTransition';
import { catService } from '@/services/catService';

const catSchema = z.object({
  name: z.string().min(1, 'Cat name is required').max(100),
  breed: z.string().min(1, 'Breed is required'),
  gender: z.string().min(1, 'Gender is required'),
  ageMonths: z.number().min(1, 'Age must be at least 1 month').max(300),
  weightKg: z.number().optional(),
  color: z.string().optional(),
  vaccinated: z.boolean().optional(),
  neutered: z.boolean().optional(),
  temperament: z.string().optional(),
  healthIssues: z.string().optional(),
  description: z.string().optional(),
  city: z.string().optional(),
  state: z.string().optional(),
  country: z.string().optional(),
});

type CatFormData = z.infer<typeof catSchema>;

export function AddCatPage() {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const { register, handleSubmit, formState: { errors }, watch } = useForm<CatFormData>({
    resolver: zodResolver(catSchema),
    defaultValues: {
      vaccinated: false,
      neutered: false,
    },
  });

  const onSubmit = async (data: CatFormData) => {
    setLoading(true);
    try {
      await catService.createCat(data);
      toast.success('Cat profile created! 🎉🐱');
      navigate('/my-cats');
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Failed to create cat profile');
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageTransition>
      <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-24 md:pb-8">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          className="mb-8"
        >
          <h1 className="text-2xl font-bold text-text-primary flex items-center gap-2">
            <PawPrint className="text-primary" size={28} />
            Add Your Cat
          </h1>
          <p className="text-text-secondary mt-1">Create a profile for your feline companion</p>
        </motion.div>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
          {/* Photo Upload Section */}
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 }}>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Photos</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex gap-3 flex-wrap">
                  <motion.div
                    whileHover={{ scale: 1.02 }}
                    whileTap={{ scale: 0.98 }}
                    className="w-24 h-24 rounded-2xl border-2 border-dashed border-primary-200 bg-primary-50/50 flex flex-col items-center justify-center cursor-pointer hover:border-primary transition-colors"
                  >
                    <Camera size={20} className="text-primary mb-1" />
                    <span className="text-[10px] text-primary font-medium">Add Photo</span>
                  </motion.div>
                </div>
                <p className="text-xs text-text-secondary mt-3">Upload up to 6 photos. AI will analyze breed and health.</p>
              </CardContent>
            </Card>
          </motion.div>

          {/* Basic Info */}
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.2 }}>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Basic Information</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <Input {...register('name')} placeholder="Cat's name" error={errors.name?.message} />
                
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <select
                      {...register('breed')}
                      className="input-field"
                    >
                      <option value="">Select breed</option>
                      <option value="Persian">Persian</option>
                      <option value="Siamese">Siamese</option>
                      <option value="Maine Coon">Maine Coon</option>
                      <option value="British Shorthair">British Shorthair</option>
                      <option value="Bengal">Bengal</option>
                      <option value="Ragdoll">Ragdoll</option>
                      <option value="Abyssinian">Abyssinian</option>
                      <option value="Scottish Fold">Scottish Fold</option>
                      <option value="Sphynx">Sphynx</option>
                      <option value="Indian Domestic">Indian Domestic</option>
                      <option value="Mixed">Mixed Breed</option>
                      <option value="Other">Other</option>
                    </select>
                    {errors.breed && <p className="mt-1 text-xs text-error">{errors.breed.message}</p>}
                  </div>
                  <div>
                    <select {...register('gender')} className="input-field">
                      <option value="">Gender</option>
                      <option value="MALE">Male ♂</option>
                      <option value="FEMALE">Female ♀</option>
                    </select>
                    {errors.gender && <p className="mt-1 text-xs text-error">{errors.gender.message}</p>}
                  </div>
                </div>

                <div className="grid grid-cols-3 gap-3">
                  <Input
                    {...register('ageMonths', { valueAsNumber: true })}
                    type="number"
                    placeholder="Age (months)"
                    error={errors.ageMonths?.message}
                  />
                  <Input
                    {...register('weightKg', { valueAsNumber: true })}
                    type="number"
                    step="0.1"
                    placeholder="Weight (kg)"
                  />
                  <Input {...register('color')} placeholder="Color" />
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Health */}
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.3 }}>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Health & Temperament</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <label className="flex items-center gap-3 p-3 bg-light-gray rounded-xl cursor-pointer hover:bg-primary-50 transition-colors">
                    <input type="checkbox" {...register('vaccinated')} className="w-4 h-4 rounded accent-primary" />
                    <span className="text-sm font-medium">Vaccinated</span>
                  </label>
                  <label className="flex items-center gap-3 p-3 bg-light-gray rounded-xl cursor-pointer hover:bg-primary-50 transition-colors">
                    <input type="checkbox" {...register('neutered')} className="w-4 h-4 rounded accent-primary" />
                    <span className="text-sm font-medium">Neutered/Spayed</span>
                  </label>
                </div>
                <Input {...register('temperament')} placeholder="Temperament (e.g., playful, calm, friendly)" />
                <textarea
                  {...register('healthIssues')}
                  placeholder="Any health issues (optional)"
                  className="input-field resize-none h-20"
                />
              </CardContent>
            </Card>
          </motion.div>

          {/* Location */}
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.4 }}>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">Location</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-3 gap-3">
                  <Input {...register('city')} placeholder="City" />
                  <Input {...register('state')} placeholder="State" />
                  <Input {...register('country')} placeholder="Country" />
                </div>
              </CardContent>
            </Card>
          </motion.div>

          {/* Description */}
          <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.5 }}>
            <Card>
              <CardHeader>
                <CardTitle className="text-lg">About Your Cat</CardTitle>
              </CardHeader>
              <CardContent>
                <textarea
                  {...register('description')}
                  placeholder="Tell us about your cat's personality, habits, likes..."
                  className="input-field resize-none h-28"
                />
              </CardContent>
            </Card>
          </motion.div>

          {/* Submit */}
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.6 }}
            className="flex gap-4"
          >
            <Button type="submit" size="lg" className="flex-1" disabled={loading}>
              {loading ? 'Creating Profile...' : 'Create Cat Profile'}
            </Button>
          </motion.div>
        </form>
      </div>
    </PageTransition>
  );
}
