import { useState, useEffect } from 'react';
import { FileText, Download, Printer, Filter, Calendar, File } from 'lucide-react';
import { cn } from '@/lib/utils';

interface Report {
  id: number;
  title: string;
  docType: string;
  department: string;
  content: string;
  uploadedAt: string;
}

export function Reports() {
  const [reports, setReports] = useState<Report[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('http://localhost:8080/api/documents')
      .then(r => r.json())
      .then(data => {
        // Filter specifically for "REPORT" and "POLICY"
        setReports(data.filter((d: Report) => d.docType === 'REPORT' || d.docType === 'POLICY'));
      })
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="space-y-6 animate-in fade-in">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">System Reports</h1>
          <p className="text-sm text-slate-400 mt-1">Generate and export official SmartCityX insights</p>
        </div>
        <button className="flex items-center gap-2 px-4 py-2 bg-cyan-600 hover:bg-cyan-700 text-white font-medium rounded-lg transition-colors">
          <FileText className="w-4 h-4" /> Generate New Report
        </button>
      </div>

      <div className="flex gap-4 mb-6">
        <button className="flex items-center gap-2 px-4 py-2 bg-slate-800/50 border border-slate-700/50 text-slate-300 rounded-lg">
          <Filter className="w-4 h-4" /> All Departments
        </button>
        <button className="flex items-center gap-2 px-4 py-2 bg-slate-800/50 border border-slate-700/50 text-slate-300 rounded-lg">
          <Calendar className="w-4 h-4" /> Last 30 Days
        </button>
      </div>

      <div className="bg-slate-800/40 border border-slate-700/50 rounded-xl overflow-hidden backdrop-blur-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm">
            <thead className="bg-slate-900/50 text-slate-400 border-b border-slate-700/50">
              <tr>
                <th className="px-6 py-4 font-medium">Report Title</th>
                <th className="px-6 py-4 font-medium">Type</th>
                <th className="px-6 py-4 font-medium">Department</th>
                <th className="px-6 py-4 font-medium">Date Generated</th>
                <th className="px-6 py-4 font-medium text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-700/50">
              {loading ? (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-slate-500">
                    Compiling reports...
                  </td>
                </tr>
              ) : reports.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-slate-500">
                    No reports generated yet.
                  </td>
                </tr>
              ) : (
                reports.map(report => (
                  <tr key={report.id} className="hover:bg-slate-800/60 transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <File className="w-5 h-5 text-cyan-400" />
                        <span className="font-medium text-slate-200">{report.title}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4">
                      <span className="px-2.5 py-1 text-xs font-medium border rounded-full bg-cyan-500/10 text-cyan-400 border-cyan-500/20">
                        {report.docType}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-slate-400">{report.department}</td>
                    <td className="px-6 py-4 text-slate-400 font-mono text-xs">
                      {new Date(report.uploadedAt).toLocaleDateString()}
                    </td>
                    <td className="px-6 py-4 text-right">
                      <div className="flex items-center justify-end gap-2">
                        <button className="flex items-center gap-1.5 px-3 py-1.5 bg-slate-700 hover:bg-slate-600 text-slate-200 rounded text-xs transition-colors">
                          <Download className="w-3.5 h-3.5" /> PDF
                        </button>
                        <button className="flex items-center gap-1.5 px-3 py-1.5 bg-slate-700 hover:bg-slate-600 text-slate-200 rounded text-xs transition-colors">
                          <Printer className="w-3.5 h-3.5" /> Print
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
