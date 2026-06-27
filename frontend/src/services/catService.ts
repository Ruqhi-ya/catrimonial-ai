import api from './api';
import type { Cat, CatRequest, Page } from '@/types';

export const catService = {
  async createCat(data: CatRequest): Promise<Cat> {
    const response = await api.post<Cat>('/cats', data);
    return response.data;
  },

  async updateCat(catId: string, data: CatRequest): Promise<Cat> {
    const response = await api.put<Cat>(`/cats/${catId}`, data);
    return response.data;
  },

  async deleteCat(catId: string): Promise<void> {
    await api.delete(`/cats/${catId}`);
  },

  async getCat(catId: string): Promise<Cat> {
    const response = await api.get<Cat>(`/cats/${catId}`);
    return response.data;
  },

  async getMyCats(page = 0, size = 20): Promise<Page<Cat>> {
    const response = await api.get<Page<Cat>>('/cats/my-cats', { params: { page, size } });
    return response.data;
  },

  async searchCats(params: {
    breed?: string;
    gender?: string;
    city?: string;
    color?: string;
    page?: number;
    size?: number;
  }): Promise<Page<Cat>> {
    const response = await api.get<Page<Cat>>('/cats/public/search', { params });
    return response.data;
  },

  async getNearbyCats(city: string, page = 0): Promise<Page<Cat>> {
    const response = await api.get<Page<Cat>>('/cats/public/nearby', { params: { city, page } });
    return response.data;
  },

  async getBreeds(): Promise<string[]> {
    const response = await api.get<string[]>('/cats/public/breeds');
    return response.data;
  },
};
