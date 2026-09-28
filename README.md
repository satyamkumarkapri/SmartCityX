# 🏙️ SmartCityX – Integrated Smart-City Analytics Platform

SmartCityX is a production-quality, full-stack Spring Boot web application that serves as an interactive platform for visualizing, learning, and applying advanced Data Structures and Algorithms (DSA) to real-world smart city infrastructure problems.

This project was built to satisfy the academic requirements of a university DSA curriculum across 6 comprehensive modules, combining theoretical computer science with a modern web interface.

![SmartCityX Dashboard Overview](./src/main/resources/static/images/dashboard.png) *(Preview placeholder)*

## 🚀 Tech Stack

- **Backend**: Java 21, Spring Boot 3.2, Spring MVC, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, HTML5, Vanilla CSS3 (Custom Dark/Glassmorphism Theme), Chart.js
- **Database**: PostgreSQL (Production), H2 (Testing)
- **Deployment**: Docker, Docker Compose, Maven

---

## 🧩 Academic Modules & Algorithms Implemented

The application includes an **Analytics Workbench** that allows interactive execution of the following algorithms:

### M1 – String Algorithms
- **Knuth-Morris-Pratt (KMP)**: Citizen report keyword matching.
- **Z-Function**: Duplicate incident detection.
- **Rabin-Karp**: Infrastructure ID verification.
- **Aho-Corasick**: Multi-keyword emergency scanning.

### M2 – Suffix Structures
- **Suffix Array & Kasai LCP**: Fast substring search in city documents.
- **Suffix Automaton (SAM)**: Minimal DFA representation of all document substrings.

### M3 – Advanced Dynamic Programming
- **Levenshtein / Damerau-Levenshtein Distance**: Fuzzy search and typo correction for user queries.
- **Subset Sum**: Resource capacity combination finding.
- **Matrix Chain Multiplication**: Query optimization sequencing.
- **Bitmask DP (Set Cover)**: Optimal combination of city resources to cover required tasks.

### M4 – Network Flow
- **Ford-Fulkerson / Edmonds-Karp / Dinic's Algorithm**: City water and power grid flow optimization.
- **Min-Cut**: Identifying critical bottlenecks in infrastructure.
- **Bipartite Matching**: Optimally assigning available city resources (ambulances, tankers) to active emergencies.

### M5 – NP-Completeness & Approximations
- **Vertex Cover (2-Approximation)**: Identifying minimum intersections to place traffic cameras.
- **Max Clique**: Finding maximum clusters of mutually connected secure city nodes.
- **Independent Set**: Distributing autonomous monitoring stations without overlap.
- **Reductions**: Theoretical mapping of 3-SAT to CLIQUE to IS to VC.

### M6 – Randomized & Parallel Algorithms
- **Randomized QuickSort**: Sorting service priority queues.
- **Reservoir Sampling**: Taking representative uniform samples from infinite IoT sensor data streams.
- **Miller-Rabin Primality Test**: Generating large primes for secure city data encryption.
- **Blelloch Scan & Parallel Reduce**: O(log n) parallel aggregation of city-wide sensor telemetry.
- **Brent's Theorem**: Theoretical calculator bounding parallel execution time based on processors.

---

## 🛠️ Setup & Run Instructions

### Option 1: Docker (Recommended)
You can run the entire stack (PostgreSQL + Spring Boot app) using Docker Compose.
```bash
# Start the application and database
docker-compose up --build -d

# The app will be available at http://localhost:8080
# Default login: admin / admin
```

### Option 2: Local Development (Maven)
If you want to run it directly via your IDE or terminal:

1. **Start PostgreSQL**: Make sure you have a database named `smartcityx` running on `localhost:5432` with username `postgres` and password `postgres`.
2. **Compile and Run**:
```bash
./mvnw spring-boot:run
```
*(Note: If `mvnw` is not available, ensure you have Maven installed and run `mvn spring-boot:run`)*

---

## 📚 Viva / Defense Questions

Here are common questions an examiner might ask during a project defense, along with answers based on this implementation:

**Q: How did you implement the algorithms—are they tightly coupled to your framework?**
> A: No, they are strictly isolated. All algorithms are implemented as static, pure utility methods in the `com.smartcityx.algorithm` package. They take standard Java data structures as input and return custom `Result` DTOs. The `AlgorithmService` class acts as the bridge, calling these utilities and saving the run history to the database.

**Q: Why use Dinic's algorithm over Edmonds-Karp for Network Flow?**
> A: While Edmonds-Karp runs in $O(V \cdot E^2)$ using BFS to find augmenting paths, Dinic's algorithm is faster in practice, running in $O(V^2 \cdot E)$. It achieves this by constructing a Level Graph using BFS, and then sending blocking flows using DFS, significantly reducing redundant path searches.

**Q: Explain how your Bipartite Matching algorithm works.**
> A: It uses the maximum flow approach (or augmenting paths). We create a bipartite graph where left nodes are Resources (e.g., Ambulances) and right nodes are Requirements (e.g., Emergencies). We add a super-source connected to all left nodes, and a super-sink connected to all right nodes. By running max-flow (Edmonds-Karp), the flow capacity reveals the maximum optimal 1-to-1 matching.

**Q: What is a Suffix Automaton (SAM) and how does it compare to a Suffix Array?**
> A: A SAM is a Directed Acyclic Graph (DAG) that acts as the minimal deterministic finite automaton accepting all suffixes of a string. It is built online in $O(n)$ time. Suffix Arrays take $O(n \log n)$ to build. SAM is faster for checking if a substring exists, while Suffix Arrays (combined with LCP) are better for lexicographical sorting and frequency counting.

**Q: In NP-Completeness, why do we use a 2-Approximation for Vertex Cover?**
> A: Finding the absolute minimum Vertex Cover is NP-Hard. However, by greedily picking an edge, adding both its endpoints to the cover, and discarding all edges connected to them, we guarantee finding a cover that is at most exactly twice the size of the optimal cover. It runs extremely fast in $O(E)$ time.

**Q: How does Bitmask DP work in your Set Cover example?**
> A: We have a small number of tasks (e.g., $N \le 15$). A bitmask integer represents which tasks are completed (e.g., `1101` in binary means tasks 0, 2, and 3 are covered). The DP array stores the minimum cost to reach each bitmask state. We iterate over all masks and try adding each available resource, updating the target mask cost.

**Q: Explain the difference between Levenshtein and Damerau-Levenshtein distance.**
> A: Levenshtein calculates the minimum number of insertions, deletions, and substitutions to change string A to string B. Damerau-Levenshtein includes a fourth operation: the transposition of two adjacent characters. This is highly useful in typo correction because swapping adjacent keys (like typing "hte" instead of "the") is a common human error.

## 📄 Architecture Diagram

```
User (Browser) <--> Thymeleaf Templates (HTML/CSS)
                        |
              Spring MVC Controllers
                        |
     +------------------+------------------+
     |                                     |
Services (Dashboard, Auth)        AlgorithmService
     |                                     |
Spring Data JPA                   [Pure DSA Utility Classes]
     |                               (M1 - M6 Packages)
PostgreSQL Database
```
