#include <cstdint>
#include <exception>
#include <iostream>
#include <cstdlib>
#include <cmath>

int main() {
    int MiB = std::pow(2, 20);
    size_t t_mem = 0;
    while(true) {
        try {
            int64_t* ptr = new int64_t[MiB / sizeof(int64_t)]; //Allocate 1MiB
            t_mem += MiB;
            std::cout << "mem allocated: " << t_mem << "\n";
        }
        catch(std::exception exception) {
            std::cerr << exception.what() << std::endl;
            std::cin.get();
        }
    }
}
