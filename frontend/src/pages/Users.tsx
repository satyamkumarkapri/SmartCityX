import { useState, useEffect } from 'react';
import { Users as UsersIcon, Shield, Search, MoreVertical, Edit2, Trash2, Mail, CheckCircle2, XCircle, Plus, X } from 'lucide-react';
import { cn } from '@/lib/utils';

export function Users() {
  const [users, setUsers] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [newUser, setNewUser] = useState({ username: '', fullName: '', email: '', role: 'OPERATOR' });

  const fetchUsers = () => {
    setLoading(true);
    fetch('http://localhost:8080/api/users')
      .then(r => r.json())
      .then(data => setUsers(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleAddUser = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await fetch('http://localhost:8080/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          ...newUser,
          roles: [{ name: newUser.role }],
          enabled: true
        })
      });
      setShowModal(false);
      setNewUser({ username: '', fullName: '', email: '', role: 'OPERATOR' });
      fetchUsers();
    } catch (err) {
      console.error('Failed to add user', err);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Are you sure you want to remove this user?')) {
      await fetch(`http://localhost:8080/api/users/${id}`, { method: 'DELETE' });
      fetchUsers();
    }
  };

  const getRoleBadge = (roles: any[]) => {
    if (!roles || roles.length === 0) return 'bg-slate-500/10 text-slate-400';
    const role = roles[0].name;
    switch(role) {
      case 'SUPER_ADMIN': return 'bg-purple-500/10 text-purple-400 border-purple-500/20';
      case 'ADMIN': return 'bg-fuchsia-500/10 text-fuchsia-400 border-fuchsia-500/20';
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
        <button onClick={() => setShowModal(true)} className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors flex items-center gap-2">
          <Shield className="w-4 h-4" /> Add User
        </button>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input 
            type="text" 
            placeholder="Search by name, email, or role..."
            className="w-full bg-slate-800/50 border border-slate-700/50 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-blue-500"
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
                <th className="px-6 py-4 font-medium">Joined</th>
                <th className="px-6 py-4 font-medium text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-700/50">
              {loading ? (
                <tr><td colSpan={5} className="px-6 py-8 text-center text-slate-500">Loading users...</td></tr>
              ) : users.map((user) => (
                <tr key={user.id} className="hover:bg-slate-800/60 transition-colors">
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-slate-700 flex items-center justify-center text-slate-300 font-bold">
                        {user.fullName?.charAt(0) || user.username?.charAt(0) || 'U'}
                      </div>
                      <div>
                        <p className="font-medium text-slate-200">{user.fullName || user.username}</p>
                        <p className="text-xs text-slate-500 flex items-center gap-1 mt-0.5">
                          <Mail className="w-3 h-3" /> {user.email}
                        </p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4">
                    <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getRoleBadge(user.roles))}>
                      {user.roles?.[0]?.name || 'CITIZEN'}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-1.5">
                      {user.enabled ? (
                        <CheckCircle2 className="w-4 h-4 text-emerald-500" />
                      ) : (
                        <XCircle className="w-4 h-4 text-slate-500" />
                      )}
                      <span className={user.enabled ? 'text-emerald-400' : 'text-slate-400'}>
                        {user.enabled ? 'ACTIVE' : 'DISABLED'}
                      </span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-slate-400 text-xs font-mono">
                    {new Date(user.createdAt).toLocaleDateString()}
                  </td>
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      <button onClick={() => handleDelete(user.id)} className="p-1.5 text-slate-400 hover:text-red-400 hover:bg-red-400/10 rounded transition-colors">
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {showModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
          <div className="bg-slate-900 border border-slate-700 rounded-xl w-full max-w-md shadow-2xl">
            <div className="flex justify-between items-center p-5 border-b border-slate-800">
              <h2 className="text-xl font-bold text-slate-100">Add New User</h2>
              <button onClick={() => setShowModal(false)} className="text-slate-400 hover:text-white">
                <X className="w-5 h-5" />
              </button>
            </div>
            <form onSubmit={handleAddUser} className="p-5 space-y-4">
              <div>
                <label className="block text-sm font-medium text-slate-300 mb-1">Username</label>
                <input required type="text" value={newUser.username} onChange={e => setNewUser({...newUser, username: e.target.value})} className="w-full bg-slate-800 border border-slate-700 rounded-lg px-3 py-2 text-white focus:border-blue-500 outline-none" />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-300 mb-1">Full Name</label>
                <input required type="text" value={newUser.fullName} onChange={e => setNewUser({...newUser, fullName: e.target.value})} className="w-full bg-slate-800 border border-slate-700 rounded-lg px-3 py-2 text-white focus:border-blue-500 outline-none" />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-300 mb-1">Email</label>
                <input required type="email" value={newUser.email} onChange={e => setNewUser({...newUser, email: e.target.value})} className="w-full bg-slate-800 border border-slate-700 rounded-lg px-3 py-2 text-white focus:border-blue-500 outline-none" />
              </div>
              <div>
                <label className="block text-sm font-medium text-slate-300 mb-1">Role</label>
                <select value={newUser.role} onChange={e => setNewUser({...newUser, role: e.target.value})} className="w-full bg-slate-800 border border-slate-700 rounded-lg px-3 py-2 text-white focus:border-blue-500 outline-none">
                  <option value="OPERATOR">Operator</option>
                  <option value="DISPATCHER">Dispatcher</option>
                  <option value="FIELD_WORKER">Field Worker</option>
                  <option value="ADMIN">Admin</option>
                </select>
              </div>
              <div className="pt-4 flex justify-end gap-3">
                <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 bg-slate-800 hover:bg-slate-700 text-slate-300 rounded-lg">Cancel</button>
                <button type="submit" className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg">Create User</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
