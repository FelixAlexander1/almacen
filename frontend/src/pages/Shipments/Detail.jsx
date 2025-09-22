import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getShipmentById, confirmShipment } from '../../services/api';
import { Sun, Moon } from 'lucide-react';
import '../../styles/Shipments.css';

const ShipmentDetails = () => {
  const { id } = useParams();
  const [shipment, setShipment] = useState(null);
  const [darkMode, setDarkMode] = useState(false);

  useEffect(() => {
    getShipmentById(id).then((res) => setShipment(res.data));
  }, [id]);

  const handleConfirm = async () => {
    await confirmShipment(id); // backend confirma shipment
    alert('Shipment confirmado y Pick Orders asociadas marcadas como COMPLETED');
    window.location.reload();
  };


  if (!shipment) return <p>Cargando...</p>;

  return (
      <div className={`shipments-container ${darkMode ? 'dark-mode' : ''}`}>
        <div className="shipments-header flex justify-between items-center mb-4">
          <h1 className="title">Detalle de Shipment</h1>
          <button className="mode-switch" onClick={() => setDarkMode(!darkMode)}>
            {darkMode ? <Sun className="icon"/> : <Moon className="icon"/>}
            {darkMode ? ' Light Mode' : ' Dark Mode'}
          </button>
        </div>

        <p><strong>Tracking:</strong> {shipment.trackingNumber}</p>
        <p><strong>Carrier:</strong> {shipment.carrier}</p>
        <p><strong>Status:</strong> {shipment.status}</p>

        <h2 className="mt-4 text-lg font-semibold">Items</h2>
        <ul className="list-disc ml-6">
          {shipment.shipmentItems.map((item, idx) => (
            <li key={idx}>
              Producto: {item.productId} | Cantidad: {item.qty}
            </li>
          ))}
        </ul>

        {shipment.status === 'CREATED' && (
          <button
            onClick={handleConfirm}
            className="btn-confirm mt-6"
          >
            Confirmar Shipment
          </button>
        )}
      </div>
  );
};

export default ShipmentDetails;
