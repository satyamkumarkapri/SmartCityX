import { useState, useEffect } from 'react';
import { FileDigit, Search, Download, File, FolderOpen, Calendar } from 'lucide-react';
import { cn } from '@/lib/utils';

interface CityDocument {
  id: number;
  title: string;
  docType: string;
  department: string;
  content: string;
  uploadedAt: string;
}

export function Documents() {
  const [documents, setDocuments] = useState<CityDocument[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/documents')
      .then(r => r.json())
      .then(data => setDocuments(data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'POLICY': return 'bg-indigo-500/10 text-indigo-400 border-indigo-500/20';
      case 'REPORT': return 'bg-cyan-500/10 text-cyan-400 border-cyan-500/20';
      case 'CONTRACT': return 'bg-amber-500/10 text-amber-400 border-amber-500/20';
      case 'BLUEPRINT': return 'bg-purple-500/10 text-purple-400 border-purple-500/20';
      default: return 'bg-slate-500/10 text-slate-400 border-slate-500/20';
    }
  };

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">Document Repository</h1>
          <p className="text-sm text-slate-400 mt-1">Indexed city records and operational documents</p>
        </div>
      </div>

      <div className="flex gap-4 mb-6">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-500" />
          <input 
            type="text" 
            placeholder="Search documents by title, content, or department..."
            className="w-full bg-slate-800/50 border border-slate-700/50 rounded-lg pl-10 pr-4 py-2 text-sm text-slate-200 focus:outline-none focus:ring-1 focus:ring-indigo-500"
          />
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {loading ? (
          <div className="col-span-full h-40 flex items-center justify-center text-slate-500">
            <FileDigit className="w-8 h-8 animate-pulse mr-3" /> Loading documents...
          </div>
        ) : documents.length === 0 ? (
          <div className="col-span-full h-40 flex items-center justify-center text-slate-500">
            No documents found.
          </div>
        ) : (
          documents.map(doc => (
            <div key={doc.id} className="group p-5 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm hover:bg-slate-800/80 transition-all cursor-pointer">
              <div className="flex justify-between items-start mb-3">
                <div className="p-2.5 bg-slate-900/50 rounded-lg text-indigo-400 border border-slate-700/50">
                  <File className="w-5 h-5" />
                </div>
                <span className={cn("px-2.5 py-1 text-xs font-medium border rounded-full", getTypeColor(doc.docType))}>
                  {doc.docType}
                </span>
              </div>
              
              <h3 className="text-slate-200 font-medium text-lg leading-tight mb-2 group-hover:text-indigo-400 transition-colors line-clamp-2">
                {doc.title}
              </h3>
              
              <p className="text-sm text-slate-400 line-clamp-3 mb-4 h-14">
                {doc.content.substring(0, 120)}...
              </p>
              
              <div className="pt-4 border-t border-slate-700/50 flex items-center justify-between text-xs text-slate-400">
                <div className="flex items-center gap-1.5">
                  <FolderOpen className="w-3.5 h-3.5" />
                  {doc.department}
                </div>
                <div className="flex items-center gap-1.5">
                  <Calendar className="w-3.5 h-3.5" />
                  {new Date(doc.uploadedAt).toLocaleDateString()}
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
}
