import { useState, useEffect } from 'react';
import { 
  Activity, Users, Car, Map as MapIcon, Droplet, Zap, FileText, AlertTriangle, Wifi, 
  MapPin, ShieldAlert, Wrench, CheckCircle, ArrowUp, ArrowDown, ChevronRight, CheckSquare, Plus, Layers, Bell
} from 'lucide-react';
import { cn } from '@/lib/utils';
import { 
  ResponsiveContainer, AreaChart, Area, BarChart, Bar, LineChart, Line, 
  PieChart, Pie, Cell, XAxis, YAxis, Tooltip, CartesianGrid 
} from 'recharts';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import { Link } from 'react-router-dom';

// Custom Map Marker Icons using HTML
const createCustomIcon = (color: string, iconHtml: string) => {
  return L.divIcon({
    className: 'custom-leaflet-icon',
    html: `
      <div class="relative group cursor-pointer w-6 h-6 flex items-center justify-center">
        <div class="absolute inset-0 bg-${color}-500/30 rounded-full animate-ping"></div>
        <div class="absolute inset-0 bg-${color}-500 text-white rounded-full border border-${color}-300 flex items-center justify-center shadow-[0_0_10px_var(--tw-shadow-color)] shadow-${color}-500">
          ${iconHtml}
        </div>
      </div>
    `,
    iconSize: [24, 24],
    iconAnchor: [12, 12],
  });
};

const getSensorIcon = (type: string, status: string) => {
  const color = status === 'OFFLINE' ? 'red' : 'emerald';
  let iconHtml = '<svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M5 12.55a11 11 0 0 1 14.08 0"></path><path d="M1.42 9a16 16 0 0 1 21.16 0"></path><path d="M8.53 16.11a6 6 0 0 1 6.95 0"></path><line x1="12" y1="20" x2="12.01" y2="20"></line></svg>'; // default wifi
  
  if (type === 'TRAFFIC') {
    iconHtml = '<svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 16H9m10 0h3v-3.15a1 1 0 0 0-.84-.99L16 11l-2.7-3.6a1 1 0 0 0-.8-.4H8.4c-.32 0-.63.15-.8.4L5 11l-5.16.86a1 1 0 0 0-.84.99V16h3m10 0a2 2 0 1 0 4 0 2 2 0 0 0-4 0ZM3 16a2 2 0 1 0 4 0 2 2 0 0 0-4 0Z"></path></svg>';
  } else if (type === 'AIR_QUALITY' || type === 'TEMPERATURE') {
    iconHtml = '<svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline></svg>';
  }
  
  return createCustomIcon(color, iconHtml);
};

export function Dashboard() {
  const [stats, setStats] = useState<any>(null);
  const [chartData, setChartData] = useState<any>(null);
  const [emergencies, setEmergencies] = useState<any[]>([]);
  const [sensors, setSensors] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      fetch('http://localhost:8080/api/dashboard/stats').then(r => r.json()),
      fetch('http://localhost:8080/api/dashboard/chart-data').then(r => r.json()),
      fetch('http://localhost:8080/api/dashboard/emergencies').then(r => r.json()),
      fetch('http://localhost:8080/api/sensors').then(r => r.json())
    ])
    .then(([sData, cData, eData, sensorData]) => {
      setStats(sData);
      setChartData(cData);
      setEmergencies(eData);
      setSensors(sensorData);
    })
    .catch(console.error)
    .finally(() => setLoading(false));
  }, []);

  const metricCards = [
    { title: 'Population', value: '1,248,520', trend: '+ 2.5%', isUp: true, icon: Users, color: 'text-blue-400', border: 'border-blue-500/30', bg: 'bg-blue-500/10' },
    { title: 'Active Vehicles', value: '58,423', trend: '+ 4.1%', isUp: true, icon: Car, color: 'text-cyan-400', border: 'border-cyan-500/30', bg: 'bg-cyan-500/10' },
    { title: 'Traffic Congestion', value: 'Moderate', trend: '- 12%', isUp: false, icon: MapIcon, color: 'text-amber-400', border: 'border-amber-500/30', bg: 'bg-amber-500/10' },
    { title: 'Water Level', value: '78%', trend: '+ 3%', isUp: true, icon: Droplet, color: 'text-blue-400', border: 'border-blue-500/30', bg: 'bg-blue-500/10' },
    { title: 'Energy Usage', value: '82%', trend: '+ 1.8%', isUp: true, icon: Zap, color: 'text-purple-400', border: 'border-purple-500/30', bg: 'bg-purple-500/10' },
    { title: 'Open Requests', value: stats?.openRequests || '0', trend: '+ 6%', isUp: true, icon: FileText, color: 'text-red-400', border: 'border-red-500/30', bg: 'bg-red-500/10' },
    { title: 'Active Emergencies', value: stats?.activeEmergencies || '0', trend: '+ 2', isUp: true, icon: AlertTriangle, color: 'text-red-500', border: 'border-red-500/50', bg: 'bg-red-500/10' },
    { title: 'IoT Sensors', value: stats?.totalSensors || '0', trend: '+ 98%', isUp: true, icon: Wifi, color: 'text-emerald-400', border: 'border-emerald-500/30', bg: 'bg-emerald-500/10' },
  ];

  // Map API Chart Data to Recharts arrays
  const trafficData = chartData?.traffic?.labels.map((l: string, i: number) => ({
    name: l,
    val: chartData.traffic.current[i]
  })) || [];

  const waterEnergyData = chartData?.resource?.labels.map((l: string, i: number) => ({
    name: l,
    water: chartData.resource.water[i],
    energy: chartData.resource.energy[i]
  })) || [];

  const pieData = [
    { name: 'Road Damage', value: 32, color: '#0ea5e9' },
    { name: 'Streetlight', value: 28, color: '#f43f5e' },
    { name: 'Garbage', value: 24, color: '#f59e0b' },
    { name: 'Water Leakage', value: 18, color: '#10b981' },
    { name: 'Electricity', value: 14, color: '#8b5cf6' },
    { name: 'Others', value: 8, color: '#6366f1' },
  ];

  // Helper function to map location string to approx coordinates in Vijayawada
  const getCoordinates = (location: string): [number, number] => {
    if (!location) return [16.5062, 80.6480];
    const locHash = Array.from(location).reduce((hash, char) => hash + char.charCodeAt(0), 0);
    // Base Vijayawada Coords: 16.5062, 80.6480
    // Add small random deterministic offset
    const lat = 16.5062 + (locHash % 100) / 1000 - 0.05;
    const lng = 80.6480 + ((locHash * 3) % 100) / 1000 - 0.05;
    return [lat, lng];
  };

  return (
    <div className="space-y-4 animate-in fade-in pb-10">
      
      {/* Header Section */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div>
          <h1 className="text-3xl font-bold text-white tracking-wide">Welcome to <span className="text-blue-500">SmartCityX</span></h1>
          <p className="text-sm text-slate-400 mt-1">Real-Time Insights for a Smarter, Safer, and Greener City</p>
        </div>
        <div className="flex items-center gap-3 bg-[#0f172a]/80 border border-emerald-500/30 rounded-full pl-4 pr-1.5 py-1.5 backdrop-blur-sm">
          <span className="text-xs font-semibold text-slate-300 tracking-wider">CITY STATUS</span>
          <div className="bg-emerald-500/20 border border-emerald-500/50 text-emerald-400 px-4 py-1.5 rounded-full flex items-center gap-2 shadow-[0_0_15px_rgba(16,185,129,0.3)]">
            <span className="text-sm font-bold tracking-wide">OPERATIONAL</span>
            <Activity className="w-4 h-4 animate-pulse" />
          </div>
        </div>
      </div>

      {/* 8 Metric Cards Row */}
      <div className="grid grid-cols-2 md:grid-cols-4 xl:grid-cols-8 gap-3">
        {metricCards.map((card, i) => (
          <div key={i} className={cn("p-3 rounded-xl border bg-gradient-to-b from-[#111827] to-[#0a0f18] flex flex-col justify-between shadow-lg relative overflow-hidden group hover:border-blue-500/50 transition-colors", card.border)}>
            <div className={cn("absolute bottom-0 left-0 w-full h-1/2 bg-gradient-to-t to-transparent opacity-20", card.bg)}></div>
            <div className="flex items-center gap-2 mb-2 relative z-10">
              <div className={cn("p-1.5 rounded-md", card.bg)}>
                <card.icon className={cn("w-4 h-4", card.color)} />
              </div>
              <span className="text-xs font-medium text-slate-300 truncate">{card.title}</span>
            </div>
            <div className="relative z-10">
              <p className="text-xl font-bold text-white">{card.value}</p>
              <div className={cn("flex items-center gap-1 text-[10px] mt-1 font-medium", card.isUp ? "text-emerald-400" : "text-amber-400")}>
                {card.isUp ? <ArrowUp className="w-3 h-3" /> : <ArrowDown className="w-3 h-3" />}
                {card.trend}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Main Grid: Map & Alerts */}
      <div className="grid grid-cols-1 xl:grid-cols-12 gap-4">
        
        {/* Real Leaflet Map Section */}
        <div className="xl:col-span-8 bg-[#0f172a] rounded-xl border border-slate-700/80 overflow-hidden relative shadow-[0_4px_20px_rgba(0,0,0,0.4)] h-[500px] z-0">
          
          <MapContainer 
            center={[16.5062, 80.6480]} 
            zoom={12} 
            className="w-full h-full z-0" 
            zoomControl={false}
          >
            <TileLayer
              attribution='Tiles &copy; Esri &mdash; Esri, DeLorme, NAVTEQ'
              url="https://server.arcgisonline.com/ArcGIS/rest/services/Canvas/World_Dark_Gray_Base/MapServer/tile/{z}/{y}/{x}"
            />
            {sensors.map(sensor => (
              <Marker 
                key={sensor.id} 
                position={getCoordinates(sensor.zone)}
                icon={getSensorIcon(sensor.sensorType, sensor.status)}
              >
                <Popup className="dark-popup">
                  <div className="p-1 min-w-[200px]">
                    <div className="flex gap-3 items-start mb-2">
                      <div className="p-2 bg-blue-500/20 text-blue-400 rounded-lg border border-blue-500/30">
                        <Wifi className="w-4 h-4" />
                      </div>
                      <div>
                        <h4 className="text-sm font-bold text-slate-800">Sensor {sensor.sensorId}</h4>
                        <p className="text-xs text-blue-600 font-medium">{sensor.sensorType ? sensor.sensorType.replace('_', ' ') : 'SENSOR'}</p>
                      </div>
                    </div>
                    <div className="space-y-1 text-xs text-slate-600">
                      <p className="flex items-center gap-2"><MapPin className="w-3 h-3" /> {sensor.zone}</p>
                      <p className="flex items-center gap-2"><Activity className="w-3 h-3" /> Reading: {sensor.value} {sensor.unit}</p>
                      <p className="flex items-center gap-2"><AlertTriangle className="w-3 h-3" /> Status: {sensor.status}</p>
                    </div>
                    <Link to="/iot" className="w-full mt-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white text-xs font-bold rounded flex items-center justify-center gap-1 transition-colors">
                      View Hub <ChevronRight className="w-3 h-3" />
                    </Link>
                  </div>
                </Popup>
              </Marker>
            ))}
            
            {emergencies.map(em => (
              <Marker 
                key={`em-${em.id}`} 
                position={getCoordinates(em.location)}
                icon={createCustomIcon('red', '<svg class="w-3 h-3" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"></path><line x1="12" y1="9" x2="12" y2="13"></line><line x1="12" y1="17" x2="12.01" y2="17"></line></svg>')}
              >
                <Popup className="dark-popup">
                   <div className="p-1 min-w-[200px]">
                    <div className="flex gap-3 items-start mb-2">
                      <div className="p-2 bg-red-500/20 text-red-600 rounded-lg border border-red-500/30">
                        <ShieldAlert className="w-4 h-4" />
                      </div>
                      <div>
                        <h4 className="text-sm font-bold text-red-600">EMERGENCY</h4>
                        <p className="text-xs text-slate-800 font-medium">{em.emergencyType ? em.emergencyType.replace('_', ' ') : 'INCIDENT'}</p>
                      </div>
                    </div>
                    <div className="space-y-1 text-xs text-slate-600">
                      <p className="flex items-center gap-2"><MapPin className="w-3 h-3" /> {em.location}</p>
                      <p className="flex items-center gap-2"><AlertTriangle className="w-3 h-3" /> Severity: {em.severity}</p>
                    </div>
                    <Link to="/emergency" className="w-full mt-3 py-1.5 bg-red-600 hover:bg-red-700 text-white text-xs font-bold rounded flex items-center justify-center gap-1 transition-colors">
                      Dispatch <ChevronRight className="w-3 h-3" />
                    </Link>
                  </div>
                </Popup>
              </Marker>
            ))}
          </MapContainer>
          
          <div className="absolute inset-0 bg-blue-900/10 mix-blend-overlay pointer-events-none"></div>
          
          <div className="absolute top-4 left-4 flex items-center gap-2 bg-[#0f172a]/90 backdrop-blur border border-slate-700 p-2 px-3 rounded-lg shadow-lg z-10 pointer-events-none">
            <MapPin className="w-4 h-4 text-cyan-400" />
            <span className="text-sm font-bold text-white tracking-wide">Live City Map</span>
          </div>

          <div className="absolute top-16 left-4 bg-[#0f172a]/90 backdrop-blur border border-slate-700 rounded-lg p-3 shadow-lg w-40 z-10">
            <div className="space-y-2 text-xs font-medium text-slate-300">
              <label className="flex items-center gap-2 cursor-pointer hover:text-white"><div className="w-4 h-4 rounded bg-emerald-500/20 border border-emerald-500/50 flex items-center justify-center"><CheckSquare className="w-3 h-3 text-emerald-400" /></div> Sensors</label>
              <label className="flex items-center gap-2 cursor-pointer hover:text-white"><div className="w-4 h-4 rounded bg-red-500/20 border border-red-500/50 flex items-center justify-center"><CheckSquare className="w-3 h-3 text-red-400" /></div> Emergencies</label>
            </div>
          </div>
        </div>

        {/* Alerts & Quick Actions */}
        <div className="xl:col-span-4 flex flex-col gap-4">
          
          {/* Recent Alerts List - From Real API */}
          <div className="flex-1 bg-[#0f172a] rounded-xl border border-slate-700/80 p-5 shadow-lg overflow-hidden flex flex-col h-[300px]">
            <div className="flex justify-between items-center mb-5 border-b border-slate-800 pb-2">
              <h3 className="text-sm font-bold text-white flex items-center gap-2"><Bell className="w-4 h-4 text-amber-400" /> Recent Alerts</h3>
              <Link to="/emergency" className="text-xs text-blue-400 hover:text-blue-300 flex items-center">View All <ChevronRight className="w-3 h-3" /></Link>
            </div>
            
            <div className="space-y-4 flex-1 overflow-y-auto custom-scrollbar pr-2">
              {loading ? (
                <div className="text-slate-500 text-sm text-center py-4">Fetching live alerts...</div>
              ) : emergencies.length === 0 ? (
                <div className="text-emerald-500 text-sm text-center py-4">No active emergencies in the city.</div>
              ) : (
                emergencies.map(em => (
                  <div key={em.id} className="flex gap-3 items-start pb-4 border-b border-slate-800/50 last:border-0">
                    <div className="p-1.5 rounded-full bg-red-500/10 text-red-500 shrink-0"><AlertTriangle className="w-4 h-4" /></div>
                    <div className="flex-1">
                      <p className="text-xs font-medium text-slate-200 leading-tight mb-1">{em.description}</p>
                      <p className="text-[10px] text-slate-500">{new Date(em.reportedAt).toLocaleString()}</p>
                    </div>
                    <span className={cn("px-2 py-0.5 text-[10px] font-bold border rounded", 
                      em.severity === 'CRITICAL' ? 'bg-red-600/10 border-red-600/30 text-red-500 animate-pulse' : 'bg-amber-500/10 border-amber-500/30 text-amber-400'
                    )}>
                      {em.severity}
                    </span>
                  </div>
                ))
              )}
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-[#0f172a] rounded-xl border border-slate-700/80 p-5 shadow-lg shrink-0">
            <h3 className="text-sm font-bold text-white flex items-center gap-2 mb-4 border-b border-slate-800 pb-2"><Activity className="w-4 h-4 text-cyan-400" /> Quick Actions</h3>
            <div className="grid grid-cols-2 gap-3">
              <Link to="/service-requests" className="py-2.5 px-3 bg-blue-600 hover:bg-blue-500 text-white text-xs font-bold rounded-lg transition-colors flex items-center justify-center gap-2 shadow-[0_0_10px_rgba(37,99,235,0.3)]">
                <Wrench className="w-4 h-4" /> Report Issue
              </Link>
              <Link to="/emergency" className="py-2.5 px-3 bg-red-600 hover:bg-red-500 text-white text-xs font-bold rounded-lg transition-colors flex items-center justify-center gap-2 shadow-[0_0_10px_rgba(220,38,38,0.3)]">
                <ShieldAlert className="w-4 h-4" /> Report Emergency
              </Link>
              <Link to="/documents" className="py-2.5 px-3 bg-purple-600 hover:bg-purple-500 text-white text-xs font-bold rounded-lg transition-colors flex items-center justify-center gap-2 shadow-[0_0_10px_rgba(147,51,234,0.3)]">
                <FileText className="w-4 h-4" /> Documents
              </Link>
              <Link to="/analytics" className="py-2.5 px-3 bg-emerald-600 hover:bg-emerald-500 text-white text-xs font-bold rounded-lg transition-colors flex items-center justify-center gap-2 shadow-[0_0_10px_rgba(16,185,129,0.3)]">
                <Activity className="w-4 h-4" /> View Analytics
              </Link>
            </div>
          </div>

        </div>
      </div>

      {/* 4 Chart Cards Row - Real API Data */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-4 mt-4">
        
        {/* Traffic Analytics */}
        <div className="p-4 rounded-xl border border-slate-700/80 bg-[#0f172a] shadow-lg flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-sm font-bold text-white flex items-center gap-2"><Car className="w-4 h-4 text-blue-400" /> Traffic Volume</h3>
            <span className="text-[10px] bg-slate-800 border border-slate-700 text-slate-300 px-2 py-1 rounded">Live Data</span>
          </div>
          <div className="h-32 w-full mb-4">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={trafficData} margin={{ top: 5, right: 0, left: -20, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorTraffic" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#3b82f6" stopOpacity={0.8}/>
                    <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <YAxis stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', border: '1px solid #1e293b' }} />
                <Area type="monotone" dataKey="val" stroke="#3b82f6" strokeWidth={2} fillOpacity={1} fill="url(#colorTraffic)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Water Consumption */}
        <div className="p-4 rounded-xl border border-slate-700/80 bg-[#0f172a] shadow-lg flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-sm font-bold text-white flex items-center gap-2"><Droplet className="w-4 h-4 text-cyan-400" /> Water Consumption</h3>
            <span className="text-[10px] bg-slate-800 border border-slate-700 text-slate-300 px-2 py-1 rounded">Live Data</span>
          </div>
          <div className="h-32 w-full mb-4">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={waterEnergyData} margin={{ top: 5, right: 0, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <YAxis stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', border: '1px solid #1e293b' }} />
                <Bar dataKey="water" fill="#06b6d4" radius={[2, 2, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Energy Usage */}
        <div className="p-4 rounded-xl border border-slate-700/80 bg-[#0f172a] shadow-lg flex flex-col">
          <div className="flex justify-between items-center mb-4">
            <h3 className="text-sm font-bold text-white flex items-center gap-2"><Zap className="w-4 h-4 text-purple-400" /> Energy Usage</h3>
            <span className="text-[10px] bg-slate-800 border border-slate-700 text-slate-300 px-2 py-1 rounded">Live Data</span>
          </div>
          <div className="h-32 w-full mb-4">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={waterEnergyData} margin={{ top: 5, right: 0, left: -20, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#1e293b" />
                <XAxis dataKey="name" stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <YAxis stroke="#475569" fontSize={10} tickLine={false} axisLine={false} />
                <Tooltip contentStyle={{ backgroundColor: '#0f172a', border: '1px solid #1e293b' }} />
                <Line type="monotone" dataKey="energy" stroke="#9333ea" strokeWidth={2} dot={{ fill: '#9333ea', r: 3 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </div>

        {/* Service Requests Donut */}
        <div className="p-4 rounded-xl border border-slate-700/80 bg-[#0f172a] shadow-lg flex flex-col">
          <div className="flex justify-between items-center mb-2">
            <h3 className="text-sm font-bold text-white flex items-center gap-2"><FileText className="w-4 h-4 text-amber-400" /> Service Requests</h3>
            <span className="text-[10px] bg-slate-800 border border-slate-700 text-slate-300 px-2 py-1 rounded">Analytics</span>
          </div>
          <div className="flex-1 flex items-center gap-4">
            <div className="relative w-32 h-32">
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie data={pieData} cx="50%" cy="50%" innerRadius={35} outerRadius={55} paddingAngle={2} dataKey="value" stroke="none">
                    {pieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.color} />
                    ))}
                  </Pie>
                </PieChart>
              </ResponsiveContainer>
              <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
                <span className="text-lg font-bold text-white leading-none">{stats?.totalRequests || 0}</span>
                <span className="text-[10px] text-slate-400">Total</span>
              </div>
            </div>
            
            <div className="flex-1 space-y-1.5">
              {pieData.map((item) => (
                <div key={item.name} className="flex justify-between items-center text-[10px]">
                  <div className="flex items-center gap-1.5">
                    <div className="w-2 h-2 rounded-full" style={{ backgroundColor: item.color }}></div>
                    <span className="text-slate-300">{item.name}</span>
                  </div>
                  <span className="text-white font-bold">{item.value}%</span>
                </div>
              ))}
            </div>
          </div>
        </div>

      </div>
    </div>
  );
}
