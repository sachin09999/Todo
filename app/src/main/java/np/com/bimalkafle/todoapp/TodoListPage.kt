package np.com.bimalkafle.todoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TodoListPage(viewModel: TodoViewModel) {

    val todoList by viewModel.todoList.observeAsState()
    var inputText by remember {
        mutableStateOf("")
    }
    val openAlertDialog = remember { mutableStateOf(false) }

    Box() {
        todoList?.let {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 200.dp),
                content = {
                    itemsIndexed(it) { index: Int, item: Todo ->
                        TodoItem(item = item, onDelete = {
                            viewModel.deleteTodo(item.id)
                        })
                    }
                }
            )
        } ?: Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "No items yet",
            fontSize = 16.sp
        )

        FloatingActionButton(
            onClick = {
                openAlertDialog.value = true // Open the dialog
            },
            modifier = Modifier
                .padding(bottom = 32.dp, end = 20.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
    }

    // Call addDialog conditionally based on openAlertDialog state
    if (openAlertDialog.value) {
        openDialog(
            onDismissRequest = { openAlertDialog.value = false }, // Close dialog on dismiss
            onConfirmation = { // Handle confirmation logic here (e.g., add note)
                // Add your logic to add a note using viewModel or inputText
                inputText = "" // Reset input after confirmation
                openAlertDialog.value = false // Close dialog after confirmation
            },
            viewModel
        )
    }
}

@Composable
fun openDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: TodoViewModel
) {
    val todoList by viewModel.todoList.observeAsState()
    var inputText by remember {
        mutableStateOf("")
    }
    AlertDialog(
        title = {
            Text("Add Notes here")
        },
        onDismissRequest = { onDismissRequest() },
        text = {
                Box(modifier = Modifier.fillMaxSize()
                    .background(Color.Transparent)
                ){
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = inputText,
                        onValueChange = { inputText = it },
                        shape = RoundedCornerShape(9.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
        },
        confirmButton = {
            Button(onClick = {
                viewModel.addTodo(inputText)
                inputText = ""
                onConfirmation()
            },) {
                Text(text = "Save")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(500.dp)
            .background(Color.Transparent)

    )
}

@Composable
fun TodoItem(item : Todo,onDelete : ()-> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = SimpleDateFormat("HH:mm:aa, dd/mm", Locale.ENGLISH).format(item.createdAt),
                fontSize = 12.sp,
                color = Color.LightGray
            )
            Text(
                text = item.title,
                fontSize = 20.sp,
                color = Color.White
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_delete_24),
                contentDescription = "Delete",
                tint = Color.White
            )
        }
    }
}

















