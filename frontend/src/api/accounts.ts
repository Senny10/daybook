import apiClient from './axios'
import type { Account, BalanceResponse } from '../types'

export const getAccounts = async (): Promise<Account[]> => {
    const response = await apiClient.get<Account[]>('/accounts')
    return response.data
}

export const getAccountBalance = async (
    id: number
): Promise<BalanceResponse> => {
    const response = await apiClient.get<BalanceResponse>(
        `/accounts/${id}/balance`
    )
    return response.data
}

export const createAccount = async (data: {
    name: string
    type: string
    description: string
}): Promise<Account> => {
    const response = await apiClient.post<Account>('/accounts', data)
    return response.data
}