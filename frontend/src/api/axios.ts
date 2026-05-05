import axios from 'axios'

const apiClient = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json',
    },
})

// Request interceptor — add JWT to every request
apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('daybook_token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// Response interceptor — handle 401 globally
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            localStorage.removeItem('daybook_token')
            localStorage.removeItem('daybook_user')
            window.location.href = '/login'
        }
        return Promise.reject(error)
    }
)

export default apiClient