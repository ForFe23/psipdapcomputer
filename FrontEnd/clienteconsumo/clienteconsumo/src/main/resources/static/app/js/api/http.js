import { API_BASE_URL } from "../config.js";

function upperDeep(value) {
  if (value === null || value === undefined) return value;
  if (typeof value === "string") return value.toUpperCase();
  if (Array.isArray(value)) return value.map(upperDeep);
  if (value instanceof Date) return value.toISOString();
  if (typeof value === "object") {
    const result = {};
    for (const [k, v] of Object.entries(value)) {
      result[k] = upperDeep(v);
    }
    return result;
  }
  return value;
}

function normalizeBody(body) {
  if (body instanceof FormData) {
    const normalized = new FormData();
    for (const [k, v] of body.entries()) {
      if (v instanceof File || (typeof File !== "undefined" && v instanceof Blob && v.name)) {
        normalized.append(k, v);
      } else {
        normalized.append(k, typeof v === "string" ? v.toUpperCase() : v);
      }
    }
    return normalized;
  }
  if (typeof body === "string") return body.toUpperCase();
  if (typeof body === "object") return upperDeep(body);
  return body;
}

async function request(path, { method = "GET", body = null, headers = {} } = {}) {
  const opts = { method, headers: { ...headers } };
  if (body !== null && body !== undefined) {
    const normalized = normalizeBody(body);
    if (normalized instanceof FormData) {
      opts.body = normalized;
    } else {
      opts.headers["Content-Type"] = "application/json";
      opts.body = JSON.stringify(normalized);
    }
  }
  const res = await fetch(`${API_BASE_URL}${path}`, opts);
  const contentType = res.headers.get("content-type") || "";
  const data = contentType.includes("application/json") ? await res.json() : await res.text();
  if (!res.ok) {
    const message = typeof data === "string" ? data : data?.message || `Error HTTP ${res.status}`;
    throw new Error(message);
  }
  return data;
}

export const http = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body }),
  put: (path, body) => request(path, { method: "PUT", body }),
  del: (path) => request(path, { method: "DELETE" })
};
