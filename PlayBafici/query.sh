curl -X post 'http://elastic.restopengov.org/gcba/bafici/_search?pretty=1' -d '
{
  "query": { 
    "custom_score": { 
      "script" : "random()*20",
      "query" : {
        "query_string": { 
          "query" : "_id:bafici12-films-*"
        }
      }
    }
  },
  "sort": {
    "_score": { 
      "order":"desc"
    }
  }
}

'
