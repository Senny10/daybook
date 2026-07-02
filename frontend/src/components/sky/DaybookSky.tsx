import { useState, type ReactNode } from 'react'
import Sun from '../sun/Sun'
import Cloud from './Cloud'

interface DaybookSkyProps {
    children: ReactNode
    variant?: 'hero' | 'app'
}

const STARS = [
    { top: 62, left: '26%', size: 9 },
    { top: 120, left: '68%', size: 7 },
    { top: 90, left: '87%', size: 11 },
    { top: 200, left: '50%', size: 7 },
]

export default function DaybookSky({ children, variant = 'hero' }: DaybookSkyProps) {
    const [mode, setMode] = useState<'day' | 'night'>('day')
    const isNight = mode === 'night'
    const isApp = variant === 'app'

    return (
        <div
            className="min-h-screen relative overflow-hidden"
            style={{
                background: isNight
                    ? 'radial-gradient(125% 120% at 82% 10%, #38476D 0%, #283651 55%, #1E2B45 100%)'
                    : 'radial-gradient(125% 120% at 18% 12%, #93D0E8 0%, #6FBDDC 56%, #5DAFD3 100%)',
                transition: 'background 0.9s ease',
            }}
        >
            {STARS.map((star, i) => (
                <div
                    key={i}
                    className="absolute z-[1]"
                    style={{
                        top: star.top,
                        left: star.left,
                        width: star.size,
                        height: star.size,
                        background: '#f4ecd2',
                        borderRadius: 2,
                        transform: 'rotate(45deg)',
                        opacity: isNight ? 1 : 0,
                        transition: 'opacity .9s ease',
                        boxShadow: '1px 2px 3px rgba(0,0,0,.25)',
                    }}
                />
            ))}

            <div className="absolute top-16 left-10 z-[2]">
                <Sun size={140} mode={mode} phase={0.25} />
            </div>

            {isApp ? (
                <>
                    <div className="absolute left-[8%] bottom-56 z-[2]">
                        <Cloud w={120} mode={mode} dur={34} range={24} delay={0} />
                    </div>
                    <div className="absolute left-[55%] bottom-64 z-[2]">
                        <Cloud w={100} mode={mode} dur={28} range={20} delay={-12} />
                    </div>
                    <div className="absolute left-[80%] bottom-48 z-[2]">
                        <Cloud w={140} mode={mode} dur={38} range={26} delay={-6} />
                    </div>
                </>
            ) : (
                <>
                    <div className="absolute left-[10%] bottom-64 z-[2]">
                        <Cloud w={130} mode={mode} dur={24} range={28} delay={-6} />
                    </div>
                    <div className="absolute left-[70%] bottom-72 z-[2]">
                        <Cloud w={110} mode={mode} dur={20} range={24} delay={-16} />
                    </div>
                    <div className="absolute left-[88%] bottom-56 z-[2]">
                        <Cloud w={170} mode={mode} dur={30} range={32} delay={-2} />
                    </div>
                    <div className="absolute left-[42%] bottom-72 z-[2]">
                        <Cloud w={150} mode={mode} dur={26} range={40} delay={-14} />
                    </div>
                    <div className="absolute left-[4%] bottom-32 z-[2]">
                        <Cloud w={160} mode={mode} dur={26} range={30} delay={0} />
                    </div>
                    <div className="absolute left-[80%] bottom-24 z-[2]">
                        <Cloud w={190} mode={mode} dur={32} range={36} delay={-20} />
                    </div>
                    <div className="absolute left-[20%] -bottom-8 z-[2]">
                        <Cloud w={260} mode={mode} dur={40} range={44} delay={-8} />
                    </div>
                    <div className="absolute left-[62%] bottom-4 z-[2]">
                        <Cloud w={300} mode={mode} dur={46} range={50} delay={-4} />
                    </div>
                </>
            )}

            <div className={isApp ? 'relative z-[3]' : 'relative z-[3] min-h-screen flex items-center justify-center'}>
                {children}
            </div>

            <button
                type="button"
                onClick={() => setMode((m) => (m === 'day' ? 'night' : 'day'))}
                className="absolute top-6 right-6 z-[10] font-semibold text-sm text-sky-900
       bg-white rounded-full px-4 py-2 shadow-sky-lg"
            >
                {mode === 'day' ? '☾ Night' : '☀ Day'}
            </button>
        </div>
    )
}