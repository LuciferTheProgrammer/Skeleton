# ICSI404 - Computer Architecture and Organization

## Java Processor and Computer Architecture Simulator

This repository contains a Java-based processor and computer architecture simulation project. The project was created for a computer architecture/systems course and implements core low-level computing concepts, including bits, words, arithmetic operations, an ALU, assembler, memory, processor execution, instruction fetching, decoding, executing, storing, branching, stack-based call/return behavior, and cache simulation.

The project builds a simplified processor from the ground up using custom bit-level data structures instead of relying directly on Java's built-in arithmetic for the simulated machine operations.

## Features

- Custom `Bit` implementation
- 16-bit word representation
- 32-bit word representation
- Bitwise operations such as AND, OR, XOR, and NOT
- 32-bit addition and subtraction
- 32-bit multiplication
- Left shift and right shift operations
- ALU instruction execution
- Assembly instruction parsing
- Conversion from assembly instructions to binary-style instruction strings
- Main memory simulation
- Processor fetch, decode, execute, and store cycle
- Register-based instruction execution
- Branching and comparison support
- Call and return support using a stack
- Load and store instructions
- Instruction cache simulation
- L2 cache simulation
- Clock cycle tracking
- JUnit tests for major components

## Project Files

- `Bit.java` - represents a single bit as either true or false
- `Word16.java` - represents a 16-bit word
- `Word32.java` - represents a 32-bit word
- `TestConverter.java` - converts between Java integers and simulated 32-bit words
- `Adder.java` - performs 32-bit addition and subtraction
- `Multiplier.java` - performs 32-bit multiplication
- `Shifter.java` - performs left and right shift operations
- `ALU.java` - executes arithmetic, logical, shift, and comparison instructions
- `Assembler.java` - converts assembly-style instructions into simulated machine instruction strings
- `Memory.java` - simulates main memory
- `Processor.java` - simulates the processor execution cycle
- `InstructionCache.java` - simulates an instruction cache
- `L2Cache.java` - simulates an L2 cache with set-associative behavior
- `Main.java` - basic program entry point
- `Sum.java` - simple test program for summing values
- `CacheTests.java` - tests cache behavior using array and linked-list style programs

## Test Files

- `BitTest.java` - tests bit operations
- `Word16Test.java` - tests 16-bit word behavior
- `Word32Test.java` - tests 32-bit word behavior
- `AdderTest.java` - tests addition and subtraction
- `MultiplierTest.java` - tests multiplication
- `ShifterTest.java` - tests left and right shifts
- `ALUTest.java` - tests ALU operations
- `AssemblerTest.java` - tests assembler instruction conversion
- `AssemblerTest2.java` - tests final 32-bit instruction output formatting
- `MemoryTest.java` - tests memory read, write, and load behavior
- `ProcessorTest.java` - tests processor execution using sample programs
- `TestOr.java` - additional OR operation tests

## How It Works

The project begins with a custom `Bit` class, which represents a single binary value. The `Word16` and `Word32` classes build on top of this by storing arrays of bits and supporting operations such as copy, equality checking, bit access, AND, OR, XOR, and NOT.

Arithmetic is implemented manually at the bit level. The `Adder` class performs 32-bit addition using carry logic and performs subtraction using two's complement behavior. The `Multiplier` class performs multiplication by shifting and adding. The `Shifter` class performs left and right shifts on simulated 32-bit words.

The `ALU` class executes instructions based on opcodes. It supports operations such as add, subtract, multiply, AND, OR, left shift, right shift, and compare.

The `Assembler` converts assembly-style instructions into 16-bit instruction strings made of `t` and `f` characters. These 16-bit instructions are later combined into 32-bit instruction lines that can be loaded into memory.

The `Memory` class simulates main memory using an array of `Word32` values. Programs are loaded into memory as 32-bit instruction strings.

The `Processor` class runs the simulated program using a fetch, decode, execute, and store cycle. It maintains registers, a program counter, ALU inputs, instruction decoding fields, branch flags, and a call/return stack. The processor supports arithmetic instructions, memory load/store instructions, syscalls, branches, comparisons, calls, returns, and halt.

The cache system includes an `InstructionCache` and an `L2Cache`. The instruction cache stores blocks of instructions, while the L2 cache reads from main memory and supports both instruction and data access. The processor also tracks simulated clock cycles to compare performance.

## Supported Instruction Examples

The assembler supports instructions such as:

- `add r1 r2`
- `subtract 10 r4`
- `multiply r31 r0`
- `and r7 r13`
- `or -6 r9`
- `leftshift 2 r6`
- `rightshift 0 r2`
- `compare r18 r29`
- `copy 2 r6`
- `load r31 r0`
- `store 2 r6`
- `call 1010`
- `return`
- `halt`
- `beq 100`
- `bne 100`
- `blt 100`
- `ble -100`
- `bgt 100`
- `bge -100`
- `syscall 0`
- `syscall 1`

## Technologies Used

- Java
- JUnit
- Object-oriented programming
- Bit-level data structures
- Simulated machine instructions
- Processor simulation
- Memory simulation
- Cache simulation
- Assembly instruction parsing
- Arithmetic logic unit design
- Fetch/decode/execute/store cycle

## Purpose

This project was created for a computer architecture/systems course to practice building the major parts of a simplified processor in Java. It demonstrates how binary values, words, arithmetic circuits, an ALU, assembler, memory, processor execution, branching, and caching can work together inside a simulated computer system.

The project helped reinforce low-level computing concepts by implementing them manually through Java classes and unit tests.
