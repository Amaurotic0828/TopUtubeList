package com.example.toplist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.toplist.handlers.TopListHandler
import com.example.toplist.models.TopList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Modify_List : AppCompatActivity() {
    lateinit var rankEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var addEditButton: Button
    lateinit var toplistHandler: TopListHandler
    lateinit var toplists: ArrayList<TopList>
    lateinit var toplistListView: ListView
    lateinit var toplistGettingEdited: TopList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify__list)

        rankEditText = findViewById(R.id.rankET)
        nameEditText = findViewById(R.id.nameET)
        linkEditText = findViewById(R.id.linkET)
        reasonEditText = findViewById(R.id.reasonET)
        addEditButton = findViewById(R.id.addBtn)
        toplistHandler = TopListHandler()
        toplists = ArrayList()
        toplistListView = findViewById(R.id.displayListView)

        addEditButton.setOnClickListener {
            val rank = rankEditText.text.toString()
            val name = nameEditText.text.toString()
            val link = linkEditText.text.toString()
            val reason = reasonEditText.text.toString()
            if(addEditButton.text.toString() == "Add"){
            val toplist = TopList(rank = rank, name = name, link = link, reason = reason)
            if (toplistHandler.create(toplist)) {
                Toast.makeText(applicationContext, "Added on List", Toast.LENGTH_SHORT).show()
                clearFields()
            }
        } else if (addEditButton.text.toString() == "Update"){
                val toplist = TopList(id = toplistGettingEdited.id, name = name, link = link, reason = reason, rank = rank)
                if(toplistHandler.update(toplist)) {
                    Toast.makeText(applicationContext, "Top List Updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
        }
        registerForContextMenu(toplistListView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val inflater = menuInflater
        inflater.inflate(R.menu.toplistoptions, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_toplist -> {
                toplistGettingEdited = toplists[info.position]
                nameEditText.setText(toplistGettingEdited.name)
                linkEditText.setText(toplistGettingEdited.link)
                rankEditText.setText(toplistGettingEdited.rank)
                reasonEditText.setText(toplistGettingEdited.reason)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_toplist -> {

                if (toplistHandler.delete(toplists[info.position])){
                    Toast.makeText(applicationContext,"Selected item deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> return super.onContextItemSelected(item)
        }

    }

    override fun onStart() {
        super.onStart()
        toplistHandler.toplistRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                toplists.clear()

                snapshot.children.forEach {
                    it -> val toplist = it.getValue(TopList::class.java)
                    toplists.add(toplist!!)
                }
                val adapter = ArrayAdapter<TopList>(applicationContext, android.R.layout.simple_list_item_1, toplists)
                toplistListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })
    }
    fun clearFields (){
        nameEditText.text.clear()
        rankEditText.text.clear()
        linkEditText.text.clear()
        reasonEditText.text.clear()
        addEditButton.setText("Add")
    }
}
