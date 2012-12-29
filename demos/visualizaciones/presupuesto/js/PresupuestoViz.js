
var PresupuestoViz = function(options) {

  this.conn = new PresupuestoData(options)

  this.retrieveByAnio = function() {
    this.conn.search({}, function(data){
      this._dispatchEvent(this._dataByAnio(data), 'retrieveByAnioComplete');
    }, this);
  }

  this._dataByAnio = function(presupuesto) {
    // los nombres de las cuentas
    var label = this.conn.getCuentas(presupuesto);
    var values = [];

    _.each(presupuesto.detalle, function(anioData, anioLabel) {
      var value = {
        label: anioLabel,
        values: anioData.valores
      }
      values.push(value);
    }, this);
    return {
      label: label,
      values: values
    }
  }

  this._dispatchEvent = function(data, event) {
    var e = jQuery.Event(event);
    e.results = data;
    $(this).trigger(e);
  };

};