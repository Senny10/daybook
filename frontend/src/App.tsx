import { Routes, Route, Navigate } from 'react-router-dom'
import { useAuth } from './contexts/AuthContext'
import ProtectedRoute from './components/layout/ProtectedRoute'
import LoginPage from './pages/LoginPage'
import AccountsPage from './pages/AccountsPage'
import TransactionsPage from './pages/TransactionsPage'
import ReportsPage from './pages/ReportsPage'

function App() {
    const { isAuthenticated } = useAuth()

    return (
        <Routes>
            {/* Public */}
            <Route
                path="/login"
                element={
                    isAuthenticated
                        ? <Navigate to="/accounts" replace />
                        : <LoginPage />
                }
            />

            {/* Protected */}
            <Route path="/accounts" element={
                <ProtectedRoute><AccountsPage /></ProtectedRoute>
            }/>
            <Route path="/transactions" element={
                <ProtectedRoute><TransactionsPage /></ProtectedRoute>
            }/>
            <Route path="/reports" element={
                <ProtectedRoute><ReportsPage /></ProtectedRoute>
            }/>

            {/* Default */}
            <Route path="/" element={<Navigate to="/accounts" replace />}/>
            <Route path="*" element={<Navigate to="/accounts" replace />}/>
        </Routes>
    )
}

export default App