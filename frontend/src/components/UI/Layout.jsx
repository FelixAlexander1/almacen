import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { ThemeContext } from '../../context/ThemeContext'; 
import { Sun, Moon } from 'lucide-react';
import '../../styles/Layout.css';

const Layout = ({ children }) => {
  const { darkMode, toggleDarkMode } = useContext(ThemeContext);

  return (
    <div className={`layout-container ${darkMode ? 'dark-mode' : ''}`}>
      {/* Navbar superior */}
      <header className="navbar">
        <div className="navbar-logo">
          <h1>Warehouse</h1>
        </div>

        <nav className="navbar-links">
          <Link to="/">Dashboard</Link>
          <Link to="/products">Productos</Link>
          <Link to="/inventory">Inventario</Link>
          <Link to="/inbounds">Inbounds</Link>
          <Link to="/shipments">Shipments</Link>
          <Link to="/pickorders/workstation">Pick Orders</Link>
        </nav>

        {/* Botón Dark Mode */}
        <div className="darkmode-toggle">
          <button className="mode-switch" onClick={toggleDarkMode}>
            {darkMode ? <Sun className="icon" /> : <Moon className="icon" />}
            <span className="mode-text">{darkMode ? 'Light' : 'Dark'}</span>
          </button>
        </div>
      </header>

      {/* Main content */}
      <main className="main-content">{children}</main>
    </div>
  );
};

export default Layout;
