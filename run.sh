
echo "$0 rate number of connections $1, start rate $2, end rate $3, step $4, file /file_$5.html, output out_$5.tsv"

#./autobench.sh --clients xen:4600,lancre:4601 --uri1 /file_$5.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html --host2 ch_nginx --port2 80 --num_conn $1 --num_call 10 --low_rate $2 --high_rate $3 --rate_step $4 --timeout 3 --file master_slave_out_con$1_start$2_end$3_step$4_$5.tsv --verbose yes --process-timeout 60 --short-form yes --master yes --remote yes

#./autobench.sh --clients xen:4600,lancre:4601 --uri1 /file_$5.html --host1 ch_resin --port1 8080 --uri2 /file_0k.html --host2 ch_resin --port2 8080 --num_conn $1 --num_call 10 --low_rate $2 --high_rate $3 --rate_step $4 --timeout 3 --file master_slave_out_con$1_start$2_end$3_step$4_$5.tsv --verbose yes --process-timeout 60 --short-form yes --master yes --remote yes

./autobench.sh --clients xen:4600,lancre:4601 --uri1 /file_$5.html --host1 ch_resin --port1 80 --uri2 /file_0k.html --host2 ch_resin --port2 80 --num_conn $1 --num_call 10 --low_rate $2 --high_rate $3 --rate_step $4 --timeout 3 --file master_slave_out_con$1_start$2_end$3_step$4_$5.tsv --verbose yes --process-timeout 60 --short-form yes --master yes --remote yes

