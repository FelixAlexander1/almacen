import React, { useEffect, useState, useContext } from 'react';
import { 
  getPickOrders, 
  completePickOrder, 
  getProducts, 
  getLocations, 
  getInbounds, 
  createPickOrder 
} from '../../services/api';
import '../../styles/Workstation.css';
import { ThemeContext } from '../../context/ThemeContext';

export default function Workstation() {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [locations, setLocations] = useState([]);
  const [inbounds, setInbounds] = useState([]);
  const [newLines, setNewLines] = useState([{ productId: '', qty: 0, locationId: '' }]);
  const { darkMode } = useContext(ThemeContext);

  // Cargar datos iniciales
  const loadData = async () => {
    try {
      const [ordersRes, productsRes, locationsRes, inboundsRes] = await Promise.all([
        getPickOrders(),
        getProducts(),
        getLocations(),
        getInbounds()
      ]);

      setOrders(ordersRes.data || []);
      setProducts(productsRes.data || []);
      setLocations(locationsRes.data || []);
      setInbounds(inboundsRes.data || []);
    } catch (err) {
      console.error('Error cargando datos:', err);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleComplete = async (order) => {
    if (window.confirm('¿Marcar Pick Order como PICKED?')) {
      try {
        const pickedLines = (order.items || []).map(item => ({
          productId: item.productId,
          locationId: item.locationId,
          qty: item.qty || item.expectedQty || 0
        }));

        await completePickOrder(order.id, { pickedLines }); // backend marca PICKED
        alert('Pick Order marcada como PICKED');
        loadData();
      } catch (err) {
        console.error(err);
        alert('Error al marcar Pick Order como PICKED');
      }
    }
  };


  // Crear nuevas líneas de Pick Order
  const addLine = () => setNewLines([...newLines, { productId: '', qty: 0, locationId: '' }]);
  const removeLine = (idx) => setNewLines(newLines.filter((_, i) => i !== idx));
  const handleLineChange = (idx, field, value) => {
    const updated = [...newLines];
    updated[idx][field] = field === 'qty' ? parseInt(value) : value;
    setNewLines(updated);
  };

  // Crear Pick Order
  const handleCreatePickOrder = async () => {
    try {
      // Validar líneas
      for (const line of newLines) {
        if (!line.productId || !line.locationId || !line.qty) {
          alert("Todas las líneas deben tener producto, cantidad y ubicación.");
          return;
        }
      }

      const request = { reference: `PO-${Date.now()}`, lines: newLines };
      await createPickOrder(request);
      alert('Pick Order creada');
      setNewLines([{ productId: '', qty: 0, locationId: '' }]);
      loadData();
    } catch (err) {
      console.error('Error al crear Pick Order:', err);
      alert('Error al crear Pick Order');
    }
  };

  const getStatusClass = (status) => {
    switch ((status || '').toLowerCase()) {
      case 'created': return 'status pending';
      case 'picked': return 'status in-progress';
      case 'completed': return 'status completed';
      default: return 'status';
    }
  };

  return (
    <div className={`workstation-container ${darkMode ? 'dark-mode' : ''}`}>
      <h1>Pick Orders Workstation</h1>

      {/* Tabla de Pick Orders */}
      <table className="workstation-table">
        <thead>
          <tr>
            <th>Referencia</th>
            <th>Estado</th>
            <th>Items</th>
            <th className="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          {orders.map(o => (
            <tr key={o.id}>
              <td>{o.reference}</td>
              <td><span className={getStatusClass(o.status)}>{o.status}</span></td>
              <td>{o.items ? o.items.length : 0}</td>
              <td className="text-center">
                {o.status === 'CREATED' && (
                  <button onClick={() => handleComplete(o)} className="btn-complete">
                    Completar Pick
                  </button>
                )}
                {o.status === 'PICKED' && <span className="status in-progress">Pendiente de Shipment</span>}
                {o.status === 'COMPLETED' && <span className="status completed">Completada</span>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="create-pickorder">
        <h2>Crear Pick Order</h2>
        {newLines.map((line, idx) => (
          <div key={idx} className="line-item">
            <select value={line.productId} onChange={e => handleLineChange(idx, 'productId', e.target.value)}>
              <option value="">Producto</option>
              {products.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
            </select>

            <input
              type="number"
              value={line.qty}
              onChange={e => handleLineChange(idx, 'qty', e.target.value)}
              min={1}
              placeholder="Cantidad"
            />

            <select value={line.locationId} onChange={e => handleLineChange(idx, 'locationId', e.target.value)}>
              <option value="">Ubicación</option>
              {locations.map(l => <option key={l.id} value={l.id}>{l.code}</option>)}
            </select>

            <button onClick={() => removeLine(idx)}>Eliminar</button>
          </div>
        ))}

        <button onClick={addLine}>Agregar línea</button>
        <button onClick={handleCreatePickOrder}>Crear Pick Order</button>
      </div>
    </div>
  );
}
