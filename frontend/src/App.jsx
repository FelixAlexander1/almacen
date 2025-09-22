import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/UI/Layout';
import Dashboard from './pages/Dashboard';
import ProductList from './pages/Products/List';
import InventoryView from './pages/Inventory/View';
import CreateInbound from './pages/Inbounds/Create';
import ShipmentsList from './pages/Shipments/List';
import CrossDockCreate from './pages/CrossDocking/Create';
import CrossDockComplete from './pages/CrossDocking/Complete';
import Workstation from './pages/PickOrders/Workstation';
import ShipmentDetails from './pages/Shipments/Detail';
import InboundList from './pages/Inbounds/InboundList';
import ShipmentsPage from './pages/Shipments/ShipmentPage';

const App = () => {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/products" element={<ProductList />} />
          <Route path="/inventory" element={<InventoryView />} />
          <Route path="/inbounds/create" element={<CreateInbound />} />
          <Route path="/inbounds" element={<InboundList />} />
          <Route path="/shipments" element={<ShipmentsPage />} />
          <Route path="/shipments/create" element={<ShipmentsPage />} />
          <Route path="/shipments/:id" element={<ShipmentDetails />} />
          <Route path="/crossdock/create" element={<CrossDockCreate />} />
          <Route path="/crossdock/complete" element={<CrossDockComplete />} />
          <Route path="/pickorders/workstation" element={<Workstation />} />
        </Routes>
      </Layout>
    </Router>
  );
};

export default App;
