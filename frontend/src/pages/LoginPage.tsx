import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { useAuth } from '../contexts/AuthContext'
import { login as loginApi } from '../api/auth'

const loginSchema = z.object({
    username: z.string().min(1, 'Username is required'),
    password: z.string().min(1, 'Password is required'),
})

type LoginForm = z.infer<typeof loginSchema>

export default function LoginPage() {
    const navigate = useNavigate()
    const { login } = useAuth()
    const [serverError, setServerError] = useState<string | null>(null)
    const [isLoading, setIsLoading] = useState(false)

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<LoginForm>({
        resolver: zodResolver(loginSchema),
    })

    const onSubmit = async (data: LoginForm) => {
        setIsLoading(true)
        setServerError(null)

        try {
            const response = await loginApi(data)
            login(response.token, {
                username: response.username,
                role: response.role,
            })
            navigate('/accounts')
        } catch (error: any) {
            setServerError(
                error.response?.data?.message
                ?? 'Invalid username or password'
            )
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <div className="min-h-screen bg-gradient-to-b from-sky-50 to-sky-100
                    flex items-center justify-center p-4">
            <div className="w-full max-w-md">

                {/* Header */}
                <div className="text-center mb-8">
                    <h1 className="text-4xl font-bold text-sky-900 mb-2">
                        Daybook 🌞
                    </h1>
                    <p className="text-sky-600">
                        Double-entry bookkeeping, brought to light.
                    </p>
                </div>

                {/* Card */}
                <div className="bg-white rounded-2xl shadow-sky-lg p-8">
                    <h2 className="text-xl font-semibold text-sky-900 mb-6">
                        Sign in to your account
                    </h2>

                    {/* Server Error */}
                    {serverError && (
                        <div className="mb-4 p-3 bg-red-50 border border-coral-400
                            rounded-lg text-coral-500 text-sm">
                            {serverError}
                        </div>
                    )}

                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

                        {/* Username */}
                        <div>
                            <label className="block text-sm font-medium text-sky-800 mb-1">
                                Username
                            </label>
                            <input
                                {...register('username')}
                                type="text"
                                placeholder="Enter your username"
                                className="w-full px-4 py-2.5 rounded-xl border border-sky-200
                           focus:outline-none focus:ring-2 focus:ring-sky-300
                           text-sky-900 placeholder-sky-300
                           disabled:opacity-50"
                                disabled={isLoading}
                            />
                            {errors.username && (
                                <p className="mt-1 text-sm text-coral-400">
                                    {errors.username.message}
                                </p>
                            )}
                        </div>

                        {/* Password */}
                        <div>
                            <label className="block text-sm font-medium text-sky-800 mb-1">
                                Password
                            </label>
                            <input
                                {...register('password')}
                                type="password"
                                placeholder="Enter your password"
                                className="w-full px-4 py-2.5 rounded-xl border border-sky-200
                           focus:outline-none focus:ring-2 focus:ring-sky-300
                           text-sky-900 placeholder-sky-300
                           disabled:opacity-50"
                                disabled={isLoading}
                            />
                            {errors.password && (
                                <p className="mt-1 text-sm text-coral-400">
                                    {errors.password.message}
                                </p>
                            )}
                        </div>

                        {/* Submit */}
                        <button
                            type="submit"
                            disabled={isLoading}
                            className="w-full py-2.5 px-4 bg-amber-400 hover:bg-amber-500
           text-white font-semibold rounded-xl
           transition-colors duration-200
           disabled:opacity-50 disabled:cursor-not-allowed
           focus:outline-none focus:ring-2 focus:ring-amber-300">
                            {isLoading ? 'Signing in...' : 'Sign in'}
                        </button>

                    </form>
                </div>

                {/* Register link — shown conditionally later */}
                <p className="text-center mt-4 text-sky-600 text-sm">
                    Need an account?{' '}
                    <a href="/register"
                       className="font-medium text-sky-800 hover:text-sky-900">
                        Register
                    </a>
                </p>

            </div>
        </div>
    )
}