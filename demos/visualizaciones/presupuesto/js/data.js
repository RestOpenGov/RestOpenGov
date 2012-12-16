function Data() {
}

Data.prototype = (function() {

  var _anio, _marca, _restOpenGov, _resultado;

  var _reset = function() {
    //_anio = '';
    _marca = '';
    _restOpenGov = new RestOpenGov({ dataSource:'test' });
    _instance = {};
  };

  var _queryElastic = function(){
    consulta = { anio: _anio, marca: _marca };
    _restOpenGov.search({ dataset: 'autos-'+_anio, query: '' }, _orderResults);
  };

  var _orderResults = function(res) {
    var field = (_marca == '') ? 'TOTAL' : _marca;
    res = res.sort(function(a, b) {
      return b._source[field]-a._source[field]
    });  
    _processResults(res);
  };

  var _processResults = function(res) {

    var procesado = {
      label: [],
      values: []
    };

    var obj;
    $.each(res, function(i,e) {

      if (procesado.label.length == 0) {
        procesado.label = (_marca == '') ? _getMarcasNames(e._source) : [_marca];
      }

      if (e._source.PROVINCIA!='TOTAL') {
        obj = {  
          'label': _getShortProvName(e._source.PROVINCIA),
          'values': _getMarcasValues(e._source)
        };
        procesado.values.push(obj);
      };
    });

    _resultado = procesado;
    _dispatchEvent();

  };

  var _getMarcasValues = function(marcas){
    var resp = [];
    if(_marca==''){
      $.each(marcas,function(i,e) {
        if (i != 'PROVINCIA' && i != 'TOTAL') {
          resp.push(e);
        }
      });
    } else {
      resp.push(marcas[_marca]);
    }
    return resp;
  };

  var _getMarcasNames = function(marcas){
    var resp = [];
    $.each(marcas,function(i,e){
      if (i != 'PROVINCIA' && i != 'TOTAL') {
        resp.push(i);
      }
    });
    return resp;
  };

  var _getShortProvName = function(prov){
    switch (prov) {
      case'TIERRA DEL FUEGO':
        prov = 'T. DEL FUEGO';
        break;
      case'BUENOS AIRES':
        prov = 'B. AIRES';
        break;
      case'SANTIAGO DEL ESTERO':
        prov = 'SGO. DEL ESTERO';
        break;
      case'CAPITAL FEDERAL':
        prov = 'CAP. FED.';
        break;
    }
    return prov;
  };

  var _dispatchEvent = function() {
    var event = jQuery.Event("retrieveInfoComplete");
    event.results = _resultado;
    $(_instance).trigger(event);
  };

  return {
    constructor: Data,
    retrieveInfo: function(anio, marca) {
      _reset();
      _marca = marca;
      _anio = anio;
      _instance = this;
      _queryElastic();
    }

  };

})();
