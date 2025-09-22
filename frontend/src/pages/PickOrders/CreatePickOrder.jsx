import React, { useEffect, useState } from 'react';
import { getProducts, getLocations, createPickOrder } from '../../services/api';

export default function CreatePickOrder() {
  const [products, setProducts] = useState([]);
  const [locations, setLocations] = useState([]);
  const [lines, setLines] = useState([{ productId: '', qty: 0, locationId: '' }]);

  useEffect(() => {
    getProducts().then(res => setProducts(res.data));
    getLocations().then(res => setLocations(res.data));
  }, []);

  const addLine = () => setLines([...lines, { productId: '', qty: 0, locationId: '' }]);
  const removeLine = (i) => setLines(lines.filter((_, idx) => idx !== i));

  const handleSubmit = async () => {
    const request = { reference: `PO-${Date.now()}`, lines };
    try {
      await createPickOrder(request);
      alert('Pick Order creada');
    } catch (err) {
      console.error(err);
      alert('Error al crear Pick Order');
    }
  };

  return (
    <div>
      {lines.map((line, idx) => (
        <div key={idx}>
          <select onChange={e => line.productId = e.target.value} value={line.productId}>
            <option value="">Producto</option>
            {products.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
          </select>
          <input type="number" value={line.qty} onChange={e => line.qty = parseInt(e.target.value)} />
          <select onChange={e => line.locationId = e.target.value} value={line.locationId}>
            <option value="">Ubicación</option>
            {locations.map(l => <option key={l.id} value={l.id}>{l.code}</option>)}
          </select>
          <button onClick={() => removeLine(idx)}>Eliminar</button>
        </div>
      ))}
      <button onClick={addLine}>Agregar línea</button>
      <button onClick={handleSubmit}>Crear Pick Order</button>
    </div>
  );
}
