import { loadLayout } from "../ui/render.js";

async function main() {
  await loadLayout("plantilla");
}

document.addEventListener("DOMContentLoaded", main);
