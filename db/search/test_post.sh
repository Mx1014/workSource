{
  "from" : 0,
  "size" : 21,
  "query" : {
    "filtered" : {
      "query" : {
        "multi_match" : {
          "query" : "物业",
          "fields" : [ "subject^1.2", "content^1.0" ],
          "prefix_length" : 2
        }
      },
      "filter" : {
        "bool" : {
          "should" : [ {
            "term" : {
              "forumId" : 1
            }
          }, {
            "term" : {
              "forumId" : 1497
            }
          }, {
            "term" : {
              "forumId" : 1503
            }
          }, {
            "term" : {
              "forumId" : 1506
            }
          }, {
            "term" : {
              "forumId" : 1527
            }
          }, {
            "term" : {
              "forumId" : 1612
            }
          }, {
            "term" : {
              "forumId" : 1616
            }
          }, {
            "term" : {
              "forumId" : 1617
            }
          }, {
            "term" : {
              "forumId" : 1618
            }
          }, {
            "term" : {
              "forumId" : 1624
            }
          }, {
            "term" : {
              "forumId" : 1627
            }
          }, {
            "term" : {
              "forumId" : 1630
            }
          }, {
            "term" : {
              "forumId" : 1631
            }
          }, {
            "term" : {
              "forumId" : 1674
            }
          }, {
            "term" : {
              "forumId" : 1675
            }
          }, {
            "term" : {
              "forumId" : 1676
            }
          }, {
            "term" : {
              "forumId" : 1677
            }
          }, {
            "term" : {
              "forumId" : 1678
            }
          }, {
            "term" : {
              "forumId" : 1679
            }
          }, {
            "term" : {
              "forumId" : 1680
            }
          }, {
            "term" : {
              "forumId" : 1681
            }
          }, {
            "term" : {
              "forumId" : 1683
            }
          }, {
            "term" : {
              "forumId" : 1684
            }
          }, {
            "term" : {
              "forumId" : 1685
            }
          }, {
            "term" : {
              "forumId" : 1687
            }
          }, {
            "term" : {
              "forumId" : 1690
            }
          }, {
            "term" : {
              "forumId" : 1691
            }
          }, {
            "term" : {
              "forumId" : 1692
            }
          }, {
            "term" : {
              "forumId" : 1694
            }
          }, {
            "term" : {
              "forumId" : 1695
            }
          }, {
            "term" : {
              "forumId" : 1697
            }
          }, {
            "term" : {
              "forumId" : 1698
            }
          }, {
            "term" : {
              "forumId" : 1699
            }
          }, {
            "term" : {
              "forumId" : 1700
            }
          }, {
            "term" : {
              "forumId" : 1701
            }
          }, {
            "term" : {
              "forumId" : 1702
            }
          }, {
            "term" : {
              "forumId" : 1703
            }
          }, {
            "term" : {
              "forumId" : 1704
            }
          }, {
            "term" : {
              "forumId" : 1705
            }
          }, {
            "term" : {
              "forumId" : 1707
            }
          }, {
            "term" : {
              "forumId" : 1710
            }
          }, {
            "term" : {
              "forumId" : 1713
            }
          }, {
            "term" : {
              "forumId" : 1757
            }
          }, {
            "term" : {
              "forumId" : 1771
            }
          }, {
            "term" : {
              "forumId" : 1779
            }
          }, {
            "term" : {
              "forumId" : 1876
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
org.elasticsearch.client.transport.TransportClient@4ca3af7b
org.elasticsearch.action.search.SearchRequest@1a41f8b5
