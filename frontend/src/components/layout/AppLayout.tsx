import type { ReactNode } from 'react'
import Header from './Header'

export default function AppLayout({ children }: { children: ReactNode }) {
    return (
        <div className="min-h-screen bg-gradient-to-b from-sky-50 to-sky-100">
            <Header />
            <main className="max-w-6xl mx-auto px-4 py-8">
                {children}
            </main>
        </div>
    )
}