import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { getTransactions } from '../api/transactions'
import { useAuth } from '../contexts/AuthContext'
import AppLayout from '../components/layout/AppLayout'
import NewTransactionModal from '../components/transactions/NewTransactionModal'
import type { Transaction } from '../types'

export default function TransactionsPage() {
    const { isAdmin } = useAuth()
    const [showModal, setShowModal] = useState(false)
    const [search, setSearch] = useState('')

    const { data: transactions = [], isLoading, error, refetch } =
        useQuery({
            queryKey: ['transactions'],
            queryFn: getTransactions,
        })

    const filtered = transactions.filter((t: Transaction) =>
        t.description.toLowerCase().includes(search.toLowerCase()) ||
        t.reference.toLowerCase().includes(search.toLowerCase())
    )

    return (
        <AppLayout>
            {/* Page Header */}
            <div className="flex items-center justify-between mb-6">
                <div>
                    <h1 className="text-2xl font-bold text-sky-900">
                        Transactions
                    </h1>
                    <p className="text-sky-600 text-sm mt-1">
                        {transactions.length} transaction
                        {transactions.length !== 1 ? 's' : ''} recorded
                    </p>
                </div>
                {isAdmin && (
                    <button
                        onClick={() => setShowModal(true)}
                        className="px-4 py-2 bg-amber-400 hover:bg-amber-500
                       text-white font-semibold rounded-xl
                       transition-colors duration-200"
                    >
                        + New Transaction
                    </button>
                )}
            </div>

            {/* Search/Filter */}
            <div className="mb-4">
                <input
                    type="text"
                    value={search}
                    onChange={e => setSearch(e.target.value)}
                    placeholder="Search by description or reference..."
                    className="w-full px-4 py-2.5 rounded-xl border border-sky-200
                     focus:outline-none focus:ring-2 focus:ring-sky-300
                     text-sky-900 bg-white"
                />
            </div>

            {/* Loading */}
            {isLoading && (
                <div className="space-y-3">
                    {[1, 2, 3].map(i => (
                        <div key={i} className="bg-white rounded-2xl p-5 animate-pulse">
                            <div className="flex justify-between mb-3">
                                <div className="space-y-2">
                                    <div className="w-48 h-4 bg-sky-100 rounded"/>
                                    <div className="w-32 h-3 bg-sky-50 rounded"/>
                                </div>
                                <div className="w-8 h-4 bg-sky-50 rounded"/>
                            </div>
                            <div className="border-t border-sky-50 pt-3 space-y-2">
                                <div className="flex justify-between">
                                    <div className="w-24 h-3 bg-sky-100 rounded"/>
                                    <div className="w-16 h-3 bg-sky-50 rounded"/>
                                </div>
                                <div className="flex justify-between">
                                    <div className="w-28 h-3 bg-sky-100 rounded"/>
                                    <div className="w-16 h-3 bg-sky-50 rounded"/>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* Error */}
            {error && (
                <div className="bg-red-50 border border-red-200 rounded-2xl
                        p-6 text-red-600">
                    Failed to load transactions. Please try again.
                </div>
            )}

            {/* Empty state */}
            {!isLoading && filtered.length === 0 && (
                <div className="bg-white rounded-2xl shadow-sky-md p-12
                        text-center">
                    <p className="text-sky-400 text-lg mb-2">
                        {search ? 'No transactions match your search'
                            : 'No transactions yet'}
                    </p>
                    {isAdmin && !search && (
                        <p className="text-sky-300 text-sm">
                            Click "+ New Transaction" to post your first entry
                        </p>
                    )}
                </div>
            )}

            {/* Transaction list */}
            {filtered.length > 0 && (
                <div className="space-y-3">
                    {filtered.map((transaction: Transaction) => (
                        <div
                            key={transaction.id}
                            className="bg-white rounded-2xl shadow-sky-sm p-5
                         hover:shadow-sky-md transition-shadow duration-200"
                        >
                            {/* Transaction header */}
                            <div className="flex items-center justify-between mb-3">
                                <div>
                                    <p className="font-semibold text-sky-900">
                                        {transaction.description}
                                    </p>
                                    <p className="text-sm text-sky-500">
                                        {transaction.reference} · {transaction.date}
                                    </p>
                                </div>
                                <span className="text-sm text-sky-400">
                  #{transaction.id}
                </span>
                            </div>

                            {/* Entries */}
                            <div className="border-t border-sky-50 pt-3 space-y-2">
                                {transaction.entries.map(entry => (
                                    <div
                                        key={entry.id}
                                        className="flex items-center justify-between
                               text-sm"
                                    >
                                        <div className="flex items-center gap-2">
                      <span className={`px-2 py-0.5 rounded text-xs
                                        font-semibold ${
                          entry.type === 'DEBIT'
                              ? 'bg-sky-100 text-sky-700'
                              : 'bg-amber-100 text-amber-700'
                      }`}>
                        {entry.type}
                      </span>
                                            <span className="text-sky-700">
                        {entry.accountName}
                      </span>
                                        </div>
                                        <span className="font-medium text-sky-900">
                      £{Number(entry.amount).toFixed(2)}
                    </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>
            )}

            {/* New Transaction Modal */}
            <NewTransactionModal
                isOpen={showModal}
                onClose={() => setShowModal(false)}
                onSuccess={refetch}
            />
        </AppLayout>
    )
}