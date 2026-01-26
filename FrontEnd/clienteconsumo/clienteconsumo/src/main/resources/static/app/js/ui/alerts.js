function ensureContainer() {
  let el = document.querySelector("#app-alerts");
  if (!el) {
    el = document.createElement("div");
    el.id = "app-alerts";
    el.className = "container-fluid mt-3";
    document.body.prepend(el);
  }
  return el;
}

function render(type, message) {
  const container = ensureContainer();
  container.innerHTML = "";
  const alert = document.createElement("div");
  alert.className = `alert alert-${type} alert-dismissible fade show`;
  alert.role = "alert";
  alert.textContent = message;
  const close = document.createElement("button");
  close.type = "button";
  close.className = "close";
  close.innerHTML = "&times;";
  close.addEventListener("click", () => alert.remove());
  alert.appendChild(close);
  container.appendChild(alert);
}

export function showSuccess(message) {
  render("success", message);
}

export function showError(message) {
  render("danger", message);
}
