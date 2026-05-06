import { http, HttpResponse } from 'msw'

export const handlers = [
    // Auth
    http.post('/api/auth/login', () => {
        return HttpResponse.json({
            token: 'test.jwt.token',
            username: 'admin',
            role: 'ADMIN',
        })
    }),

    http.post('/api/auth/login-fail', () => {
        return HttpResponse.json(
            { message: 'Invalid username or password' },
            { status: 401 }
        )
    }),

    // Accounts
    http.get('/api/accounts', () => {
        return HttpResponse.json([
            {
                id: 1,
                name: 'Cash',
                type: 'ASSET',
                description: 'Main cash account',
            },
            {
                id: 2,
                name: 'Service Revenue',
                type: 'REVENUE',
                description: 'Income from services',
            },
        ])
    }),

    http.post('/api/accounts', () => {
        return HttpResponse.json({
            id: 3,
            name: 'New Account',
            type: 'ASSET',
            description: '',
        }, { status: 201 })
    }),

    // Transactions
    http.get('/api/transactions', () => {
        return HttpResponse.json([
            {
                id: 1,
                date: '2026-04-23',
                description: 'Received payment for services',
                reference: 'TXN-001',
                entries: [
                    {
                        id: 1,
                        accountId: 1,
                        accountName: 'Cash',
                        amount: 500.00,
                        type: 'DEBIT',
                    },
                    {
                        id: 2,
                        accountId: 2,
                        accountName: 'Service Revenue',
                        amount: 500.00,
                        type: 'CREDIT',
                    },
                ],
            },
        ])
    }),

    // Config
    http.get('/api/config', () => {
        return HttpResponse.json({
            publicRegistrationEnabled: true,
            version: '1.0.0',
        })
    }),
]