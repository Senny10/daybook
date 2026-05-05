import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './contexts/AuthContext'
import ProtectedRoute from './components/layout/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import AccountsPage from './pages/AccountsPage'

function App() {
    const { isAuthenticated } = useAuth()

    return (
        <Routes>
            {/* Public routes */}
            <Route
                path="/login"
                element={
                    isAuthenticated
                        ? <Navigate to="/accounts" replace />
                        : <LoginPage />
                }
            />

            {/* Protected routes */}
            <Route
                path="/accounts"
                element={
                    <ProtectedRoute>
                        <AccountsPage />
                    </ProtectedRoute>
                }
            />

            {/* Default redirect */}
            <Route
                path="/"
                element={<Navigate to="/accounts" replace />}
            />
            <Route
                path="*"
                element={<Navigate to="/accounts" replace />}
            />
        </Routes>
    )
}

export default App