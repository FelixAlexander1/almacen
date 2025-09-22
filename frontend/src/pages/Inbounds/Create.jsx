import React, { useState, useEffect, useContext } from 'react';
import { createInbound, getProducts } from '../../services/api';
import { useNavigate } from 'react-router-dom';
import { ThemeContext } from '../../context/ThemeContext'; 
import '../../styles/CreateInbound.css';

export default function CreateInbound() {
  const [asnNumber, setAsnNumber] = useState('');
  const [expectedDate, setExpectedDate] = useState('');
  const [products, setProducts] = useState([]);
  const [lines, setLines] = useState([{ productId: '', expectedQty: 0, lot: '' }]);
  const { darkMode } = useContext(ThemeContext); // ✅ Usamos el context global
  const navigate = useNavigate();

  useEffect(() => {
    getProducts()
      .then(res => setProducts(res.data))
      .catch(console.error);
  }, []);

  const addLine = () => setLines([...lines, { productId: '', expectedQty: 0, lot: '' }]);
  const removeLine = (index) => setLines(lines.filter((_, i) => i !== index));
  const handleLineChange = (index, field, value) => {
    const newLines = [...lines];
    newLines[index][field] = field === 'expectedQty' ? parseInt(value) || 0 : value;
    setLines(newLines);
  };
  const isValid = () => lines.every(line => line.productId && line.expectedQty > 0);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!isValid()) {
      alert('Todas las líneas deben tener producto y cantidad válida.');
      return;
    }
    try {
      await createInbound({ asnNumber, expectedDate, items: lines });
      alert('Inbound creado con éxito');
      navigate('/inbounds');
    } catch (err) {
      console.error(err);
      alert('Error al crear inbound');
    }
  };

  return (
    <div className={`create-inbound-container ${darkMode ? 'dark-mode' : ''}`}>
      {/* Header */}
      <div className="inbound-header flex justify-between items-center mb-4">
        <h1 className="title">Crear Inbound</h1>
      </div>

      <form onSubmit={handleSubmit} className="inbound-form">
        <input
          type="text"
          placeholder="Referencia"
          value={asnNumber}
          onChange={e => setAsnNumber(e.target.value)}
          required
        />
        <input
          type="date"
          placeholder="Fecha esperada"
          value={expectedDate}
          onChange={e => setExpectedDate(e.target.value)}
          required
        />

        <h2>Líneas de productos</h2>
        {lines.map((line, idx) => (
          <div key={idx} className="line-item">
            <select
              value={line.productId}
              onChange={e => handleLineChange(idx, 'productId', e.target.value)}
              required
            >
              <option value="">Selecciona producto</option>
              {products.map(p => (
                <option key={p.id} value={p.id}>{p.name}</option>
              ))}
            </select>
            <input
              type="number"
              placeholder="Cantidad"
              value={line.expectedQty}
              onChange={e => handleLineChange(idx, 'expectedQty', e.target.value)}
              min={1}
              required
            />
            <input
              type="text"
              placeholder="Lote"
              value={line.lot}
              onChange={e => handleLineChange(idx, 'lot', e.target.value)}
            />
            <button type="button" onClick={() => removeLine(idx)} className="btn-remove">
              Eliminar
            </button>
          </div>
        ))}

        <button type="button" onClick={addLine} className="btn-add">
          Agregar línea
        </button>
        <button type="submit" className="btn-submit">
          Crear Inbound
        </button>
      </form>
    </div>
  );
}
