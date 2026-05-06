import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import NewTransactionModal from '../components/transactions/NewTransactionModal'

const renderModal = (isOpen = true) => {
    const queryClient = new QueryClient({
        defaultOptions: { queries: { retry: false } }
    })

    return render(
        <QueryClientProvider client={queryClient}>
            <NewTransactionModal
                isOpen={isOpen}
                onClose={vi.fn()}
                onSuccess={vi.fn()}
            />
        </QueryClientProvider>
    )
}

describe('NewTransactionModal', () => {
    it('renders with two entry rows by default', async () => {
        renderModal()

        await waitFor(() => {
            const selects = screen.getAllByDisplayValue('Select account')
            expect(selects).toHaveLength(2)
        })
    })

    it('Post Transaction button is disabled when not balanced', async () => {
        renderModal()

        await waitFor(() => {
            expect(
                screen.getByRole('button', { name: /post transaction/i })
            ).toBeDisabled()
        })
    })

    it('adds a new entry row when + Add entry is clicked', async () => {
        const user = userEvent.setup()
        renderModal()

        await waitFor(() => {
            expect(screen.getAllByDisplayValue('Select account'))
                .toHaveLength(2)
        })

        await user.click(screen.getByText('+ Add entry'))

        await waitFor(() => {
            expect(screen.getAllByDisplayValue('Select account'))
                .toHaveLength(3)
        })
    })

    it('shows difference when entries are unbalanced', async () => {
        const user = userEvent.setup()
        renderModal()

        await waitFor(() => {
            expect(screen.getAllByDisplayValue('Select account'))
                .toHaveLength(2)
        })

        const amountInputs = screen.getAllByPlaceholderText('0.00')
        await user.type(amountInputs[0], '500')

        await waitFor(() => {
            expect(screen.getByText(/Difference: £/))
                .toBeInTheDocument()
        })
    })

    it('shows balanced indicator when debits equal credits', async () => {
        const user = userEvent.setup()
        renderModal()

        await waitFor(() => {
            expect(screen.getAllByDisplayValue('Select account'))
                .toHaveLength(2)
        })

        const amountInputs = screen.getAllByPlaceholderText('0.00')
        await user.type(amountInputs[0], '500')
        await user.type(amountInputs[1], '500')

        await waitFor(() => {
            expect(screen.getByText('✓ Balanced')).toBeInTheDocument()
        })
    })

    it('enables Post Transaction when balanced', async () => {
        const user = userEvent.setup()
        renderModal()

        await waitFor(() => {
            expect(screen.getAllByDisplayValue('Select account'))
                .toHaveLength(2)
        })

        const amountInputs = screen.getAllByPlaceholderText('0.00')
        await user.type(amountInputs[0], '500')
        await user.type(amountInputs[1], '500')

        await waitFor(() => {
            expect(
                screen.getByRole('button', { name: /post transaction/i })
            ).not.toBeDisabled()
        })
    })
})