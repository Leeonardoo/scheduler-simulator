package io.github.leeonardoo.so.scheduler

enum class Algorithm(val label: String) {
    FIFO(label = "FIFO (First-in-First-out"), // Não Preemptivo
    SJF(label = "SJF (Shortest-Job-First"), // Não Preemptivo
    RoundRobin(label = "RR (Round Robin)"), // Preemptivo
    NonPreemptiveStaticPriority(label = "Prioridade (Não preemptivo, Estática)"), // Não Preemptivo
    PreemptiveStaticPriority(label = "Prioridade (Preemptivo, Estática)") // Preemptivo
}