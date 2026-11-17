package ui.Data // TODO: ajusta al package de tu proyecto


import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

object MatchRules {
    const val MAX_STARTERS = 5      // jugadores en pista
    const val MAX_SUBSTITUTES = 4   // reservas
    const val MIN_STARTERS = 1      // mínimo para poder crear el partido (ajústalo, p. ej. a 5)
}

/** Equipo tal y como se guarda en Firestore. */
data class Team(
    val name: String = "",
    val starters: List<String> = emptyList(),
    val substitutes: List<String> = emptyList()
)

/** Documento de la colección "matches". */
data class Match(
    @DocumentId val id: String = "",
    val teamA: Team = Team(),
    val teamB: Team = Team(),
    val status: String = "scheduled",
    @ServerTimestamp val createdAt: Timestamp? = null
)
