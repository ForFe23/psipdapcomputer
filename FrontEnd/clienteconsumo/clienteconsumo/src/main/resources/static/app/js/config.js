const meta = typeof document !== "undefined" ? document.querySelector('meta[name="api-base-url"]') : null;
const globalApi = typeof window !== "undefined" ? window.API_BASE_URL : null;

function computeHostApi() {
  if (meta && meta.content) return meta.content;
  if (globalApi) return globalApi;

  if (typeof window === "undefined") return "http://127.0.0.1:8080/api";

  const { protocol, hostname, port, origin } = window.location;
  const portNumber = port ? Number(port) : null;
  const isLanIp = hostname && /^(\d{1,3}\.){3}\d{1,3}$/.test(hostname);

  const sameOriginApi = `${origin.replace(/\/$/, "")}/api`;

  if (protocol === "file:" || origin === "null" || origin === null || origin === undefined) {
    return "http://127.0.0.1:8080/api";
  }

  if (portNumber === 8090 || portNumber === 3000 || portNumber === 8443) {
    return `https://${hostname}:8444/api`;
  }

  if (isLanIp || !port) {
    return sameOriginApi;
  }

  return "http://127.0.0.1:8080/api";
}

export const API_BASE_URL = computeHostApi();


