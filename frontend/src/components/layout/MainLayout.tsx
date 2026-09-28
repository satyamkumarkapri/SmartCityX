import { Outlet, Link, useLocation } from 'react-router-dom';
import { 
  LayoutDashboard, Map, Car, Droplet, FileText, AlertTriangle, 
  Building2, Wifi, FileDigit, Activity, PieChart, ClipboardList, 
  Database, Users, Settings, LogOut, Search, Bell, Sun, MapPin, CheckCircle2
} from 'lucide-react';
import { cn } from '@/lib/utils';
import { useState, useEffect } from 'react';

export function MainLayout() {
  const location = useLocation();
  const [currentTime, setCurrentTime] = useState('');

  useEffect(() => {
    const updateTime = () => {
      const now = new Date();
      const options: Intl.DateTimeFormatOptions = { 
        weekday: 'short', day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit', hour12: true 
      };
      setCurrentTime(now.toLocaleString('en-US', options).replace(',', ''));
    };
    updateTime();
    const timer = setInterval(updateTime, 60000);
    return () => clearInterval(timer);
  }, []);

  const menuItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/' },
    { icon: Map, label: 'City Overview', path: '/overview' },
    { icon: Car, label: 'Traffic', path: '/traffic' },
    { icon: Droplet, label: 'Water & Energy', path: '/resources' },
    { icon: FileText, label: 'Service Requests', path: '/service-requests' },
    { icon: AlertTriangle, label: 'Emergency', path: '/emergency' },
    { icon: Building2, label: 'Infrastructure', path: '/infrastructure' },
    { icon: Wifi, label: 'IoT Sensors', path: '/iot' },
    { icon: FileDigit, label: 'Documents', path: '/documents' },
    { icon: Activity, label: 'Algorithm Lab', path: '/algorithm-lab' },
    { icon: PieChart, label: 'Analytics', path: '/analytics' },
    { icon: ClipboardList, label: 'Reports', path: '/reports' },
    { icon: Database, label: 'Data Management', path: '/data' },
    { icon: Users, label: 'Users', path: '/users' },
    { icon: Settings, label: 'Settings', path: '/settings' },
  ];

  return (
    <div className="flex h-screen bg-[#070b14] text-slate-300 font-sans overflow-hidden">
      {/* Sidebar */}
      <aside className="w-64 bg-[#0a101d] border-r border-slate-800 flex flex-col flex-shrink-0 z-20 shadow-[4px_0_24px_rgba(0,0,0,0.5)]">
        <div className="p-5 border-b border-slate-800">
          <div className="flex items-center gap-2 mb-1">
            <div className="flex items-end gap-0.5 text-blue-500">
              <div className="w-1.5 h-4 bg-blue-500 rounded-sm"></div>
              <div className="w-1.5 h-6 bg-cyan-400 rounded-sm"></div>
              <div className="w-1.5 h-5 bg-blue-600 rounded-sm"></div>
            </div>
            <h1 className="text-2xl font-bold text-white tracking-wide">SmartCity<span className="text-blue-500">X</span></h1>
          </div>
          <p className="text-[10px] text-slate-500 leading-tight">Integrated Smart-City<br/>Analytics Platform</p>
        </div>
        
        <nav className="flex-1 overflow-y-auto py-4 custom-scrollbar px-3 space-y-1">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={cn(
                "flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-all duration-200",
                location.pathname === item.path
                  ? "bg-blue-600 text-white font-medium shadow-[0_0_15px_rgba(37,99,235,0.4)]"
                  : "text-slate-400 hover:text-white hover:bg-slate-800"
              )}
            >
              <item.icon className={cn("w-5 h-5", location.pathname === item.path ? "text-white" : "text-slate-500")} />
              {item.label}
            </Link>
          ))}
        </nav>

        {/* System Status */}
        <div className="p-4 mx-3 mb-4 rounded-xl border border-slate-800 bg-slate-900/50">
          <h3 className="text-xs font-semibold text-slate-400 mb-3">System Status</h3>
          <div className="space-y-2 text-xs">
            <div className="flex justify-between items-center">
              <span className="flex items-center gap-2 text-slate-300"><div className="w-1.5 h-1.5 rounded-full bg-emerald-500 shadow-[0_0_5px_#10b981]"></div> Backend</span>
              <span className="text-emerald-500">Online</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="flex items-center gap-2 text-slate-300"><div className="w-1.5 h-1.5 rounded-full bg-emerald-500 shadow-[0_0_5px_#10b981]"></div> Database</span>
              <span className="text-emerald-500">Online</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="flex items-center gap-2 text-slate-300"><div className="w-1.5 h-1.5 rounded-full bg-emerald-500 shadow-[0_0_5px_#10b981]"></div> IoT Gateway</span>
              <span className="text-emerald-500">Online</span>
            </div>
            <div className="flex justify-between items-center">
              <span className="flex items-center gap-2 text-slate-300"><div className="w-1.5 h-1.5 rounded-full bg-emerald-500 shadow-[0_0_5px_#10b981]"></div> Analytics Engine</span>
              <span className="text-emerald-500">Online</span>
            </div>
          </div>
        </div>

        {/* User Profile */}
        <div className="p-4 border-t border-slate-800">
          <div className="flex items-center gap-3 mb-4">
            <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-blue-500 to-purple-500 flex items-center justify-center border-2 border-slate-700 overflow-hidden">
               <img src="https://i.pravatar.cc/150?u=satyam" alt="User" className="w-full h-full object-cover" />
            </div>
            <div className="flex-1 overflow-hidden">
              <p className="text-sm font-medium text-white truncate">Satyam Kumar Kapri</p>
              <p className="text-xs text-slate-500 truncate">Admin</p>
            </div>
          </div>
          <button className="flex items-center gap-2 text-slate-400 hover:text-white transition-colors text-sm w-full">
            <LogOut className="w-4 h-4" /> Logout
          </button>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex-1 flex flex-col min-w-0 bg-[#070b14] relative">
        {/* Background glow effects */}
        <div className="absolute top-0 left-1/4 w-96 h-96 bg-blue-600/10 rounded-full blur-[120px] pointer-events-none"></div>
        <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-purple-600/10 rounded-full blur-[120px] pointer-events-none"></div>
        
        {/* Topbar */}
        <header className="h-16 bg-[#0a101d]/80 backdrop-blur-md border-b border-slate-800 flex items-center justify-between px-6 z-10">
          <div className="flex-1 max-w-2xl flex items-center gap-4">
            <div className="relative w-full max-w-md">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
              <input 
                type="text" 
                placeholder="Search city data, documents, services, sensors..."
                className="w-full bg-slate-900/50 border border-slate-700/80 rounded-full pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:bg-slate-900 transition-all shadow-inner"
              />
            </div>
          </div>
          
          <div className="flex items-center gap-5">
            <div className="flex items-center gap-2 px-3 py-1.5 bg-slate-900/50 border border-slate-700/80 rounded-full text-sm text-slate-300">
              <MapPin className="w-4 h-4 text-cyan-400" />
              <span>Vijayawada</span>
            </div>
            
            <div className="relative cursor-pointer hover:text-white transition-colors">
              <Bell className="w-5 h-5 text-slate-400" />
              <span className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 rounded-full text-[10px] text-white flex items-center justify-center font-bold border border-[#0a101d]">5</span>
            </div>
            
            <button className="text-slate-400 hover:text-white transition-colors">
              <Sun className="w-5 h-5" />
            </button>
            
            <div className="h-6 w-px bg-slate-700 mx-1"></div>
            
            <div className="flex items-center gap-3">
              <div className="w-8 h-8 rounded-full bg-gradient-to-tr from-blue-500 to-purple-500 flex items-center justify-center overflow-hidden border border-slate-600">
                 <img src="https://i.pravatar.cc/150?u=satyam" alt="User" className="w-full h-full object-cover" />
              </div>
              <div className="text-right hidden md:block">
                <p className="text-[11px] text-slate-400 leading-tight">{currentTime.split(' ').slice(0, 4).join(' ')}</p>
                <p className="text-sm font-medium text-white leading-tight">{currentTime.split(' ').slice(4).join(' ')}</p>
              </div>
            </div>
          </div>
        </header>

        {/* Page Content */}
        <main className="flex-1 overflow-auto custom-scrollbar p-6 relative z-0">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
