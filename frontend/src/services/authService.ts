import api from './api';
import type { AuthResponse, LoginRequest, RegisterRequest } from '@/types';

export const authService = {
  async register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/register', data);
    return response.data;
  },

  async login(data: LoginRequest): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    const response = await api.post<AuthResponse>('/auth/refresh-token', { refreshToken });
    return response.data;
  },

  async forgotPassword(email: string): Promise<void> {
    await api.post('/auth/forgot-password', { email });
  },

  async resetPassword(token: string, newPassword: string): Promise<void> {
    await api.post('/auth/reset-password', { token, newPassword });
  },

  async verifyEmail(token: string): Promise<void> {
    await api.get(`/auth/verify-email?token=${token}`);
  },

  async logout(): Promise<void> {
    await api.post('/auth/logout');
  },
};
