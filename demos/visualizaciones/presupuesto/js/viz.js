function Viz() {
}

Viz.prototype = (function() {

  var _barChart = undefined;

    var _renderBarChart = function(json) {

      //if(!_barChart){
      var w = parseInt($('.container').width() - 50);

      $('#infovis').html('');
      //responsive width
      $('#infovis').css('width',w+"px");

      _barChart = new $jit.BarChart({
        //id of the visualization container
        injectInto: 'infovis',
        //whether to add animations
        animate: true,
        //horizontal or vertical barcharts
        orientation: 'horizontal',
        //bars separation
        barsOffset: 10,
        //visualization offset
        Margin: {
          top: 5,
          left: 5,
          right: 5,
          bottom: 5
        },
        //labels offset position
        labelOffset: 20,
        //bars style
        type: useGradients ? 'stacked:gradient' : 'stacked',
        //whether to show the aggregation of the values
        showAggregates: false,
        //whether to show the labels for the bars
        showLabels: true,
        //label styles
        Label: {
          type: labelType, //Native or HTML  
          size: 10,
          family: 'Arial',
          color: 'black'
        },
        //tooltip options
        Tips: {
          enable: true,
          onShow: function(tip, elem) {
            tip.innerHTML = "<b>" + elem.name + "</b>: " + elem.value;
          }
        }
      });
      //load JSON data.
      _barChart.loadJSON(json);

      /*}else{
        _barChart.updateJSON(json);
      }*/
    };

    return {

      constructor: Viz,

      render: function(json) {

        _renderBarChart(json);

        var legendContainer = $('#legendContainer');
        var legend = _barChart.getLegend(),listItems = [];
        for(var name in legend) {
          listItems.push('<div class=\'query-color\' style=\'background-color:'
            + legend[name] +'\'>&nbsp;</div>' + name);
        }
        legendContainer.html('<li>' + listItems.join('</li><li>') + '</li>');

        var event = jQuery.Event("renderComplete");
        $(this).trigger(event);
      }

    };
})();
