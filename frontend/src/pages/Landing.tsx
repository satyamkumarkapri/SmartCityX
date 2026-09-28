import { Link } from 'react-router-dom';
import { ArrowRight, Activity, Shield, Zap, Database } from 'lucide-react';

export function Landing() {
  return (
    <div className="min-h-screen bg-[#070b14] text-white overflow-hidden font-sans">
      {/* Background glow effects */}
      <div className="absolute top-0 left-1/4 w-96 h-96 bg-blue-600/10 rounded-full blur-[120px] pointer-events-none"></div>
      <div className="absolute bottom-0 right-1/4 w-96 h-96 bg-purple-600/10 rounded-full blur-[120px] pointer-events-none"></div>
      
      {/* Navbar */}
      <nav className="relative z-10 flex items-center justify-between px-8 py-6 max-w-7xl mx-auto">
        <div className="flex items-center gap-2">
          <div className="flex items-end gap-0.5 text-blue-500">
            <div className="w-2 h-5 bg-blue-500 rounded-sm"></div>
            <div className="w-2 h-7 bg-cyan-400 rounded-sm"></div>
            <div className="w-2 h-6 bg-blue-600 rounded-sm"></div>
          </div>
          <span className="text-2xl font-bold tracking-wide">SmartCity<span className="text-blue-500">X</span></span>
        </div>
        <div className="flex gap-4">
          <Link to="/login" className="px-5 py-2.5 text-sm font-medium text-slate-300 hover:text-white transition-colors">
            Log In
          </Link>
          <Link to="/signup" className="px-5 py-2.5 text-sm font-medium bg-blue-600 hover:bg-blue-700 rounded-full transition-all shadow-[0_0_20px_rgba(37,99,235,0.4)]">
            Sign Up
          </Link>
        </div>
      </nav>

      {/* Hero Section */}
      <main className="relative z-10 max-w-7xl mx-auto px-8 pt-20 pb-32 flex flex-col items-center text-center">
        <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/10 border border-blue-500/20 text-blue-400 text-sm mb-8 animate-in fade-in slide-in-from-bottom-4 duration-700">
          <span className="w-2 h-2 rounded-full bg-blue-500 animate-pulse"></span>
          SmartCityX 2.0 is now live
        </div>
        
        <h1 className="text-5xl md:text-7xl font-bold tracking-tight mb-8 animate-in fade-in slide-in-from-bottom-8 duration-700 delay-100 max-w-4xl">
          The Operating System for <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-cyan-300">Modern Cities</span>
        </h1>
        
        <p className="text-lg md:text-xl text-slate-400 max-w-2xl mb-12 animate-in fade-in slide-in-from-bottom-8 duration-700 delay-200">
          Integrate IoT sensors, emergency dispatch, traffic algorithms, and public services into a single, unified command center.
        </p>
        
        <div className="flex gap-4 animate-in fade-in slide-in-from-bottom-8 duration-700 delay-300">
          <Link to="/signup" className="flex items-center gap-2 px-8 py-4 bg-white text-slate-900 rounded-full font-bold hover:bg-slate-200 transition-colors">
            Get Started <ArrowRight className="w-5 h-5" />
          </Link>
          <Link to="/login" className="flex items-center gap-2 px-8 py-4 bg-slate-800 border border-slate-700 rounded-full font-bold hover:bg-slate-700 transition-colors">
            View Demo Dashboard
          </Link>
        </div>

        {/* Feature Grid */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-32 w-full animate-in fade-in slide-in-from-bottom-12 duration-1000 delay-500">
          <div className="p-6 rounded-2xl bg-slate-900/50 border border-slate-800 backdrop-blur-sm text-left">
            <div className="w-12 h-12 rounded-xl bg-cyan-500/10 flex items-center justify-center mb-6">
              <Activity className="w-6 h-6 text-cyan-400" />
            </div>
            <h3 className="text-xl font-bold mb-2">Live IoT Telemetry</h3>
            <p className="text-slate-400 text-sm leading-relaxed">Connect thousands of city sensors for real-time traffic, water, and energy monitoring on a live GIS map.</p>
          </div>
          
          <div className="p-6 rounded-2xl bg-slate-900/50 border border-slate-800 backdrop-blur-sm text-left">
            <div className="w-12 h-12 rounded-xl bg-purple-500/10 flex items-center justify-center mb-6">
              <Shield className="w-6 h-6 text-purple-400" />
            </div>
            <h3 className="text-xl font-bold mb-2">Emergency Dispatch</h3>
            <p className="text-slate-400 text-sm leading-relaxed">Automated incident routing and emergency vehicle tracking to ensure rapid response times across all zones.</p>
          </div>

          <div className="p-6 rounded-2xl bg-slate-900/50 border border-slate-800 backdrop-blur-sm text-left">
            <div className="w-12 h-12 rounded-xl bg-blue-500/10 flex items-center justify-center mb-6">
              <Database className="w-6 h-6 text-blue-400" />
            </div>
            <h3 className="text-xl font-bold mb-2">Algorithm Lab</h3>
            <p className="text-slate-400 text-sm leading-relaxed">Deploy custom graph algorithms to optimize city infrastructure flow, identify bottlenecks, and plan upgrades.</p>
          </div>
        </div>
      </main>
    </div>
  );
}
