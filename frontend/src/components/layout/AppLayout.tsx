import type { ReactNode } from 'react'
import DaybookSky from '../sky/DaybookSky'
import Header from './Header'

export default function AppLayout({ children }: { children: ReactNode }) {
    return (
        <DaybookSky variant="app">
            <Header />
            <main className="max-w-3xl mx-auto px-4 py-8">
                {children}
            </main>
        </DaybookSky>
    )
}