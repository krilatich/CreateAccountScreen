package com.example.test

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test.ui.theme.TestTheme
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen() {

    val mainViewModel: MainViewModel = viewModel()
    val uiState by mainViewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        Text(
            stringResource(R.string.create_a_new_account),
            style = MaterialTheme.typography.headlineMedium
        )
        Card {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(value = uiState.name,
                    onValueChange = { mainViewModel.updateUserName(it) },
                    label = {
                        Text(stringResource(R.string.name))
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }),
                    modifier = Modifier.fillMaxWidth())
                if (uiState.name.isEmpty()) Text(
                    text = stringResource(R.string.empty_name),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )

                Column {
                    OutlinedTextField(value = uiState.currentBalance,
                        onValueChange = { mainViewModel.updateCurrentBalance(it) },
                        label = {
                            Text(stringResource(R.string.current_balance))
                        },
                        placeholder = {
                            Text(stringResource(R.string._0_00))
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier.fillMaxWidth())
                    Text(
                        stringResource(R.string.balance_hint),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (uiState.currentBalance.isEmpty()) Text(
                        text = stringResource(R.string.empty_balance),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                DatePickerField(date = uiState.dateOfCurrentBalance,
                    label = stringResource(R.string.date_of_current_balance),
                    onDateChanged = { year, month, day ->
                        when {
                            day < 10 && month < 10 -> mainViewModel.updateCurrentDate("0$day.0$month.$year")

                            day < 10 -> mainViewModel.updateCurrentDate("0$day.$month.$year")

                            month < 10 -> mainViewModel.updateCurrentDate("$day.0$month.$year")

                            else -> mainViewModel.updateCurrentDate("$day.$month.$year")
                        }
                    })
                if (uiState.dateOfCurrentBalance.isEmpty()) Text(
                    text = stringResource(R.string.choose_date),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )

                AccountTypeDropDownMenu(
                    expanded = uiState.accountTypeMenuExpanded,
                    onExpandedChange = { mainViewModel.updateAccountTypeExpanded(it) },
                    onDismissRequest = { mainViewModel.updateAccountTypeExpanded(isExpanded = false) },
                    onSelectType = { mainViewModel.updateAccountType(type = it) },
                    selectedItem = uiState.selectedAccountType
                )
                if (uiState.selectedAccountType == "Select an Account Type") Text(
                    text = stringResource(R.string.select_account_type),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )

                Column(
                    Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row {
                        RadioButton(selected = uiState.selectedBudget == BudgetType.BudgetAccount,
                            onClick = { mainViewModel.updateBudgetType(budgetType = BudgetType.BudgetAccount) })
                        Text(stringResource(R.string.budget_account_button))
                    }
                    Row {
                        RadioButton(selected = uiState.selectedBudget == BudgetType.OffBudget,
                            onClick = { mainViewModel.updateBudgetType(budgetType = BudgetType.OffBudget) })
                        Text(stringResource(R.string.off_budget_button))
                    }
                }
                if (uiState.selectedBudget == null) Text(
                    text = stringResource(R.string.select_budget_affect),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {/*TODO*/ }) {
            Text(stringResource(R.string.create_account))
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountTypeDropDownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
    onSelectType: (String) -> Unit,
    selectedItem: String,
) {
    val listItems = arrayOf("Account Type 1", "Account Type 2", "Account Type 3", "Account Type 4")

    ExposedDropdownMenuBox(
        expanded = expanded, onExpandedChange = onExpandedChange
    ) {
        TextField(value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.type)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = onDismissRequest
        ) {
            listItems.forEach { selectedOption ->
                DropdownMenuItem(
                    onClick = { onSelectType(selectedOption) },
                ) {
                    Text(selectedOption)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    date: String, label: String, onDateChanged: (Int, Int, Int) -> Unit
) {
    val mContext = LocalContext.current
    val mCalendar = Calendar.getInstance()
    val mYear = mCalendar.get(Calendar.YEAR)
    val mMonth = mCalendar.get(Calendar.MONTH)
    val mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    val mDatePickerDialog = DatePickerDialog(
        mContext, { _: DatePicker, mYearOfLife: Int, mMonthOfYear: Int, mDayOfMonth: Int ->
            onDateChanged(mYearOfLife, mMonthOfYear + 1, mDayOfMonth)
        }, mYear, mMonth, mDay
    )

    Box {
        OutlinedTextField(readOnly = true,
            value = date,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.DateRange, contentDescription = "dateIcon"
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = { mDatePickerDialog.show() }),
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CreateAccountPreview() {
    TestTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
        ) {
            CreateAccountScreen()
        }
    }
}