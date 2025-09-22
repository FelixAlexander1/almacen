import React, { useState } from 'react';
import { createShipment } from '../../services/api';
import { Sun, Moon } from 'lucide-react';
import '../../styles/Shipments.css';

const CreateShipment = () => {
  const [carrier, setCarrier] = useState('');
  const [trackingNumber, setTrackingNumber] = useState('');
  const [productId, setProductId] = useState('');
  const [qty, setQty] = useState(0);
  const [darkMode, setDarkMode] = useState(false);
  const [selectedPickOrders, setSelectedPickOrders] = useState([]); // IDs de Pick Orders seleccionadas
  const [itemsToShip, setItemsToShip] = useState([]); // Items a incluir en el shipment

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      carrier,
      trackingNumber,
      pickOrderIds: selectedPickOrders, // IDs de Pick Orders PICKED
      shipmentItems: itemsToShip, // items seleccionados
    };
    await createShipment(payload);
    alert('Shipment creado');
  };


  return (
    <div className={`shipments-container ${darkMode ? 'dark-mode' : ''} p-6`}>
      <div className="shipments-header flex justify-between items-center mb-4">
        <h1 className="title">Crear Shipment</h1>
        <button className="mode-switch" onClick={() => setDarkMode(!darkMode)}>
          {darkMode ? <Sun className="icon"/> : <Moon className="icon"/>}
          {darkMode ? ' Light Mode' : ' Dark Mode'}
        </button>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4 max-w-lg">
        <input type="text" placeholder="Carrier" value={carrier} onChange={e => setCarrier(e.target.value)} required />
        <input type="text" placeholder="Tracking Number" value={trackingNumber} onChange={e => setTrackingNumber(e.target.value)} required />
        <input type="text" placeholder="Product ID" value={productId} onChange={e => setProductId(e.target.value)} required />
        <input type="number" placeholder="Cantidad" value={qty} onChange={e => setQty(e.target.value)} required />
        <button type="submit" className="btn-submit">Crear Shipment</button>
      </form>
    </div>
  );
};

export default CreateShipment;
