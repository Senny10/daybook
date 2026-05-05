import apiClient from './axios'
import type { LoginRequest, RegisterRequest, AuthResponse } from '../types'

export const login = async (
    request: LoginRequest
): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>(
        '/auth/login',
        request
    )
    return response.data
}

export const register = async (
    request: RegisterRequest
): Promise<AuthResponse> => {
    const response = await apiClient.post<AuthResponse>(
        '/auth/register',
        request
    )
    return response.data
}