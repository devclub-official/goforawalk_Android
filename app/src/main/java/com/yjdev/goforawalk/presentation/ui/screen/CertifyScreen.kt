package com.yjdev.goforawalk.presentation.ui.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.yjdev.goforawalk.domain.model.PostResult
import com.yjdev.goforawalk.presentation.ui.theme.GrayD3D3D3
import com.yjdev.goforawalk.presentation.ui.theme.GrayE0E0E0
import com.yjdev.goforawalk.presentation.ui.theme.GrayF5F5F5
import com.yjdev.goforawalk.presentation.ui.theme.Green8AA76D
import com.yjdev.goforawalk.presentation.viewmodel.MainViewModel

@Composable
fun CertifyScreen(viewModel: MainViewModel, onFinish: () -> Unit) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var text by remember { mutableStateOf("") }
    val context = LocalContext.current
    val postResult by viewModel.postFootstepResult.collectAsState()
    val maxLength = 50

    LaunchedEffect(postResult) {
        when (postResult) {
            is PostResult.Success -> {
                Toast.makeText(context, "업로드 완료", Toast.LENGTH_SHORT).show()
                viewModel.resetPostFootstepResult()
                viewModel.fetchList()
                onFinish()
            }

            is PostResult.Failure -> {
                val errorMessage = (postResult as PostResult.Failure).errorMsg
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetPostFootstepResult()
                viewModel.fetchList()
            }

            null -> {}
        }
    }

    if (imageUri == null) {
        CameraScreen(
            onImageCaptured = { uri -> imageUri = uri },
            onError = { exception ->
                Log.e("CameraScreen", "Image capture failed: ${exception.message}")
            }
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                text = "발자취",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(GrayF5F5F5)
                    .border(1.dp, GrayE0E0E0)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { newText ->
                    if (newText.length <= maxLength) {
                        text = newText
                    }
                },
                placeholder = { Text("(선택) 오늘의 한 마디를 남겨주세요 :)", color = Color.Gray) },
                trailingIcon = {
                    Text(
                        "${text.length}/$maxLength",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray,
                    disabledBorderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        imageUri?.let { uri ->
                            val compressedFile = compressAndResizeImage(context, uri)
                            viewModel.postFootstep(
                                imageFile = compressedFile,
                                contentText = text
                            )
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green8AA76D,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("남기기 😽", style = MaterialTheme.typography.bodyMedium)
                }

                Button(
                    onClick = { onFinish() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GrayD3D3D3,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("취소", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}