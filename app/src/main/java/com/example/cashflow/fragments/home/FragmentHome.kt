package com.example.cashflow.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cashflow.R
import com.example.cashflow.api.models.AddInbox
import com.example.cashflow.api.models.EditInbox
import com.example.cashflow.callbacks.InboxDetailsDialogCallback
import com.example.cashflow.callbacks.InboxLongClickedListener
import com.example.cashflow.callbacks.InsertDialogCallback
import com.example.cashflow.callbacks.RequestCallback
import com.example.cashflow.data.Inbox
import com.example.cashflow.databinding.FragmentHomeBinding
import com.example.cashflow.fragments.FragmentBase
import com.example.cashflow.navigation.Navigation
import com.example.cashflow.views.DialogEdit
import com.example.cashflow.views.DialogInboxDetails

class FragmentHome : FragmentBase() {
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private val adapter by lazy {
        InboxAdapter(inboxLongClickListener)
    }

    private val onCTAButtonClickListener = View.OnClickListener {
        showInsertInboxDialog(inputDialogCallback)
    }

    private val onMenuClickListener = View.OnClickListener {
        navigationHelper.showScreen(Navigation.MENU)
    }

    private val inputDialogCallback = object : InsertDialogCallback {
        override fun onInsertInbox(inboxName: String) {
            handleInboxInsert(inboxName)
        }

        override fun onEditInbox(inboxName: String) {
            handleInboxEdit(inboxName)
        }
    }

    private val inboxDetailsDialogCallback = object : InboxDetailsDialogCallback {
        override fun onEditInboxClicked() {
            val activity = activity ?: return
            DialogEdit(activity, selectedItem?.name, inputDialogCallback).show()
        }

        override fun onDeactivateInboxClicked() {
            val inboxId = selectedItem?.inboxId ?: return
            viewModel.homeRepository.deactivateInbox(authToken, inboxId, deactivateInboxCallback)
            spinner.visibility = View.VISIBLE
        }
    }

    private val inboxLongClickListener = object : InboxLongClickedListener {
        override fun openInboxDetails(inbox: Inbox) {
            val activity = activity ?: return
            selectedItem = inbox
            DialogInboxDetails(
                activity,
                (selectedItem?.active ?: false),
                inboxDetailsDialogCallback
            ).show()
        }

        override fun openInbox(inbox: Inbox) {
            val activity = activity ?: return
            viewModel.homeRepository.seenInbox(authToken, (inbox.inboxId ?: -1))
            navigationHelper.showEntriesScreen(inbox)
        }
    }

    private val swipeRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadUI()
    }

    private val insertInboxCallback = object : RequestCallback {
        override fun onSuccess() {
            loadUI()
            Toast.makeText(activity, resources.getString(R.string.toast_insert_inbox_success), Toast.LENGTH_SHORT).show()
        }

        override fun onFailure() {
            spinner.visibility = View.GONE
            Toast.makeText(activity, resources.getString(R.string.toast_insert_inbox_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val deleteInboxCallback = object : RequestCallback {
        override fun onSuccess() {
            loadUI()
            Toast.makeText(activity, resources.getString(R.string.toast_remove_inbox_success), Toast.LENGTH_SHORT).show()
        }

        override fun onFailure() {
            spinner.visibility = View.GONE
            Toast.makeText(activity, resources.getString(R.string.toast_remove_inbox_error), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val editInboxCallback = object : RequestCallback {
        override fun onSuccess() {
            editInbox()
            Toast.makeText(activity, resources.getString(R.string.toast_edit_inbox_success), Toast.LENGTH_SHORT).show()
            spinner.visibility = View.GONE
        }

        override fun onFailure() {
            Toast.makeText(activity, resources.getString(R.string.toast_edit_inbox_error), Toast.LENGTH_SHORT).show()
            spinner.visibility = View.GONE
        }
    }

    private val deactivateInboxCallback = object : RequestCallback {
        override fun onSuccess() {
            val active = selectedItem?.active ?: false
            if (active) {
                Toast.makeText(activity, resources.getString(R.string.toast_deactivate_inbox_success), Toast.LENGTH_SHORT)
                    .show()
                changeInboxActiveStatus(false)
                spinner.visibility = View.GONE
            } else {
                Toast.makeText(activity, resources.getString(R.string.toast_activate_inbox_success), Toast.LENGTH_SHORT).show()
                changeInboxActiveStatus(true)
                spinner.visibility = View.GONE
            }
        }

        override fun onFailure() {
            val active = selectedItem?.active ?: false
            if (active) {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.toast_deactivate_inbox_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.toast_activate_inbox_error),
                    Toast.LENGTH_SHORT
                ).show()
            }
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
            swipeRefreshLayout.isEnabled = false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val inboxId = adapter.getInboxIdByAdapterPosition(viewHolder.adapterPosition)
            viewModel.homeRepository.deleteInbox(authToken, inboxId, deleteInboxCallback)
            spinner.visibility = View.VISIBLE
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            swipeRefreshLayout.isEnabled = true
        }
    })

    private var selectedItem: Inbox? = null
    private var editedName: String? = null
    private lateinit var dataBinding: FragmentHomeBinding

    // views
    private lateinit var spinner: RelativeLayout
    private lateinit var inboxRecyclerView: RecyclerView
    private lateinit var emptyInboxListContainer: ConstraintLayout
    private lateinit var addInboxButton: ImageView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializeObservers()
        setUpBinding()
        loadUI()
    }

    private fun loadUI() {
        spinner.visibility = View.VISIBLE
        viewModel.homeRepository.getInboxList(authToken)
    }

    private fun handleInboxInsert(inboxName: String) {
        spinner.visibility = View.VISIBLE
        val addInboxRequest = AddInbox.Request(inboxName)
        viewModel.homeRepository.insertInbox(authToken, addInboxRequest, insertInboxCallback)
    }

    private fun handleInboxEdit(inboxName: String) {
        val editedInboxId = selectedItem?.inboxId ?: return
        spinner.visibility = View.VISIBLE
        editedName = inboxName
        val request = EditInbox.Request(editedInboxId, inboxName)
        viewModel.homeRepository.editInbox(authToken, request, editInboxCallback)
    }

    private fun changeInboxActiveStatus(isActive: Boolean) {
        val deactivatedInbox = selectedItem ?: return
        val inboxes = adapter.getInboxList()
        val newInbox = Inbox(
            deactivatedInbox.inboxId,
            deactivatedInbox.name,
            deactivatedInbox.date,
            deactivatedInbox.seenAll,
            isActive
        )
        inboxes[inboxes.indexOf(deactivatedInbox)] = newInbox
        adapter.notifyDataSetChanged()
    }

    private fun editInbox() {
        val editedInbox = selectedItem ?: return
        val inboxes = adapter.getInboxList()
        val newInbox = Inbox(
            editedInbox.inboxId,
            editedName,
            editedInbox.date,
            editedInbox.seenAll,
            editedInbox.active
        )
        inboxes[inboxes.indexOf(editedInbox)] = newInbox
        adapter.notifyDataSetChanged()
    }

    override fun setUpBinding() {
        spinner = dataBinding.progressBarContainer
        inboxRecyclerView = dataBinding.inboxList
        addInboxButton = dataBinding.addInboxButton
        emptyInboxListContainer = dataBinding.emptyListContainer
        swipeRefreshLayout = dataBinding.swipeToRefreshContainer
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener)

        dataBinding.menuButton.setOnClickListener(onMenuClickListener)
        addInboxButton.setOnClickListener(onCTAButtonClickListener)

        inboxRecyclerView.adapter = this.adapter
        inboxRecyclerView.layoutManager = LinearLayoutManager(activity)
        itemTouchHelper.attachToRecyclerView(inboxRecyclerView)
    }

    override fun initializeObservers() {
        viewModel.homeRepository.inboxList.observe(viewLifecycleOwner,
            Observer<List<Inbox>> {
                swipeRefreshLayout.isRefreshing = false
                spinner.visibility = View.GONE
                if (it.isEmpty()) {
                    emptyInboxListContainer.visibility = View.VISIBLE
                    inboxRecyclerView.visibility = View.GONE
                } else {
                    adapter.setInboxList(it)
                    emptyInboxListContainer.visibility = View.GONE
                    inboxRecyclerView.visibility = View.VISIBLE
                }
            })
    }
}