package com.flow.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.flow.example.theme.ComposeTheme
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.toList

class MainActivity : ComponentActivity() {
    private val listOfItems: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            simpleFlow().toList(listOfItems)
        }

        setContent {
            ComposeTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "navigate1") {
                    composable("navigate1") {
                        Navigate1(navController)
                    }
                    composable("navigate2") {
                        Navigate2(navController)
                    }
                }
            }
        }

        lifecycleScope.launch {
            simpleFlow().collect{ value ->
                Timber.d("Flow value:$value")
            }
        }
    }

    private fun simpleFlow(): kotlinx.coroutines.flow.Flow<String> = flow { // flow builder
        for (i in 1..10) {
            emit("Число:$i")
        }
    }

    @Composable
    fun Navigate1(navController: NavController, modifier: Modifier = Modifier) {
        Greeting(name = "test")

        Button(
            onClick = {
                navController.navigate("navigate2")
            },
            modifier = Modifier
                .padding(48.dp, 56.dp, 0.dp, 0.dp)
        ) {
            Text(text = "Navigate 2")
        }
    }

    @Composable
    fun Navigate2(navController: NavController, modifier: Modifier = Modifier) {
        Button(
            onClick = {
                navController.navigate("navigate1")
            },
            modifier = Modifier
                .padding(48.dp, 56.dp, 0.dp, 0.dp)
        ) {
            Text(text = "Navigate 1")
        }
        LazyColumn(
            modifier = Modifier
                .padding(0.dp, 108.dp, 0.dp, 0.dp)
        ) {
            items(items = listOfItems, itemContent = { item ->
                when(item){
                    is String -> {
                        val stringItem: String = item as String
                        StringItem(stringItem = stringItem)
                    }
                }
            })
        }
    }

    @Composable
    fun Greeting(name: String) {
        Text(text = "Hello\n", modifier = Modifier.padding(8.dp, 8.dp, 0.dp, 0.dp))
        Text(text = "$name!", modifier = Modifier.padding(8.dp, 26.dp, 0.dp, 0.dp))
    }

    @Composable
    fun StringItem(stringItem: String) {
        Text(text = stringItem)
        Divider(color = Color.Blue, thickness = 1.dp)
    }
}