{
  "from" : 0,
  "size" : 21,
  "query" : {
    "filtered" : {
      "query" : {
        "multi_match" : {
          "query" : "图片",
          "fields" : [ "subject^1.2", "content^1.0" ],
          "prefix_length" : 2
        }
      },
      "filter" : {
        "bool" : {
          "should" : [ {
            "term" : {
              "forumId" : 1796
            }
          }, {
            "term" : {
              "forumId" : 1820
            }
          }, {
            "term" : {
              "forumId" : 1892
            }
          } ]
        }
      }
    }
  },
  "highlight" : {
    "fragment_size" : 60,
    "number_of_fragments" : 8,
    "fields" : {
      "subject" : { },
      "content" : { }
    }
  }
}
