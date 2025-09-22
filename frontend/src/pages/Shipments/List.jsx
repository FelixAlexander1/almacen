import React, { useEffect, useState, useContext } from 'react';
import { getShipments } from '../../services/api';
import '../../styles/Shipments.css';
import { ThemeContext } from '../../context/ThemeContext';

const ShipmentsList = () => {
  const [shipments, setShipments] = useState([]);
  const { darkMode } = useContext(ThemeContext); // Usamos el Dark Mode global

  useEffect(() => {
    getShipments()
      .then((res) => setShipments(res.data))
      .catch((err) => console.error(err));
  }, []);

  return (
    <div className={`shipments-container ${darkMode ? 'dark-mode' : ''}`}>
      <div className="shipments-header">
        <h1 className="title">Shipments</h1>
      </div>

      <div className="table-wrapper">
        <table className="shipments-table">
          <thead>
            <tr>
              <th>Tracking #</th>
              <th>Carrier</th>
              <th>Estado</th>
              <th>Items</th>
            </tr>
          </thead>
          <tbody>
            {shipments.length === 0 ? (
              <tr>
                <td colSpan="4" className="empty">
                  No hay shipments disponibles
                </td>
              </tr>
            ) : (
              shipments.map((shipment) => (
                <tr key={shipment.id}>
                  <td>{shipment.trackingNumber}</td>
                  <td>{shipment.carrier}</td>
                  <td>
                    <span className={`status ${shipment.status.toLowerCase()}`}>
                      {shipment.status}
                    </span>
                  </td>
                  <td className="text-center">
                    {shipment.shipmentItems.length}
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ShipmentsList;
