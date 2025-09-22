import React from 'react';

const Table = ({ columns, data }) => {
  return (
    <table className="min-w-full border border-gray-300">
      <thead>
        <tr className="bg-gray-100">
          {columns.map((col) => (
            <th key={col} className="p-2 border">{col}</th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.length === 0 ? (
          <tr>
            <td colSpan={columns.length} className="text-center p-4">
              Sin datos
            </td>
          </tr>
        ) : (
          data.map((row, idx) => (
            <tr key={idx} className="border-t hover:bg-gray-50">
              {Object.values(row).map((value, i) => (
                <td key={i} className="p-2 border">{value}</td>
              ))}
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
};

export default Table;
