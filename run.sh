echo "$0 rate number of connections $1, start rate $2, end rate $3, step $4, file /file_$5.html, output out_$5.tsv"

./autobench.sh --clients xen.caucho.com:4600,lancre.caucho.com:4600 --uri1 /file_$5.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html --host2 ch_nginx --port2 80 --num_conn $1 --num_call 10 --low_rate $2 --high_rate $3 --rate_step \
$4 --timeout 3 --file master_slave_out_con$1_start$2_end$3_step$4_$5.tsv --verbose yes --process-timeout 60 --master yes --short-form yes

