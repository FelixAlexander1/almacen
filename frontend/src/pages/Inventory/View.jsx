import React, { useEffect, useState, useContext } from "react";
import { getInventory, adjustInventory, transferInventory, getLocations } from "../../services/api";
import { ThemeContext } from "../../context/ThemeContext"; 

export default function InventoryView() {
  const [inventory, setInventory] = useState([]);
  const [locations, setLocations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filters, setFilters] = useState({ product: "", sku: "", location: "" });
  
  const [transferItemId, setTransferItemId] = useState(null);
  const [transferQty, setTransferQty] = useState(0);
  const [transferLocation, setTransferLocation] = useState("");

  const [adjustItemId, setAdjustItemId] = useState(null);
  const [adjustQty, setAdjustQty] = useState(0);

  const { darkMode } = useContext(ThemeContext); 

  const loadInventory = async () => {
    setLoading(true);
    try {
      const res = await getInventory();
      setInventory(res.data);
    } catch (err) {
      console.error("Error fetching inventory:", err);
    } finally {
      setLoading(false);
    }
  };

  const loadLocations = async () => {
    try {
      const res = await getLocations();
      setLocations(res.data);
    } catch (err) {
      console.error("Error fetching locations:", err);
    }
  };

  useEffect(() => {
    loadInventory();
    loadLocations();
  }, []);

  const handleFilterChange = (e) => {
    setFilters({ ...filters, [e.target.name]: e.target.value });
  };

  const filteredInventory = inventory.filter(
    (item) =>
      (filters.product === "" || item.product?.name.toLowerCase().includes(filters.product.toLowerCase())) &&
      (filters.sku === "" || item.product?.sku.toLowerCase().includes(filters.sku.toLowerCase())) &&
      (filters.location === "" || item.location?.code.toLowerCase().includes(filters.location.toLowerCase()))
  );

  // --- Ajustar Stock ---
  const startAdjust = (item) => {
    setAdjustItemId(item.id);
    setAdjustQty(item.quantityTotal);
  };

  const confirmAdjust = async (item) => {
    if (adjustQty < item.quantityReserved) {
      alert(`No puedes reducir el stock total por debajo del stock reservado (${item.quantityReserved})`);
      return;
    }
    try {
      await adjustInventory({ productId: item.product.id, locationId: item.location.id, countedQuantity: adjustQty });
      setAdjustItemId(null);
      loadInventory();
    } catch (error) {
      console.error(error);
    }
  };

  // --- Transferir Stock ---
  const startTransfer = (item) => {
    setTransferItemId(item.id);
    setTransferQty(item.quantityTotal - item.quantityReserved); // stock disponible
    setTransferLocation("");
  };

  const confirmTransfer = async (item) => {
    const available = item.quantityTotal - item.quantityReserved;

    if (!transferLocation || transferQty <= 0) return;

    if (transferQty > available) {
      alert(`No puedes transferir más de ${available} unidades. Stock disponible: ${available}`);
      return;
    }

    try {
      await transferInventory({
        productId: item.product.id,
        fromLocationId: item.location.id,
        toLocationId: transferLocation,
        quantity: transferQty,
      });
      setTransferItemId(null);
      loadInventory();
    } catch (error) {
      console.error(error);
    }
  };

  if (loading) return <p className="p-4">Cargando inventario...</p>;

  return (
    <div className={`product-list-container ${darkMode ? 'dark-mode' : ''}`}>
      <div className="header flex justify-between items-center mb-4">
        <h1 className="title">Inventario</h1>
      </div>

      <div className="filters">
        <input name="product" placeholder="Filtrar por Producto" value={filters.product} onChange={handleFilterChange} />
        <input name="sku" placeholder="Filtrar por SKU" value={filters.sku} onChange={handleFilterChange} />
        <input name="location" placeholder="Filtrar por Ubicación" value={filters.location} onChange={handleFilterChange} />
      </div>

      <table className="products-table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>Producto</th>
            <th>Ubicación</th>
            <th>Stock Total</th>
            <th>Reservado</th>
            <th>Disponible</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {filteredInventory.map((item) => {
            const available = item.quantityTotal - item.quantityReserved;
            const isTransferring = transferItemId === item.id;
            const isAdjusting = adjustItemId === item.id;

            return (
              <React.Fragment key={item.id}>
                <tr>
                  <td>{item.product?.sku || "-"}</td>
                  <td>{item.product?.name || "-"}</td>
                  <td>{item.location?.code || "-"}</td>
                  <td>{item.quantityTotal}</td>
                  <td>{item.quantityReserved}</td>
                  <td>{available} / {item.quantityTotal} <small>(Reservado: {item.quantityReserved})</small></td>
                  <td>
                    <button className="btn-edit" onClick={() => startAdjust(item)}>Ajustar</button>
                    <button className="btn-delete" onClick={() => startTransfer(item)}>Transferir</button>
                  </td>
                </tr>

                {/* Ajuste inline */}
                {isAdjusting && (
                  <tr>
                    <td colSpan={7}>
                      <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
                        <input 
                          type="number" 
                          value={adjustQty} 
                          min={item.quantityReserved} // no puede bajar de reservado
                          onChange={e => setAdjustQty(Number(e.target.value))}
                        />
                        <button onClick={() => confirmAdjust(item)}>Confirmar</button>
                        <button onClick={() => setAdjustItemId(null)}>Cancelar</button>
                      </div>
                    </td>
                  </tr>
                )}

                {/* Transferencia inline */}
                {isTransferring && (
                  <tr>
                    <td colSpan={7}>
                      <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
                        <select value={transferLocation} onChange={e => setTransferLocation(e.target.value)}>
                          <option value="">Seleccione ubicación destino</option>
                          {locations
                            .filter(loc => loc.id !== item.location.id)
                            .map(loc => (
                              <option key={loc.id} value={loc.id}>{loc.code}</option>
                            ))}
                        </select>
                        <input 
                          type="number" 
                          value={transferQty} 
                          min={1} 
                          max={available} // límite por stock disponible
                          onChange={e => setTransferQty(Number(e.target.value))}
                        />
                        <button onClick={() => confirmTransfer(item)}>Confirmar</button>
                        <button onClick={() => setTransferItemId(null)}>Cancelar</button>
                      </div>
                    </td>
                  </tr>
                )}
              </React.Fragment>
            );
          })}
        </tbody>
      </table>
    </div>
  );
}
