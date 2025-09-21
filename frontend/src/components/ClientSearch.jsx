import React, { useState } from 'react';

export default function ClientSearch({ onSearch }) {
  const [empresaId, setEmpresaId] = useState('');
  const [busqueda, setBusqueda] = useState('');

  const submit = (e) => {
    e.preventDefault();
    if (!empresaId) return alert('Ingresa empresaId');
    onSearch(empresaId, busqueda);
  };

  return (
    <form className="card neon-card" onSubmit={submit}>
      <h3>Buscar clientes</h3>
      <div className="row">
        <label>Empresa ID</label>
        <input value={empresaId} onChange={(e)=>setEmpresaId(e.target.value)} placeholder="Ej: 1" />
      </div>
      <div className="row">
        <label>Criterio</label>
        <input value={busqueda} onChange={(e)=>setBusqueda(e.target.value)} placeholder="cédula / nombre (opcional)"/>
      </div>
      <button className="btn primary" type="submit">Buscar</button>
    </form>
  );
}
