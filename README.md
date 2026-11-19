# Software-Fundamentals-Assignment

Student name: Thow Khay Wei
Student ID: SCSJ2500275
Course: Software Fundamentals (BIT1244)

## Project Description
A Java console application that manages pantry items and stock requests.
Requests are processed in first-in, first-out order. A request is fulfilled
only if the full quantity is available; otherwise it is rejected and stock
is unchanged.

## Features
- Register and find items
- Register requests (arrival number assigned by the program)
- Process pending requests in FIFO order
- Display request statuses and history
- List low-stock items at a chosen threshold
- Input validation and safe exit

## How to Run
1. Use JDK 17 or later.
2. Open the project in Eclipse and mark `src` and `test` as source folders.
3. Run the main class `pantry.PantryApp`.
4. To run the tests, run `pantry.PantryTests`.
5. Menu option 8 loads the assignment dataset (see test-data/sample-dataset.md).

## Notes
Data is kept in memory only (Version 1). Save/load is planned for the project.
