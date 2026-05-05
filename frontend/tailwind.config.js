/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                sky: {
                    50:  '#EBF4FA',
                    100: '#D6EAF8',
                    200: '#ADD5F0',
                    300: '#84C0E8',
                    400: '#5BABE0',
                    500: '#3296D8',
                    600: '#2478AD',
                    700: '#1A5A82',
                    800: '#113C57',
                    900: '#1E3A5F',
                },
                sun: {
                    300: '#FDE68A',
                    400: '#FCD34D',
                    500: '#FDB813',
                    600: '#F5A623',
                },
                coral: {
                    400: '#EF6B6B',
                    500: '#E85555',
                },
            },
            fontFamily: {
                sans: ['Inter', 'system-ui', 'sans-serif'],
            },
            boxShadow: {
                'sky-sm': '0 1px 3px rgba(50, 150, 216, 0.15)',
                'sky-md': '0 4px 12px rgba(50, 150, 216, 0.2)',
                'sky-lg': '0 8px 24px rgba(50, 150, 216, 0.25)',
            },
        },
    },
    plugins: [],
}