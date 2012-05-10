
var opengov = new RestOpenGov();

$(document).ready(function() {

    if(typeof sessionStorage.getItem("imagesHtml") == 'undefined' || sessionStorage.getItem("imagesHtml") == null) {
        opengov.search({ dataset: 'bafici', query: '', limit: 100 }, function(obj) {
            var html = '';
            
            var x = 0, y = 0, counter=0;
            
            for(var i in obj) {
                if(typeof obj[i]._source.filepic1 != "undefined" && obj[i]._source.filepic1.length > 0) {
                	counter++;
                    html += '<div style="position:absolute;top:'+y+'px;left:'+x+'px"><img id="'+i+'" height="150" src="http://www.bafici.gov.ar/home11/photobase/films/' + obj[i]._source.filepic1 + '" /></div>';
                	x+=150;
	            	if(counter%10==0){
	   		            y+=150;
	   		            x=0;
	   		        }
                }

            }

            sessionStorage.setItem("imagesHtml", html);

            fx(html);
        });
    } else {
        fx(sessionStorage.getItem("imagesHtml"));
    }

});

function fx(html) {
    $('#container').html(html);
    $('#screen').show();
    $('#title').show();

    setInterval(function() {
        $($('#container img').get(Math.floor(Math.random() * $('#container img').length)))
            .animate({ opacity: 1 }, { duration: 200 })
            .animate({ opacity: 0.7 }, { duration: 100 });
    }, 70);
}