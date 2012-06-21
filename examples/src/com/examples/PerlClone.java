package com.examples;

import java.io.IOException;

import static org.facile.ContextParser.*;
import static org.facile.ContextParser.Input.*;
import static org.facile.Facile.*;
import static com.examples.Results.*;

public class PerlClone {
	
	
	public static void main(String[] args) throws IOException {

		my $results = my(Results.class, all);

		ctx();
		open(IN, "./src/com/examples/sample.txt");

		while (IN()) {
			if (ok("^\\#.*|^\\s+$|^\\s+\\#|^$"))
				continue;

			if (ok("^Total: .*replies (\\d+)")) {
				$results.i(replies, $1());
			}
			if (ok("^Connection rate: (\\d+\\.\\d)")) {
				$results.i(conn_rate, $1());
			}
			if (ok("^Request rate: (\\d+\\.\\d)")) {
				$results.i(req_rate, $1());
			}
			if (ok("^Reply rate .*min (\\d+\\.\\d) avg (\\d+\\.\\d) max (\\d+\\.\\d) stddev (\\d+\\.\\d)")) {
				$results.i(rep_rate_min, $1());
				$results.i(rep_rate_avg, $2());
				$results.i(rep_rate_max, $3());
				$results.i(rep_rate_stdv, $4());
			}
			if (ok("^Reply time .* response (\\d+\\.\\d)")) {
				$results.i(rep_time, $1());
			}
			 if (ok("^Reply status.*1xx=(\\d+) 2xx=(\\d+) 3xx=(\\d+) 4xx=(\\d+) 5xx=(\\d+)")) {
				 $results.i(status_100, $1());
				 $results.i(status_200, $2());
				 $results.i(status_300, $3());
				 $results.i(status_400, $4());
				 $results.i(status_500, $5());
			 }

			print($_());
		}

		cleanCtx();

		print($results);

	}

}
