import type { ReactNode } from 'react'
import Header from './Header'

export default function AppLayout({ children }: { children: ReactNode }) {
    return (
        <div
            className="min-h-screen"
            style={{
                backgroundImage: 'url(/login-bg.png)',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                backgroundAttachment: 'fixed',
            }}
        >
            <Header />
            <main className="max-w-6xl mx-auto px-4 py-8">
                {children}
            </main>
        </div>
    )
}