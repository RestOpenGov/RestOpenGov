
var opengov = new RestOpenGov();
var currentFrom = 0;
var rows = [];

$(function() {

	$('#form').submit(function(e) {
		e.preventDefault();

		$('#submit').val("Buscando...").attr("disabled", "disabled");

		opengov.search({ dataset: $("#dataset").val(), from: currentFrom, query: $("#query").val() }, buildGrid);
	});

});

var buildGrid = function(obj) {
    currentFrom = obj.length;
    $('#submit').val("Buscar").removeAttr("disabled");
    $('#results').html('<table id="grid" />');
    $('#grid').flexigrid({ height: obj.length * 40, dataType: 'json', colModel: getColumns(obj) }).flexAddData(getData(obj)).flexReload();
};

var getColumns = function(obj) {
    var list = [];
    for(var key in obj[0]._source) {
        list.push({ display: key, name: key, align: 'center', width: 200 });
    }
    return list;
};

var getData = function(obj) {

    for(var i in obj) {

        var row = {};

        for(var j in obj[i]._source) {
            row[j] = '';

            if(typeof obj[i]._source[j] != 'object') {
                row[j] = obj[i]._source[j].replace('\<p\>', '').replace('\<\/p\>', '');
            }
        }

        rows.push({ cell: row });
    }

    return {
        total: obj.length,
        page: 1,
        rows: rows
    };
}

var showResults = function(obj) {
	$('#submit').val("Buscar").removeAttr("disabled");
	var html = "<h3>Resultados:</h3>";
	for(var i in obj) {
		html += "<h4><a target=\"_blank\" href=\"" + obj[i].resourceURL + "\">Resultado #" + (parseInt(i) + 1) + "</a></h4>";
		html += "<table class=\"table\">";
		for(var key in obj[i]._source) {
			html += "<tr><th>" + key + "</th><td>";
			if(typeof obj[i]._source[key] == "object") {
				for(var j in obj[i]._source[key]) {
					if(typeof obj[i]._source[key][j] == "object") {
						html += "<table class=\"table\">";
						for(k in obj[i]._source[key][j]) {	
							html += "<tr><th>" + k + "</th><td>" + obj[i]._source[key][j][k] + "</td></tr>";
						}
						html += "</table>";
					} else {
						html += obj[i]._source[key][j] + ", ";	
					}
				}
			} else {
				html += obj[i]._source[key];	
			}
			html += "</td></tr>";
		}
		html += "</table>";
	}
	$('#results').html(html);
};

