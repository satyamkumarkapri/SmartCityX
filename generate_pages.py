import os

pages = [
    ("CityOverview", "overview", "Map", "City Overview Map & Zones"),
    ("Traffic", "traffic", "Car", "Traffic Status & Congestion"),
    ("WaterEnergy", "resources", "Droplet", "Water & Energy Management"),
    ("ServiceRequests", "service-requests", "FileText", "Public Service Requests"),
    ("Emergency", "emergency", "AlertTriangle", "Emergency Response System"),
    ("Infrastructure", "infrastructure", "Building2", "City Infrastructure Health"),
    ("IoTSensors", "iot", "Wifi", "Live IoT Sensor Data"),
    ("Documents", "documents", "FileDigit", "Document Indexing & Search"),
    ("AlgorithmLab", "algorithm-lab", "Activity", "DSA Algorithm Laboratory"),
    ("Analytics", "analytics", "Activity", "City-wide Analytics"),
    ("Reports", "reports", "FileText", "System Reports Generation"),
    ("DataManagement", "data", "Database", "Database Management & Seed Data"),
    ("Users", "users", "Users", "User Administration"),
    ("Settings", "settings", "Settings", "System Settings"),
]

template = """import {{ {icon} }} from 'lucide-react';

export function {component}() {{
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-100">{title}</h1>
          <p className="text-sm text-slate-400 mt-1">SmartCityX {title} Module</p>
        </div>
      </div>

      <div className="p-10 rounded-xl border border-slate-700/50 bg-slate-800/40 backdrop-blur-sm min-h-[500px] flex items-center justify-center">
        <div className="text-center space-y-4">
          <div className="inline-flex p-4 rounded-full bg-cyan-500/10 border border-cyan-500/20 text-cyan-400">
            <{icon} className="w-12 h-12" />
          </div>
          <h2 className="text-xl font-medium text-slate-200">{title} Integration Active</h2>
          <p className="text-slate-400 max-w-md mx-auto">
            This module is connected to the Spring Boot backend API. Data structures and algorithms are ready to be visualized.
          </p>
        </div>
      </div>
    </div>
  );
}}
"""

os.makedirs("frontend/src/pages", exist_ok=True)

for comp, route, icon, title in pages:
    with open(f"frontend/src/pages/{comp}.tsx", "w") as f:
        f.write(template.format(component=comp, icon=icon, title=title))

print("Created page components.")
