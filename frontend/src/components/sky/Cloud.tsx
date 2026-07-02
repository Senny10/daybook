interface CloudProps {
    w?: number
    mode?: 'day' | 'night'
    dur?: number
    range?: number
    delay?: number
}

export default function Cloud({ w = 200, mode = 'day', dur = 26, range = 34, delay = 0 }: CloudProps) {
    const scale = w / 200
    const front = mode === 'night' ? '#cdd6e6' : '#ffffff'
    const back = mode === 'night' ? '#a9b6d0' : '#e3ebf1'

    const puffs = (color: string, offset: boolean) => (
        <div style={{ position: 'absolute', inset: 0, transform: offset ? 'translate(5px,9px)' : 'none' }}>
            <div style={{ position: 'absolute', left: 0, bottom: 0, width: 200, height: 64, borderRadius: 40, background: color, transition: 'background-color .9s ease' }} />
            <div style={{ position: 'absolute', left: 14, top: 46, width: 66, height: 66, borderRadius: '50%', background: color, transition: 'background-color .9s ease' }} />
            <div style={{ position: 'absolute', left: 58, top: 8, width: 104, height: 104, borderRadius: '50%', background: color, transition: 'background-color .9s ease' }} />
            <div style={{ position: 'absolute', left: 118, top: 34, width: 80, height: 80, borderRadius: '50%', background: color, transition: 'background-color .9s ease' }} />
        </div>
    )

    return (
        <div
            style={{
                position: 'relative',
                '--drift': `${range}px`,
                animation: `clouddrift ${dur}s ease-in-out ${delay}s infinite`,
                willChange: 'transform',
            } as React.CSSProperties}
        >
            <div style={{ transform: `scale(${scale})`, transformOrigin: 'bottom left', width: 200, height: 120 }}>
                <div style={{ position: 'relative', width: 200, height: 120, filter: 'drop-shadow(5px 12px 9px rgba(26,50,72,.24))' }}>
                    {puffs(back, true)}
                    {puffs(front, false)}
                </div>
            </div>
        </div>
    )
}