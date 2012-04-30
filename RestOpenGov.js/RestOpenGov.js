
var RestOpenGov = function(args) {
	
	var defaultArgs = { 
		entryPointURL: "http://elastic.restopengov.org/",
		dataSource: "gcba"
	};

	args = $.extend(defaultArgs, args); 

	var url = args.entryPointURL + args.dataSource;

	this.search = function(dataset, query, callback) {

		if(query.length == 0) {
			query = "*:*";
		}

		$.getJSON(url + "/" + dataset + "/_search", { q: query }, function(obj) {
			var result = obj.hits.hits;

			for(i in result) {
				result[i].resourceURL = url + "/" + dataset + "/" + result[i]._id;
			}

			callback(result);
		});
	};

};
