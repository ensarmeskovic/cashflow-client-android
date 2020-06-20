package com.example.cashflow.views

import DigitsDecimalFilter
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.cashflow.AppConstants.EMPTY_STRING
import com.example.cashflow.AppConstants.ENTRY_AMOUNT_DECIMAL_NUMBERS
import com.example.cashflow.AppConstants.MAX_ENTRY_AMOUNT_CHARACTERS
import com.example.cashflow.R
import com.example.cashflow.api.models.AddEntry
import com.example.cashflow.callbacks.AddEntryCallback

class DialogAddEntry(
    activity: Activity,
    private val inboxId: Long,
    private val addEntryCallback: AddEntryCallback
) : Dialog(activity), View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private var isExpense = true

    private lateinit var entryNameField: EditText
    private lateinit var entryAmountField: EditText
    private lateinit var entryNoteField: EditText
    private lateinit var entrySubmitButton: Button
    private lateinit var entryCancelButton: Button
    private lateinit var entryRadioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_entry)

        entryNameField = findViewById(R.id.entry_name_edit_text)
        entryAmountField = findViewById(R.id.entry_amount_edit_text)
        entryNoteField = findViewById(R.id.entry_note_edit_text)
        entrySubmitButton = findViewById(R.id.insert_entry_button)
        entryCancelButton = findViewById(R.id.cancel_entry_button)
        entryRadioGroup = findViewById(R.id.entry_radio_group)

        entryAmountField.filters = arrayOf(DigitsDecimalFilter(ENTRY_AMOUNT_DECIMAL_NUMBERS), InputFilter.LengthFilter(MAX_ENTRY_AMOUNT_CHARACTERS))
        entryRadioGroup.setOnCheckedChangeListener(this)
        entrySubmitButton.setOnClickListener(this)
        entryCancelButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val context = v?.context ?: return
        when (v.id) {
            R.id.insert_entry_button -> {
                val entryName = entryNameField.text?.toString() ?: EMPTY_STRING
                val entryAmount = entryAmountField.text?.toString()?.toDoubleOrNull()
                if (entryAmount == null) {
                    Toast.makeText(context, context.getString(R.string.toast_entry_amount_value_invalid), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val entryNote = entryNoteField.text?.toString() ?: EMPTY_STRING
                if (entryNote.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.toast_entry_note_field_empty), Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                if (isExpense) {
                    if (entryName.isEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.toast_entry_name_field_empty),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }

                    val request =
                        AddEntry.ExpenseRequest(inboxId, entryName, entryAmount, entryNote)
                    addEntryCallback.onAddExpense(request)
                } else {
                    val request = AddEntry.PaymentRequest(inboxId, entryAmount, entryNote)
                    addEntryCallback.onAddPayment(request)
                }
                dismiss()
            }

            R.id.cancel_entry_button -> {
                dismiss()
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.payment_radio_button -> {
                entryNameField.visibility = View.GONE
                isExpense = false
            }
            R.id.expense_radio_button -> {
                entryNameField.visibility = View.VISIBLE
                isExpense = true
            }
        }
    }
}