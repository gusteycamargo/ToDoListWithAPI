package com.camargo.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.camargo.todolist.adapters.ToDoAdapter
import com.camargo.todolist.adapters.ToDoListener
import com.camargo.todolist.model.ToDo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.item_edit.*

class MainActivity : AppCompatActivity(), ToDoListener {

    private lateinit var adapter: ToDoAdapter
    private var indexDaAtividadeSelecionada = 0
    private var todoEdit: ToDo? = null
    private var statusBkp = ""
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            insertItem()
        }

        adapter = ToDoAdapter(this, applicationContext)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

    }

    private fun insertItem() {
        val newItem = ToDo(
            "EDITANDO",
            "",
            ""
        )

        val position = adapter.add(newItem)
        (recycler_view.layoutManager as LinearLayoutManager).scrollToPosition(position)

        todoEdit = newItem
        isEditing = false
        adapter.notifyItemChanged(position)
    }

    fun saveToDo() {
        val todoo = todoEdit as ToDo
        val notDone: String = getString(R.string.not_done)

        todoo.status = notDone
        todoo.title = title_edit.text.toString()
        todoo.description = description_edit.text.toString()

        adapter.save(todoo)

        val p = adapter.returnPositionOfToDo(todoo)
        adapter.notifyItemChanged(p)
        isEditing = true
    }

    private fun updateItem() {
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        clickedItem.status = statusBkp
        clickedItem.title = title_edit.text.toString()
        clickedItem.description = description_edit.text.toString()

        adapter.update(clickedItem)
        adapter.notifyItemChanged(indexDaAtividadeSelecionada)

        isEditing = true
    }


    override fun onBtSaveClick() {
        if(isEditing) {
            updateItem()
        }
        else {
            saveToDo()
        }
    }

    override fun onBtShareClick(todo: ToDo) {
        val textShare: String = getString(R.string.text_share)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "${textShare} ${todo.title}")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


    override fun onBtDeleteClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)

        adapter.removeToDoInPosition(indexDaAtividadeSelecionada)
    }

    override fun onItemEditClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        clickedItem.status = statusBkp
        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
        //textView1.text = clickedItem.title
        isEditing = false
    }

    override fun onItemClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        statusBkp = clickedItem.status
        isEditing = true
        clickedItem.status = "EDITANDO"

        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
    }

    override fun onLongClick(todo: ToDo) {
        indexDaAtividadeSelecionada = adapter.returnPositionOfToDo(todo)
        val clickedItem = adapter.getToDoInPosition(indexDaAtividadeSelecionada)
        val notDone: String = getString(R.string.not_done)
        val done: String = getString(R.string.done)

        if(clickedItem.status == notDone) {
            clickedItem.status = done
        }
        else if(clickedItem.status == done) {
            clickedItem.status = notDone
        }

        adapter.update(clickedItem)
        adapter.notifyItemChanged(indexDaAtividadeSelecionada)
    }

}