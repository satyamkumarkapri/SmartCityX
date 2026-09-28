import { useState, useEffect } from 'react';
import { FileText, Search, Plus, Filter, AlertTriangle } from 'lucide-react';
import { cn } from '@/lib/utils';

interface ServiceRequest {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  location: string;
  createdAt: string;
}

export function ServiceRequests() {
  const [requests, setRequests] = useState<ServiceRequest[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/requests')
      .then(r => r.json())
      .then(data => setRequests(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'CRITICAL': return 'bg-red-500/10 text-red-400 border-red-500/20';
      case 'HIGH': return 'bg-orange-500/10 text-orange-400 border-orange-500/20';
      case 'MEDIUM': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      default: return 'bg-blue-500/10 text-blue-400 border-blue-500/20';
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'OPEN': return 'bg-blue-500/10 text-blue-400 border-blue-500/20';
      case 'IN_PROGRESS': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      case 'RESOLVED': return 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20';
      case 'CLOSED': return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Service Requests</h1>
          <p className="text-sm text-slate-400 mt-1">Manage public service reports and maintenance tickets</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-cyan-500 hover:bg-cyan-600 text-slate-950 font-medium rounded-lg transition-colors">
          <Plus className="w-4 h-4" /> New Request
        </button>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input 
            type="text" 
            placeholder="Search tickets by title, ID, or location..."
            className="w-full bg-slate-800/50 border border-slate-700/50 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-cyan-500"
          />
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-slate-800/50 border border-slate-700/50 text-slate-300 hover:text-slate-100 rounded-lg transition-colors">
          <Filter className="w-4 h-4" /> Filter
        </button>
      </div>

      <div className="bg-slate-800/40 border border-slate-700/50 rounded-xl overflow-hidden backdrop-blur-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-900/50 text-slate-400 border-b border-slate-700/50">
              <tr>
                <th className="px-6 py-4 font-medium">ID</th>
                <th className="px-6 py-4 font-medium">Title</th>
                <th className="px-6 py-4 font-medium">Location</th>
                <th className="px-6 py-4 font-medium">Priority</th>
                <th className="px-6 py-4 font-medium">Status</th>
                <th className="px-6 py-4 font-medium">Created</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-700/50">
              {loading ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-slate-500">
                    Loading requests...
                  </td>
                </tr>
              ) : requests.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-slate-500">
                    No service requests found.
                  </td>
                </tr>
              ) : (
                requests.map((req) => (
                  <tr key={req.id} className="hover:bg-slate-800/60 transition-colors">
                    <td className="px-6 py-4 font-mono text-slate-400">#{req.id}</td>
                    <td className="px-6 py-4 text-slate-200 font-medium">{req.title}</td>
                    <td className="px-6 py-4 text-slate-400">{req.location}</td>
                    <td className="px-6 py-4">
                      <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getPriorityColor(req.priority))}>
                        {req.priority}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getStatusColor(req.status))}>
                        {req.status ? req.status.replace('_', ' ') : 'UNKNOWN'}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-slate-400">
                      {new Date(req.createdAt).toLocaleDateString()}
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
