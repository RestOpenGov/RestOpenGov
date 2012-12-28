
var PresupuestoViz = function(options) {

  this.conn = new PresupuestoData(options)

  this.retrieveByAnio = function(anio) {
    this.conn.search({'anio':anio,'graph':'ring'}, function(data){
      this._dispatchEvent(data, 'retrieveByAnioComplete');
    }, this);
  }

  this._dispatchEvent = function(data, event) {
    var e = jQuery.Event(event);
    e.results = data;
    $(this).trigger(e);
  };

};