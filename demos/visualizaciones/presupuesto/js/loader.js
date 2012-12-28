function Loader() {
}

Loader.prototype = (function() {

  var loaderHtml = 
  '<div id="loaderContainer" class="progress progress-striped active">'+
  '<div class="bar" style="width: 100%;"></div>'
  '</div>';

  return {
    constructor: Loader,
    open: function() {
      $('#loaderContainer').html(loaderHtml);
      $('#legendContainer').html('Buscando información para generar el gráfico.');
    },
    close: function() {
      $('#loaderContainer').html('');
      $('#legendContainer').html('');
    }
  };
})();
