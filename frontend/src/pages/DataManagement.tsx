import { useState } from 'react';
import { Database, Server, HardDrive, RefreshCw, AlertTriangle, ShieldCheck } from 'lucide-react';

export function DataManagement() {
  const [seeding, setSeeding] = useState(false);
  const [clearing, setClearing] = useState(false);

  const handleSeed = () => {
    setSeeding(true);
    setTimeout(() => setSeeding(false), 2000);
  };

  const handleClear = () => {
    setClearing(true);
    setTimeout(() => setClearing(false), 2000);
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Database Management</h1>
          <p className="text-sm text-slate-400 mt-1">Control panel for Neon Cloud PostgreSQL and Seed Data</p>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        <div className="lg:col-span-2 space-y-6">
          <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
            <h3 className="text-lg font-medium text-slate-200 mb-4 flex items-center gap-2">
              <Server className="w-5 h-5 text-emerald-400" /> Connection Status
            </h3>
            
            <div className="space-y-4">
              <div className="flex items-center justify-between p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <Database className="w-5 h-5 text-emerald-500" />
                  <div>
                    <p className="text-sm font-medium text-slate-200">Neon Serverless Postgres</p>
                    <p className="text-xs text-slate-500 font-mono mt-0.5">ep-still-forest-atigo975-pooler.c-9.us-east-1.aws.neon.tech</p>
                  </div>
                </div>
                <span className="px-3 py-1 bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-xs font-medium rounded-full flex items-center gap-1.5">
                  <span className="w-1.5 h-1.5 bg-emerald-400 rounded-full animate-pulse"></span> Connected
                </span>
              </div>
              
              <div className="flex items-center justify-between p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                <div className="flex items-center gap-3">
                  <ShieldCheck className="w-5 h-5 text-blue-500" />
                  <div>
                    <p className="text-sm font-medium text-slate-200">Connection Encryption</p>
                    <p className="text-xs text-slate-500 font-mono mt-0.5">SSLmode=require | Role: default_user</p>
                  </div>
                </div>
                <span className="px-3 py-1 bg-blue-500/10 text-blue-400 border border-blue-500/20 text-xs font-medium rounded-full">
                  Secured
                </span>
              </div>
            </div>
          </div>

          <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
            <h3 className="text-lg font-medium text-slate-200 mb-4 flex items-center gap-2">
              <HardDrive className="w-5 h-5 text-cyan-400" /> Storage Metrics
            </h3>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-slate-400">Total Assigned Compute</span>
                  <span className="text-slate-200 font-medium">1/4 CU</span>
                </div>
                <div className="w-full bg-slate-900 rounded-full h-2">
                  <div className="bg-cyan-500 h-2 rounded-full w-1/4"></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-sm mb-1">
                  <span className="text-slate-400">Storage Size</span>
                  <span className="text-slate-200 font-medium">42.5 MB / 5 GB</span>
                </div>
                <div className="w-full bg-slate-900 rounded-full h-2">
                  <div className="bg-purple-500 h-2 rounded-full w-[2%]"></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="space-y-6">
          <div className="p-6 rounded-xl border border-red-500/20 bg-red-500/5 backdrop-blur-sm">
            <h3 className="text-lg font-medium text-red-400 mb-2 flex items-center gap-2">
              <AlertTriangle className="w-5 h-5" /> Danger Zone
            </h3>
            <p className="text-xs text-slate-400 mb-6">
              These actions will directly affect the production database and cannot be undone.
            </p>

            <div className="space-y-4">
              <button 
                onClick={handleSeed}
                disabled={seeding}
                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-slate-800 hover:bg-slate-700 border border-slate-700 text-slate-200 font-medium rounded-lg transition-colors disabled:opacity-50"
              >
                <RefreshCw className={`w-4 h-4 ${seeding ? 'animate-spin text-cyan-400' : ''}`} /> 
                {seeding ? 'Seeding Data...' : 'Run Data Seeder'}
              </button>
              
              <button 
                onClick={handleClear}
                disabled={clearing}
                className="w-full flex items-center justify-center gap-2 px-4 py-3 bg-red-600/20 hover:bg-red-600/30 border border-red-500/30 text-red-400 font-medium rounded-lg transition-colors disabled:opacity-50"
              >
                {clearing ? 'Clearing Cache...' : 'Flush Redis Cache'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
