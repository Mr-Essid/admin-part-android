
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ErrorAlert(title: String, body: String, onDismiss_: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss_,
        confirmButton = { println("ok") },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Clear,
                contentDescription = "Your Have Error",
                tint = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        },
        dismissButton = { Text(text = "got it", Modifier.clickable { onDismiss_() }) },
        containerColor = MaterialTheme.colorScheme.errorContainer
    )
}


@Preview
@Composable
fun ShowAlert() {
    WarningAlert(
        title = "REQUEST SEND SUCCESSFULLY",
        body = "your request send to admin you have to wait until admin approve your request, you'll get email",
        {}
    ) {
        print("ok")
    }
}


@Composable
fun SuccessAlert(title: String, body: String, onDismiss_: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss_,
        confirmButton = { println("ok") },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Your Have Error",
                tint = Color.Green
            )
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        },
        dismissButton = { Text(text = "got it", Modifier.clickable { onDismiss_() }) },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    )
}


@Composable
fun WarningAlert(
    title: String,
    body: String,
    onDismiss_: () -> Unit,
    confirmationCallback: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss_,
        confirmButton = {
            TextButton(onClick = confirmationCallback) {
                Text(text = "confirm")
            }
        },
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        },

        icon = {
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = "Your Have Error",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        },
        text = {
            Text(
                text = body,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        },
        dismissButton = { TextButton(onClick = {onDismiss_()}){
            Text(text = "cancel")
        } },
        containerColor = MaterialTheme.colorScheme.errorContainer
    )
}