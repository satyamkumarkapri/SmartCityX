import { Link, useLocation } from 'react-router-dom';
import { 
  LayoutDashboard, Map, Car, Droplet, FileText, 
  AlertTriangle, Building2, Wifi, FileDigit, Activity,
  Users, Database, Settings, LogOut
} from 'lucide-react';
import { cn } from '@/lib/utils';

const navigation = [
  { name: 'Dashboard', href: '/', icon: LayoutDashboard },
  { name: 'City Overview', href: '/overview', icon: Map },
  { name: 'Traffic', href: '/traffic', icon: Car },
  { name: 'Water & Energy', href: '/resources', icon: Droplet },
  { name: 'Service Requests', href: '/service-requests', icon: FileText },
  { name: 'Emergency', href: '/emergency', icon: AlertTriangle },
  { name: 'Infrastructure', href: '/infrastructure', icon: Building2 },
  { name: 'IoT Sensors', href: '/iot', icon: Wifi },
  { name: 'Documents', href: '/documents', icon: FileDigit },
  { name: 'Algorithm Lab', href: '/algorithm-lab', icon: Activity },
];

const bottomNavigation = [
  { name: 'Analytics', href: '/analytics', icon: Activity },
  { name: 'Reports', href: '/reports', icon: FileText },
  { name: 'Data Management', href: '/data', icon: Database },
  { name: 'Users', href: '/users', icon: Users },
  { name: 'Settings', href: '/settings', icon: Settings },
];

export function Sidebar() {
  const location = useLocation();

  return (
    <div className="flex flex-col w-64 bg-slate-900/60 backdrop-blur-xl border-r border-slate-700/50 h-screen sticky top-0 left-0 overflow-y-auto">
      <div className="flex items-center h-16 px-6 border-b border-slate-700/50 shrink-0">
        <span className="text-2xl font-bold bg-gradient-to-r from-cyan-400 to-blue-500 bg-clip-text text-transparent tracking-wider">
          SmartCityX
        </span>
      </div>

      <nav className="flex-1 px-4 py-6 space-y-1 overflow-y-auto custom-scrollbar">
        {navigation.map((item) => {
          const isActive = location.pathname === item.href || (item.href !== '/' && location.pathname.startsWith(item.href));
          return (
            <Link
              key={item.name}
              to={item.href}
              className={cn(
                "flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 group",
                isActive 
                  ? "bg-cyan-500/10 text-cyan-400 border border-cyan-500/20 shadow-[0_0_15px_rgba(6,182,212,0.15)]" 
                  : "text-slate-400 hover:text-slate-100 hover:bg-slate-800/50"
              )}
            >
              <item.icon className={cn("mr-3 h-5 w-5", isActive ? "text-cyan-400" : "text-slate-500 group-hover:text-slate-300")} />
              {item.name}
            </Link>
          );
        })}

        <div className="mt-8 mb-4">
          <h3 className="px-3 text-xs font-semibold text-slate-500 uppercase tracking-wider">Administration</h3>
        </div>

        {bottomNavigation.map((item) => {
          const isActive = location.pathname === item.href;
          return (
            <Link
              key={item.name}
              to={item.href}
              className={cn(
                "flex items-center px-3 py-2.5 text-sm font-medium rounded-lg transition-all duration-200 group",
                isActive 
                  ? "bg-cyan-500/10 text-cyan-400 border border-cyan-500/20" 
                  : "text-slate-400 hover:text-slate-100 hover:bg-slate-800/50"
              )}
            >
              <item.icon className={cn("mr-3 h-5 w-5", isActive ? "text-cyan-400" : "text-slate-500 group-hover:text-slate-300")} />
              {item.name}
            </Link>
          );
        })}
      </nav>

      <div className="p-4 border-t border-slate-700/50 shrink-0">
        <div className="flex items-center gap-3 p-3 bg-slate-800/40 rounded-xl border border-slate-700/50">
          <div className="w-10 h-10 rounded-full bg-gradient-to-br from-indigo-500 to-cyan-500 flex items-center justify-center text-white font-bold shrink-0">
            A
          </div>
          <div className="flex-1 overflow-hidden">
            <p className="text-sm font-medium text-white truncate">Admin User</p>
            <p className="text-xs text-slate-400 truncate">System Administrator</p>
          </div>
          <button className="text-slate-400 hover:text-red-400 transition-colors">
            <LogOut className="w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  );
}
