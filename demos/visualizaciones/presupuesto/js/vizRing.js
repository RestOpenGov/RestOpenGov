function Viz() {
}

Viz.prototype = (function() {

  var _ringChart = undefined;

    var _renderRingChart = function(json) {

      //if(!_barChart){
      var w = parseInt($('.container').width() - 50);

      $('#infovis-ring').html('');
      //responsive width
      $('#infovis-ring').css('width',w+"px");

      _ringChart =  new $jit.Sunburst({
          //id container for the visualization
          injectInto: 'infovis-ring',
          //Distance between levels
          levelDistance: 90,
          //Change node and edge styles such as
          //color, width and dimensions.
          Node: {
            overridable: true,
            type: useGradients? 'gradient-multipie' : 'multipie'
          },
          interpolation:'polar',
          //Select canvas labels
          //'HTML', 'SVG' and 'Native' are possible options
          Label: {
            type: labelType
          },
          //Change styles when hovering and clicking nodes
          NodeStyles: {
            enable: true,
            type: 'Native',
            stylesClick: {
              'color': '#33dddd'
            },
            stylesHover: {
              'color': '#dd3333'
            }
          },
          //Add tooltips
          Tips: {
            enable: true,
            onShow: function(tip, node) {
              var html = "<div class='tip-custom'><div class=\"tip-title\">" + node.name + "</div>"; 
              var data = node.data;

              if("size" in data) {
                html += "<b>Presupuesto:</b> $ " + parseFloat(data.size).toFixed(2);
              }
              html += '</div>';
              tip.innerHTML = html;
            }
          },
          //implement event handlers
          Events: {
            enable: true,
            onClick: function(node) {
              if(!node) return;
              //Build detailed information about the file/folder
              //and place it in the right column.
              var html = "<h4>" 

              if("$color" in node.styles) {
                html +='<div class=\'query-color\' style=\'background-color:'
                + node.styles['$color'] +'\'>&nbsp;</div>';
              }

              html+= node.name + "</h4>", data = node.data;

              if("size" in data) {
                html += "<b>Presupuesto:</b> $ " + parseFloat(data.size).toFixed(2);
              }

              $jit.id('inner-details').innerHTML = html;
              //hide tip
              _ringChart.tips.hide();
              //rotate
              _ringChart.rotate(node, animate? 'animate' : 'replot', {
                duration: 1000,
                transition: $jit.Trans.Quart.easeInOut
              });
            }
          },
          // Only used when Label type is 'HTML' or 'SVG'
          // Add text to the labels. 
          // This method is only triggered on label creation
          onCreateLabel: function(domElement, node){
            var labels = _ringChart.config.Label.type,
                aw = node.getData('angularWidth');
            if (labels === 'HTML' && (node._depth < 2 || aw > 2000)) {
              domElement.innerHTML = node.name;
            } else if (labels === 'SVG' && (node._depth < 2 || aw > 2000)) {
              domElement.firstChild.appendChild(document.createTextNode(node.name));
            }
          },
          // Only used when Label type is 'HTML' or 'SVG'
          // Change node styles when labels are placed
          // or moved.
          onPlaceLabel: function(domElement, node){
            var labels = _ringChart.config.Label.type;
            if (labels === 'SVG') {
              var fch = domElement.firstChild;
              var style = fch.style;
              style.display = '';
              style.cursor = 'pointer';
              style.fontSize = "0.8em";
              fch.setAttribute('fill', "#fff");
            } else if (labels === 'HTML') {
              var style = domElement.style;
              style.display = '';
              style.cursor = 'pointer';
              style.fontSize = "0.8em";
              style.color = "#ddd";
              var left = parseInt(style.left);
              var w = domElement.offsetWidth;
              style.left = (left - w / 2) + 'px';
            }
          }
     });
    //load JSON data.
    _ringChart.loadJSON(json);
    //compute positions and plot.
    _ringChart.refresh();
    //end

    };

    return {

      constructor: Viz,

      render: function(json) {

        _renderRingChart(json);

        var event = jQuery.Event("renderComplete");
        $(this).trigger(event);
      }

    };
})();
