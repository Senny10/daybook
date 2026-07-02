interface SunProps {
    size?: number
    mode?: 'day' | 'night'
    phase?: number
}

export default function Sun({ size = 220, mode = 'day', phase = 0.5 }: SunProps) {
    const scale = size / 220
    const rays = Array.from({ length: 20 }, (_, i) => i * 18)

    const p = ((phase % 1) + 1) % 1
    const c = Math.cos(2 * Math.PI * p)
    const lit = '#ECE7D7'
    const dark = '#707B96'
    const waxing = p < 0.5

    const sunOpacity = mode === 'night' ? 0 : 1
    const moonOpacity = mode === 'night' ? 1 : 0

    return (
        <div style={{ position: 'relative', width: 440 * scale, height: 440 * scale }}>
            <div
                style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    width: 440,
                    height: 440,
                    transform: `scale(${scale})`,
                    transformOrigin: 'top left',
                }}
            >
                {/* Rays */}
                <div
                    style={{
                        position: 'absolute',
                        inset: 0,
                        opacity: sunOpacity,
                        transition: 'opacity .8s ease',
                        animation: 'spin 150s linear infinite',
                    }}
                >
                    {rays.map((rot, i) => (
                        <div
                            key={i}
                            style={{
                                position: 'absolute',
                                left: 'calc(50% - 8px)',
                                top: 0,
                                width: 16,
                                height: 220,
                                transformOrigin: '50% 100%',
                                transform: `rotate(${rot}deg)`,
                            }}
                        >
                            <div
                                style={{
                                    position: 'absolute',
                                    top: 0,
                                    left: 0,
                                    width: 16,
                                    height: 98,
                                    borderRadius: 8,
                                    background: '#EAB30F',
                                    boxShadow: '3px 6px 7px rgba(120,82,0,.24)',
                                }}
                            />
                        </div>
                    ))}
                </div>

                {/* Moon */}
                <div style={{ position: 'absolute', inset: 0, opacity: moonOpacity, transition: 'opacity .8s ease' }}>
                    <div
                        style={{
                            position: 'absolute',
                            left: 'calc(50% - 117px)',
                            top: 'calc(50% - 115px)',
                            width: 234,
                            height: 234,
                            borderRadius: '50%',
                            background: '#AEB7CB',
                            boxShadow: '0 8px 15px rgba(36,48,78,.32)',
                        }}
                    />
                    <div
                        style={{
                            position: 'absolute',
                            left: 'calc(50% - 110px)',
                            top: 'calc(50% - 110px)',
                            width: 220,
                            height: 220,
                            borderRadius: '50%',
                            overflow: 'hidden',
                            background: lit,
                            boxShadow: 'inset 0 0 30px rgba(44,56,90,.20)',
                        }}
                    >
                        <div
                            style={{
                                position: 'absolute',
                                left: 0,
                                top: 0,
                                width: 220,
                                height: 220,
                                borderRadius: '50%',
                                background: c < 0 ? lit : dark,
                                transform: `scaleX(${Math.max(0, Math.abs(c))})`,
                                transformOrigin: waxing ? 'left' : 'right',
                                transition: 'transform .35s ease, background-color .35s ease',
                            }}
                        />
                    </div>
                    <div style={{ position: 'absolute', left: 'calc(50% - 38px)', top: 'calc(50% - 22px)', width: 30, height: 30, borderRadius: '50%', background: 'rgba(86,98,128,.16)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% + 8px)', top: 'calc(50% + 18px)', width: 20, height: 20, borderRadius: '50%', background: 'rgba(86,98,128,.14)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% - 6px)', top: 'calc(50% - 48px)', width: 16, height: 16, borderRadius: '50%', background: 'rgba(86,98,128,.12)' }} />
                </div>

                {/* Sun body */}
                <div style={{ position: 'absolute', inset: 0, opacity: sunOpacity, transition: 'opacity .8s ease' }}>
                    <div style={{ position: 'absolute', left: 'calc(50% - 110px)', top: 'calc(50% - 110px)', width: 220, height: 220, borderRadius: '50%', background: '#DBA200', boxShadow: '0 7px 12px rgba(120,82,0,.30)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% - 99px)', top: 'calc(50% - 100px)', width: 192, height: 192, borderRadius: '50%', background: '#E6B015', boxShadow: '0 4px 9px rgba(120,82,0,.20)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% - 84px)', top: 'calc(50% - 89px)', width: 162, height: 162, borderRadius: '50%', background: '#EFC121', boxShadow: '0 4px 8px rgba(120,82,0,.18)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% - 68px)', top: 'calc(50% - 77px)', width: 130, height: 130, borderRadius: '50%', background: '#F5D139', boxShadow: '0 3px 7px rgba(120,82,0,.16)' }} />
                    <div style={{ position: 'absolute', left: 'calc(50% - 51px)', top: 'calc(50% - 64px)', width: 96, height: 96, borderRadius: '50%', background: '#FADF55', boxShadow: '0 3px 6px rgba(120,82,0,.14)' }} />
                </div>
            </div>
        </div>
    )
}