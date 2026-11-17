package ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ui.Data.MatchRules
import ui.Data.Team
import ui.Data.repository.MatchRepository

enum class TeamSide { A, B }

data class TeamForm(
    val name: String = "",
    val starters: List<String> = List(MatchRules.MAX_STARTERS) { "" },
    val substitutes: List<String> = List(MatchRules.MAX_SUBSTITUTES) { "" }
) {
    fun toTeam() = Team(
        name = name.trim(),
        starters = starters.map { it.trim() }.filter { it.isNotEmpty() },
        substitutes = substitutes.map { it.trim() }.filter { it.isNotEmpty() }
    )
}

data class MatchsUiState(
    val teamA: TeamForm = TeamForm(),
    val teamB: TeamForm = TeamForm(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val createdMatchId: String? = null
)

class MatchsViewModel(
    private val repository: MatchRepository = MatchRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchsUiState())
    val uiState: StateFlow<MatchsUiState> = _uiState.asStateFlow()

    fun onTeamNameChange(side: TeamSide, value: String) =
        updateTeam(side) { it.copy(name = value) }

    fun onStarterChange(side: TeamSide, index: Int, value: String) =
        updateTeam(side) { form ->
            form.copy(starters = form.starters.toMutableList().also { it[index] = value })
        }

    fun onSubstituteChange(side: TeamSide, index: Int, value: String) =
        updateTeam(side) { form ->
            form.copy(substitutes = form.substitutes.toMutableList().also { it[index] = value })
        }

    private fun updateTeam(side: TeamSide, transform: (TeamForm) -> TeamForm) {
        _uiState.update {
            when (side) {
                TeamSide.A -> it.copy(teamA = transform(it.teamA))
                TeamSide.B -> it.copy(teamB = transform(it.teamB))
            }
        }
    }

    fun createMatch() {
        val state = _uiState.value
        if (state.isSaving) return

        val teamA = state.teamA.toTeam()
        val teamB = state.teamB.toTeam()

        validate(teamA, teamB)?.let { error ->
            _uiState.update { it.copy(errorMessage = error) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            repository.createMatch(teamA, teamB)
                .onSuccess { id ->
                    _uiState.update { MatchsUiState(createdMatchId = id) } // limpia el formulario
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isSaving = false, errorMessage = e.message ?: "Error al crear el partido")
                    }
                }
        }
    }

    private fun validate(a: Team, b: Team): String? {
        if (a.name.isEmpty() || b.name.isEmpty()) return "Ambos equipos necesitan un nombre"
        if (a.name.equals(b.name, ignoreCase = true)) return "Los equipos deben tener nombres distintos"
        for (team in listOf(a, b)) {
            if (team.starters.size < MatchRules.MIN_STARTERS)
                return "${team.name}: mínimo ${MatchRules.MIN_STARTERS} titular(es)"
            val all = (team.starters + team.substitutes).map { it.lowercase() }
            if (all.size != all.distinct().size) return "${team.name}: hay jugadores repetidos"
        }
        return null
    }

    fun consumeMessage() {
        _uiState.update { it.copy(errorMessage = null, createdMatchId = null) }
    }
}
