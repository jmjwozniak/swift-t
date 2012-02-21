
# generated by parser
# date: 2012/01/27 19:05:01

package require turbine 0.0.1
namespace import turbine::*

if { [ info exists env(TURBINE_TEST_PARAM_1) ] } {
    set N $env(TURBINE_TEST_PARAM_1)
} else {
    set N 7
}

proc cmpf:fib { stack u:o u:n } {
    turbine::c::log function:cmpf:fib
    set __rule_id [ turbine::c::rule_new ]
    turbine::c::rule ${__rule_id} if-0 "${u:n}" "" "tl: if-0 ${stack} ${__rule_id} ${u:n} ${u:n} ${u:o}"
}

proc if-0 { stack __rule_id u:n u:n u:o } {
    set __valueofn [ turbine::get_integer ${u:n} ]
    if { ${__valueofn} } {
        set l:0 [ adlb::unique __l0 ]
        turbine::integer_init ${l:0}
        set l:1 [ adlb::unique __l1 ]
        turbine::integer_init ${l:1}
        set_integer ${l:1} 1
        turbine::minus ${stack} [ list ${l:0} ] [ list ${u:n} ${l:1} ]
        set __rule_id [ turbine::c::rule_new ]
        turbine::c::rule ${__rule_id} if-1 "${l:0}" "" "tl: if-1 ${stack} ${__rule_id} ${l:0} ${u:n} ${u:o}"
    } else {
        # set_integer ${u:o} 0
	turbine::set0 no_stack ${u:o}
    }
}

proc if-1 { stack __rule_id l:0 u:n u:o } {
    set __valueof__l0 [ turbine::get_integer ${l:0} ]
    if { ${__valueof__l0} } {
        set l:2 [ adlb::unique __l2 ]
        turbine::integer_init ${l:2}
        set l:3 [ adlb::unique __l3 ]
        turbine::integer_init ${l:3}
        set l:4 [ adlb::unique __l4 ]
        turbine::integer_init ${l:4}
        set_integer ${l:4} 1
        turbine::minus ${stack} [ list ${l:3} ] [ list ${u:n} ${l:4} ]
        set __rule_id [ turbine::c::rule_new ]
        turbine::c::rule ${__rule_id} fib [ list ${l:3} ] [ list ${l:2} ] "tl: cmpf:fib ${stack} ${l:2} ${l:3}"
        set l:5 [ adlb::unique __l5 ]
        turbine::integer_init ${l:5}
        set l:6 [ adlb::unique __l6 ]
        turbine::integer_init ${l:6}
        set l:7 [ adlb::unique __l7 ]
        turbine::integer_init ${l:7}
        set_integer ${l:7} 2
        turbine::minus ${stack} [ list ${l:6} ] [ list ${u:n} ${l:7} ]
        set __rule_id [ turbine::c::rule_new ]
        turbine::c::rule ${__rule_id} fib [ list ${l:6} ] [ list ${l:5} ] "tl: cmpf:fib ${stack} ${l:5} ${l:6}"
        turbine::plus ${stack} [ list ${u:o} ] [ list ${l:2} ${l:5} ]
    } else {
        # set_integer ${u:o} 1
	turbine::set1 no_stack ${u:o}
    }
}

proc __swiftmain {  } {
    global N
    puts "N: $N"
    turbine::c::log function:__swiftmain
    set stack 0
    set l:0 [ adlb::unique __l0 ]
    turbine::integer_init ${l:0}
    set l:1 [ adlb::unique __l1 ]
    turbine::integer_init ${l:1}
    set_integer ${l:1} $N
    set __rule_id [ turbine::c::rule_new ]
    turbine::c::rule ${__rule_id} fib [ list ${l:1} ] [ list ${l:0} ] "tl: cmpf:fib ${stack} ${l:0} ${l:1}"
    turbine::trace ${stack} [ list ] [ list ${l:0} ]
}

turbine::defaults
turbine::init $engines $servers
turbine::start __swiftmain
turbine::finalize

