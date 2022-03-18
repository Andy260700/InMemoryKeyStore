#In Memory key store for storing key value pairs, retreivable using command line arguments.

A key-value store using socket programming. The server implements the key-value store and
clients make use of it. The server accepts clients’ connections and serve their requests for ‘get’ and
‘put’ key value pairs. All key-value pairs are stored by the server only in memory. Keys and values
are strings.
The client accepts a variable no of command line arguments where the first argument is the server
hostname followed by port no. It is followed by any sequence of “get <key>” and/or “put <key>
<value>”.
./client 192.168.124.5 5555 put city Kolkata put country India get country get city get Institute
India
Kolkata
<blank>
The server will be running on a designated port no. The server supports multiple clients and
maintain their key-value stores separately.

Implemented authorization so that only a few clients having the role “manager” can access other’s key-
value stores. A user is assigned the “guest” role by default. The server can upgrade a “guest” user to a
“manager” user.


