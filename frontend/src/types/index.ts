// ====== Auth Types ======
export interface User {
  id: string;
  name: string;
  email: string;
  phone?: string;
  profileImage?: string;
  country?: string;
  state?: string;
  city?: string;
  bio?: string;
  verified: boolean;
  emailVerified: boolean;
  profileCompletionScore: number;
  roles: string[];
  catCount?: number;
  createdAt: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: {
    id: string;
    name: string;
    email: string;
    profileImage?: string;
    verified: boolean;
    roles: string[];
  };
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  phone?: string;
  city?: string;
  state?: string;
  country?: string;
}

// ====== Cat Types ======
export interface Cat {
  id: string;
  name: string;
  breed: string;
  gender: 'MALE' | 'FEMALE';
  ageMonths: number;
  weightKg?: number;
  color?: string;
  vaccinated: boolean;
  neutered: boolean;
  temperament?: string;
  healthIssues?: string;
  description?: string;
  latitude?: number;
  longitude?: number;
  city?: string;
  state?: string;
  country?: string;
  verificationStatus: 'PENDING' | 'VERIFIED' | 'REJECTED';
  active: boolean;
  images: CatImage[];
  owner: CatOwner;
  createdAt: string;
}

export interface CatImage {
  id: string;
  url: string;
  thumbnailUrl?: string;
  isPrimary: boolean;
  aiBreedDetection?: string;
}

export interface CatOwner {
  id: string;
  name: string;
  profileImage?: string;
  city?: string;
  verified: boolean;
}

export interface CatRequest {
  name: string;
  breed: string;
  gender: string;
  ageMonths: number;
  weightKg?: number;
  color?: string;
  vaccinated?: boolean;
  neutered?: boolean;
  temperament?: string;
  healthIssues?: string;
  description?: string;
  latitude?: number;
  longitude?: number;
  city?: string;
  state?: string;
  country?: string;
}

// ====== Match Types ======
export interface MatchResult {
  id: string;
  cat: Cat;
  compatibilityScore: number;
  reasons: CompatibilityReason[];
}

export interface CompatibilityReason {
  factor: string;
  score: number;
  description: string;
  positive: boolean;
}

// ====== Health Types ======
export interface Vaccination {
  id: string;
  catId: string;
  catName: string;
  vaccineName: string;
  dateGiven: string;
  nextDueDate?: string;
  vetName?: string;
  vetClinic?: string;
  batchNumber?: string;
  notes?: string;
  documentUrl?: string;
  isOverdue: boolean;
  createdAt: string;
}

export interface HealthRecord {
  id: string;
  catId: string;
  catName: string;
  recordType: string;
  title: string;
  description?: string;
  recordDate: string;
  vetName?: string;
  vetClinic?: string;
  createdAt: string;
}

export interface Appointment {
  id: string;
  catId: string;
  title: string;
  description?: string;
  appointmentDate: string;
  vetName?: string;
  vetClinic?: string;
  location?: string;
  status: 'SCHEDULED' | 'COMPLETED' | 'CANCELLED' | 'MISSED';
  notes?: string;
}

// ====== Messaging Types ======
export interface Message {
  id: string;
  senderId: string;
  senderName: string;
  senderImage?: string;
  receiverId: string;
  receiverName: string;
  content: string;
  messageType: 'TEXT' | 'IMAGE' | 'SYSTEM';
  imageUrl?: string;
  read: boolean;
  readAt?: string;
  createdAt: string;
}

export interface Conversation {
  id: string;
  otherUserId: string;
  otherUserName: string;
  otherUserImage?: string;
  otherUserOnline?: boolean;
  lastMessage?: string;
  lastMessageAt?: string;
  unreadCount: number;
  createdAt: string;
}

// ====== Community Types ======
export interface CommunityPost {
  id: string;
  title: string;
  content: string;
  postType: string;
  images?: string[];
  tags?: string;
  likesCount: number;
  commentsCount: number;
  pinned: boolean;
  liked: boolean;
  author: {
    id: string;
    name: string;
    profileImage?: string;
    verified: boolean;
  };
  createdAt: string;
}

export interface Comment {
  id: string;
  postId: string;
  parentId?: string;
  content: string;
  likesCount: number;
  author: {
    id: string;
    name: string;
    profileImage?: string;
    verified: boolean;
  };
  createdAt: string;
}

// ====== Notification Types ======
export interface Notification {
  id: string;
  title: string;
  message: string;
  notificationType: string;
  read: boolean;
  actionUrl?: string;
  createdAt: string;
}

// ====== Lost & Found Types ======
export interface LostFoundReport {
  id: string;
  catName: string;
  reportType: 'LOST' | 'FOUND';
  description: string;
  breed?: string;
  color?: string;
  gender?: string;
  lastSeenLocation?: string;
  lastSeenDate?: string;
  images?: string[];
  contactPhone?: string;
  contactEmail?: string;
  rewardOffered: boolean;
  rewardAmount?: number;
  status: 'ACTIVE' | 'RESOLVED' | 'EXPIRED';
  createdAt: string;
}

// ====== Adoption Types ======
export interface AdoptionPost {
  id: string;
  title: string;
  description: string;
  catName: string;
  breed?: string;
  ageMonths?: number;
  gender?: string;
  vaccinated: boolean;
  neutered: boolean;
  specialNeeds?: string;
  requirements?: string;
  adoptionFee?: number;
  images?: string[];
  location?: string;
  status: 'AVAILABLE' | 'PENDING' | 'ADOPTED' | 'CLOSED';
  createdAt: string;
}

// ====== Pagination ======
export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

// ====== Dashboard Stats ======
export interface DashboardStats {
  totalUsers: number;
  verifiedUsers: number;
  totalCats: number;
  verifiedCats: number;
  totalMatchRequests: number;
  totalMessages: number;
  totalPosts: number;
  pendingReports: number;
}
