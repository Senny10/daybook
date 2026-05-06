import { useState, useEffect } from 'react'
import { useQuery } from '@tanstack/react-query'
import { getAccounts } from '../../api/accounts'
import { createTransaction } from '../../api/transactions'
import type { Account } from '../../types'

interface Entry {
    accountId: number | ''
    amount: string
    type: 'DEBIT' | 'CREDIT'
}

interface Props {
    isOpen: boolean
    onClose: () => void
    onSuccess: () => void
}

const emptyEntry = (): Entry => ({
    accountId: '',
    amount: '',
    type: 'DEBIT'
})

export default function NewTransactionModal({
                                                isOpen, onClose, onSuccess
                                            }: Props) {
    const [date, setDate] = useState(
        new Date().toISOString().split('T')[0]
    )
    const [description, setDescription] = useState('')
    const [reference, setReference] = useState('')
    const [entries, setEntries] = useState<Entry[]>([
        { accountId: '', amount: '', type: 'DEBIT' },
        { accountId: '', amount: '', type: 'CREDIT' }
    ])
    const [error, setError] = useState<string | null>(null)
    const [isSubmitting, setIsSubmitting] = useState(false)

    const { data: accounts = [] } = useQuery({
        queryKey: ['accounts'],
        queryFn: getAccounts,
        enabled: isOpen,
    })

    // Reset form when modal opens
    useEffect(() => {
        if (isOpen) {
            setDate(new Date().toISOString().split('T')[0])
            setDescription('')
            setReference('')
            setEntries([
                { accountId: '', amount: '', type: 'DEBIT' },
                { accountId: '', amount: '', type: 'CREDIT' }
            ])
            setError(null)
        }
    }, [isOpen])

    // Calculate balance
    const totalDebits = entries
        .filter(e => e.type === 'DEBIT')
        .reduce((sum, e) => sum + (parseFloat(e.amount) || 0), 0)

    const totalCredits = entries
        .filter(e => e.type === 'CREDIT')
        .reduce((sum, e) => sum + (parseFloat(e.amount) || 0), 0)

    const difference = Math.abs(totalDebits - totalCredits)
    const isBalanced = difference === 0 && totalDebits > 0

    const updateEntry = (
        index: number,
        field: keyof Entry,
        value: string | number
    ) => {
        setEntries(prev => prev.map((entry, i) =>
            i === index ? { ...entry, [field]: value } : entry
        ))
    }

    const addEntry = () => {
        setEntries(prev => [...prev, emptyEntry()])
    }

    const removeEntry = (index: number) => {
        if (entries.length <= 2) return
        setEntries(prev => prev.filter((_, i) => i !== index))
    }

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()
        if (!isBalanced) return

        setIsSubmitting(true)
        setError(null)

        try {
            await createTransaction({
                date,
                description,
                reference,
                entries: entries.map(e => ({
                    accountId: Number(e.accountId),
                    amount: parseFloat(e.amount),
                    type: e.type
                }))
            })
            onSuccess()
            onClose()
        } catch (err: any) {
            setError(
                err.response?.data?.message ?? 'Failed to create transaction'
            )
        } finally {
            setIsSubmitting(false)
        }
    }

    if (!isOpen) return null

    return (
        <div className="fixed inset-0 bg-sky-900/50 backdrop-blur-sm
                    flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-2xl shadow-sky-lg w-full
                      max-w-2xl max-h-[90vh] overflow-y-auto">

                {/* Modal Header */}
                <div className="flex items-center justify-between p-6
                        border-b border-sky-100">
                    <h2 className="text-xl font-bold text-sky-900">
                        New Transaction
                    </h2>
                    <button
                        onClick={onClose}
                        className="text-sky-400 hover:text-sky-600
                       text-2xl leading-none"
                    >
                        ×
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="p-6 space-y-5">

                    {/* Error */}
                    {error && (
                        <div className="p-3 bg-red-50 border border-red-200
                            rounded-lg text-red-600 text-sm">
                            {error}
                        </div>
                    )}

                    {/* Transaction Details */}
                    <div className="grid grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium
                                text-sky-800 mb-1">
                                Date
                            </label>
                            <input
                                type="date"
                                value={date}
                                onChange={e => setDate(e.target.value)}
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
                                Reference
                            </label>
                            <input
                                type="text"
                                value={reference}
                                onChange={e => setReference(e.target.value)}
                                placeholder="e.g. INV-001"
                                className="w-full px-4 py-2.5 rounded-xl border
                           border-sky-200 focus:outline-none
                           focus:ring-2 focus:ring-sky-300
                           text-sky-900"
                                required
                            />
                        </div>
                    </div>

                    <div>
                        <label className="block text-sm font-medium
                              text-sky-800 mb-1">
                            Description
                        </label>
                        <input
                            type="text"
                            value={description}
                            onChange={e => setDescription(e.target.value)}
                            placeholder="e.g. Received payment for services"
                            className="w-full px-4 py-2.5 rounded-xl border
                         border-sky-200 focus:outline-none
                         focus:ring-2 focus:ring-sky-300
                         text-sky-900"
                            required
                        />
                    </div>

                    {/* Entries */}
                    <div>
                        <div className="flex items-center justify-between mb-3">
                            <label className="text-sm font-medium text-sky-800">
                                Entries
                            </label>
                            {/* Balance Indicator */}
                            {totalDebits > 0 || totalCredits > 0 ? (
                                <div className={`text-sm font-medium px-3 py-1 
                                 rounded-full ${
                                    isBalanced
                                        ? 'bg-green-100 text-green-700'
                                        : 'bg-red-100 text-red-600'
                                }`}>
                                    {isBalanced
                                        ? '✓ Balanced'
                                        : `Difference: £${difference.toFixed(2)}`
                                    }
                                </div>
                            ) : null}
                        </div>

                        {/* Entry rows */}
                        <div className="space-y-3">
                            {entries.map((entry, index) => (
                                <div key={index}
                                     className="grid grid-cols-12 gap-2 items-center">

                                    {/* Account selector */}
                                    <div className="col-span-5">
                                        <select
                                            value={entry.accountId}
                                            onChange={e => updateEntry(
                                                index, 'accountId', e.target.value
                                            )}
                                            className="w-full px-3 py-2 rounded-xl border
                                 border-sky-200 focus:outline-none
                                 focus:ring-2 focus:ring-sky-300
                                 text-sky-900 bg-white text-sm"
                                            required
                                        >
                                            <option value="">Select account</option>
                                            {accounts.map((account: Account) => (
                                                <option key={account.id} value={account.id}>
                                                    {account.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>

                                    {/* Amount */}
                                    <div className="col-span-3">
                                        <input
                                            type="number"
                                            step="0.01"
                                            min="0.01"
                                            value={entry.amount}
                                            onChange={e => updateEntry(
                                                index, 'amount', e.target.value
                                            )}
                                            placeholder="0.00"
                                            className="w-full px-3 py-2 rounded-xl border
                                 border-sky-200 focus:outline-none
                                 focus:ring-2 focus:ring-sky-300
                                 text-sky-900 text-sm"
                                            required
                                        />
                                    </div>

                                    {/* DEBIT/CREDIT toggle */}
                                    <div className="col-span-3">
                                        <select
                                            value={entry.type}
                                            onChange={e => updateEntry(
                                                index, 'type',
                                                e.target.value as 'DEBIT' | 'CREDIT'
                                            )}
                                            className="w-full px-3 py-2 rounded-xl border
                                 border-sky-200 focus:outline-none
                                 focus:ring-2 focus:ring-sky-300
                                 text-sky-900 bg-white text-sm
                                 font-medium"
                                        >
                                            <option value="DEBIT">DEBIT</option>
                                            <option value="CREDIT">CREDIT</option>
                                        </select>
                                    </div>

                                    {/* Remove button */}
                                    <div className="col-span-1 flex justify-center">
                                        <button
                                            type="button"
                                            onClick={() => removeEntry(index)}
                                            disabled={entries.length <= 2}
                                            className="text-sky-300 hover:text-red-400
                                 disabled:opacity-20 disabled:cursor-not-allowed
                                 text-xl leading-none"
                                        >
                                            ×
                                        </button>
                                    </div>
                                </div>
                            ))}
                        </div>

                        {/* Add entry button */}
                        <button
                            type="button"
                            onClick={addEntry}
                            className="mt-3 text-sm text-sky-500 hover:text-sky-700
                         font-medium flex items-center gap-1"
                        >
                            + Add entry
                        </button>
                    </div>

                    {/* Running totals */}
                    <div className="bg-sky-50 rounded-xl p-4 grid grid-cols-2 gap-4">
                        <div className="text-center">
                            <p className="text-xs text-sky-500 mb-1">Total Debits</p>
                            <p className="text-lg font-bold text-sky-900">
                                £{totalDebits.toFixed(2)}
                            </p>
                        </div>
                        <div className="text-center">
                            <p className="text-xs text-sky-500 mb-1">Total Credits</p>
                            <p className="text-lg font-bold text-sky-900">
                                £{totalCredits.toFixed(2)}
                            </p>
                        </div>
                    </div>

                    {/* Submit */}
                    <div className="flex gap-3 pt-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 py-2.5 border border-sky-200
                         text-sky-700 rounded-xl hover:bg-sky-50
                         transition-colors duration-200"
                        >
                            Cancel
                        </button>
                        <button
                            type="submit"
                            disabled={!isBalanced || isSubmitting}
                            className="flex-1 py-2.5 bg-amber-400 hover:bg-amber-500
                         text-white font-semibold rounded-xl
                         transition-colors duration-200
                         disabled:opacity-50 disabled:cursor-not-allowed"
                        >
                            {isSubmitting ? 'Posting...' : 'Post Transaction'}
                        </button>
                    </div>

                </form>
            </div>
        </div>
    )
}