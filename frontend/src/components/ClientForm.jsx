import React, { useEffect, useState } from 'react';

const emptyClient = {
  tipoIdentificacion: 'CEDULA',
  numeroIdentificacion: '',
  nombres: '',
  correo: '',
  celular: '',
  empresaId: '',
  direccionMatriz: {
    provincia: '',
    ciudad: '',
    direccion: ''
  }
};

export default function ClientForm({ initial, onCancel, onSubmit }) {
  const [client, setClient] = useState(initial || emptyClient);

  useEffect(() => { setClient(initial || emptyClient); }, [initial]);

  const change = (k, v) => setClient(prev => ({ ...prev, [k]: v }));
  
  const changeAddress = (k, v) => setClient(prev => ({
    ...prev,
    direccionMatriz: { ...prev.direccionMatriz, [k]: v }
  }));

  const submit = (e) => {
    e.preventDefault();
    if (!client.empresaId) return alert('empresaId es requerido');
    if (!client.numeroIdentificacion) return alert('numeroIdentificacion es requerido');
    if (!client.nombres) return alert('nombres es requerido');
    
    // Para crear cliente, validar dirección matriz
    if (!client.id) {
      if (!client.direccionMatriz?.provincia) return alert('Provincia de dirección matriz es requerida');
      if (!client.direccionMatriz?.ciudad) return alert('Ciudad de dirección matriz es requerida');
      if (!client.direccionMatriz?.direccion) return alert('Dirección matriz es requerida');
    }
    
    onSubmit(client);
  };

  return (
    <form className="card glass-card" onSubmit={submit}>
      <h3>{client.id ? 'Editar cliente' : 'Crear cliente'}</h3>
      <div className="grid-2">
        <div>
          <label>Empresa ID</label>
          <input value={client.empresaId} onChange={e=>change('empresaId', e.target.value)} />
        </div>
        <div>
          <label>Tipo Identificación</label>
          <select value={client.tipoIdentificacion} onChange={e=>change('tipoIdentificacion', e.target.value)}>
            <option value="RUC">RUC</option>
            <option value="CEDULA">CÉDULA</option>
            <option value="PASAPORTE">PASAPORTE</option>
          </select>
        </div>
        <div>
          <label>Número Identificación</label>
          <input value={client.numeroIdentificacion} onChange={e=>change('numeroIdentificacion', e.target.value)} />
        </div>
        <div>
          <label>Nombres</label>
          <input value={client.nombres} onChange={e=>change('nombres', e.target.value)} />
        </div>
        <div>
          <label>Correo</label>
          <input value={client.correo} onChange={e=>change('correo', e.target.value)} />
        </div>
        <div>
          <label>Celular</label>
          <input value={client.celular} onChange={e=>change('celular', e.target.value)} />
        </div>
      </div>
      
      {/* Dirección matriz - solo para crear cliente nuevo */}
      {!client.id && (
        <div className="direccion-section" style={{ marginTop: '20px', padding: '15px', border: '1px solid #333', borderRadius: '8px' }}>
          <h4 style={{ marginBottom: '15px', color: '#64ffda' }}>Dirección Matriz</h4>
          <div className="grid-3">
            <div>
              <label>Provincia</label>
              <input 
                value={client.direccionMatriz?.provincia || ''} 
                onChange={e=>changeAddress('provincia', e.target.value)} 
                placeholder="Ej: Pichincha"
              />
            </div>
            <div>
              <label>Ciudad</label>
              <input 
                value={client.direccionMatriz?.ciudad || ''} 
                onChange={e=>changeAddress('ciudad', e.target.value)} 
                placeholder="Ej: Quito"
              />
            </div>
            <div>
              <label>Dirección</label>
              <input 
                value={client.direccionMatriz?.direccion || ''} 
                onChange={e=>changeAddress('direccion', e.target.value)} 
                placeholder="Ej: Av. Principal 123"
              />
            </div>
          </div>
        </div>
      )}
      <div className="actions" style={{ marginTop: '20px' }}>
        <button className="btn primary" type="submit">{client.id ? 'Actualizar' : 'Crear'}</button>
        <button className="btn" type="button" onClick={onCancel}>Cancelar</button>
      </div>
    </form>
  );
}
