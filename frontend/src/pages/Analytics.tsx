import { useState, useEffect } from 'react';
import { Activity, TrendingUp, Users, Database } from 'lucide-react';
import { ResponsiveContainer, BarChart, Bar, LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, PieChart, Pie, Cell } from 'recharts';

export function Analytics() {
  const [stats, setStats] = useState<any>(null);
  const [chartData, setChartData] = useState<any>(null);

  useEffect(() => {
    Promise.all([
      fetch('http://localhost:8080/api/dashboard/stats').then(r => r.json()),
      fetch('http://localhost:8080/api/dashboard/chart-data').then(r => r.json())
    ])
    .then(([s, c]) => {
      setStats(s);
      
      const traffic = c.traffic.labels.map((l: string, i: number) => ({
        time: l,
        density: c.traffic.current[i]
      }));
      setChartData({ traffic });
    })
    .catch(console.error);
  }, []);

  const resourceData = [
    { name: 'Water', value: stats?.totalResources || 45, color: '#0ea5e9' },
    { name: 'Energy', value: stats?.availableResources || 30, color: '#f59e0b' },
    { name: 'Sensors', value: stats?.totalSensors || 120, color: '#10b981' },
    { name: 'Requests', value: stats?.totalRequests || 60, color: '#8b5cf6' },
  ];

  return (
    <div className="space-y-6 animate-in fade-in">
      <div>
        <h1 className="text-2xl font-bold text-slate-100">City-wide Analytics</h1>
        <p className="text-sm text-slate-400 mt-1">Advanced data modeling and performance metrics</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
          <p className="text-slate-400 text-sm">System Uptime</p>
          <p className="text-2xl font-bold text-emerald-400 mt-1 flex items-center gap-2">
            99.99% <TrendingUp className="w-4 h-4" />
          </p>
        </div>
        <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
          <p className="text-slate-400 text-sm">Data Processing Rate</p>
          <p className="text-2xl font-bold text-cyan-400 mt-1 flex items-center gap-2">
            1.2 GB/s <Activity className="w-4 h-4" />
          </p>
        </div>
        <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
          <p className="text-slate-400 text-sm">Active Connections</p>
          <p className="text-2xl font-bold text-purple-400 mt-1 flex items-center gap-2">
            8,432 <Users className="w-4 h-4" />
          </p>
        </div>
        <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
          <p className="text-slate-400 text-sm">Database Queries</p>
          <p className="text-2xl font-bold text-amber-400 mt-1 flex items-center gap-2">
            4.2M /hr <Database className="w-4 h-4" />
          </p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm h-[400px]">
          <h3 className="font-medium text-slate-200 mb-6">Traffic & Sensor Data Volume</h3>
          {chartData ? (
            <ResponsiveContainer width="100%" height="80%">
              <BarChart data={chartData.traffic}>
                <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
                <XAxis dataKey="time" stroke="#64748b" tickLine={false} axisLine={false} />
                <YAxis stroke="#64748b" tickLine={false} axisLine={false} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#1e293b' }} />
                <Bar dataKey="density" fill="#8b5cf6" radius={[4, 4, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          ) : (
            <div className="h-full flex items-center justify-center text-slate-500">Loading...</div>
          )}
        </div>

        <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm h-[400px]">
          <h3 className="font-medium text-slate-200 mb-6">System Resource Allocation</h3>
          <ResponsiveContainer width="100%" height="80%">
            <PieChart>
              <Pie
                data={resourceData}
                cx="50%"
                cy="50%"
                innerRadius={80}
                outerRadius={120}
                paddingAngle={5}
                dataKey="value"
              >
                {resourceData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={entry.color} />
                ))}
              </Pie>
              <Tooltip contentStyle={{ backgroundColor: '#0f172a', borderColor: '#1e293b' }} />
            </PieChart>
          </ResponsiveContainer>
          <div className="flex justify-center gap-6 mt-4">
            {resourceData.map(item => (
              <div key={item.name} className="flex items-center gap-2">
                <div className="w-3 h-3 rounded-full" style={{ backgroundColor: item.color }} />
                <span className="text-xs text-slate-400">{item.name}</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
