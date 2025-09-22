import React, { useEffect, useState, useContext } from 'react';
import { getInbounds, confirmInbound, getLocations, getProducts } from '../../services/api';
import { RefreshCw } from 'lucide-react';
import '../../styles/InboundList.css';
import { ThemeContext } from '../../context/ThemeContext';

export default function InboundList() {
  const [inbounds, setInbounds] = useState([]);
  const [locations, setLocations] = useState([]);
  const [products, setProducts] = useState([]);
  const { darkMode } = useContext(ThemeContext);

  useEffect(() => {
    loadInbounds();
    getLocations().then(res => setLocations(res.data)).catch(console.error);
    getProducts().then(res => setProducts(res.data)).catch(console.error);
  }, []);

  const loadInbounds = async () => {
    try {
      const res = await getInbounds();
      setInbounds(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const getProductName = (productId) => {
    const product = products.find(p => p.id === productId);
    return product ? product.name : 'Producto desconocido';
  };

  const handleLocationChange = (inboundId, itemIndex, locationId) => {
    setInbounds(prev =>
      prev.map(inb => {
        if (inb.id !== inboundId) return inb;
        const newItems = [...inb.items];
        newItems[itemIndex].locationId = locationId;
        return { ...inb, items: newItems };
      })
    );
  };

  const handleReceive = async (inbound) => {
    try {
      const receivedItems = inbound.items.map(item => {
        if (!item.productId) throw new Error(`Falta productId en el item: ${JSON.stringify(item)}`);
        if (!item.locationId) throw new Error(`Falta locationId en el item: ${JSON.stringify(item)}`);
        return {
          productId: item.productId,
          locationId: item.locationId,
          receivedQty: item.expectedQty,
          lot: item.lot || null
        };
      });

      console.log("Payload enviado a confirmInbound:", receivedItems);

      await confirmInbound(inbound.id, { receivedItems });
      alert('Inbound recibido');
      loadInbounds();
    } catch (err) {
      console.error("Error en confirmInbound:", err);
      alert(`Error: ${err.message}`);
    }
  };

  return (
    <div className={`inbound-container ${darkMode ? 'dark-mode' : ''}`}>
      {/* Header */}
      <div className="inbound-header flex justify-between items-center mb-4">
        <h1 className="title">Inbounds</h1>
        <div className="header-actions">
          <button className="btn-refresh" onClick={loadInbounds}>
            <RefreshCw className="icon" /> Recargar
          </button>
        </div>
      </div>

      {/* Tabla */}
      <table className="inbound-table">
        <thead>
          <tr>
            <th>Referencia</th>
            <th>Fecha esperada</th>
            <th>Status</th>
            <th>Productos / Ubicación</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {inbounds.map((inb) => (
            <tr key={inb.id}>
              <td>{inb.asnNumber}</td>
              <td>{inb.expectedDate}</td>
              <td>{inb.status}</td>
              <td>
                <ul>
                  {inb.items.map((item, index) => (
                    <li key={index}>
                      <strong>{getProductName(item.productId)}</strong> - Cantidad: {item.expectedQty} - Lote: {item.lot || 'N/A'}
                      <br />
                      <select
                        value={item.locationId || ''}
                        onChange={e => handleLocationChange(inb.id, index, e.target.value)}
                        required
                      >
                        <option value="">Selecciona ubicación</option>
                        {locations.map(loc => (
                          <option key={loc.id} value={loc.id}>{loc.code}</option>
                        ))}
                      </select>
                    </li>
                  ))}
                </ul>
              </td>
              <td>
                {inb.status === 'CREATED' && (
                  <button
                    onClick={() => handleReceive(inb)}
                    className="btn-receive"
                  >
                    Confirmar Recepción
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {inbounds.length === 0 && (
        <p className="empty">No hay inbounds disponibles.</p>
      )}
    </div>
  );
}
