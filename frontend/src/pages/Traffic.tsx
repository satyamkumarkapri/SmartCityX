import { useState, useEffect } from 'react';
import { Car, Map, AlertTriangle, Activity } from 'lucide-react';
import { ResponsiveContainer, AreaChart, Area, XAxis, YAxis, Tooltip, CartesianGrid } from 'recharts';

interface Sensor {
  id: number;
  sensorId: string;
  type: string;
  value: number;
  location: string;
  status: string;
}

export function Traffic() {
  const [sensors, setSensors] = useState<Sensor[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/sensors')
      .then(r => r.json())
      .then(data => {
        setSensors(data.filter((s: Sensor) => s.type === 'TRAFFIC'));
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const chartData = [
    { time: '6:00', density: 30, limit: 100 },
    { time: '8:00', density: 85, limit: 100 },
    { time: '10:00', density: 65, limit: 100 },
    { time: '12:00', density: 75, limit: 100 },
    { time: '14:00', density: 60, limit: 100 },
    { time: '16:00', density: 90, limit: 100 },
    { time: '18:00', density: 95, limit: 100 },
    { time: '20:00', density: 45, limit: 100 },
  ];

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Traffic & Congestion</h1>
          <p className="text-sm text-slate-400 mt-1">Real-time analysis of city traffic flow and bottlenecks</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
            <h3 className="text-lg font-medium text-slate-200 mb-6 flex items-center gap-2">
              <Activity className="w-5 h-5 text-cyan-400" /> City-Wide Average Density
            </h3>
            <div className="h-[300px] w-full">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={chartData}>
                  <defs>
                    <linearGradient id="colorDensity" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.3}/>
                      <stop offset="95%" stopColor="#06b6d4" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
                  <XAxis dataKey="time" stroke="#64748b" tickLine={false} axisLine={false} />
                  <YAxis stroke="#64748b" tickLine={false} axisLine={false} />
                  <Tooltip 
                    contentStyle={{ backgroundColor: '#0f172a', border: '1px solid #1e293b', borderRadius: '0.5rem' }}
                  />
                  <Area type="monotone" dataKey="density" stroke="#06b6d4" strokeWidth={2} fillOpacity={1} fill="url(#colorDensity)" />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </div>
        </div>

        <div className="space-y-4">
          <h3 className="text-lg font-medium text-slate-200 mb-2">Live Traffic Sensors</h3>
          {loading ? (
            <div className="p-6 text-center text-slate-500">Loading traffic sensors...</div>
          ) : sensors.length === 0 ? (
            <div className="p-6 text-center text-slate-500">No traffic sensors online.</div>
          ) : (
            sensors.map((sensor) => (
              <div key={sensor.id} className="p-4 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm flex justify-between items-center">
                <div className="flex items-center gap-3">
                  <div className={`p-2 rounded-lg ${sensor.value > 80 ? 'bg-red-500/10 text-red-400' : 'bg-cyan-500/10 text-cyan-400'}`}>
                    <Car className="w-5 h-5" />
                  </div>
                  <div>
                    <p className="text-sm font-medium text-slate-200">{sensor.location}</p>
                    <p className="text-xs text-slate-400 mt-0.5">{sensor.sensorId}</p>
                  </div>
                </div>
                <div className="text-right">
                  <p className={`text-lg font-bold ${sensor.value > 80 ? 'text-red-400' : 'text-slate-100'}`}>
                    {sensor.value.toFixed(0)} <span className="text-xs text-slate-500">vpm</span>
                  </p>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
