export interface LoginRequest {
    username: string
    password: string
}

export interface RegisterRequest {
    username: string
    password: string
    role?: 'USER' | 'ADMIN'
}

export interface AuthResponse {
    token: string
    username: string
    role: 'USER' | 'ADMIN'
}

export interface User {
    username: string
    role: 'USER' | 'ADMIN'
}