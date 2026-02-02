$(() => {
  enableTooltips();
  startAmbientMotion();
});

function enableTooltips() {
  if (!window.$ || !$.fn.tooltip) return;
  $('[data-toggle="tooltip"]').tooltip();
  $(window).on("resize", () => {
    if (!window.matchMedia("(max-width: 600px)").matches) {
      $('[data-toggle="tooltip"]').tooltip("hide");
    }
  });
}

function startAmbientMotion() {
  const prefersReduce = window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
  if (!("IntersectionObserver" in window) || prefersReduce) return;

  const targets = Array.from(
    document.querySelectorAll(
      ".card, .filter-bar, .hero-card, .mini-card, .kpi-card, .table-responsive, .timeline"
    )
  );

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          entry.target.classList.add("is-visible");
          observer.unobserve(entry.target);
        }
      });
    },
    { threshold: 0.16 }
  );

  targets.forEach((el, idx) => {
    el.classList.add("reveal-up");
    el.style.setProperty("--reveal-delay", `${0.04 * (idx % 8)}s`);
    observer.observe(el);
  });
}
