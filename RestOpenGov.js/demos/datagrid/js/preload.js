
var opengov = new RestOpenGov();

$(function() {

	opengov.search({ dataset: 'bafici', query: '', limit: 100 }, function(obj) {
		var html = '';
		for(var i in obj) {
			if(typeof obj[i]._source.filepic1 != "undefined" && obj[i]._source.filepic1.length > 0) {
				html += '<span><img src="http://www.bafici.gov.ar/home11/photobase/films/' + obj[i]._source.filepic1 + '" /></span>';
			}
		}
		
		$('#container').html(html);
		$('#screen, #title').show();
		
		setInterval(function() { 
            $($('#container img').get(Math.floor(Math.random() * $('#container img').length)))
                .animate({ opacity: 1 }, { duration: 200 })
                .animate({ opacity: 0.7 }, { duration: 100 }); 
		}, 70);
		
	});

});
