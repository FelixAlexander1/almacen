import React, { useEffect, useState } from 'react';
import Layout from '../../components/UI/Layout';
import { getCrossDockOperations, completeCrossDock } from '../../services/api';

const CrossDockComplete = () => {
  const [operations, setOperations] = useState([]);

  const loadData = () => {
    getCrossDockOperations().then((res) => setOperations(res.data));
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleComplete = async (id) => {
    await completeCrossDock(id);
    alert('Operación completada');
    loadData();
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-4">Completar CrossDock</h1>
      <ul className="space-y-2">
        {operations.map((op) => (
          <li
            key={op.id}
            className="flex justify-between items-center border p-2 rounded"
          >
            <div>
              <p><strong>Inbound:</strong> {op.inboundId}</p>
              <p><strong>Shipment:</strong> {op.outShipmentId}</p>
              <p><strong>Status:</strong> {op.status}</p>
            </div>
            {op.status === 'CREATED' && (
              <button
                onClick={() => handleComplete(op.id)}
                className="bg-blue-600 text-white px-4 py-2 rounded"
              >
                Completar
              </button>
            )}
          </li>
        ))}
      </ul>
    </Layout>
  );
};

export default CrossDockComplete;
