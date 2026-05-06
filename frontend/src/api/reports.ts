import apiClient from './axios'

export interface TrialBalanceEntry {
    accountId: number
    accountName: string
    accountType: string
    debitBalance: number
    creditBalance: number
}

export interface TrialBalanceReport {
    entries: TrialBalanceEntry[]
    totalDebits: number
    totalCredits: number
    isBalanced: boolean
}

export interface ProfitAndLossReport {
    totalRevenue: number
    totalExpenses: number
    netIncome: number
    isProfit: boolean
}

export interface BalanceSheetReport {
    totalAssets: number
    totalLiabilities: number
    totalEquity: number
    isBalanced: boolean
}

export const getTrialBalance = async (): Promise<TrialBalanceReport> => {
    const response = await apiClient.get<TrialBalanceReport>(
        '/reports/trial-balance'
    )
    return response.data
}

export const getProfitAndLoss = async (): Promise<ProfitAndLossReport> => {
    const response = await apiClient.get<ProfitAndLossReport>(
        '/reports/profit-and-loss'
    )
    return response.data
}

export const getBalanceSheet = async (): Promise<BalanceSheetReport> => {
    const response = await apiClient.get<BalanceSheetReport>(
        '/reports/balance-sheet'
    )
    return response.data
}