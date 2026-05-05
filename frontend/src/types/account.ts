export interface Account {
    id: number
    name: string
    type: 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE'
    description: string
}

export interface BalanceResponse {
    accountId: number
    accountName: string
    balance: number
}