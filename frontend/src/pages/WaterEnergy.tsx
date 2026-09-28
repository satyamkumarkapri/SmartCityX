import { useState, useEffect } from 'react';
import { Droplet, Zap, Battery, AlertCircle, RefreshCw } from 'lucide-react';
import { cn } from '@/lib/utils';

interface Resource {
  id: number;
  resourceId: string;
  type: string;
  capacity: number;
  currentLevel: number;
  location: string;
  status: string;
  lastUpdated: string;
}

export function WaterEnergy() {
  const [resources, setResources] = useState<Resource[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchResources = () => {
    setLoading(true);
    fetch('http://localhost:8080/api/resources')
      .then(r => r.json())
      .then(data => setResources(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchResources();
  }, []);

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'AVAILABLE': return 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20';
      case 'DEPLETED': return 'bg-red-500/10 text-red-400 border-red-500/20';
      case 'MAINTENANCE': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  const waterResources = resources.filter(r => r.type === 'WATER');
  const energyResources = resources.filter(r => r.type === 'ENERGY');

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Water & Energy Resources</h1>
          <p className="text-sm text-slate-400 mt-1">Real-time monitoring of city utility grids</p>
        </div>
        <button 
          onClick={fetchResources}
          className="flex items-center gap-2 px-4 py-2 bg-slate-800 border border-slate-700 hover:bg-slate-700 text-slate-200 font-medium rounded-lg transition-colors"
        >
          <RefreshCw className={cn("w-4 h-4", loading && "animate-spin")} /> Refresh Data
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="space-y-4">
          <div className="flex items-center gap-3 mb-4">
            <div className="p-2 bg-blue-500/10 border border-blue-500/20 rounded-lg text-blue-400">
              <Droplet className="w-6 h-6" />
            </div>
            <h2 className="text-lg font-medium text-slate-200">Water Supply Network</h2>
          </div>
          
          {loading ? (
            <div className="h-40 flex items-center justify-center text-slate-500">Loading water resources...</div>
          ) : waterResources.length === 0 ? (
            <div className="h-40 flex items-center justify-center text-slate-500">No water resources found.</div>
          ) : (
            waterResources.map(resource => (
              <div key={resource.id} className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-slate-200 font-medium">{resource.resourceId}</h3>
                    <p className="text-xs text-slate-400 mt-1">{resource.location}</p>
                  </div>
                  <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getStatusColor(resource.status))}>
                    {resource.status}
                  </span>
                </div>
                
                <div className="space-y-2">
                  <div className="flex justify-between text-xs">
                    <span className="text-slate-400">Capacity Level</span>
                    <span className="text-slate-200 font-medium">{(resource.currentLevel / resource.capacity * 100).toFixed(1)}%</span>
                  </div>
                  <div className="w-full bg-slate-900 rounded-full h-2 overflow-hidden border border-slate-700/50">
                    <div 
                      className="bg-blue-500 h-full rounded-full transition-all duration-1000" 
                      style={{ width: `${(resource.currentLevel / resource.capacity) * 100}%` }}
                    />
                  </div>
                  <div className="flex justify-between text-xs text-slate-500 pt-1">
                    <span>{resource.currentLevel.toLocaleString()} Gal</span>
                    <span>{resource.capacity.toLocaleString()} Gal</span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        <div className="space-y-4">
          <div className="flex items-center gap-3 mb-4">
            <div className="p-2 bg-amber-500/10 border border-amber-500/20 rounded-lg text-amber-400">
              <Zap className="w-6 h-6" />
            </div>
            <h2 className="text-lg font-medium text-slate-200">Power Grid Status</h2>
          </div>
          
          {loading ? (
            <div className="h-40 flex items-center justify-center text-slate-500">Loading energy resources...</div>
          ) : energyResources.length === 0 ? (
            <div className="h-40 flex items-center justify-center text-slate-500">No energy resources found.</div>
          ) : (
            energyResources.map(resource => (
              <div key={resource.id} className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
                <div className="flex justify-between items-start mb-4">
                  <div>
                    <h3 className="text-slate-200 font-medium">{resource.resourceId}</h3>
                    <p className="text-xs text-slate-400 mt-1">{resource.location}</p>
                  </div>
                  <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getStatusColor(resource.status))}>
                    {resource.status}
                  </span>
                </div>
                
                <div className="space-y-2">
                  <div className="flex justify-between text-xs">
                    <span className="text-slate-400">Power Output</span>
                    <span className="text-slate-200 font-medium">{(resource.currentLevel / resource.capacity * 100).toFixed(1)}%</span>
                  </div>
                  <div className="w-full bg-slate-900 rounded-full h-2 overflow-hidden border border-slate-700/50">
                    <div 
                      className="bg-amber-500 h-full rounded-full transition-all duration-1000" 
                      style={{ width: `${(resource.currentLevel / resource.capacity) * 100}%` }}
                    />
                  </div>
                  <div className="flex justify-between text-xs text-slate-500 pt-1">
                    <span>{resource.currentLevel.toLocaleString()} MW</span>
                    <span>{resource.capacity.toLocaleString()} MW</span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
