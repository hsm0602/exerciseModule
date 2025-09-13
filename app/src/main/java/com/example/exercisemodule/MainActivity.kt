package com.example.exercisemodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    private val exercises = listOf("스쿼트", "푸쉬업", "풀업", "숄더 프레스", "레그 레이즈")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()

                NavHost(navController = nav, startDestination = "select") {

                    // 운동 선택 화면
                    composable("select") {
                        ExerciseSelectionScreen(
                            exercises = exercises,
                            onSelect = { name ->
                                // 라우트 파라미터로 전달
                                nav.navigate("pose?name=${name}")
                            }
                        )
                    }

                    // 카메라 + 포즈 카운팅 화면
                    composable(
                        route = "pose?name={name}&reps={reps}",
                        arguments = listOf(
                            navArgument("name") { type = NavType.StringType; defaultValue = "스쿼트" },
                        )
                    ) { backStackEntry ->
                        val exerciseName = backStackEntry.arguments?.getString("name") ?: "스쿼트"

                        PoseCameraScreen(
                            exerciseName = exerciseName,
                            onBackToSelection = {
                                // 선택 화면으로 복귀
                                nav.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseSelectionScreen(
    exercises: List<String>,
    onSelect: (name: String) -> Unit
) {

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("운동 선택 (테스트)") })
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "테스트할 운동을 고르세요\n",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            exercises.forEach { name ->
                Button(
                    onClick = { onSelect(name) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(name)
                }
            }
        }
    }
}
