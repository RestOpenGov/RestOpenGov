
var RestOpenGov = function(args) {
	
	var defaultArgs = { 
		entryPointURL: "http://zenithsistemas.com:9200/",
		dataSource: "gcba"
	};

	args = $.extend(defaultArgs, args); 

	var url = args.entryPointURL + args.dataSource;

	this.search = function(params, callback) {
	    
	    var defaultParams = { 
            dataset: 'metadata',
            query: '*:*',
    		limit: 100,
    		from: 0
    	};

    	params = $.extend(defaultParams, params);

		if(params.query.length == 0) {
			params.query = "*:*";
		}

        $.getJSON(url + "/" + params.dataset + "/_search", { q: params.query, from: params.from, size: params.limit }, function(obj) {
            var result = obj.hits.hits;

            for(var i in result) {
                result[i].resourceURL = url + "/" + params.dataset + "/" + result[i]._id;
            }

            callback(result);
        });

	};

};

jQuery.extend({
   postJSON: function( url, data, callback) {
      return jQuery.post(url, data, callback, "json");
   }
});
