import React, { useEffect, useState, useContext } from 'react';
import {
  getProducts,
  getPickOrders,
  getInventory,
  getCrossDockOperations,
  getLatestActivities,
} from '../services/api';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import {
  Package,
  ClipboardList,
  Layers,
  ShoppingBag,
  MoveHorizontal,
  PlusCircle
} from 'lucide-react';
import { ThemeContext } from '../context/ThemeContext'; 
import '../styles/Dashboard.css';

const Dashboard = () => {
  const [stats, setStats] = useState({
    products: 0,
    pendingPickOrders: 0,
    totalStock: 0,
    stockReserved: 0,
    activeCrossDock: 0,
  });
  const [inventoryData, setInventoryData] = useState([]);
  const [inventoryByLocation, setInventoryByLocation] = useState([]);
  const [inventoryByProduct, setInventoryByProduct] = useState([]);
  const [latestActivities, setLatestActivities] = useState([]);

  const { darkMode } = useContext(ThemeContext); 
  const navigate = useNavigate();

  const loadStats = async () => {
    try {
      const [productsRes, pickOrdersRes, inventoryRes, crossDockRes, activitiesRes] =
        await Promise.all([
          getProducts(),
          getPickOrders(),
          getInventory(),
          getCrossDockOperations(),
          getLatestActivities(),
        ]);

      const totalStock = inventoryRes.data.reduce(
        (acc, item) => acc + (item.quantityTotal - item.quantityReserved),
        0
      );

      const stockReserved = inventoryRes.data.reduce(
        (acc, item) => acc + item.quantityReserved,
        0
      );

      setStats({
        products: productsRes.data.length,
        pendingPickOrders: pickOrdersRes.data.filter((o) => o.status === 'CREATED').length,
        totalStock,
        stockReserved,
        activeCrossDock: crossDockRes.data.length,
      });

      setInventoryData(inventoryRes.data);

      // Agrupación de inventario por ubicación
      const byLocation = inventoryRes.data.reduce((acc, item) => {
        if (item.location) {
          const loc = acc.find((l) => l.location === item.location.code);
          if (loc) loc.total += item.quantityTotal - item.quantityReserved;
          else acc.push({ location: item.location.code, total: item.quantityTotal - item.quantityReserved });
        }
        return acc;
      }, []);
      setInventoryByLocation(byLocation);

      // Agrupación de inventario por producto
      const byProduct = inventoryRes.data.reduce((acc, item) => {
        if (item.product) {
          const existing = acc.find((p) => p.name === item.product.name);
          const quantityAvailable = item.quantityTotal - item.quantityReserved;

          if (existing) {
            existing.quantityTotal += quantityAvailable;
          } else {
            acc.push({ name: item.product.name, quantityTotal: quantityAvailable });
          }
        }
        return acc;
      }, []);
      setInventoryByProduct(byProduct);

      setLatestActivities(activitiesRes.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    loadStats();
  }, []);

  const cards = [
    { title: 'Productos', value: stats.products, icon: <Package className="icon" />, style: 'kpi-blue' },
    { title: 'Pick Orders Pendientes', value: stats.pendingPickOrders, icon: <ClipboardList className="icon" />, style: 'kpi-yellow' },
    { title: 'Stock Total', value: stats.totalStock, icon: <Layers className="icon" />, style: 'kpi-green' },
    { title: 'Stock Reservado', value: stats.stockReserved, icon: <ShoppingBag className="icon" />, style: 'kpi-red' },
    { title: 'Cross Dock Activos', value: stats.activeCrossDock, icon: <MoveHorizontal className="icon" />, style: 'kpi-purple' },
  ];

  return (
    <div className={`dashboard-container ${darkMode ? 'dark-mode' : ''}`}>
      {/* Header */}
      <motion.div
        className="dashboard-header"
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <div>
          <h1>Dashboard</h1>
          <p>Vista general de operaciones y métricas del almacén</p>
        </div>
      </motion.div>

      {/* Quick Actions */}
      <div className="quick-actions">
        {[
          { label: 'Nuevo Producto', className: 'btn-blue', route: '/products' },
          { label: 'Nuevo Inbound', className: 'btn-green', route: '/inbounds/create' },
          { label: 'Completar Pick Order', className: 'btn-yellow', route: '/pickorders/workstation' },
        ].map((btn, i) => (
          <motion.button
            key={i}
            className={btn.className}
            onClick={() => navigate(btn.route)}
            whileHover={{ scale: 1.05 }}
            whileTap={{ scale: 0.95 }}
          >
            <PlusCircle className="icon" /> {btn.label}
          </motion.button>
        ))}
      </div>

      {/* KPI Cards */}
      <div className="kpi-cards">
        {cards.map((card, index) => (
          <motion.div
            key={index}
            className={`kpi-card ${card.style}`}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: index * 0.1 }}
            whileHover={{ scale: 1.05 }}
          >
            <div>{card.icon}</div>
            <h2>{card.title}</h2>
            <p>{card.value}</p>
          </motion.div>
        ))}
      </div>

      {/* Charts */}
      <div
        style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '16px',
          marginTop: '20px',
        }}
      >
        <motion.div
          className="chart-card"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5 }}
        >
          <h2>Inventario por Producto</h2>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={inventoryByProduct}>
              <XAxis dataKey="name" stroke={darkMode ? '#f9fafb' : '#6b7280'} />
              <YAxis stroke={darkMode ? '#f9fafb' : '#6b7280'} />
              <Tooltip />
              <Bar dataKey="quantityTotal" fill="#3b82f6" radius={[6, 6, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </motion.div>

        <motion.div
          className="chart-card"
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.2 }}
        >
          <h2>Inventario por Ubicación</h2>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={inventoryByLocation}>
              <XAxis dataKey="location" stroke={darkMode ? '#f9fafb' : '#6b7280'} />
              <YAxis stroke={darkMode ? '#f9fafb' : '#6b7280'} />
              <Tooltip />
              <Bar dataKey="total" fill="#10b981" radius={[6, 6, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </motion.div>
      </div>

      {/* Latest Activities */}
      <motion.div
        className="activities-card"
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5, delay: 0.3 }}
      >
        <h2>Últimas Actividades</h2>
        {latestActivities.length > 0 ? (
          latestActivities.slice(0, 10).map((event, i) => (
            <motion.div
              key={i}
              className="activity-item"
              initial={{ opacity: 0, x: -10 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: i * 0.05 }}
            >
              <div className="activity-item-dot"></div>
              <div>
                <p>{event.description}</p>
                <span>{new Date(event.createdAt).toLocaleString()}</span>
              </div>
            </motion.div>
          ))
        ) : (
          <p>No hay actividades recientes.</p>
        )}
      </motion.div>
    </div>
  );
};

export default Dashboard;
