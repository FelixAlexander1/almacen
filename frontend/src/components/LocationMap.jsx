import React from 'react';

const LocationMap = ({ locations }) => {
  return (
    <div className="grid grid-cols-4 gap-4">
      {locations.map((loc) => (
        <div
          key={loc.id}
          className={`p-4 text-center rounded border ${
            loc.available ? 'bg-green-100' : 'bg-red-100'
          }`}
        >
          {loc.name}
        </div>
      ))}
    </div>
  );
};

export default LocationMap;
