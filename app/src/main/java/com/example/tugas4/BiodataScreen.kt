package com.example.tugas4

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen() {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf("") }
    var selectedSkills by rememberSaveable { mutableStateOf(listOf<Int>()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }


    val skillOptions = listOf(R.string.skill_android, R.string.skill_uiux, R.string.skill_web)
    val experienceOptions = stringArrayResource(R.array.experience_options)
    val dateFormat = stringResource(R.string.date_format)

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && birthDate.isNotBlank() &&
            experience.isNotBlank() && selectedSkills.isNotEmpty()

    val fieldShape = RoundedCornerShape(dimensionResource(R.dimen.field_corner))
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = colorResource(R.color.purple_title),
        unfocusedBorderColor = colorResource(R.color.gray_border),
        disabledBorderColor = colorResource(R.color.gray_border)
    )

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            // Gambar header
            Image(
                painter = painterResource(R.drawable.wallpaper),
                contentDescription = stringResource(R.string.cd_background),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.header_height))
            )
            // Box putih yang mengapung di atas Scaffold
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = dimensionResource(R.dimen.card_top_offset))
                    .clip(
                        RoundedCornerShape(
                            topStart = dimensionResource(R.dimen.card_corner),
                            topEnd = dimensionResource(R.dimen.card_corner)
                        )
                    )
                    .background(colorResource(R.color.white))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(dimensionResource(R.dimen.screen_padding)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.form_title),
                        color = colorResource(R.color.purple_title),
                        fontSize = dimensionResource(R.dimen.title_size).value.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.field_spacing) / 2))
                    Text(
                        text = stringResource(R.string.form_subtitle),
                        color = colorResource(R.color.gray_subtitle),
                        fontSize = dimensionResource(R.dimen.subtitle_size).value.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.section_spacing)))

                    // Nama Depan & Belakang
                    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_spacing))) {
                        OutlinedTextField(
                            value = firstName, onValueChange = { firstName = it },
                            placeholder = { Text(stringResource(R.string.hint_first_name)) },
                            singleLine = true, shape = fieldShape, colors = fieldColors,
                            modifier = Modifier.weight(1f).heightIn(min = dimensionResource(R.dimen.field_height))
                        )
                        OutlinedTextField(
                            value = lastName, onValueChange = { lastName = it },
                            placeholder = { Text(stringResource(R.string.hint_last_name)) },
                            singleLine = true, shape = fieldShape, colors = fieldColors,
                            modifier = Modifier.weight(1f).heightIn(min = dimensionResource(R.dimen.field_height))
                        )
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.field_spacing)))

                    // Email
                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        placeholder = { Text(stringResource(R.string.hint_email)) },
                        singleLine = true, shape = fieldShape, colors = fieldColors,
                        modifier = Modifier.fillMaxWidth().heightIn(min = dimensionResource(R.dimen.field_height))
                    )
                    Spacer(Modifier.height(dimensionResource(R.dimen.field_spacing)))

                    // Tgl Lahir & Experience
                    Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_spacing))) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = birthDate, onValueChange = {}, readOnly = true,
                                placeholder = { Text(stringResource(R.string.hint_birthdate)) },
                                trailingIcon = {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_calendar),
                                        contentDescription = stringResource(R.string.cd_calendar)
                                    )
                                },
                                singleLine = true, shape = fieldShape, colors = fieldColors,
                                modifier = Modifier.fillMaxWidth().heightIn(min = dimensionResource(R.dimen.field_height))
                            )
                            // Layer transparan agar seluruh field bisa diklik
                            Box(Modifier.matchParentSize().clickable { showDatePicker = true })
                        }

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = experience, onValueChange = {}, readOnly = true,
                                placeholder = { Text(stringResource(R.string.hint_experience)) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                singleLine = true, shape = fieldShape, colors = fieldColors,
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                                    .heightIn(min = dimensionResource(R.dimen.field_height))
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                experienceOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = { experience = option; expanded = false }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(dimensionResource(R.dimen.section_spacing)))

                    // Skill
                    Text(
                        text = stringResource(R.string.label_skill),
                        color = colorResource(R.color.black_label),
                        fontSize = dimensionResource(R.dimen.label_size).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        skillOptions.forEach { skillRes ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = skillRes in selectedSkills,
                                    onCheckedChange = { checked ->
                                        selectedSkills = if (checked) selectedSkills + skillRes
                                        else selectedSkills - skillRes
                                    }
                                )
                                Text(
                                    text = stringResource(skillRes),
                                    fontSize = dimensionResource(R.dimen.skill_text_size).value.sp
                                )
                            }
                        }
                    }









