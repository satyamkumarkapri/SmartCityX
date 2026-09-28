import { useState, useEffect } from 'react';
import { Map, Layers, Navigation, ZoomIn, Building2, Trees, Activity } from 'lucide-react';
import { cn } from '@/lib/utils';

export function CityOverview() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Simulate loading map tiles
    setTimeout(() => setLoading(false), 800);
  }, []);

  return (
    <div className="space-y-6 animate-in fade-in h-[calc(100vh-8rem)]">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">City Overview & Zones</h1>
          <p className="text-sm text-slate-400 mt-1">Geospatial visualization of smart city sectors</p>
        </div>
        <div className="flex gap-2">
          <button className="p-2 bg-slate-800 border border-slate-700 text-slate-300 hover:text-cyan-400 rounded-lg transition-colors">
            <Layers className="w-5 h-5" />
          </button>
          <button className="p-2 bg-slate-800 border border-slate-700 text-slate-300 hover:text-cyan-400 rounded-lg transition-colors">
            <Navigation className="w-5 h-5" />
          </button>
        </div>
      </div>

      <div className="relative w-full h-full rounded-xl border border-slate-700/50 bg-slate-900 overflow-hidden shadow-2xl flex flex-col md:flex-row">
        
        {/* Map visualization area */}
        <div className="flex-1 relative bg-[url('https://api.maptiler.com/maps/dataviz-dark/256/11/593/781.png?key=get_your_own_OpIi9ZULNHzrESv6T2vL')] bg-cover bg-center">
          <div className="absolute inset-0 bg-slate-900/60 mix-blend-multiply pointer-events-none"></div>
          
          {/* Overlay Grid */}
          <div className="absolute inset-0 bg-[linear-gradient(rgba(255,255,255,0.05)_1px,transparent_1px),linear-gradient(90deg,rgba(255,255,255,0.05)_1px,transparent_1px)] bg-[size:40px_40px] pointer-events-none opacity-50"></div>
          
          {loading ? (
            <div className="absolute inset-0 flex items-center justify-center bg-slate-900/80 backdrop-blur-sm z-20">
              <div className="flex flex-col items-center text-cyan-500">
                <Activity className="w-10 h-10 animate-spin mb-4" />
                <p className="text-sm font-medium animate-pulse">Rendering Geospatial Data...</p>
              </div>
            </div>
          ) : (
            <>
              {/* Interactive Map Nodes */}
              <div className="absolute top-[30%] left-[40%]">
                <div className="relative group cursor-pointer">
                  <div className="w-4 h-4 bg-cyan-500 rounded-full animate-ping absolute"></div>
                  <div className="w-4 h-4 bg-cyan-400 rounded-full relative z-10 border-2 border-slate-900"></div>
                  <div className="absolute top-6 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-opacity bg-slate-800 border border-slate-700 text-xs px-3 py-1.5 rounded whitespace-nowrap z-20 shadow-xl pointer-events-none">
                    <p className="font-bold text-cyan-400">Sector 7G</p>
                    <p className="text-slate-300">Commercial Hub</p>
                  </div>
                </div>
              </div>
              
              <div className="absolute top-[60%] left-[65%]">
                <div className="relative group cursor-pointer">
                  <div className="w-4 h-4 bg-emerald-500 rounded-full animate-ping absolute" style={{ animationDelay: '1s' }}></div>
                  <div className="w-4 h-4 bg-emerald-400 rounded-full relative z-10 border-2 border-slate-900"></div>
                  <div className="absolute top-6 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-opacity bg-slate-800 border border-slate-700 text-xs px-3 py-1.5 rounded whitespace-nowrap z-20 shadow-xl pointer-events-none">
                    <p className="font-bold text-emerald-400">Green Zone</p>
                    <p className="text-slate-300">Renewable Energy Farm</p>
                  </div>
                </div>
              </div>
              
              <div className="absolute top-[20%] left-[70%]">
                <div className="relative group cursor-pointer">
                  <div className="w-4 h-4 bg-red-500 rounded-full animate-ping absolute" style={{ animationDelay: '0.5s' }}></div>
                  <div className="w-4 h-4 bg-red-500 rounded-full relative z-10 border-2 border-slate-900"></div>
                  <div className="absolute top-6 left-1/2 -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-opacity bg-slate-800 border border-slate-700 text-xs px-3 py-1.5 rounded whitespace-nowrap z-20 shadow-xl pointer-events-none">
                    <p className="font-bold text-red-400">Industrial District</p>
                    <p className="text-slate-300">Alert: Heavy Traffic</p>
                  </div>
                </div>
              </div>
            </>
          )}
          
          <div className="absolute bottom-4 right-4 flex flex-col gap-2">
            <button className="p-2 bg-slate-800/80 backdrop-blur border border-slate-700 text-slate-200 hover:text-white rounded-lg shadow-lg">
              <ZoomIn className="w-5 h-5" />
            </button>
          </div>
        </div>

        {/* Sidebar Info */}
        <div className="w-full md:w-80 bg-slate-800/95 backdrop-blur border-l border-slate-700/50 p-6 flex flex-col gap-6 z-10">
          <div>
            <h3 className="font-medium text-slate-200 mb-4 flex items-center gap-2">
              <Map className="w-4 h-4 text-cyan-400" /> Map Legend
            </h3>
            <div className="space-y-3">
              <div className="flex items-center gap-3 text-sm text-slate-300">
                <div className="w-3 h-3 rounded-full bg-cyan-400"></div> Commercial Zones
              </div>
              <div className="flex items-center gap-3 text-sm text-slate-300">
                <div className="w-3 h-3 rounded-full bg-emerald-400"></div> Green Zones
              </div>
              <div className="flex items-center gap-3 text-sm text-slate-300">
                <div className="w-3 h-3 rounded-full bg-red-500"></div> Industrial & Alerts
              </div>
              <div className="flex items-center gap-3 text-sm text-slate-300">
                <div className="w-3 h-3 rounded-full bg-purple-400"></div> Residential Sectors
              </div>
            </div>
          </div>
          
          <div className="pt-6 border-t border-slate-700/50">
            <h3 className="font-medium text-slate-200 mb-4 flex items-center gap-2">
              <Building2 className="w-4 h-4 text-indigo-400" /> Infrastructure Health
            </h3>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-xs mb-1">
                  <span className="text-slate-400">Power Grid</span>
                  <span className="text-emerald-400 font-medium">Stable</span>
                </div>
                <div className="w-full bg-slate-900 rounded-full h-1.5">
                  <div className="bg-emerald-500 h-1.5 rounded-full w-[95%]"></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-xs mb-1">
                  <span className="text-slate-400">Water Supply</span>
                  <span className="text-emerald-400 font-medium">Optimal</span>
                </div>
                <div className="w-full bg-slate-900 rounded-full h-1.5">
                  <div className="bg-emerald-500 h-1.5 rounded-full w-[88%]"></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-xs mb-1">
                  <span className="text-slate-400">Road Network</span>
                  <span className="text-amber-400 font-medium">Congested</span>
                </div>
                <div className="w-full bg-slate-900 rounded-full h-1.5">
                  <div className="bg-amber-500 h-1.5 rounded-full w-[65%]"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
