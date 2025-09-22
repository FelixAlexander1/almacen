import React, { useEffect, useState, useContext } from "react";
import { getProducts, deleteProduct } from "../../services/api";
import ProductForm from "../../components/ProductForm";
import { ThemeContext } from "../../context/ThemeContext"; 
import "../../styles/ProductList.css";

export default function ProductList() {
  const [products, setProducts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editProduct, setEditProduct] = useState(null);

  const { darkMode } = useContext(ThemeContext); // 🔹 Usamos darkMode global

  const loadProducts = () => {
    getProducts()
      .then((res) => setProducts(res.data))
      .catch((err) => console.error("Error fetching products:", err));
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const handleSuccess = () => {
    setShowForm(false);
    setEditProduct(null);
    loadProducts();
  };

  const handleDelete = async (id) => {
    if (window.confirm("¿Seguro que quieres eliminar este producto?")) {
      try {
        await deleteProduct(id);
        alert("Producto eliminado con éxito");
        loadProducts();
      } catch (error) {
        console.error("Error eliminando producto:", error);
        alert("No se pudo eliminar el producto");
      }
    }
  };

  const handleEdit = (product) => {
    setEditProduct(product);
    setShowForm(true);
  };

  return (
    <div className={`product-list-container ${darkMode ? "dark-mode" : ""}`}>
      {/* Header sin el botón de dark mode, solo el de crear producto */}
      <div className="header flex justify-between items-center mb-4">
        <h1 className="title">Lista de Productos</h1>
        <div className="header-actions">
          <button
            className="btn-create"
            onClick={() => {
              setEditProduct(null);
              setShowForm(true);
            }}
          >
            + Crear Producto
          </button>
        </div>
      </div>

      {/* Tabla de productos */}
      <table className="products-table">
        <thead>
          <tr>
            <th>SKU</th>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>UOM</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {products.map((p) => (
            <tr key={p.id}>
              <td>{p.sku}</td>
              <td>{p.name}</td>
              <td>{p.description}</td>
              <td>{p.uom}</td>
              <td>
                <button className="btn-edit" onClick={() => handleEdit(p)}>
                  Editar
                </button>
                <button className="btn-delete" onClick={() => handleDelete(p.id)}>
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal */}
      {showForm && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>{editProduct ? "Editar Producto" : "Crear Producto"}</h2>
            <ProductForm
              onSuccess={handleSuccess}
              initialData={editProduct}
              mode={editProduct ? "edit" : "create"}
            />
            <button
              className="btn-cancel"
              onClick={() => {
                setShowForm(false);
                setEditProduct(null);
              }}
            >
              Cancelar
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
