import React, { useEffect, useState } from 'react';

export default function AddressPanel({ cliente, onAdd, onRefresh, addresses, adicionales, matriz }) {
  const [newAddr, setNewAddr] = useState({
    clienteId: cliente?.id,
    provincia: '',
    ciudad: '',
    direccion: '',
  });

  useEffect(()=> {
    setNewAddr(prev => ({...prev, clienteId: cliente?.id || ''}));
  }, [cliente]);

  const change = (k,v)=> setNewAddr(p=>({...p,[k]:v}));

  const submit = (e) => {
    e.preventDefault();
    if (!newAddr.provincia || !newAddr.ciudad || !newAddr.direccion) {
      return alert('Completa provincia, ciudad y dirección');
    }
    onAdd(newAddr);
    // Limpiar formulario después de enviar
    setNewAddr({
      clienteId: cliente?.id,
      provincia: '',
      ciudad: '',
      direccion: '',
    });
  };

  return (
    <div className="card neon-card">
      <h3>Direcciones de: <span className="pill">{cliente?.nombres}</span></h3>

      <div className="grid-3">
        <div className="subcard">
          <h4>Dirección Matriz</h4>
          {matriz ? (
            <div className="badge-list">
              <div className="badge">Provincia: {matriz.provincia}</div>
              <div className="badge">Ciudad: {matriz.ciudad}</div>
              <div className="badge">Dirección: {matriz.direccion}</div>
            </div>
          ) : <div className="muted">No registrada</div>}
        </div>

        <div className="subcard">
          <h4>Direcciones Adicionales ({adicionales?.length || 0})</h4>
          {adicionales?.length ? adicionales.map(a=>(
            <div key={a.id} className="chip">{a.provincia} · {a.ciudad} · {a.direccion}</div>
          )) : <div className="muted">Sin direcciones adicionales</div>}
        </div>

        <div className="subcard">
          <h4>Nueva Dirección Adicional</h4>
          <form onSubmit={submit} className="stack">
            <input 
              placeholder="Provincia" 
              value={newAddr.provincia} 
              onChange={e=>change('provincia', e.target.value)} 
            />
            <input 
              placeholder="Ciudad" 
              value={newAddr.ciudad} 
              onChange={e=>change('ciudad', e.target.value)} 
            />
            <input 
              placeholder="Dirección" 
              value={newAddr.direccion} 
              onChange={e=>change('direccion', e.target.value)} 
            />
            <button className="btn primary" type="submit">Agregar Adicional</button>
            <button className="btn" type="button" onClick={onRefresh}>Refrescar</button>
          </form>
        </div>
      </div>
    </div>
  );
}
