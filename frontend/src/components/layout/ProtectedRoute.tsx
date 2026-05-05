import { Navigate, useLocation } from 'react-router-dom'
import type { ReactNode } from 'react'
import { useAuth } from '../../contexts/AuthContext'

interface ProtectedRouteProps {
    children: ReactNode
    requireAdmin?: boolean
}

export default function ProtectedRoute({
                                           children,
                                           requireAdmin = false
                                       }: ProtectedRouteProps) {
    const { isAuthenticated, isAdmin } = useAuth()
    const location = useLocation()

    // Not logged in → redirect to login, remember where they were going
    if (!isAuthenticated) {
        return (
            <Navigate
                to="/login"
                state={{ from: location }}
                replace
            />
        )
    }

    // Logged in but wrong role → redirect to accounts
    if (requireAdmin && !isAdmin) {
        return <Navigate to="/accounts" replace />
    }

    return <>{children}</>
}