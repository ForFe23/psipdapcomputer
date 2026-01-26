$(() => {
  enableTooltips();
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
