import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
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
import { Login } from '@/pages/Login';
import { Landing } from '@/pages/Landing';
import { Signup } from '@/pages/Signup';

// Protected Route Component
const ProtectedRoute = ({ user, children }: { user: any, children: React.ReactNode }) => {
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return <>{children}</>;
};

function App() {
  const [user, setUser] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem('smartcityx_user');
    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);

  const handleLogin = (userData: any) => {
    localStorage.setItem('smartcityx_user', JSON.stringify(userData));
    setUser(userData);
  };

  if (loading) return null;

  // Check if user is admin
  const isAdmin = user?.roles?.some((r: any) => r.name === 'SUPER_ADMIN' || r.name === 'ADMIN');

  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={user ? <Navigate to="/dashboard" /> : <Landing />} />
        <Route path="/login" element={<Login onLogin={handleLogin} />} />
        <Route path="/signup" element={<Signup onLogin={handleLogin} />} />

        {/* Protected Routes */}
        <Route path="/" element={<ProtectedRoute user={user}><MainLayout user={user} /></ProtectedRoute>}>
          <Route path="dashboard" element={<Dashboard />} />
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
          
          {/* Admin only routes */}
          {isAdmin && (
            <>
              <Route path="users" element={<Users />} />
              <Route path="settings" element={<Settings />} />
            </>
          )}
          
          <Route path="*" element={<div className="p-10 text-center text-slate-400">Page not found or unauthorized.</div>} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
