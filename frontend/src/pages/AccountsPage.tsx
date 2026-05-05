import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useAuth } from '../contexts/AuthContext'
import { getAccounts, createAccount } from '../api/accounts'
import AppLayout from '../components/layout/AppLayout'
import type { Account } from '../types'

const ACCOUNT_TYPES = [
    'ASSET', 'LIABILITY', 'EQUITY', 'REVENUE', 'EXPENSE'
] as const

const typeColors: Record<string, string> = {
    ASSET:     'bg-sky-100 text-sky-800',
    LIABILITY: 'bg-amber-100 text-amber-800',
    EQUITY:    'bg-purple-100 text-purple-800',
    REVENUE:   'bg-green-100 text-green-800',
    EXPENSE:   'bg-red-100 text-red-800',
}

export default function AccountsPage() {
    const { isAdmin } = useAuth()
    const [showForm, setShowForm] = useState(false)
    const [formData, setFormData] = useState({
        name: '', type: 'ASSET', description: ''
    })
    const [formError, setFormError] = useState<string | null>(null)
    const [isSubmitting, setIsSubmitting] = useState(false)

    const { data: accounts, isLoading, error, refetch } = useQuery({
        queryKey: ['accounts'],
        queryFn: getAccounts,
    })

    const handleCreateAccount = async (e: React.FormEvent) => {
        e.preventDefault()
        setIsSubmitting(true)
        setFormError(null)

        try {
            await createAccount(formData)
            setFormData({ name: '', type: 'ASSET', description: '' })
            setShowForm(false)
            refetch()
        } catch (error: any) {
            setFormError(
                error.response?.data?.message ?? 'Failed to create account'
            )
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <AppLayout>
            {/* Page Header */}
            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-2xl font-bold text-sky-900">Accounts</h1>
                    <p className="text-sky-600 text-sm mt-1">
                        Chart of accounts for Daybook
                    </p>
                </div>
                {isAdmin && (
                    <button
                        onClick={() => setShowForm(!showForm)}
                        className="px-4 py-2 bg-amber-400 hover:bg-amber-500
                       text-white font-semibold rounded-xl
                       transition-colors duration-200"
                    >
                        {showForm ? 'Cancel' : '+ New Account'}
                    </button>
                )}
            </div>

            {/* Create Account Form — ADMIN only */}
            {showForm && isAdmin && (
                <div className="bg-white rounded-2xl shadow-sky-md p-6 mb-6">
                    <h2 className="text-lg font-semibold text-sky-900 mb-4">
                        Create Account
                    </h2>
                    {formError && (
                        <div className="mb-4 p-3 bg-red-50 border border-red-200
                            rounded-lg text-red-600 text-sm">
                            {formError}
                        </div>
                    )}
                    <form onSubmit={handleCreateAccount} className="space-y-4">
                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium
                                  text-sky-800 mb-1">
                                    Account Name
                                </label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={e => setFormData(
                                        prev => ({ ...prev, name: e.target.value })
                                    )}
                                    placeholder="e.g. Cash, Revenue"
                                    className="w-full px-4 py-2.5 rounded-xl border
                             border-sky-200 focus:outline-none
                             focus:ring-2 focus:ring-sky-300
                             text-sky-900"
                                    required
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium
                                  text-sky-800 mb-1">
                                    Account Type
                                </label>
                                <select
                                    value={formData.type}
                                    onChange={e => setFormData(
                                        prev => ({ ...prev, type: e.target.value })
                                    )}
                                    className="w-full px-4 py-2.5 rounded-xl border
                             border-sky-200 focus:outline-none
                             focus:ring-2 focus:ring-sky-300
                             text-sky-900 bg-white"
                                >
                                    {ACCOUNT_TYPES.map(type => (
                                        <option key={type} value={type}>{type}</option>
                                    ))}
                                </select>
                            </div>
                        </div>
                        <div>
                            <label className="block text-sm font-medium
                                text-sky-800 mb-1">
                                Description
                            </label>
                            <input
                                type="text"
                                value={formData.description}
                                onChange={e => setFormData(
                                    prev => ({ ...prev, description: e.target.value })
                                )}
                                placeholder="Optional description"
                                className="w-full px-4 py-2.5 rounded-xl border
                           border-sky-200 focus:outline-none
                           focus:ring-2 focus:ring-sky-300
                           text-sky-900"
                            />
                        </div>
                        <button
                            type="submit"
                            disabled={isSubmitting}
                            className="px-6 py-2.5 bg-amber-400 hover:bg-amber-500
                         text-white font-semibold rounded-xl
                         transition-colors duration-200
                         disabled:opacity-50"
                        >
                            {isSubmitting ? 'Creating...' : 'Create Account'}
                        </button>
                    </form>
                </div>
            )}

            {/* Accounts List */}
            {isLoading && (
                <div className="text-center py-12 text-sky-500">
                    Loading accounts...
                </div>
            )}

            {error && (
                <div className="bg-red-50 border border-red-200 rounded-2xl
                        p-6 text-red-600">
                    Failed to load accounts. Please try again.
                </div>
            )}

            {accounts && accounts.length === 0 && (
                <div className="bg-white rounded-2xl shadow-sky-md p-12
                        text-center">
                    <p className="text-sky-400 text-lg mb-2">No accounts yet</p>
                    {isAdmin && (
                        <p className="text-sky-300 text-sm">
                            Click "+ New Account" to get started
                        </p>
                    )}
                </div>
            )}

            {accounts && accounts.length > 0 && (
                <div className="grid gap-4">
                    {accounts.map((account: Account) => (
                        <div
                            key={account.id}
                            className="bg-white rounded-2xl shadow-sky-sm p-5
                         flex items-center justify-between
                         hover:shadow-sky-md transition-shadow duration-200"
                        >
                            <div className="flex items-center gap-4">
                <span className={`px-3 py-1 rounded-full text-xs 
                                  font-semibold ${typeColors[account.type]}`}>
                  {account.type}
                </span>
                                <div>
                                    <p className="font-semibold text-sky-900">
                                        {account.name}
                                    </p>
                                    {account.description && (
                                        <p className="text-sm text-sky-500">
                                            {account.description}
                                        </p>
                                    )}
                                </div>
                            </div>
                            <div className="text-right">
                                <p className="text-xs text-sky-400 mb-1">ID: {account.id}</p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </AppLayout>
    )
}