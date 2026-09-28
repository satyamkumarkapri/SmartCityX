import { useState, useEffect } from 'react';
import { Users as UsersIcon, Shield, Search, MoreVertical, Edit2, Trash2, Mail, CheckCircle2, XCircle } from 'lucide-react';
import { cn } from '@/lib/utils';

export function Users() {
  const [users, setUsers] = useState([
    { id: 1, name: 'Admin System', username: 'admin', email: 'admin@smartcityx.gov', role: 'SUPER_ADMIN', status: 'ACTIVE', lastLogin: 'Just now' },
    { id: 2, name: 'Traffic Operator', username: 'traffic_ops', email: 'traffic@smartcityx.gov', role: 'OPERATOR', status: 'ACTIVE', lastLogin: '2 hours ago' },
    { id: 3, name: 'Emergency Dispatch', username: 'dispatch_911', email: 'emergency@smartcityx.gov', role: 'DISPATCHER', status: 'ACTIVE', lastLogin: '5 mins ago' },
    { id: 4, name: 'Water Grid AI', username: 'ai_water_node', email: 'service@smartcityx.gov', role: 'SYSTEM', status: 'ACTIVE', lastLogin: '1 min ago' },
    { id: 5, name: 'Maintenance Crew 4', username: 'm_crew4', email: 'mcrew4@smartcityx.gov', role: 'FIELD_WORKER', status: 'INACTIVE', lastLogin: '2 days ago' },
  ]);

  const getRoleBadge = (role: string) => {
    switch(role) {
      case 'SUPER_ADMIN': return 'bg-purple-500/10 text-purple-400 border-purple-500/20';
      case 'OPERATOR': return 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20';
      case 'DISPATCHER': return 'bg-red-500/10 text-red-400 border-red-500/20';
      case 'SYSTEM': return 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">User Administration</h1>
          <p className="text-sm text-slate-400 mt-1">Manage system access, roles, and security policies</p>
        </div>
        <button className="px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-lg transition-colors flex items-center gap-2">
          <Shield className="w-4 h-4" /> Add User
        </button>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input 
            type="text" 
            placeholder="Search by name, email, or role..."
            className="w-full bg-slate-800/50 border border-slate-700/50 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
      </div>

      <div className="bg-slate-800/40 border border-slate-700/50 rounded-xl overflow-hidden backdrop-blur-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-900/50 text-slate-400 border-b border-slate-700/50">
              <tr>
                <th className="px-6 py-4 font-medium">User Profile</th>
                <th className="px-6 py-4 font-medium">System Role</th>
                <th className="px-6 py-4 font-medium">Status</th>
                <th className="px-6 py-4 font-medium">Last Login</th>
                <th className="px-6 py-4 font-medium text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-700/50">
              {users.map((user) => (
                <tr key={user.id} className="hover:bg-slate-800/60 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-slate-700 flex items-center justify-center text-slate-300 font-bold">
                        {user.name.charAt(0)}
                      </div>
                      <div>
                        <p className="font-medium text-slate-200">{user.name}</p>
                        <p className="text-xs text-slate-500 flex items-center gap-1 mt-0.5">
                          <Mail className="w-3 h-3" /> {user.email}
                        </p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getRoleBadge(user.role))}>
                      {user.role}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5">
                      {user.status === 'ACTIVE' ? (
                        <CheckCircle2 className="w-4 h-4 text-emerald-500" />
                      ) : (
                        <XCircle className="w-4 h-4 text-slate-500" />
                      )}
                      <span className={user.status === 'ACTIVE' ? 'text-emerald-400' : 'text-slate-400'}>
                        {user.status}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-400 text-xs font-mono">
                    {user.lastLogin}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button className="p-1.5 text-slate-400 hover:text-cyan-400 hover:bg-cyan-400/10 rounded transition-colors">
                        <Edit2 className="w-4 h-4" />
                      </button>
                      <button className="p-1.5 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded transition-colors">
                        <Trash2 className="w-4 h-4" />
                      </button>
                      <button className="p-1.5 text-slate-400 hover:text-slate-200 hover:bg-slate-700 rounded transition-colors">
                        <MoreVertical className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
