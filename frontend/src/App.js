import React, { useEffect, useState } from 'react';
import './index.css';
import './App.css';
import ClientSearch from './components/ClientSearch';
import ClientForm from './components/ClientForm';
import ClientTable from './components/ClientTable';
import AddressPanel from './components/AddressPanel';
import {
  searchClients, createClient, updateClient, deleteClient,
  createAddress, listAddresses, listAdicionales, getMatriz
} from './api';

export default function App() {
  const [loading, setLoading] = useState(false);
  const [results, setResults] = useState([]);
  const [editing, setEditing] = useState(null);
  const [viewing, setViewing] = useState(null);
  const [addresses, setAddresses] = useState([]);
  const [adicionales, setAdicionales] = useState([]);
  const [matriz, setMatriz] = useState(null);
  const [lastSearch, setLastSearch] = useState({ empresaId: '', busqueda: '' });
  const [toast, setToast] = useState('');

  const notify = (msg) => {
    setToast(msg);
    setTimeout(()=>setToast(''), 2800);
  };

  const doSearch = async (empresaId, busqueda) => {
    try {
      setLoading(true);
      setLastSearch({ empresaId, busqueda });
      const response = await searchClients(empresaId, busqueda);
      console.log('API Response:', response); // Para debug
      
      // La API devuelve { success, message, data, timestamp }
      const clientes = response?.data || [];
      setResults(Array.isArray(clientes) ? clientes : []);
    } catch (e) {
      console.error(e);
      notify(`Error buscando: ${e.message}`);
      setResults([]); // Limpiar resultados en caso de error
    } finally {
      setLoading(false);
    }
  };

  const submitClient = async (payload) => {
    try {
      setLoading(true);
      if (editing?.id) {
        const response = await updateClient(editing.id, payload);
        const updated = response?.data || response;
        notify(`Cliente ${updated.id || 'actualizado'} actualizado`);
        setEditing(null);
      } else {
        const response = await createClient(payload);
        const created = response?.data || response;
        notify(`Cliente ${created.id || 'creado'} creado`);
      }
      if (lastSearch.empresaId) await doSearch(lastSearch.empresaId, lastSearch.busqueda);
    } catch (e) {
      notify(`Error guardando: ${e.message}`);
      console.error('Submit error:', e);
    } finally {
      setLoading(false);
    }
  };

  const removeClient = async (c) => {
    if (!window.confirm(`¿Eliminar cliente ${c.nombres}?`)) return;
    try {
      setLoading(true);
      await deleteClient(c.id);
      notify('Cliente eliminado');
      setResults(prev => prev.filter(x=>x.id !== c.id));
      if (viewing?.id === c.id) setViewing(null);
    } catch (e) {
      notify(`Error eliminando: ${e.message}`);
    } finally {
      setLoading(false);
    }
  };

  const openAddresses = async (c) => {
    setViewing(c);
  };

  const refreshAddresses = async () => {
    if (!viewing?.id) return;
    try {
      setLoading(true);
      const [allRes, adRes, mRes] = await Promise.all([
        listAddresses(viewing.id).catch(()=>({ data: [] })),
        listAdicionales(viewing.id).catch(()=>({ data: [] })),
        getMatriz(viewing.id).catch(()=>({ data: null })),
      ]);
      
      // Extraer datos de las respuestas
      setAddresses(allRes?.data || []);
      setAdicionales(adRes?.data || []);
      setMatriz(mRes?.data || null);
    } catch (e) {
      notify(`Error cargando direcciones: ${e.message}`);
      console.error('Address refresh error:', e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(()=>{ if (viewing) refreshAddresses(); }, [viewing]);

  const addAddress = async (payload) => {
    try {
      setLoading(true);
      const response = await createAddress(payload);
      notify('Dirección registrada');
      await refreshAddresses();
    } catch (e) {
      notify(`Error creando dirección: ${e.message}`);
      console.error('Add address error:', e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <header className="navbar pro">
        <div className="brand">
          <span className="logo-dot" />
          Mi Negocio <strong>· AlquimiaSoft</strong>
        </div>

        <div className="nav-actions">
          <span className="pill soft">API</span>
          <span className="env">
            {process.env.REACT_APP_BASE_URL ? process.env.REACT_APP_BASE_URL : 'proxy'}
          </span>
        </div>
      </header>

      <main className="container">
        <div className="left-panel">
          <ClientForm
            initial={editing}
            onCancel={() => setEditing(null)}
            onSubmit={submitClient}
          />
          <ClientSearch onSearch={doSearch} />
        </div>

        <div className="right-panel">
          {loading && <div className="loader">Cargando…</div>}

          <ClientTable
            data={results}
            onView={(c) => openAddresses(c)}
            onEdit={(c) => setEditing(c)}
            onDelete={(c) => removeClient(c)}
          />

          {viewing && (
            <AddressPanel
              cliente={viewing}
              addresses={addresses}
              adicionales={adicionales}
              matriz={matriz}
              onAdd={addAddress}
              onRefresh={refreshAddresses}
            />
          )}
        </div>
      </main>

      <footer className="footer pro">
        <div className="foot-wrap">
          <div className="foot-left">
            <span className="logo-dot small" />
            Hecho por <strong>Jharol Uchuari</strong> — Este front es generado por IA con React
          </div>
          <div className="foot-right">
            <span className="spark" />
          </div>
        </div>
      </footer>

      {toast && <div className="toast">{toast}</div>}
    </div>
  );
}
