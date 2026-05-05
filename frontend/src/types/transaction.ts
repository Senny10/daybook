export type EntryType = 'DEBIT' | 'CREDIT'

export interface Transaction {
    id: number
    date: string
    description: string
    reference: string
    entries: Entry[]
}

export interface Entry {
    id: number
    accountId: number
    accountName: string
    amount: number
    type: EntryType
}

