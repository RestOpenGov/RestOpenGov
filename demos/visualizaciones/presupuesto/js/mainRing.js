var labelType, useGradients, nativeTextSupport, animate, queryStringList;

(function() {
  var ua = navigator.userAgent,
      iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
      typeOfCanvas = typeof HTMLCanvasElement,
      nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
      textSupport = nativeCanvasSupport
        && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');

  //I'm setting this based on the fact that ExCanvas provides text support for IE
  //and that as of today iPhone/iPad current text support is lame
  labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
  nativeTextSupport = (labelType == 'Native');
  useGradients = nativeCanvasSupport;
  animate = !(iStuff || !nativeCanvasSupport);
})();

jQuery(document).ready(function(){

  var LOADER = new Loader(),
      presupuestoViz = new PresupuestoViz(),
      VIZ = new Viz(),
      initialized = false;

  $("#anio-selector").on('change',function(e) {
    initLoad();
  });

  var legendContainer = $('#labelContainer');
  var listItems = [];

  $.each(presupuestoViz.conn.COLORS,function(i,e){

      listItems.push('<div class=\'query-color\' style=\'background-color:'
      + e +'\'>&nbsp;</div>' + i); 
  });
  legendContainer.html('<li>' + listItems.join('</li><li>') + '</li>');

  function initLoad() {

    $('#infovis-ring').html('');

    $('#infoContainer').show();

    //loader on
    LOADER.open();

    anio = $('#anio-selector').val();

    //anio = '2011';

    //Listener to retrieve Async info
    $(presupuestoViz).on('retrieveByAnioComplete', renderByAnio);
    presupuestoViz.retrieveByAnio(anio);
  }

  //load complete callback
  function renderByAnio(e){
    $(presupuestoViz).off('retrieveByAnioComplete', renderByAnio);
    //Listener to Async data generation
    $(VIZ).on('renderComplete', renderComplete);
    VIZ.render(e.results);
  }

  //render complete callback
  function renderComplete(){
    $(VIZ).off('renderComplete', renderComplete);
    LOADER.close();
  }

  //have fun!
  initLoad();

});