package org.facile;

public class TodoAndExperiments {

	//TODO # Move primitive filter, map and enumerate into separate module.
	//TODO # Create map primitive versions
	//TODO # Create reduce primitive versions
	//TODO # create a shift function like Perl's, look at Python pop, push. It should work with arrays and lists.
	//TODO # Add an ARGS holder for the main method. See if this is already a system property. 
	//TODO # Extract useful System properties into a class called System, which is system done right.
	//TODO # create len, _int, _float, _double, functions (rejected scalar, but scalar could be used for length of array, collection, and conversion of wrapper to primitive)
	//TODO # create len, _int, _float, _double, converters
	//TODO # the name enumerate kind of sucks... Perl, Groovy use forEach, Ruby uses each. I like enumerate the least. Change enumerate to each. It is shorter and more descriptive
	//TODO # Groovy uses find and findAll, we use filter like Python. We should create a find and findAll too.
	//TODO # Ability to extract properties from lists... i.e., salary of each employee. extract(depts, {dept.getEmployee()}).extract({emp.getSalary}); (where blocks are functions)
	//TODO # Safe get safeGet({employee.getAddress().getPhone()});

	
	
	//DONE ITEMS
	//DONE # Perl treats running a process like opening a file where the output of the process is a stream. I like this. 
	//DONE # What if you create a file context like Perl's <IN>. This context could work with regex too like $1 (group 1 in split), $_ (line), @_ (array), next (regex), and unless (regex)
	
	//IDEA: File parsing might be a good first application of Facile, use it to parse files.

}
