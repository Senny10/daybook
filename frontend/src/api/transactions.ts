import apiClient from './axios'
import type { Transaction } from '../types'

export const getTransactions = async (): Promise<Transaction[]> => {
    const response = await apiClient.get<Transaction[]>('/transactions')
    return response.data
}

export const createTransaction = async (data: {
    date: string
    description: string
    reference: string
    entries: {
        accountId: number
        amount: number
        type: 'DEBIT' | 'CREDIT'
    }[]
}): Promise<Transaction> => {
    const response = await apiClient.post<Transaction>('/transactions', data)
    return response.data
}