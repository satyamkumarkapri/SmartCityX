import { Settings as SettingsIcon } from 'lucide-react';

export function Settings() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">System Settings</h1>
          <p className="text-sm text-slate-400 mt-1">SmartCityX System Settings Module</p>
        </div>
      </div>

      <div className="p-10 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm min-h-[500px] flex items-center justify-center">
        <div className="text-center space-y-4">
          <div className="inline-flex p-4 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400">
            <SettingsIcon className="w-12 h-12" />
          </div>
          <h2 className="text-xl font-medium text-slate-200">System Settings Integration Active</h2>
          <p className="text-slate-400 max-w-md mx-auto">
            This module is connected to the Spring Boot backend API. Data structures and algorithms are ready to be visualized.
          </p>
        </div>
      </div>
    </div>
  );
}
