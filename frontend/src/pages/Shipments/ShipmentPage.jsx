import React, { useEffect, useState, useContext } from 'react';
import { getShipments, createShipment, getPickOrders, confirmShipment } from '../../services/api';
import { ThemeContext } from '../../context/ThemeContext';
import { Sun, Moon } from 'lucide-react';
import '../../styles/Shipments.css';

const ShipmentsPage = () => {
  const { darkMode } = useContext(ThemeContext);
  const [shipments, setShipments] = useState([]);
  const [pickOrders, setPickOrders] = useState([]);
  const [selectedPickOrders, setSelectedPickOrders] = useState([]);
  const [carrier, setCarrier] = useState('');
  const [trackingNumber, setTrackingNumber] = useState('');
  const [darkModeLocal, setDarkMode] = useState(darkMode);

  // Cargar Shipments y Pick Orders PICKED
  const loadData = async () => {
    const shipmentsRes = await getShipments();
    setShipments(shipmentsRes.data);

    const pickOrdersRes = await getPickOrders();
    setPickOrders(pickOrdersRes.data.filter(po => po.status === 'PICKED'));
  };

  useEffect(() => {
    loadData();
  }, []);

  // Crear Shipment
  // Crear Shipment
const handleCreateShipment = async (e) => {
  e.preventDefault();

  if (!carrier || !trackingNumber || selectedPickOrders.length === 0) {
    alert('Debe completar todos los campos y seleccionar al menos una Pick Order');
    return;
  }

  // Construir shipmentItems a partir de las Pick Orders seleccionadas
  const itemsToShip = selectedPickOrders.flatMap(poId => {
    const po = pickOrders.find(p => p.id === poId);
    if (!po || !po.items) return [];
    return po.items.map(item => ({
      productId: item.productId,
      qty: item.qty
    }));
  });

  const payload = {
    carrier,
    trackingNumber,
    pickOrderIds: selectedPickOrders,
    shipmentItems: itemsToShip
  };

  try {
    await createShipment(payload);
    alert('Shipment creado');
    // Resetear campos
    setCarrier('');
    setTrackingNumber('');
    setSelectedPickOrders([]);
    loadData();
  } catch (err) {
    console.error('Error creando shipment:', err);
    alert('Error al crear shipment');
  }
};


  // Confirmar Shipment
  const handleConfirmShipment = async (shipmentId) => {
    await confirmShipment(shipmentId);
    alert('Shipment confirmado y Pick Orders asociadas marcadas como COMPLETED');
    loadData();
  };

  return (
    <div className={`shipments-container ${darkMode ? 'dark-mode' : ''}`}>
      <div className="shipments-header flex justify-between items-center mb-4">
        <h1 className="title">Shipments</h1>
        <button className="mode-switch" onClick={() => setDarkMode(!darkMode)}>
          {darkMode ? <Sun className="icon" /> : <Moon className="icon" />}
          {darkMode ? ' Light Mode' : ' Dark Mode'}
        </button>
      </div>

      {/* Lista de Shipments */}
      <div className="table-wrapper mb-6">
        <table className="shipments-table">
          <thead>
            <tr>
              <th>Tracking #</th>
              <th>Carrier</th>
              <th>Estado</th>
              <th>Pick Orders</th>
              <th>Acción</th>
            </tr>
          </thead>
          <tbody>
            {shipments.length === 0 ? (
              <tr>
                <td colSpan="5" className="empty">No hay shipments disponibles</td>
              </tr>
            ) : (
              shipments.map(shipment => (
                <tr key={shipment.id}>
                  <td>{shipment.trackingNumber}</td>
                  <td>{shipment.carrier}</td>
                  <td><span className={`status ${shipment.status.toLowerCase()}`}>{shipment.status}</span></td>
                  <td>{shipment.pickOrders?.length || 0}</td>
                  <td>
                    {shipment.status === 'CREATED' && (
                      <button className="btn-confirm" onClick={() => handleConfirmShipment(shipment.id)}>
                        Confirmar
                      </button>
                    )}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Formulario Crear Shipment */}
      <div className="create-shipment">
        <h2 className="mb-2">Crear Shipment</h2>
        <form onSubmit={handleCreateShipment} className="space-y-4 max-w-lg">
          <input type="text" placeholder="Carrier" value={carrier} onChange={e => setCarrier(e.target.value)} required />
          <input type="text" placeholder="Tracking Number" value={trackingNumber} onChange={e => setTrackingNumber(e.target.value)} required />

          <div>
            <label>Pick Orders a incluir:</label>
            <select multiple value={selectedPickOrders} onChange={e => 
              setSelectedPickOrders(Array.from(e.target.selectedOptions, option => option.value))
            }>
              {pickOrders.map(po => (
                <option key={po.id} value={po.id}>
                    {po.reference} ({po.items?.length || 0} items)
                </option>
                ))}
            </select>
          </div>

          <button type="submit" className="btn-submit">Crear Shipment</button>
        </form>
      </div>
    </div>
  );
};

export default ShipmentsPage;
