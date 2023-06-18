package io.github.leeonardoo.so.scheduler

enum class Algorithm(val label: String) {
    FIFO(label = "FIFO (First-in-First-out"), // Não Preemptivo
    SJF(label = "SJF (Shortest-Job-First"), // Não Preemptivo
    RoundRobin(label = "RR (Round Robin)"), // Preemptivo
    Priority(label = "Priority") // Preemptivo
}