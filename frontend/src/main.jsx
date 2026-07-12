import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import {
  Activity,
  BarChart3,
  Car,
  ClipboardList,
  Fuel,
  LayoutDashboard,
  LogOut,
  PenTool,
  Plus,
  RefreshCw,
  ShieldCheck,
  Trash2,
  Truck,
  UserRound,
  Users,
  Wallet,
  Wrench,
} from 'lucide-react';
import './styles.css';

const API_BASE = import.meta.env.VITE_API_BASE_URL || '';

const navItems = [
  { id: 'dashboard', label: 'Dashboard', icon: LayoutDashboard },
  { id: 'vehicles', label: 'Vehicles', icon: Truck },
  { id: 'drivers', label: 'Drivers', icon: Users },
  { id: 'trips', label: 'Trips', icon: ClipboardList },
  { id: 'maintenance', label: 'Maintenance', icon: Wrench },
  { id: 'fuel', label: 'Fuel', icon: Fuel },
  { id: 'expenses', label: 'Expenses', icon: Wallet },
  { id: 'analytics', label: 'Analytics', icon: BarChart3 },
];

const emptyForms = {
  vehicle: {
    registrationNumber: '',
    vehicleName: '',
    vehicleType: 'VAN',
    maxLoadCapacity: '',
    odometer: '',
    acquisitionCost: '',
  },
  driver: {
    name: '',
    licenseNumber: '',
    licenseCategory: 'LMV',
    licenseExpiryDate: '',
    contactNumber: '',
    safetyScore: '',
  },
  trip: {
    source: '',
    destination: '',
    cargoWeight: '',
    plannedDistance: '',
    vehicleId: '',
    driverId: '',
  },
  maintenance: {
    issueDescription: '',
    priority: 'MEDIUM',
    technicianName: '',
    cost: '',
    vehicleId: '',
  },
  fuel: {
    liters: '',
    cost: '',
    fuelDate: new Date().toISOString().slice(0, 10),
    vehicleId: '',
  },
  expense: {
    expenseType: 'TOLL',
    amount: '',
    description: '',
    expenseDate: new Date().toISOString().slice(0, 10),
    vehicleId: '',
    tripId: '',
  },
};

function App() {
  const [auth, setAuth] = useState(() => {
    const saved = localStorage.getItem('transitops-auth');
    return saved ? JSON.parse(saved) : null;
  });
  const [active, setActive] = useState('dashboard');
  const [data, setData] = useState({});
  const [forms, setForms] = useState(emptyForms);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState('');
  const [error, setError] = useState('');

  const client = useMemo(() => createClient(auth), [auth]);

  useEffect(() => {
    if (auth) {
      loadAll();
    }
  }, [auth]);

  async function loadAll() {
    setLoading(true);
    setError('');
    try {
      const [
        dashboard,
        vehicles,
        drivers,
        trips,
        maintenance,
        fuel,
        expenses,
        analytics,
      ] = await Promise.all([
        client.get('/api/dashboard'),
        client.get('/api/vehicles'),
        client.get('/api/drivers'),
        client.get('/api/trips'),
        client.get('/api/maintenance'),
        client.get('/api/fuel'),
        client.get('/api/expenses'),
        client.get('/api/analytics'),
      ]);
      setData({ dashboard, vehicles, drivers, trips, maintenance, fuel, expenses, analytics });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  function updateForm(name, patch) {
    setForms((current) => ({ ...current, [name]: { ...current[name], ...patch } }));
  }

  async function submit(name, path, payload, resetKey) {
    setError('');
    setNotice('');
    try {
      await client.post(path, normalizePayload(payload));
      setForms((current) => ({ ...current, [resetKey]: emptyForms[resetKey] }));
      await loadAll();
      setNotice('Saved successfully');
    } catch (err) {
      setError(err.message);
    }
  }

  async function mutate(path, success = 'Updated successfully') {
    setError('');
    setNotice('');
    try {
      await client.put(path);
      await loadAll();
      setNotice(success);
    } catch (err) {
      setError(err.message);
    }
  }

  async function remove(path) {
    setError('');
    setNotice('');
    try {
      await client.delete(path);
      await loadAll();
      setNotice('Deleted successfully');
    } catch (err) {
      setError(err.message);
    }
  }

  if (!auth) {
    return <AuthScreen onAuth={setAuth} />;
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">
          <Car size={26} />
          <div>
            <strong>TransitOps</strong>
            <span>Operations Console</span>
          </div>
        </div>
        <nav>
          {navItems.map((item) => {
            const Icon = item.icon;
            return (
              <button
                key={item.id}
                className={active === item.id ? 'active' : ''}
                onClick={() => setActive(item.id)}
                title={item.label}
              >
                <Icon size={18} />
                <span>{item.label}</span>
              </button>
            );
          })}
        </nav>
        <button
          className="logout"
          onClick={() => {
            localStorage.removeItem('transitops-auth');
            setAuth(null);
          }}
        >
          <LogOut size={18} />
          <span>Logout</span>
        </button>
      </aside>

      <main className="content">
        <header className="topbar">
          <div>
            <p>{auth.role || 'USER'}</p>
            <h1>{navItems.find((item) => item.id === active)?.label}</h1>
          </div>
          <button className="iconButton" onClick={loadAll} disabled={loading} title="Refresh">
            <RefreshCw size={18} />
          </button>
        </header>

        {error && <div className="alert error">{error}</div>}
        {notice && <div className="alert success">{notice}</div>}

        {active === 'dashboard' && <Dashboard dashboard={data.dashboard} />}
        {active === 'vehicles' && (
          <Vehicles
            vehicles={data.vehicles || []}
            form={forms.vehicle}
            update={(patch) => updateForm('vehicle', patch)}
            submit={() => submit('vehicle', '/api/vehicles', forms.vehicle, 'vehicle')}
            remove={remove}
          />
        )}
        {active === 'drivers' && (
          <Drivers
            drivers={data.drivers || []}
            form={forms.driver}
            update={(patch) => updateForm('driver', patch)}
            submit={() => submit('driver', '/api/drivers', forms.driver, 'driver')}
            remove={remove}
          />
        )}
        {active === 'trips' && (
          <Trips
            trips={data.trips || []}
            vehicles={availableVehicles(data.vehicles || [])}
            drivers={availableDrivers(data.drivers || [])}
            form={forms.trip}
            update={(patch) => updateForm('trip', patch)}
            submit={() => submit('trip', '/api/trips', forms.trip, 'trip')}
            mutate={mutate}
            remove={remove}
          />
        )}
        {active === 'maintenance' && (
          <Maintenance
            logs={data.maintenance || []}
            vehicles={data.vehicles || []}
            form={forms.maintenance}
            update={(patch) => updateForm('maintenance', patch)}
            submit={() => submit('maintenance', '/api/maintenance', forms.maintenance, 'maintenance')}
            mutate={mutate}
            remove={remove}
          />
        )}
        {active === 'fuel' && (
          <FuelLogs
            logs={data.fuel || []}
            vehicles={data.vehicles || []}
            form={forms.fuel}
            update={(patch) => updateForm('fuel', patch)}
            submit={() => submit('fuel', '/api/fuel', forms.fuel, 'fuel')}
            remove={remove}
          />
        )}
        {active === 'expenses' && (
          <Expenses
            expenses={data.expenses || []}
            vehicles={data.vehicles || []}
            trips={data.trips || []}
            form={forms.expense}
            update={(patch) => updateForm('expense', patch)}
            submit={() => submit('expense', '/api/expenses', forms.expense, 'expense')}
            remove={remove}
          />
        )}
        {active === 'analytics' && <Analytics analytics={data.analytics} />}
      </main>
    </div>
  );
}

function AuthScreen({ onAuth }) {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({
    fullName: 'Admin User',
    email: 'admin@transitops.com',
    password: 'Admin@123',
    role: 'ADMIN',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  async function submit(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
    try {
      const path = mode === 'login' ? '/api/auth/login' : '/api/auth/register';
      const body = mode === 'login'
        ? { email: form.email, password: form.password }
        : form;
      const response = await fetch(`${API_BASE}${path}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      });
      const json = await readJson(response);
      if (!response.ok) {
        throw new Error(json.message || 'Authentication failed');
      }
      const auth = { ...json, password: form.password };
      localStorage.setItem('transitops-auth', JSON.stringify(auth));
      onAuth(auth);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="authPage">
      <section className="authPanel">
        <div className="authIntro">
          <div className="brand large">
            <Car size={30} />
            <div>
              <strong>TransitOps</strong>
              <span>Smart Transport Operations Platform</span>
            </div>
          </div>
          <div className="authStats">
            <span><ShieldCheck size={18} /> RBAC</span>
            <span><Activity size={18} /> Live KPIs</span>
            <span><PenTool size={18} /> Dispatch Rules</span>
          </div>
        </div>
        <form onSubmit={submit} className="authForm">
          <div className="segmented">
            <button type="button" className={mode === 'login' ? 'selected' : ''} onClick={() => setMode('login')}>Login</button>
            <button type="button" className={mode === 'register' ? 'selected' : ''} onClick={() => setMode('register')}>Register</button>
          </div>
          {mode === 'register' && (
            <>
              <label>Full Name<input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} /></label>
              <label>Role<select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
                <option>ADMIN</option>
                <option>FLEET_MANAGER</option>
                <option>DRIVER</option>
                <option>SAFETY_OFFICER</option>
                <option>FINANCIAL_ANALYST</option>
              </select></label>
            </>
          )}
          <label>Email<input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} /></label>
          <label>Password<input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} /></label>
          {error && <div className="alert error">{error}</div>}
          <button className="primary" disabled={loading}>{loading ? 'Please wait' : mode === 'login' ? 'Login' : 'Create Account'}</button>
        </form>
      </section>
    </main>
  );
}

function Dashboard({ dashboard = {} }) {
  const cards = [
    ['Total Vehicles', dashboard.totalVehicles, Truck],
    ['Available Vehicles', dashboard.availableVehicles, Truck],
    ['Vehicles On Trip', dashboard.vehiclesOnTrip, Activity],
    ['Vehicles In Shop', dashboard.vehiclesInShop, Wrench],
    ['Total Drivers', dashboard.totalDrivers, Users],
    ['Drivers On Trip', dashboard.driversOnTrip, UserRound],
    ['Active Trips', dashboard.activeTrips, ClipboardList],
    ['Fleet Utilization', formatPercent(dashboard.fleetUtilizationPercentage), BarChart3],
  ];
  return (
    <section className="grid cards">
      {cards.map(([label, value, Icon]) => (
        <article className="metric" key={label}>
          <Icon size={20} />
          <span>{label}</span>
          <strong>{value ?? 0}</strong>
        </article>
      ))}
      <article className="widePanel">
        <h2>Costs</h2>
        <div className="costRow">
          <span>Fuel: {money(dashboard.totalFuelCost)}</span>
          <span>Maintenance: {money(dashboard.totalMaintenanceCost)}</span>
          <span>Expenses: {money(dashboard.totalExpenses)}</span>
        </div>
      </article>
    </section>
  );
}

function Vehicles({ vehicles, form, update, submit, remove }) {
  return (
    <SectionLayout
      form={<>
        <Text label="Registration" value={form.registrationNumber} onChange={(v) => update({ registrationNumber: v })} />
        <Text label="Name / Model" value={form.vehicleName} onChange={(v) => update({ vehicleName: v })} />
        <Text label="Type" value={form.vehicleType} onChange={(v) => update({ vehicleType: v })} />
        <Text label="Capacity Kg" type="number" value={form.maxLoadCapacity} onChange={(v) => update({ maxLoadCapacity: v })} />
        <Text label="Odometer" type="number" value={form.odometer} onChange={(v) => update({ odometer: v })} />
        <Text label="Acquisition Cost" type="number" value={form.acquisitionCost} onChange={(v) => update({ acquisitionCost: v })} />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Registration', 'Name', 'Type', 'Capacity', 'Status', '']} rows={vehicles.map((v) => [
        v.id, v.registrationNumber, v.vehicleName, v.vehicleType, v.maxLoadCapacity, <Badge value={v.status} />, <DeleteButton onClick={() => remove(`/api/vehicles/${v.id}`)} />,
      ])} />}
    />
  );
}

function Drivers({ drivers, form, update, submit, remove }) {
  return (
    <SectionLayout
      form={<>
        <Text label="Name" value={form.name} onChange={(v) => update({ name: v })} />
        <Text label="License No" value={form.licenseNumber} onChange={(v) => update({ licenseNumber: v })} />
        <Text label="Category" value={form.licenseCategory} onChange={(v) => update({ licenseCategory: v })} />
        <Text label="Expiry" type="date" value={form.licenseExpiryDate} onChange={(v) => update({ licenseExpiryDate: v })} />
        <Text label="Contact" value={form.contactNumber} onChange={(v) => update({ contactNumber: v })} />
        <Text label="Safety Score" type="number" value={form.safetyScore} onChange={(v) => update({ safetyScore: v })} />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Name', 'License', 'Expiry', 'Score', 'Status', '']} rows={drivers.map((d) => [
        d.id, d.name, d.licenseNumber, d.licenseExpiryDate, d.safetyScore, <Badge value={d.status} />, <DeleteButton onClick={() => remove(`/api/drivers/${d.id}`)} />,
      ])} />}
    />
  );
}

function Trips({ trips, vehicles, drivers, form, update, submit, mutate, remove }) {
  return (
    <SectionLayout
      form={<>
        <Text label="Source" value={form.source} onChange={(v) => update({ source: v })} />
        <Text label="Destination" value={form.destination} onChange={(v) => update({ destination: v })} />
        <Text label="Cargo Kg" type="number" value={form.cargoWeight} onChange={(v) => update({ cargoWeight: v })} />
        <Text label="Distance Km" type="number" value={form.plannedDistance} onChange={(v) => update({ plannedDistance: v })} />
        <Select label="Vehicle" value={form.vehicleId} onChange={(v) => update({ vehicleId: v })} options={vehicles.map((v) => [v.id, `${v.registrationNumber} - ${v.vehicleName}`])} />
        <Select label="Driver" value={form.driverId} onChange={(v) => update({ driverId: v })} options={drivers.map((d) => [d.id, `${d.name} - ${d.licenseNumber}`])} />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Route', 'Cargo', 'Vehicle', 'Driver', 'Status', 'Actions']} rows={trips.map((t) => [
        t.id, `${t.source} to ${t.destination}`, t.cargoWeight, t.vehicleId, t.driverId, <Badge value={t.status} />, <ActionGroup>
          <SmallButton onClick={() => mutate(`/api/trips/dispatch/${t.id}`, 'Trip dispatched')}>Dispatch</SmallButton>
          <SmallButton onClick={() => mutate(`/api/trips/complete/${t.id}`, 'Trip completed')}>Complete</SmallButton>
          <SmallButton onClick={() => mutate(`/api/trips/cancel/${t.id}`, 'Trip cancelled')}>Cancel</SmallButton>
          <DeleteButton onClick={() => remove(`/api/trips/${t.id}`)} />
        </ActionGroup>,
      ])} />}
    />
  );
}

function Maintenance({ logs, vehicles, form, update, submit, mutate, remove }) {
  return (
    <SectionLayout
      form={<>
        <Text label="Issue" value={form.issueDescription} onChange={(v) => update({ issueDescription: v })} />
        <Select label="Priority" value={form.priority} onChange={(v) => update({ priority: v })} options={['LOW', 'MEDIUM', 'HIGH'].map((v) => [v, v])} />
        <Text label="Technician" value={form.technicianName} onChange={(v) => update({ technicianName: v })} />
        <Text label="Cost" type="number" value={form.cost} onChange={(v) => update({ cost: v })} />
        <Select label="Vehicle" value={form.vehicleId} onChange={(v) => update({ vehicleId: v })} options={vehicles.map((v) => [v.id, `${v.registrationNumber} - ${v.vehicleName}`])} />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Issue', 'Priority', 'Cost', 'Vehicle', 'Status', 'Actions']} rows={logs.map((m) => [
        m.id, m.issueDescription, m.priority, money(m.cost), m.vehicleId, <Badge value={m.status} />, <ActionGroup>
          <SmallButton onClick={() => mutate(`/api/maintenance/approve/${m.id}`, 'Maintenance approved')}>Approve</SmallButton>
          <SmallButton onClick={() => mutate(`/api/maintenance/resolve/${m.id}`, 'Maintenance resolved')}>Resolve</SmallButton>
          <DeleteButton onClick={() => remove(`/api/maintenance/${m.id}`)} />
        </ActionGroup>,
      ])} />}
    />
  );
}

function FuelLogs({ logs, vehicles, form, update, submit, remove }) {
  return (
    <SectionLayout
      form={<>
        <Text label="Liters" type="number" value={form.liters} onChange={(v) => update({ liters: v })} />
        <Text label="Cost" type="number" value={form.cost} onChange={(v) => update({ cost: v })} />
        <Text label="Date" type="date" value={form.fuelDate} onChange={(v) => update({ fuelDate: v })} />
        <Select label="Vehicle" value={form.vehicleId} onChange={(v) => update({ vehicleId: v })} options={vehicles.map((v) => [v.id, `${v.registrationNumber} - ${v.vehicleName}`])} />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Vehicle', 'Liters', 'Cost', 'Date', '']} rows={logs.map((f) => [
        f.id, f.vehicleId, f.liters, money(f.cost), f.fuelDate, <DeleteButton onClick={() => remove(`/api/fuel/${f.id}`)} />,
      ])} />}
    />
  );
}

function Expenses({ expenses, vehicles, trips, form, update, submit, remove }) {
  return (
    <SectionLayout
      form={<>
        <Select label="Type" value={form.expenseType} onChange={(v) => update({ expenseType: v })} options={['TOLL', 'MAINTENANCE', 'PARKING', 'OTHER'].map((v) => [v, v])} />
        <Text label="Amount" type="number" value={form.amount} onChange={(v) => update({ amount: v })} />
        <Text label="Description" value={form.description} onChange={(v) => update({ description: v })} />
        <Text label="Date" type="date" value={form.expenseDate} onChange={(v) => update({ expenseDate: v })} />
        <Select label="Vehicle" value={form.vehicleId} onChange={(v) => update({ vehicleId: v })} options={vehicles.map((v) => [v.id, `${v.registrationNumber} - ${v.vehicleName}`])} />
        <Select label="Trip Optional" value={form.tripId} onChange={(v) => update({ tripId: v })} options={trips.map((t) => [t.id, `${t.id} - ${t.source} to ${t.destination}`])} allowEmpty />
      </>}
      onSubmit={submit}
      table={<DataTable columns={['ID', 'Type', 'Amount', 'Vehicle', 'Trip', 'Date', '']} rows={expenses.map((e) => [
        e.id, e.expenseType, money(e.amount), e.vehicleId, e.tripId || '-', e.expenseDate, <DeleteButton onClick={() => remove(`/api/expenses/${e.id}`)} />,
      ])} />}
    />
  );
}

function Analytics({ analytics = {} }) {
  return (
    <section className="analytics">
      <article className="widePanel">
        <h2>Fleet Analytics</h2>
        <div className="barList">
          <Bar label="Vehicle Utilization" value={analytics.vehicleUtilizationPercentage || 0} />
          <Bar label="Trips Today" value={analytics.tripsToday || 0} max={20} />
          <Bar label="Trips This Month" value={analytics.tripsThisMonth || 0} max={100} />
          <Bar label="Trips This Year" value={analytics.tripsThisYear || 0} max={500} />
        </div>
      </article>
      <div className="grid cards">
        <article className="metric"><Fuel size={20} /><span>Fuel Cost</span><strong>{money(analytics.fuelCost)}</strong></article>
        <article className="metric"><Wrench size={20} /><span>Maintenance Cost</span><strong>{money(analytics.maintenanceCost)}</strong></article>
        <article className="metric"><Wallet size={20} /><span>Expense Cost</span><strong>{money(analytics.expenseCost)}</strong></article>
      </div>
    </section>
  );
}

function SectionLayout({ form, onSubmit, table }) {
  return (
    <section className="workArea">
      <form className="entryForm" onSubmit={(event) => { event.preventDefault(); onSubmit(); }}>
        {form}
        <button className="primary"><Plus size={17} /> Save</button>
      </form>
      <div className="tablePanel">{table}</div>
    </section>
  );
}

function DataTable({ columns, rows }) {
  return (
    <div className="tableWrap">
      <table>
        <thead><tr>{columns.map((column) => <th key={column}>{column}</th>)}</tr></thead>
        <tbody>
          {rows.length === 0 && <tr><td colSpan={columns.length} className="empty">No records yet</td></tr>}
          {rows.map((row, index) => <tr key={index}>{row.map((cell, cellIndex) => <td key={cellIndex}>{cell}</td>)}</tr>)}
        </tbody>
      </table>
    </div>
  );
}

function Text({ label, value, onChange, type = 'text' }) {
  return <label>{label}<input type={type} value={value} onChange={(e) => onChange(e.target.value)} /></label>;
}

function Select({ label, value, onChange, options, allowEmpty = false }) {
  return (
    <label>{label}
      <select value={value} onChange={(e) => onChange(e.target.value)}>
        {(allowEmpty || !value) && <option value="">Select</option>}
        {options.map(([id, labelText]) => <option key={id} value={id}>{labelText}</option>)}
      </select>
    </label>
  );
}

function Badge({ value }) {
  return <span className={`badge ${String(value || '').toLowerCase()}`}>{value}</span>;
}

function DeleteButton({ onClick }) {
  return <button className="iconDanger" type="button" onClick={onClick} title="Delete"><Trash2 size={16} /></button>;
}

function SmallButton({ children, onClick }) {
  return <button className="smallButton" type="button" onClick={onClick}>{children}</button>;
}

function ActionGroup({ children }) {
  return <div className="actions">{children}</div>;
}

function Bar({ label, value, max = 100 }) {
  const width = Math.min(100, (Number(value) / max) * 100);
  return (
    <div className="barItem">
      <div><span>{label}</span><strong>{Number(value).toFixed(value % 1 ? 1 : 0)}</strong></div>
      <div className="barTrack"><span style={{ width: `${width}%` }} /></div>
    </div>
  );
}

function createClient(auth) {
  const headers = {
    'Content-Type': 'application/json',
    Authorization: auth ? `Basic ${btoa(`${auth.email}:${auth.password}`)}` : '',
  };

  async function request(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers: { ...headers, ...(options.headers || {}) },
    });
    if (response.status === 204) return null;
    const json = await readJson(response);
    if (!response.ok) {
      throw new Error(json.message || `Request failed: ${response.status}`);
    }
    return json;
  }

  return {
    get: (path) => request(path),
    post: (path, body) => request(path, { method: 'POST', body: JSON.stringify(body) }),
    put: (path, body) => request(path, { method: 'PUT', body: body ? JSON.stringify(body) : undefined }),
    delete: (path) => request(path, { method: 'DELETE' }),
  };
}

async function readJson(response) {
  const text = await response.text();
  return text ? JSON.parse(text) : {};
}

function normalizePayload(payload) {
  return Object.fromEntries(Object.entries(payload).map(([key, value]) => {
    if (value === '') return [key, null];
    if (['maxLoadCapacity', 'odometer', 'acquisitionCost', 'safetyScore', 'cargoWeight', 'plannedDistance', 'vehicleId', 'driverId', 'cost', 'liters', 'amount', 'tripId'].includes(key)) {
      return [key, value === null ? null : Number(value)];
    }
    return [key, value];
  }));
}

function availableVehicles(vehicles) {
  return vehicles.filter((vehicle) => vehicle.status === 'AVAILABLE');
}

function availableDrivers(drivers) {
  return drivers.filter((driver) => driver.status === 'AVAILABLE');
}

function money(value = 0) {
  return `₹${Number(value || 0).toLocaleString('en-IN')}`;
}

function formatPercent(value = 0) {
  return `${Number(value || 0).toFixed(1)}%`;
}

createRoot(document.getElementById('root')).render(<App />);
