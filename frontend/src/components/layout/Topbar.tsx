import { Bell, Search, Sun, MapPin } from 'lucide-react';

export function Topbar() {
  return (
    <div className="h-16 flex items-center justify-between px-6 bg-slate-900/60 backdrop-blur-xl border-b border-slate-700/50 sticky top-0 z-10">
      <div className="flex items-center gap-4 flex-1">
        <div className="relative w-96">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Search className="h-4 w-4 text-slate-500" />
          </div>
          <input
            type="text"
            className="block w-full pl-10 pr-3 py-2 border border-slate-700/50 rounded-lg leading-5 bg-slate-800/50 text-slate-300 placeholder-slate-500 focus:outline-none focus:ring-1 focus:ring-cyan-500 focus:border-cyan-500 sm:text-sm transition-all"
            placeholder="Search across city records, documents, sensors..."
          />
        </div>
        
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-slate-800/50 border border-slate-700/50 text-sm text-slate-300">
          <MapPin className="w-4 h-4 text-cyan-400" />
          <span>Central District</span>
        </div>
      </div>

      <div className="flex items-center gap-4">
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-emerald-500/10 border border-emerald-500/20 text-sm text-emerald-400">
          <div className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></div>
          System Online
        </div>
        
        <div className="text-sm text-slate-400 pr-2 border-r border-slate-700/50">
          {new Date().toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })}
        </div>

        <button className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-full transition-colors relative">
          <Bell className="w-5 h-5" />
          <span className="absolute top-1 right-1 w-2 h-2 rounded-full bg-red-500 border-2 border-slate-900"></span>
        </button>
        
        <button className="p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-full transition-colors">
          <Sun className="w-5 h-5" />
        </button>
      </div>
    </div>
  );
}
