import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { MainLayout } from '@/components/layout/MainLayout';
import { Dashboard } from '@/pages/Dashboard';
import { CityOverview } from '@/pages/CityOverview';
import { Traffic } from '@/pages/Traffic';
import { WaterEnergy } from '@/pages/WaterEnergy';
import { ServiceRequests } from '@/pages/ServiceRequests';
import { Emergency } from '@/pages/Emergency';
import { Infrastructure } from '@/pages/Infrastructure';
import { IoTSensors } from '@/pages/IoTSensors';
import { Documents } from '@/pages/Documents';
import { AlgorithmLab } from '@/pages/AlgorithmLab';
import { Analytics } from '@/pages/Analytics';
import { Reports } from '@/pages/Reports';
import { DataManagement } from '@/pages/DataManagement';
import { Users } from '@/pages/Users';
import { Settings } from '@/pages/Settings';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<MainLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="overview" element={<CityOverview />} />
          <Route path="traffic" element={<Traffic />} />
          <Route path="resources" element={<WaterEnergy />} />
          <Route path="service-requests" element={<ServiceRequests />} />
          <Route path="emergency" element={<Emergency />} />
          <Route path="infrastructure" element={<Infrastructure />} />
          <Route path="iot" element={<IoTSensors />} />
          <Route path="documents" element={<Documents />} />
          <Route path="algorithm-lab/*" element={<AlgorithmLab />} />
          <Route path="analytics" element={<Analytics />} />
          <Route path="reports" element={<Reports />} />
          <Route path="data" element={<DataManagement />} />
          <Route path="users" element={<Users />} />
          <Route path="settings" element={<Settings />} />
          <Route path="*" element={<div className="p-10 text-center text-slate-400">Page under construction...</div>} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
