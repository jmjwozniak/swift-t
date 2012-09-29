
#include <builtins.swift>
#include <assert.swift>

main {
    int M[][][];

    M[f(0)][f(1)][f(2)] = 2;
    M[1][f(1)][f(2)] = 2;
    M[1][f(1)][f(3)] = 4;
    M[f(1)][f(1)][f(4)] = 8;
    M[f(1)][2][0] = 10;

    if (f(1)) {
        M[f(0)][f(1)][f(1)] = 3;
        M[f(0)][1][f(3)] = 7;
        M[f(0)][f(2)][f(1)] = -1;
        M[f(0)][f(2)][2] = 8;
        M[f(0)][2][f(3)] = 9;

    } else {
        M[f(0)][f(1)][f(1)] = 4;

    }

    assertEqual(M[0][1][1],  3, "[0][1][1]");
    assertEqual(M[0][1][2],  2, "[0][1][2]");
    assertEqual(M[0][1][3],  7, "[0][1][3]");
    assertEqual(M[0][2][1],  -1, "[0][2][1]");
    assertEqual(M[0][2][2],  8, "[0][2][1]");
    assertEqual(M[0][2][3],  9, "[0][2][1]");

    assertEqual(M[1][1][2],  2, "[1][1][2]");
    assertEqual(M[1][1][3],  4, "[1][1][3]");
    assertEqual(M[1][1][4],  8, "[1][1][4]");
    assertEqual(M[1][2][0],  10, "[1][2][0]");
    structs();
}


(int r) f(int x) {
    r = x;
}


type T {
    float M[][][];
}


() structs () {
    T a;
    a.M[f(0)][f(1)][f(2)] = 7.0;
    a.M[f(0)][2][f(2)] = -7.0;
    a.M[f(0)][2][3] = -8.0;
    a.M[0][f(1)][f(3)] = 8.0;
    a.M[0][f(1)][4] = 9.0;
    a.M[1][1][1] = 10.0;

    assertEqual(a.M[0][1][2], 7.0, "[0][1][2]");
    assertEqual(a.M[0][1][3], 8.0, "[0][1][3]");
    assertEqual(a.M[0][1][4], 9.0, "[0][1][4]");
    assertEqual(a.M[0][2][2], -7.0, "[0][2][2]");
    assertEqual(a.M[0][2][3], -8.0, "[0][2][3]");
    assertEqual(a.M[1][1][1], 10.0, "[1][1][1]");
}
