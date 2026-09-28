import { useState, useEffect } from 'react';
import { Wifi, Activity, MapPin, Radio, Search } from 'lucide-react';
import { cn } from '@/lib/utils';
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid } from 'recharts';

interface Sensor {
  id: number;
  sensorId: string;
  sensorType: string;
  value: number;
  unit: string;
  zone: string;
  status: string;
  timestamp: string;
}

export function IoTSensors() {
  const [sensors, setSensors] = useState<Sensor[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/sensors')
      .then(r => r.json())
      .then(data => setSensors(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE': return 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20';
      case 'OFFLINE': return 'bg-red-500/10 text-red-400 border-red-500/20';
      case 'MAINTENANCE': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'AIR_QUALITY': return <Activity className="w-5 h-5 text-purple-400" />;
      case 'TRAFFIC': return <Activity className="w-5 h-5 text-cyan-400" />;
      case 'NOISE_LEVEL': return <Activity className="w-5 h-5 text-pink-400" />;
      case 'TEMPERATURE': return <Activity className="w-5 h-5 text-orange-400" />;
      default: return <Radio className="w-5 h-5 text-slate-400" />;
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">IoT Sensor Network</h1>
          <p className="text-sm text-slate-400 mt-1">Live telemetry data from city-wide sensor grid</p>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        {['AIR_QUALITY', 'TRAFFIC', 'NOISE_LEVEL', 'TEMPERATURE'].map((type, i) => (
          <div key={type} className="p-4 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
            <div className="flex justify-between items-start">
              <div>
                <p className="text-xs font-medium text-slate-400 mb-1">{type.replace('_', ' ')}</p>
                <p className="text-xl font-bold text-slate-100">
                  {loading ? '...' : (sensors.filter(s => s.sensorType === type).length || 0)} <span className="text-sm font-normal text-slate-500">sensors</span>
                </p>
              </div>
              <div className="p-2 bg-slate-800 rounded-lg">
                {getTypeIcon(type)}
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="bg-slate-800/40 border border-slate-700/50 rounded-xl overflow-hidden backdrop-blur-sm">
        <div className="p-4 border-b border-slate-700/50 flex gap-4">
          <div className="relative flex-1 max-w-md">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
            <input 
              type="text" 
              placeholder="Search sensors by ID or location..."
              className="w-full bg-slate-900/50 border border-slate-700/50 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-emerald-500"
            />
          </div>
        </div>
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-900/50 text-slate-400 border-b border-slate-700/50">
              <tr>
                <th className="px-6 py-4 font-medium">Sensor ID</th>
                <th className="px-6 py-4 font-medium">Type</th>
                <th className="px-6 py-4 font-medium">Reading</th>
                <th className="px-6 py-4 font-medium">Location</th>
                <th className="px-6 py-4 font-medium">Status</th>
                <th className="px-6 py-4 font-medium">Last Update</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-700/50">
              {loading ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-slate-500">
                    Connecting to sensor grid...
                  </td>
                </tr>
              ) : sensors.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-slate-500">
                    No sensor data available.
                  </td>
                </tr>
              ) : (
                sensors.map((sensor) => (
                  <tr key={sensor.id} className="hover:bg-slate-800/60 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Radio className="w-4 h-4 text-slate-500" />
                        <span className="font-mono text-slate-300">{sensor.sensorId}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-slate-200">
                      {sensor.sensorType ? sensor.sensorType.replace('_', ' ') : 'SENSOR'}
                    </td>
                    <td className="px-6 py-4">
                      <span className="font-mono font-medium text-emerald-400">{sensor.value !== undefined && sensor.value !== null ? Number(sensor.value).toFixed(2) : '0.00'}</span>
                      <span className="text-slate-500 ml-1">{sensor.unit}</span>
                    </td>
                    <td className="px-6 py-4 text-slate-400 flex items-center gap-1.5">
                      <MapPin className="w-3.5 h-3.5" />
                      {sensor.zone}
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getStatusColor(sensor.status))}>
                        {sensor.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-slate-400 text-xs font-mono">
                      {new Date(sensor.timestamp).toLocaleString()}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
