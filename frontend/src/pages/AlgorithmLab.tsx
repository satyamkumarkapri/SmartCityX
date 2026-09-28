import { useState } from 'react';
import { Activity, Search, Code, Cpu, Database } from 'lucide-react';
import { cn } from '@/lib/utils';

export function AlgorithmLab() {
  const [activeTab, setActiveTab] = useState('m1');
  const [text, setText] = useState('water leakage emergency near central road, water pipe broke');
  const [pattern, setPattern] = useState('water');
  const [results, setResults] = useState<any>(null);
  const [loading, setLoading] = useState(false);

  const runAlgorithm = async (algo: string) => {
    setLoading(true);
    try {
      const res = await fetch(`http://localhost:8080/api/algorithms/${algo}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ text, pattern })
      });
      const data = await res.json();
      setResults(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const highlightMatches = (text: string, positions: number[], patternLen: number) => {
    if (!positions || positions.length === 0) return text;
    
    let highlighted = [];
    let lastIndex = 0;
    
    positions.forEach((pos, i) => {
      highlighted.push(<span key={`text-${i}`}>{text.substring(lastIndex, pos)}</span>);
      highlighted.push(<span key={`match-${i}`} className="bg-cyan-500/30 text-cyan-300 px-1 rounded">{text.substring(pos, pos + patternLen)}</span>);
      lastIndex = pos + patternLen;
    });
    
    highlighted.push(<span key={`text-end`}>{text.substring(lastIndex)}</span>);
    return <>{highlighted}</>;
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Algorithm Laboratory</h1>
          <p className="text-sm text-slate-400 mt-1">Interactive DSA execution environment for SmartCityX</p>
        </div>
      </div>

      <div className="flex gap-2 border-b border-slate-700/50 pb-2 overflow-x-auto custom-scrollbar">
        {[
          { id: 'm1', name: 'M1: String Search' },
          { id: 'm2', name: 'M2: Suffix Structures' },
          { id: 'm3', name: 'M3: Dynamic Programming' },
          { id: 'm4', name: 'M4: Network Flow' },
          { id: 'm5', name: 'M5: NP-Complete' },
          { id: 'm6', name: 'M6: Parallel/Randomized' }
        ].map(tab => (
          <button
            key={tab.id}
            onClick={() => { setActiveTab(tab.id); setResults(null); }}
            className={cn(
              "px-4 py-2 rounded-lg text-sm font-medium transition-all whitespace-nowrap",
              activeTab === tab.id 
                ? "bg-cyan-500/10 text-cyan-400 border border-cyan-500/20" 
                : "text-slate-400 hover:text-slate-200 hover:bg-slate-800/50"
            )}
          >
            {tab.name}
          </button>
        ))}
      </div>

      {activeTab === 'm1' && (
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 animate-in fade-in">
          <div className="lg:col-span-1 space-y-4">
            <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
              <h3 className="font-medium text-slate-200 mb-4 flex items-center gap-2">
                <Search className="w-4 h-4 text-cyan-400" />
                Input Data
              </h3>
              <div className="space-y-4">
                <div>
                  <label className="block text-xs font-medium text-slate-400 mb-1">Target Document / Text</label>
                  <textarea 
                    value={text}
                    onChange={(e) => setText(e.target.value)}
                    className="w-full bg-slate-900/50 border border-slate-700/50 rounded-lg p-3 text-sm text-slate-200 focus:ring-1 focus:ring-cyan-500 h-32"
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-slate-400 mb-1">Search Pattern</label>
                  <input 
                    type="text"
                    value={pattern}
                    onChange={(e) => setPattern(e.target.value)}
                    className="w-full bg-slate-900/50 border border-slate-700/50 rounded-lg p-3 text-sm text-slate-200 focus:ring-1 focus:ring-cyan-500"
                  />
                </div>
              </div>
            </div>

            <div className="p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm">
              <h3 className="font-medium text-slate-200 mb-4 flex items-center gap-2">
                <Cpu className="w-4 h-4 text-indigo-400" />
                Execute Algorithm
              </h3>
              <div className="flex flex-col gap-2">
                <button onClick={() => runAlgorithm('kmp')} disabled={loading} className="px-4 py-2 bg-indigo-500/20 hover:bg-indigo-500/30 text-indigo-300 border border-indigo-500/30 rounded-lg text-sm text-left flex justify-between items-center transition-colors">
                  Knuth-Morris-Pratt (KMP) <span>O(n + m)</span>
                </button>
                <button onClick={() => runAlgorithm('rabin-karp')} disabled={loading} className="px-4 py-2 bg-emerald-500/20 hover:bg-emerald-500/30 text-emerald-300 border border-emerald-500/30 rounded-lg text-sm text-left flex justify-between items-center transition-colors">
                  Rabin-Karp <span>O(n + m) avg</span>
                </button>
                <button onClick={() => runAlgorithm('z')} disabled={loading} className="px-4 py-2 bg-amber-500/20 hover:bg-amber-500/30 text-amber-300 border border-amber-500/30 rounded-lg text-sm text-left flex justify-between items-center transition-colors">
                  Z Algorithm <span>O(n + m)</span>
                </button>
              </div>
            </div>
          </div>

          <div className="lg:col-span-2 space-y-4">
            <div className="p-6 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm min-h-[500px]">
              <h3 className="font-medium text-slate-200 mb-6 flex items-center gap-2">
                <Code className="w-4 h-4 text-cyan-400" />
                Execution Results
              </h3>
              
              {loading ? (
                <div className="flex flex-col items-center justify-center h-64 space-y-4 text-slate-400">
                  <Activity className="w-8 h-8 animate-spin text-cyan-500" />
                  <p>Processing text of length {text.length}...</p>
                </div>
              ) : results ? (
                <div className="space-y-6">
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div className="p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                      <p className="text-xs text-slate-400">Matches Found</p>
                      <p className="text-xl font-mono text-cyan-400 mt-1">{results.matchCount || results.matchPositions?.length || 0}</p>
                    </div>
                    <div className="p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                      <p className="text-xs text-slate-400">Execution Time</p>
                      <p className="text-xl font-mono text-emerald-400 mt-1">{results.executionTimeMs} ms</p>
                    </div>
                    {results.comparisons !== undefined && (
                      <div className="p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                        <p className="text-xs text-slate-400">Comparisons</p>
                        <p className="text-xl font-mono text-amber-400 mt-1">{results.comparisons}</p>
                      </div>
                    )}
                    {results.spuriousHits !== undefined && (
                      <div className="p-4 bg-slate-900/50 rounded-lg border border-slate-700/50">
                        <p className="text-xs text-slate-400">Spurious Hits</p>
                        <p className="text-xl font-mono text-red-400 mt-1">{results.spuriousHits}</p>
                      </div>
                    )}
                  </div>

                  <div>
                    <h4 className="text-sm font-medium text-slate-300 mb-2">Visualized Output</h4>
                    <div className="p-4 bg-slate-950 rounded-lg border border-slate-800 font-mono text-sm leading-loose break-words text-slate-300">
                      {highlightMatches(text, results.matchPositions, pattern.length)}
                    </div>
                  </div>

                  <div>
                    <h4 className="text-sm font-medium text-slate-300 mb-2">Raw Data / Arrays</h4>
                    <pre className="p-4 bg-slate-950 rounded-lg border border-slate-800 font-mono text-xs overflow-x-auto text-slate-400">
                      {JSON.stringify(results, null, 2)}
                    </pre>
                  </div>
                </div>
              ) : (
                <div className="flex flex-col items-center justify-center h-64 text-slate-500">
                  <Database className="w-12 h-12 mb-3 opacity-20" />
                  <p>Select an algorithm on the left to execute.</p>
                </div>
              )}
            </div>
          </div>
        </div>
      )}
      
      {activeTab !== 'm1' && (
         <div className="p-10 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm min-h-[400px] flex items-center justify-center">
            <p className="text-slate-400">Module {activeTab.toUpperCase()} algorithms are available in the API. UI is under construction.</p>
         </div>
      )}
    </div>
  );
}
