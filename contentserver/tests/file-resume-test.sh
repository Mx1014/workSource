curl --header "Range: bytes=0-10000" http://127.0.0.1:5000/image/466c99428a8063556112d3179c3c5a45?token=xx -o part1
curl --header "Range: bytes=10001-" http://127.0.0.1:5000/image/466c99428a8063556112d3179c3c5a45?token=xx -o part2
http://127.0.0.1:5000/image/466c99428a8063556112d3179c3c5a45?token=xx
