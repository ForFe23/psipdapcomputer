import { API_BASE_URL } from "../config.js";

async function request(path, { method = "GET", body = null, headers = {} } = {}) {
  const opts = { method, headers: { ...headers } };
  if (body) {
    if (body instanceof FormData) {
      opts.body = body;
    } else {
      opts.headers["Content-Type"] = "application/json";
      opts.body = JSON.stringify(body);
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
