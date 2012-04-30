
var opengov = new RestOpenGov();

$(function() {

	$('#form').submit(function(e) {
		e.preventDefault();

		$('#submit').val("Buscando...").attr("disabled", "disabled");

		opengov.search($("#dataset").val(), $("#query").val(), showResults);
	});
	
});


var showResults = function(obj) {
	$('#submit').val("Buscar").removeAttr("disabled");
	var html = "<h3>Resultados:</h3>";
	for(i in obj) {
		html += "<h4><a target=\"_blank\" href=\"" + obj[i].resourceURL + "\">Resultado #" + (parseInt(i) + 1) + "</a></h4>";
		html += "<table class=\"table\">";
		for(key in obj[i]._source) {
			html += "<tr><th>" + key + "</th><td>";
			if(typeof obj[i]._source[key] == "object") {
				for(j in obj[i]._source[key]) {
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