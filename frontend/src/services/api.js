// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});
//Actividades recientes
export const getLatestActivities = () => api.get('/activities/latest');

// Productos
export const getProducts = () => api.get('/products');
export const getProductById = (id) => api.get(`/products/${id}`);
export const createProduct = (data) => api.post('/products', data);
export const updateProduct = (id, data) => api.put(`/products/${id}`, data);
export const deleteProduct = (id) => api.delete(`/products/${id}`);

// Shipments
export const getShipments = () => api.get('/shipments');
export const createShipment = (data) => api.post('/shipments', data);
export const confirmShipment = (id) => api.post(`/shipments/${id}/ship`);
export const getShipmentById = (id) => api.get(`/shipments/${id}`);

// Inbounds
export const createInbound = (data) => api.post('/inbounds', data);
export const getInbounds = () => api.get('/inbounds');
export const confirmInbound = (id, body) => api.post(`/inbounds/${id}/receive`, body);


// Inventario
export const getInventory = () => api.get('/inventory');
export const adjustInventory = (data) => api.post('/inventory/adjust', data);
export const transferInventory = (data) => api.post('/inventory/transfer', data);

// CrossDock
export const createCrossDock = (data) => api.post('/crossdock', data);
export const getCrossDockOperations = () => api.get('/crossdock');
export const completeCrossDock = (id) => api.post(`/crossdock/${id}/complete`);

// PickOrders
export const getPickOrders = () => api.get('/pickings');
export const completePickOrder = (id, body) => api.post(`/pickings/${id}/confirm`, body);
export const createPickOrder = (data) => api.post('/pickings', data);

// Ubicaciones
export const getLocations = () => api.get('/locations');
export const createLocation = (data) => api.post('/locations', data);
export const updateLocation = (id, data) => api.put(`/locations/${id}`, data);


export default api;
