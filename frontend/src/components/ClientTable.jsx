import React from 'react';

export default function ClientTable({ data, onView, onEdit, onDelete }) {
  if (!data?.length) {
    return <div className="card muted">Sin resultados</div>;
  }
  return (
    <div className="card glass-card">
      <h3>Resultados</h3>
      <div className="table-wrap">
        <table className="neon-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Identificación</th>
              <th>Nombres</th>
              <th>Correo</th>
              <th>Celular</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
          {data.map((c)=>(
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.tipoIdentificacion} - {c.numeroIdentificacion}</td>
              <td>{c.nombres}</td>
              <td>{c.correo}</td>
              <td>{c.celular}</td>
              <td className="actions">
                <button className="btn tiny" onClick={()=>onView(c)}>Direcciones</button>
                <button className="btn tiny" onClick={()=>onEdit(c)}>Editar</button>
                <button className="btn danger tiny" onClick={()=>onDelete(c)}>Eliminar</button>
              </td>
            </tr>
          ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
