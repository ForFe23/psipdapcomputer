const meta = typeof document !== "undefined" ? document.querySelector('meta[name=\"api-base-url\"]') : null;
const globalApi = typeof window !== "undefined" ? window.API_BASE_URL : null;
export const API_BASE_URL = (meta && meta.content) || globalApi || "http://127.0.0.1:8080/api";
