package com.example.spektar.ui.questionnaireScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.spektar.R
import com.example.spektar.ui.common.components.BottomBar
import com.example.spektar.ui.stringToEnumParser
import kotlinx.coroutines.flow.StateFlow

/*
q1:
0-12 kid
13-18 teen
18+ adult

my q2 would be asmr's q4 (sorts out general categories, general tags that a user likes)
q3 could ask "Do you like anime?" and if yes, what type
q4 would ask "Do you like games?", if yes what type
 */

@Composable
fun QuestionnaireScreen(
    onEvent: (QuestionnaireEvent) -> Unit,
) {
    // for question 1's checkboxes:
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val options = listOf(stringResource(R.string.teen),
        stringResource(R.string.adult), stringResource(R.string.anonymous_age)
    )

    Scaffold(
        contentWindowInsets = WindowInsets(left = 8.dp)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Spacer(modifier = Modifier.padding(top = 36.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.age_question), // 3 check boxes, first  2teen/adult, 3rd not comfortable saying
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                options.forEachIndexed { index, text ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = selectedIndex == index,
                            onCheckedChange = {
                                selectedIndex = index
                                val updateInd = if(index == 2) {
                                    null
                                } else {
                                    index + 1
                                }

                                if(updateInd != null) {
                                    onEvent(QuestionnaireEvent.UpdateEmbedding(updateInd, "Set"))
                                }
                            }
                        )
                        Text(text)
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 12.dp))
            }

            item {
                Text(
                    text = stringResource(R.string.general_tags_question), // q2, general tags
                    style = MaterialTheme.typography.headlineSmall
                )

                TagDropdownSelector(
                    allTags = listOf(
                        "Action", "Fantasy", "Sci-Fi", "Comedy", "Thriller", "Crime", "Philosophy",
                        "Psychology", "Drama", "Horror", "Romance", "Indie", "Sports", "Animated",
                        "Historical", "War", "Documentary", "Mystery", "Musical", "Biography", "Family", "Adventure"
                    ),
                    event = { index, action ->
                        onEvent(QuestionnaireEvent.UpdateEmbedding(index, action))
                    }
                )

                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Text(
                    stringResource(R.string.anime_prompt),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                TagDropdownSelector(
                    allTags = listOf(
                        "Shonen", "Shojo", "Seinen", "Josei", "Isekai", "Slice of Life", "Mecha", "Sports"
                    ),
                    event = { index, action ->
                        onEvent(QuestionnaireEvent.UpdateEmbedding(index, action))
                    }
                )

                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Text(
                    stringResource(R.string.video_game_question),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                TagDropdownSelector(
                    allTags = listOf(
                        "Multiplayer", "RPG", "FPS", "Open world", "Real Time Strategy", "Story driven",
                        "Simulation", "Turn-based", "Roguelike", "Survival", "Sandbox", "MMO"
                    ),

                    event = { index, action ->
                        onEvent(QuestionnaireEvent.UpdateEmbedding(index, action))
                    }
                )

                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // also move to home screen
                            onEvent(QuestionnaireEvent.FinishQuestionnaire)
                        }
                    ) {
                        Text(
                            stringResource(R.string.finish_questionnaire)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TagDropdownSelector(
    allTags : List<String>,
    event: (Int, String) -> Unit
) {
    val tagTranslations = mapOf(
        "Action" to stringResource(R.string.action),
        "Fantasy" to stringResource(R.string.fantasy),
        "Sci-Fi" to stringResource(R.string.sciFi),
        "Comedy" to stringResource(R.string.comedy),
        "Thriller" to stringResource(R.string.thriller),
        "Crime" to stringResource(R.string.crime),
        "Philosophy" to stringResource(R.string.philosophy),
        "Psychology" to stringResource(R.string.psychology),
        "Drama" to stringResource(R.string.drama),
        "Horror" to stringResource(R.string.horror),
        "Romance" to stringResource(R.string.romance),
        "Indie" to stringResource(R.string.indie),
        "Sports" to stringResource(R.string.sports),
        "Animated" to stringResource(R.string.animated),
        "Historical" to stringResource(R.string.historical),
        "War" to stringResource(R.string.war),
        "Documentary" to stringResource(R.string.documentary),
        "Mystery" to stringResource(R.string.mystery),
        "Musical" to stringResource(R.string.musical),
        "Biography" to stringResource(R.string.biography),
        "Family" to stringResource(R.string.family),
        "Adventure" to stringResource(R.string.adventure),
        "Slice of Life" to stringResource(R.string.slice_of_life),

        // these either don't have a good direct translation or are simply too uncommon to be translated
        "Multiplayer" to "Multiplayer",
        "RPG" to "RPG",
        "FPS" to "FPS",
        "Sandbox" to "Sandbox",
        "Roguelike" to "Roguelike",
        "MMO" to "MMO",

        "Open world" to stringResource(R.string.open_world),
        "Real Time Strategy" to stringResource(R.string.real_time_strategy),
        "Story driven" to stringResource(R.string.story_driven),
        "Simulation" to stringResource(R.string.simulation),
        "Turn-based" to stringResource(R.string.turn_based),
        "Survival" to stringResource(R.string.survival),
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = "", // we’ll render chips instead of text
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.select_tags)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            // instead of showing text, we render selected chips inside
            placeholder = {
                if (selectedTags.isEmpty()) {
                    Text(stringResource(R.string.no_tags_selected))
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        selectedTags.forEach { tag ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(tagTranslations[tag] ?: tag)
                                    Spacer(Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove $tag",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                selectedTags = selectedTags - tag
                                                event(stringToEnumParser(tag), "Remove")
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            },

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.outline,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                allTags.forEach { tag ->
                    FilterChip(
                        selected = tag in selectedTags,
                        onClick = {
                            selectedTags = selectedTags + tag
                            event(stringToEnumParser(tag), "Add")
                        },
                        label = { Text(tagTranslations[tag] ?: tag) }
                    )
                }
            }
        }
    }
}