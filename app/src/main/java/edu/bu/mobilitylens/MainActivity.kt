package edu.bu.mobilitylens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.bu.mobilitylens.ui.theme.MobilityLensTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilityLensTheme {
                MobilityLensScreen()
            }
        }
    }
}

private data class MobilityDimension(
    val name: String,
    val constraint: String,
    val implication: String,
)

private val mobilityDimensions = listOf(
    MobilityDimension(
        name = "Input and interaction",
        constraint = "People use a phone with touch, short attention, and often one hand while moving.",
        implication = "Keep common actions within thumb reach and make touch targets easy to hit without precise tapping.",
    ),
    MobilityDimension(
        name = "Screen size, orientation, and density",
        constraint = "A phone has limited space and may switch between portrait and landscape on displays with different pixel densities.",
        implication = "Prioritize the essential information, use responsive layouts, and avoid relying on a fixed pixel size.",
    ),
    MobilityDimension(
        name = "Lifecycle and resource constraints",
        constraint = "The system can pause, stop, or recreate an app when memory, battery, or another activity needs attention.",
        implication = "Avoid expensive work on the main thread and preserve important work so the user can resume smoothly.",
    ),
    MobilityDimension(
        name = "Context awareness",
        constraint = "A mobile device can know about location, motion, time, connectivity, and nearby conditions.",
        implication = "Use context only when it genuinely improves the task, and offer a useful experience when that context is unavailable.",
    ),
    MobilityDimension(
        name = "Usage patterns",
        constraint = "Mobile sessions are usually brief, interrupted, and focused on completing one immediate task.",
        implication = "Make the next step obvious, save progress early, and avoid forcing users through long forms in one sitting.",
    ),
    MobilityDimension(
        name = "Security and privacy expectations",
        constraint = "Phones hold sensitive personal data, and users expect apps to ask for only the access they can understand and trust.",
        implication = "Request the smallest possible amount of data and permissions, explain the benefit, and protect stored information.",
    ),
)

@Composable
fun MobilityLensScreen() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var designName by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf<String?>(null) }
    val selectedDimension = mobilityDimensions[selectedIndex]
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(text = stringResource(R.string.app_title), style = MaterialTheme.typography.headlineMedium)
        Text(text = stringResource(R.string.app_introduction), style = MaterialTheme.typography.bodyLarge)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = stringResource(
                        R.string.dimension_counter,
                        selectedIndex + 1,
                        mobilityDimensions.size,
                    ),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(text = selectedDimension.name, style = MaterialTheme.typography.titleLarge)
                Text(text = selectedDimension.constraint, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = "Developer implication: ${selectedDimension.implication}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                onClick = {
                    selectedIndex = (selectedIndex - 1 + mobilityDimensions.size) % mobilityDimensions.size
                    feedback = null
                },
            ) {
                Text(stringResource(R.string.previous))
            }
            Button(
                onClick = {
                    selectedIndex = (selectedIndex + 1) % mobilityDimensions.size
                    feedback = null
                },
            ) {
                Text(stringResource(R.string.next))
            }
        }

        OutlinedTextField(
            value = designName,
            onValueChange = {
                designName = it
                feedback = null
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.design_prompt)) },
            placeholder = { Text(stringResource(R.string.design_hint)) },
            singleLine = true,
        )

        Button(
            onClick = {
                feedback = if (designName.isBlank()) {
                    context.getString(R.string.blank_input_message)
                } else {
                    context.getString(R.string.success_message, designName.trim(), selectedDimension.name)
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.consider_button))
        }

        feedback?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 800)
@Composable
private fun MobilityLensScreenPreview() {
    MobilityLensTheme {
        MobilityLensScreen()
    }
}
