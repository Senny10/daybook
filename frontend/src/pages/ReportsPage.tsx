import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import AppLayout from '../components/layout/AppLayout'
import {
    getTrialBalance,
    getProfitAndLoss,
    getBalanceSheet,
} from '../api/reports'

type Tab = 'trial-balance' | 'profit-and-loss' | 'balance-sheet'

export default function ReportsPage() {
    const [activeTab, setActiveTab] = useState<Tab>('trial-balance')

    const { data: trialBalance, isLoading: tbLoading } = useQuery({
        queryKey: ['trial-balance'],
        queryFn: getTrialBalance,
    })

    const { data: pandl, isLoading: plLoading } = useQuery({
        queryKey: ['profit-and-loss'],
        queryFn: getProfitAndLoss,
    })

    const { data: balanceSheet, isLoading: bsLoading } = useQuery({
        queryKey: ['balance-sheet'],
        queryFn: getBalanceSheet,
    })

    const tabs: { id: Tab; label: string }[] = [
        { id: 'trial-balance', label: 'Trial Balance' },
        { id: 'profit-and-loss', label: 'Profit & Loss' },
        { id: 'balance-sheet', label: 'Balance Sheet' },
    ]

    return (
        <AppLayout>
            <div className="mb-6">
                <h1 className="text-2xl font-bold text-sky-900">Reports</h1>
                <p className="text-sky-600 text-sm mt-1">
                    Financial statements for Daybook
                </p>
            </div>

            {/* Tabs */}
            <div className="flex gap-2 mb-6">
                {tabs.map(tab => (
                    <button
                        key={tab.id}
                        onClick={() => setActiveTab(tab.id)}
                        className={`px-4 py-2 rounded-xl text-sm font-medium
                        transition-colors duration-200 ${
                            activeTab === tab.id
                                ? 'bg-sky-900 text-white'
                                : 'bg-white text-sky-700 hover:bg-sky-50 border border-sky-200'
                        }`}
                    >
                        {tab.label}
                    </button>
                ))}
            </div>

            {/* Trial Balance */}
            {activeTab === 'trial-balance' && (
                <div className="bg-white rounded-2xl shadow-sky-md p-6">
                    <div className="flex items-center justify-between mb-4">
                        <h2 className="text-lg font-semibold text-sky-900">
                            Trial Balance
                        </h2>
                        {trialBalance && (
                            <span className={`px-3 py-1 rounded-full text-sm 
                                font-medium ${
                                trialBalance.isBalanced
                                    ? 'bg-green-100 text-green-700'
                                    : 'bg-red-100 text-red-600'
                            }`}>
                {trialBalance.isBalanced ? '✓ Balanced' : '✗ Unbalanced'}
              </span>
                        )}
                    </div>

                    {tbLoading && (
                        <div className="animate-pulse space-y-3">
                            {[1, 2, 3].map(i => (
                                <div key={i} className="flex justify-between py-2">
                                    <div className="w-32 h-4 bg-sky-100 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                </div>
                            ))}
                        </div>
                    )}

                    {trialBalance && (
                        <>
                            <table className="w-full text-sm">
                                <thead>
                                <tr className="border-b border-sky-100">
                                    <th className="text-left py-2 text-sky-600
                                   font-medium">Account</th>
                                    <th className="text-left py-2 text-sky-600
                                   font-medium">Type</th>
                                    <th className="text-right py-2 text-sky-600
                                   font-medium">Debit</th>
                                    <th className="text-right py-2 text-sky-600
                                   font-medium">Credit</th>
                                </tr>
                                </thead>
                                <tbody>
                                {trialBalance.entries.map(entry => (
                                    <tr key={entry.accountId}
                                        className="border-b border-sky-50">
                                        <td className="py-2 text-sky-900 font-medium">
                                            {entry.accountName}
                                        </td>
                                        <td className="py-2 text-sky-500 text-xs">
                                            {entry.accountType}
                                        </td>
                                        <td className="py-2 text-right text-sky-900">
                                            {entry.debitBalance > 0
                                                ? `£${Number(entry.debitBalance).toFixed(2)}`
                                                : '—'}
                                        </td>
                                        <td className="py-2 text-right text-sky-900">
                                            {entry.creditBalance > 0
                                                ? `£${Number(entry.creditBalance).toFixed(2)}`
                                                : '—'}
                                        </td>
                                    </tr>
                                ))}
                                </tbody>
                                <tfoot>
                                <tr className="border-t-2 border-sky-200 font-bold">
                                    <td className="py-2 text-sky-900" colSpan={2}>
                                        Total
                                    </td>
                                    <td className="py-2 text-right text-sky-900">
                                        £{Number(trialBalance.totalDebits).toFixed(2)}
                                    </td>
                                    <td className="py-2 text-right text-sky-900">
                                        £{Number(trialBalance.totalCredits).toFixed(2)}
                                    </td>
                                </tr>
                                </tfoot>
                            </table>
                        </>
                    )}
                </div>
            )}

            {/* Profit & Loss */}
            {activeTab === 'profit-and-loss' && (
                <div className="bg-white rounded-2xl shadow-sky-md p-6">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-lg font-semibold text-sky-900">
                            Profit & Loss
                        </h2>
                        {pandl && (
                            <span className={`px-3 py-1 rounded-full text-sm 
                                font-medium ${
                                pandl.isProfit
                                    ? 'bg-green-100 text-green-700'
                                    : 'bg-red-100 text-red-600'
                            }`}>
                {pandl.isProfit ? '▲ Profit' : '▼ Loss'}
              </span>
                        )}
                    </div>

                    {plLoading && (
                        <div className="animate-pulse space-y-3">
                            {[1, 2, 3].map(i => (
                                <div key={i} className="flex justify-between py-2">
                                    <div className="w-32 h-4 bg-sky-100 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                </div>
                            ))}
                        </div>
                    )}

                    {pandl && (
                        <div className="space-y-4">
                            <div className="flex justify-between py-3 border-b
                              border-sky-100">
                                <span className="text-sky-700">Total Revenue</span>
                                <span className="font-semibold text-green-600">
                  £{Number(pandl.totalRevenue).toFixed(2)}
                </span>
                            </div>
                            <div className="flex justify-between py-3 border-b
                              border-sky-100">
                                <span className="text-sky-700">Total Expenses</span>
                                <span className="font-semibold text-red-500">
                  £{Number(pandl.totalExpenses).toFixed(2)}
                </span>
                            </div>
                            <div className="flex justify-between py-3 bg-sky-50
                              rounded-xl px-4">
                                <span className="font-bold text-sky-900">Net Income</span>
                                <span className={`font-bold text-lg ${
                                    pandl.isProfit ? 'text-green-600' : 'text-red-500'
                                }`}>
                  £{Number(pandl.netIncome).toFixed(2)}
                </span>
                            </div>
                        </div>
                    )}
                </div>
            )}

            {/* Balance Sheet */}
            {activeTab === 'balance-sheet' && (
                <div className="bg-white rounded-2xl shadow-sky-md p-6">
                    <div className="flex items-center justify-between mb-6">
                        <h2 className="text-lg font-semibold text-sky-900">
                            Balance Sheet
                        </h2>
                        {balanceSheet && (
                            <span className={`px-3 py-1 rounded-full text-sm 
                                font-medium ${
                                balanceSheet.isBalanced
                                    ? 'bg-green-100 text-green-700'
                                    : 'bg-amber-100 text-amber-700'
                            }`}>
                {balanceSheet.isBalanced
                    ? '✓ Balanced'
                    : '⚠ Incomplete data'}
              </span>
                        )}
                    </div>

                    {bsLoading && (
                        <div className="animate-pulse space-y-3">
                            {[1, 2, 3].map(i => (
                                <div key={i} className="flex justify-between py-2">
                                    <div className="w-32 h-4 bg-sky-100 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                    <div className="w-16 h-4 bg-sky-50 rounded"/>
                                </div>
                            ))}
                        </div>
                    )}

                    {balanceSheet && (
                        <div className="space-y-4">
                            <div className="flex justify-between py-3 border-b
                              border-sky-100">
                                <span className="text-sky-700">Total Assets</span>
                                <span className="font-semibold text-sky-900">
                  £{Number(balanceSheet.totalAssets).toFixed(2)}
                </span>
                            </div>
                            <div className="flex justify-between py-3 border-b
                              border-sky-100">
                                <span className="text-sky-700">Total Liabilities</span>
                                <span className="font-semibold text-sky-900">
                  £{Number(balanceSheet.totalLiabilities).toFixed(2)}
                </span>
                            </div>
                            <div className="flex justify-between py-3 border-b
                              border-sky-100">
                                <span className="text-sky-700">Total Equity</span>
                                <span className="font-semibold text-sky-900">
                  £{Number(balanceSheet.totalEquity).toFixed(2)}
                </span>
                            </div>
                            <div className="flex justify-between py-3 bg-sky-50
                              rounded-xl px-4">
                <span className="font-bold text-sky-900">
                  Liabilities + Equity
                </span>
                                <span className="font-bold text-sky-900">
                  £{(Number(balanceSheet.totalLiabilities) +
                                    Number(balanceSheet.totalEquity)).toFixed(2)}
                </span>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </AppLayout>
    )
}