const BASE_URL = process.env.REACT_APP_BASE_URL?.replace(/\/$/, '') || '';

async function http(method, url, body) {
  const resp = await fetch(`${BASE_URL}${url}`, {
    method,
    headers: { 'Content-Type': 'application/json' },
    body: body ? JSON.stringify(body) : undefined,
  });
  if (!resp.ok) {
    const text = await resp.text().catch(() => '');
    throw new Error(`${resp.status} ${resp.statusText} – ${text}`);
  }
  return resp.status !== 204 ? resp.json() : null;
}

// ---- Clientes ----
export const searchClients = (empresaId, busqueda) =>
  http('GET', `/clientes/buscar?empresaId=${encodeURIComponent(empresaId)}${busqueda ? `&busqueda=${encodeURIComponent(busqueda)}`:''}`);

export const getClientById = (clienteId) =>
  http('GET', `/clientes/${clienteId}`);

export const createClient = (payload) =>
  http('POST', `/clientes`, payload);

export const updateClient = (clienteId, payload) =>
  http('PUT', `/clientes/${clienteId}`, payload);

export const deleteClient = (clienteId) =>
  http('DELETE', `/clientes/${clienteId}`);

// ---- Direcciones ----
export const createAddress = (payload) =>
  http('POST', `/clientes/direcciones`, payload);

export const listAddresses = (clienteId) =>
  http('GET', `/clientes/${clienteId}/direcciones`);

export const listAdicionales = (clienteId) =>
  http('GET', `/clientes/${clienteId}/direcciones/adicionales`);

export const getMatriz = (clienteId) =>
  http('GET', `/clientes/${clienteId}/direcciones/matriz`);
