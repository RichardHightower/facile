./autobench.sh --clients xen.caucho.com:4600,lancre.caucho.com:4600 --uri1 /file_0k.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html --host2 ch_nginx --port2 80 --num_conn 80 --num_call 1 --low_rate 10 --high_rate 100 --rate_step 10 --timeout 3 --file test0K.tsv


