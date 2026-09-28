import { useState, useEffect } from 'react';
import { AlertTriangle, MapPin, Search, Phone, ShieldAlert } from 'lucide-react';
import { cn } from '@/lib/utils';

interface Emergency {
  id: number;
  reportId: string;
  emergencyType: string;
  description: string;
  location: string;
  status: string;
  severity: string;
  reportedAt: string;
}

export function Emergency() {
  const [emergencies, setEmergencies] = useState<Emergency[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/dashboard/emergencies')
      .then(r => r.json())
      .then(data => setEmergencies(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'CRITICAL': return 'bg-red-500 text-slate-900 border-red-500 animate-pulse';
      case 'HIGH': return 'bg-orange-500/10 text-orange-400 border-orange-500/20';
      case 'MEDIUM': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between p-6 bg-red-500/10 border border-red-500/20 rounded-xl">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-red-500/20 rounded-full text-red-500">
            <ShieldAlert className="w-8 h-8" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-red-400">Emergency Response System</h1>
            <p className="text-sm text-red-400/80 mt-1">Active incidents requiring immediate city response</p>
          </div>
        </div>
        <button className="flex items-center gap-2 px-6 py-3 bg-red-600 hover:bg-red-700 text-white font-bold rounded-lg transition-colors shadow-[0_0_15px_rgba(220,38,38,0.5)]">
          <Phone className="w-5 h-5" /> Dispatch Unit
        </button>
      </div>

      <div className="grid grid-cols-1 gap-4">
        {loading ? (
          <div className="h-40 flex items-center justify-center text-slate-500">Loading emergency reports...</div>
        ) : emergencies.length === 0 ? (
          <div className="p-10 border border-emerald-500/20 bg-emerald-500/5 rounded-xl text-center">
            <div className="inline-flex p-4 rounded-full bg-emerald-500/10 text-emerald-400 mb-4">
              <ShieldAlert className="w-8 h-8" />
            </div>
            <h3 className="text-xl font-medium text-emerald-400">No Active Emergencies</h3>
            <p className="text-slate-400 mt-2">The city is currently operating safely with zero reported incidents.</p>
          </div>
        ) : (
          emergencies.map((inc) => (
            <div key={inc.id} className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm flex flex-col md:flex-row gap-6 hover:bg-slate-800/60 transition-colors">
              <div className="flex-1 space-y-4">
                <div className="flex items-start justify-between">
                  <div className="flex items-center gap-3">
                    <span className={cn("px-3 py-1 text-xs font-bold border rounded-full", getSeverityColor(inc.severity))}>
                      {inc.severity}
                    </span>
                    <span className="text-slate-400 font-mono text-sm">{inc.reportId}</span>
                  </div>
                  <span className="px-3 py-1 text-xs font-medium border border-slate-700 bg-slate-800 text-slate-300 rounded-full">
                    {inc.status}
                  </span>
                </div>
                
                <div>
                  <h3 className="text-xl font-medium text-slate-100 mb-2">{inc.emergencyType ? inc.emergencyType.replace('_', ' ') : 'INCIDENT'}</h3>
                  <p className="text-slate-400">{inc.description}</p>
                </div>
                
                <div className="flex flex-wrap gap-4 pt-2">
                  <div className="flex items-center gap-1.5 text-sm text-slate-300">
                    <MapPin className="w-4 h-4 text-cyan-400" />
                    {inc.location}
                  </div>
                  <div className="flex items-center gap-1.5 text-sm text-slate-400">
                    <AlertTriangle className="w-4 h-4" />
                    Reported: {new Date(inc.reportedAt).toLocaleString()}
                  </div>
                </div>
              </div>
              
              <div className="md:w-48 flex flex-col justify-center gap-2 border-t md:border-t-0 md:border-l border-slate-700/50 pt-4 md:pt-0 md:pl-6">
                <button className="w-full py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 rounded-lg text-sm font-medium transition-colors">
                  View Details
                </button>
                <button className="w-full py-2 bg-cyan-600/20 border border-cyan-500/30 hover:bg-cyan-600/30 text-cyan-400 rounded-lg text-sm font-medium transition-colors">
                  Update Status
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
