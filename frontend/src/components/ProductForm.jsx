import React, { useState, useEffect } from "react";
import { createProduct, updateProduct } from "../services/api";

export default function ProductForm({ onSuccess, initialData, mode = "create" }) {
  const [form, setForm] = useState({
    sku: "",
    name: "",
    description: "",
    uom: "",
  });

  // Si estamos editando, llenamos el formulario con los datos iniciales
  useEffect(() => {
    if (initialData) {
      setForm(initialData);
    }
  }, [initialData]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (mode === "edit") {
        await updateProduct(initialData.id, form);
        alert("Producto actualizado con éxito");
      } else {
        await createProduct(form);
        alert("Producto creado con éxito");
      }
      onSuccess && onSuccess();
      setForm({ sku: "", name: "", description: "", uom: "" });
    } catch (error) {
      console.error("Error al guardar producto:", error);
      alert("Error al guardar producto");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-w-md">
      <input
        type="text"
        name="sku"
        placeholder="SKU"
        value={form.sku}
        onChange={handleChange}
        className="border p-2 w-full"
        required
      />
      <input
        type="text"
        name="name"
        placeholder="Nombre"
        value={form.name}
        onChange={handleChange}
        className="border p-2 w-full"
        required
      />
      <input
        type="text"
        name="description"
        placeholder="Descripción"
        value={form.description}
        onChange={handleChange}
        className="border p-2 w-full"
      />
      <input
        type="text"
        name="uom"
        placeholder="Unidad de medida"
        value={form.uom}
        onChange={handleChange}
        className="border p-2 w-full"
      />

      {/* Propiedades físicas */}
      <input
        type="number"
        name="length"
        placeholder="Largo"
        value={form.length || ""}
        onChange={handleChange}
        className="border p-2 w-full"
        step="0.01"
      />
      <input
        type="number"
        name="width"
        placeholder="Ancho"
        value={form.width || ""}
        onChange={handleChange}
        className="border p-2 w-full"
        step="0.01"
      />
      <input
        type="number"
        name="height"
        placeholder="Alto"
        value={form.height || ""}
        onChange={handleChange}
        className="border p-2 w-full"
        step="0.01"
      />
      <input
        type="number"
        name="weight"
        placeholder="Peso"
        value={form.weight || ""}
        onChange={handleChange}
        className="border p-2 w-full"
        step="0.01"
      />

      {/* Hazardous */}
      <label className="flex items-center gap-2">
        <input
          type="checkbox"
          name="hazardous"
          checked={form.hazardous || false}
          onChange={(e) =>
            setForm({ ...form, hazardous: e.target.checked })
          }
        />
        Peligroso
      </label>

      <button
        type="submit"
        className="bg-green-600 text-white px-4 py-2 rounded"
      >
        {mode === "edit" ? "Actualizar" : "Guardar"}
      </button>
    </form>

  );
}
