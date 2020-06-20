package com.example.cashflow.fragments.entries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cashflow.AppConstants.HTTP_CODE_200
import com.example.cashflow.AppConstants.HTTP_CODE_500
import com.example.cashflow.R
import com.example.cashflow.api.models.AddEntry
import com.example.cashflow.api.models.InboxUser
import com.example.cashflow.callbacks.AddEntryCallback
import com.example.cashflow.callbacks.InboxUserManagerDialogCallback
import com.example.cashflow.data.Entry
import com.example.cashflow.data.Inbox
import com.example.cashflow.databinding.FragmentEntriesBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.views.DialogAddEntry
import com.example.cashflow.views.DialogInboxUserManager

class FragmentEntries : FragmentBase() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(EntriesViewModel::class.java)
    }

    private val adapter by lazy {
        EntriesAdapter()
    }

    private val inboxUserManagerCallback = object : InboxUserManagerDialogCallback {
        override fun onAddUser(email: String) {
            val request = InboxUser.Request(selectedInbox?.inboxId, email)
            viewModel.entriesRepository.addInboxUser(authToken, request)
            spinner.visibility = VISIBLE
        }

        override fun onRemoveUser(email: String) {
            val request = InboxUser.Request(selectedInbox?.inboxId, email)
            viewModel.entriesRepository.deleteInboxUser(authToken, request)
            spinner.visibility = VISIBLE
        }
    }

    private val addEntryCallback = object : AddEntryCallback {
        override fun onAddPayment(request: AddEntry.PaymentRequest) {
            spinner.visibility = VISIBLE
            viewModel.entriesRepository.addPayment(authToken, request)
        }

        override fun onAddExpense(request: AddEntry.ExpenseRequest) {
            spinner.visibility = VISIBLE
            viewModel.entriesRepository.addExpense(authToken, request)
        }
    }

    private val inboxUserManagerClickListener = View.OnClickListener {
        activity?.let {
            DialogInboxUserManager(it, inboxUserManagerCallback).show()
        }
    }

    private val addEntryClickListener = View.OnClickListener {
        activity?.let {
            DialogAddEntry(it, (selectedInbox?.inboxId ?: -1), addEntryCallback).show()
        }
    }

    private val itemTouchHelper = ItemTouchHelper(object :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
//            swipeRefreshLayout.isEnabled = false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val entry = adapter.getEntryByAdapterPosition(viewHolder.adapterPosition)
            val entryId = entry.first ?: return
            if (entry.second) {
                viewModel.entriesRepository.deletePayment(authToken, entryId)
            } else {
                viewModel.entriesRepository.deleteExpense(authToken, entryId)
            }
            spinner.visibility = VISIBLE
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
//            swipeRefreshLayout.isEnabled = true
        }
    })

    var selectedInbox: Inbox? = null
    private lateinit var dataBinding: FragmentEntriesBinding

    // views
    private lateinit var spinner: RelativeLayout
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var emptyEntriesListContainer: ConstraintLayout
    private lateinit var addEntryButton: ImageView
    private lateinit var menuButton: ImageView

    private lateinit var expenseSummary: TextView
    private lateinit var paymentSummary: TextView
    private lateinit var balanceSummary: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_entries, container, false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObservers()
        setUpBinding()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadUI()
    }

    private fun loadUI() {
        if (selectedInbox?.active == false) {
            addEntryButton.visibility = GONE
            menuButton.visibility = GONE

        }
        viewModel.entriesRepository.getEntries(authToken, (selectedInbox?.inboxId ?: -1))
    }

    private fun handleEntries(entry: Entry?) {
        spinner.visibility = GONE
        expenseSummary.text = entry?.expenses?.toString()
        paymentSummary.text = entry?.payments?.toString()
        balanceSummary.text = entry?.balance?.toString()

        val balance: Double = entry?.balance ?: 0.0
        if (balance < 0) {
            context?.let {
                balanceSummary.setTextColor(ContextCompat.getColor(it, R.color.balance_color_red))
            }
        } else {
            context?.let {
                balanceSummary.setTextColor(ContextCompat.getColor(it, R.color.colorPrimary))
            }
        }

        val entryList = entry?.entries ?: arrayListOf()
        if ((entry == null) || entryList.isEmpty()) {
            emptyEntriesListContainer.visibility = View.VISIBLE
            entriesRecyclerView.visibility = View.GONE
        } else {
            adapter.setEntryList(entryList)
            emptyEntriesListContainer.visibility = View.GONE
            entriesRecyclerView.visibility = View.VISIBLE
        }
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        entriesRecyclerView = dataBinding.entriesList
        addEntryButton = dataBinding.addEntryButton
        emptyEntriesListContainer = dataBinding.emptyListContainer
        menuButton = dataBinding.menuButton
        expenseSummary = dataBinding.expensesSummaryValue
        paymentSummary = dataBinding.paymentsSummaryValue
        balanceSummary = dataBinding.balanceSummaryValue

        dataBinding.addEntryButton.setOnClickListener(addEntryClickListener)
        menuButton.setOnClickListener(inboxUserManagerClickListener)
        dataBinding.backButton.setOnClickListener { activity?.onBackPressed() }

        entriesRecyclerView.adapter = this.adapter
        entriesRecyclerView.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper.attachToRecyclerView(entriesRecyclerView)
    }

    override fun initializeObservers() {
        viewModel.entriesRepository.entriesList.observe(viewLifecycleOwner,
            Observer {
                handleEntries(it)
            })

        viewModel.entriesRepository.successfullyInsertedUser.observe(viewLifecycleOwner,
            Observer {
                spinner.visibility = GONE
                if (it != HTTP_CODE_500) {
                    Toast.makeText(
                        activity,
                        "If you have entered correct email, user will be added to the inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "There was an issue adding user to the inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        viewModel.entriesRepository.successfullyRemovedUser.observe(viewLifecycleOwner,
            Observer {
                spinner.visibility = GONE
                if (it != HTTP_CODE_500) {
                    Toast.makeText(
                        activity,
                        "If you have entered correct email, user will be removed from the inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "There was an issue removing user from the inbox.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        viewModel.entriesRepository.successfullyInsertedExpense.observe(viewLifecycleOwner,
            Observer {
                if (it == HTTP_CODE_200) {
                    loadUI()
                    Toast.makeText(activity, "Expense successfully inserted.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    spinner.visibility = GONE
                    Toast.makeText(
                        activity,
                        "There was an issue inserting your expense.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        viewModel.entriesRepository.successfullyInsertedPayment.observe(viewLifecycleOwner,
            Observer {
                if (it == HTTP_CODE_200) {
                    loadUI()
                    Toast.makeText(activity, "Payment successfully inserted.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    spinner.visibility = GONE
                    Toast.makeText(
                        activity,
                        "There was an issue inserting your payment.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        viewModel.entriesRepository.successfullyRemovedExpense.observe(viewLifecycleOwner,
            Observer {
                if (it == HTTP_CODE_200) {
                    loadUI()
                    Toast.makeText(activity, "Expense successfully removed.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    spinner.visibility = GONE
                    Toast.makeText(
                        activity,
                        "There was an issue removing expense.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        viewModel.entriesRepository.successfullyRemovedPayment.observe(viewLifecycleOwner,
            Observer {
                if (it == HTTP_CODE_200) {
                    loadUI()
                    Toast.makeText(activity, "Expense successfully removed.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    spinner.visibility = GONE
                    Toast.makeText(
                        activity,
                        "There was an issue removing expense.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}