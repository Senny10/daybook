import { useNavigate } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'

export default function Header() {
    const { user, logout, isAdmin } = useAuth()
    const navigate = useNavigate()

    const handleLogout = () => {
        logout()
        navigate('/login')
    }

    return (
        <header className="bg-white border-b border-sky-100 shadow-sky-sm">
            <div className="max-w-6xl mx-auto px-4 py-3 flex items-center
                      justify-between">

                {/* Logo */}
                <div
                    className="flex items-center gap-2 cursor-pointer"
                    onClick={() => navigate('/accounts')}
                >
                    <span className="text-xl">🌞</span>
                    <span className="text-xl font-bold text-sky-900">Daybook</span>
                </div>

                {/* Navigation */}
                <nav className="flex items-center gap-6">
                    <button
                        onClick={() => navigate('/accounts')}
                        className="text-sky-700 hover:text-sky-900 font-medium
                       transition-colors duration-200"
                    >
                        Accounts
                    </button>
                    <button
                        onClick={() => navigate('/transactions')}
                        className="text-sky-700 hover:text-sky-900 font-medium
                       transition-colors duration-200"
                    >
                        Transactions
                    </button>
                    <button
                        onClick={() => navigate('/reports')}
                        className="text-sky-700 hover:text-sky-900 font-medium
                       transition-colors duration-200"
                    >
                        Reports
                    </button>
                    {isAdmin && (
                        <button
                            onClick={() => navigate('/transactions/new')}
                            className="text-sky-700 hover:text-sky-900 font-medium
                         transition-colors duration-200"
                        >
                            New Transaction
                        </button>
                    )}
                </nav>

                {/* User info + logout */}
                <div className="flex items-center gap-3">
                    <div className="text-right">
                        <p className="text-sm font-medium text-sky-900">
                            {user?.username}
                        </p>
                        <p className="text-xs text-sky-500">{user?.role}</p>
                    </div>
                    <button
                        onClick={handleLogout}
                        className="px-3 py-1.5 text-sm border border-sky-200
                       rounded-lg text-sky-700 hover:bg-sky-50
                       transition-colors duration-200"
                    >
                        Sign out
                    </button>
                </div>

            </div>
        </header>
    )
}