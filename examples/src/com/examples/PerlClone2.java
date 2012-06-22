package com.examples;

import java.io.IOException;

import static org.facile.ContextParser.*;
import static org.facile.ContextParser.Input.*;
import static org.facile.Facile.*;
import static com.examples.Results.*;

public class PerlClone2 {

	public static void parseSampleText() {
		my $results = my(Results.class, all);

		open(IN, "./src/com/examples/sample.txt");

		while (IN()) {
			if (ok("r'{start line}#{any}{ OR }{start line}{whitespace}{one or more}{end line}{ OR }{start line}{whitespace}+#{ OR }{start line}{end line}'"))
				continue;

			if (ok("^Total: .*replies (\\d+)")) {
				$results.i(replies, $1());
			}
			if (ok("/{start line}Connection rate: ({digit}+/.{digit})/")) {
				$results.i(conn_rate, $1());
			}
			if (ok("^Request rate: (\\d+\\.\\d)")) {
				$results.i(req_rate, $1());
			}
			if (ok("/^Reply rate .*min (/d+/./d) avg (/d+/./d) max (/d+/./d) stddev (/d+/./d)/")) {
				$results.i(rep_rate_min, $1());
				$results.i(rep_rate_avg, $2());
				$results.i(rep_rate_max, $3());
				$results.i(rep_rate_stdv, $4());
			}
			if (ok("/^Reply time {any} response ({digit}+/.{digit})/")) {
				$results.i(rep_time, $1());
			}
			if (ok("/^Reply status{any}1xx=({digit}+) 2xx=({digit}+) 3xx=({digit}+) 4xx=({digit}+) 5xx=({digit}+)/")) {
				$results.i(status_100, $1());
				$results.i(status_200, $2());
				$results.i(status_300, $3());
				$results.i(status_400, $4());
				$results.i(status_500, $5());
			}
			
			if (ok("/{start line}Name:{whitespace}+({upper}{1}{alpha}{zero or more}) ({upper}{1}{alpha}{zero or more}){any}" +
					"Age: ({digit}+){any}" +
					"Street: ({any}) " +
					"Occupation: ({any}){end line}/")) {
				print( $1());
				print( $2());
				print( $3());
				print( $4());
				print( $5());
			}

			if (ok("/{start line}Name:{sp}({upper}{1}{alpha}{zero or more}){sp}({upper}{1}{alpha}{zero or more}){sp}" +
					"Age: ({digit}+){sp}" +
					"Street: ({any}){sp}" +
					"Occupation: ({any}){end line}/")) {
				print( $1());
				print( $2());
				print( $3());
				print( $4());
				print( $5());
			}
			
			
			if (ok("http://([^/?#]*)(.*)")) {
				print("here", $_());
				print("domain", $1(), "path", $2());
				print ("regex", "http://([^/?#]*)(.*)");
			}


			print($_());
		}

		print($results);

	}

	public static void main(String[] args) throws IOException {
		context(args, new Runnable() {
			@Override
			public void run() {
				parseSampleText();

			}
		});

	}

}
