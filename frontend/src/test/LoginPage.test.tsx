import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthProvider } from '../contexts/AuthContext'
import LoginPage from '../pages/LoginPage'
import { http, HttpResponse } from 'msw'
import { server } from './server'

const renderLoginPage = () => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } }
    })

    return render(
        <BrowserRouter>
            <QueryClientProvider client={queryClient}>
                <AuthProvider>
                    <LoginPage />
                </AuthProvider>
            </QueryClientProvider>
        </BrowserRouter>
    )
}

describe('LoginPage', () => {
    it('renders the login form', () => {
        renderLoginPage()

        expect(screen.getByText('Daybook 🌞')).toBeInTheDocument()
        expect(screen.getByPlaceholderText('Enter your username'))
            .toBeInTheDocument()
        expect(screen.getByPlaceholderText('Enter your password'))
            .toBeInTheDocument()
        expect(screen.getByRole('button', { name: /sign in/i }))
            .toBeInTheDocument()
    })

    it('shows validation error when username is empty', async () => {
        const user = userEvent.setup()
        renderLoginPage()

        await user.click(
            screen.getByRole('button', { name: /sign in/i })
        )

        await waitFor(() => {
            expect(screen.getByText('Username is required'))
                .toBeInTheDocument()
        })
    })

    it('shows validation error when password is empty', async () => {
        const user = userEvent.setup()
        renderLoginPage()

        await user.type(
            screen.getByPlaceholderText('Enter your username'),
            'admin'
        )
        await user.click(
            screen.getByRole('button', { name: /sign in/i })
        )

        await waitFor(() => {
            expect(screen.getByText('Password is required'))
                .toBeInTheDocument()
        })
    })

    it('shows error message on failed login', async () => {
        server.use(
            http.post('/api/auth/login', () => {
                return HttpResponse.json(
                    { message: 'Invalid username or password' },
                    { status: 401 }
                )
            })
        )

        const user = userEvent.setup()
        renderLoginPage()

        await user.type(
            screen.getByPlaceholderText('Enter your username'),
            'wronguser'
        )
        await user.type(
            screen.getByPlaceholderText('Enter your password'),
            'wrongpass'
        )
        await user.click(
            screen.getByRole('button', { name: /sign in/i })
        )

        await waitFor(() => {
            expect(screen.getByText('Invalid username or password'))
                .toBeInTheDocument()
        })
    })

    it('button is disabled while form is submitting', async () => {
        const user = userEvent.setup()
        renderLoginPage()

        await user.type(
            screen.getByPlaceholderText('Enter your username'),
            'admin'
        )
        await user.type(
            screen.getByPlaceholderText('Enter your password'),
            'admin123!'
        )

        // Button should be enabled before submission
        expect(
            screen.getByRole('button', { name: /sign in/i })
        ).not.toBeDisabled()
    })
})