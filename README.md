# Dictionary Server

This is the server-side application for a networked dictionary system. It handles multiple client connections concurrently, allowing users to query, add, remove, or update words and their meanings via socket communication.

## Features

- Multi-threaded: each client is handled in its own thread (thread-per-connection)
- Shared, synchronized dictionary data structure (`ConcurrentHashMap`)
- Persistent storage using a JSON file
- Supports commands:
  - `querymeanings`
  - `addword`
  - `removeword`
  - `addmeaning`
  - `updatemeaning`

## How to Run

1. Open the project in IntelliJ or any IDE.
2. Make sure the `dictionaryJSON` file exists in the project root or is created on the first run.
3. Run the `Server.java` file.
