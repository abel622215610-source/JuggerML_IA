package com.example.juggerml.matchs

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

/**
 * Requiere: org.jetbrains.kotlinx:kotlinx-coroutines-play-services
 * Estructura: matches/{matchId} -> { teamA, teamB, status, createdAt }
 */
class MatchRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val matches get() = db.collection("matches")

    /** Crea el partido y devuelve el id del documento generado. */
    suspend fun createMatch(teamA: Team, teamB: Team): Result<String> = try {
        val ref = matches.document()               // id autogenerado
        ref.set(Match(teamA = teamA, teamB = teamB)).await()
        Result.success(ref.id)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
