// src/pages/CrossDocking/Create.jsx
import React, { useState } from 'react';
import Layout from '../../components/UI/Layout';
import { createCrossDock } from '../../services/api';

const CrossDockCreate = () => {
  const [form, setForm] = useState({ inboundId: '', outShipmentId: '' });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createCrossDock(form);
      alert('Operación de CrossDock creada');
    } catch (err) {
      console.error(err);
      alert('Error al crear operación');
    }
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-4">Crear CrossDock</h1>
      <form onSubmit={handleSubmit} className="space-y-4 max-w-md">
        <input
          name="inboundId"
          placeholder="Inbound ID"
          value={form.inboundId}
          onChange={handleChange}
          className="border p-2 w-full"
        />
        <input
          name="outShipmentId"
          placeholder="Shipment ID"
          value={form.outShipmentId}
          onChange={handleChange}
          className="border p-2 w-full"
        />
        <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded">
          Crear
        </button>
      </form>
    </Layout>
  );
};

export default CrossDockCreate;
