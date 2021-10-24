package com.dev.groceylist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.grocery_add.*

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface {

    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel //Used to access data from ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list, this)

        //Set LayoutManager to Recyclerview
        idRVitems.layoutManager = LinearLayoutManager(this)
        //Set Adapter to Recyclerview
        idRVitems.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        //ViewModel is set
        groceryViewModel = ViewModelProvider(this, factory).get(GroceryViewModel::class.java)

        groceryViewModel.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list = it //added data to the list
            groceryRVAdapter.notifyDataSetChanged()//Data inside recyclerview will be updated
        })

        idFABAdd.setOnClickListener {
            openDialog()
        }

    }

    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add)
        val cancelBtn = dialog.idBtnCancel
        val addBtn = dialog.idBtnAdd
        val itemEdit = dialog.idEditName
        val quantityEdit = dialog.idEditQuantity
        val priceEdit = dialog.idEditPrice

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        addBtn.setOnClickListener {
            val itemName: String = itemEdit.text.toString()
            val itemQuantity: String = quantityEdit.text.toString()
            val itemPrice: String = priceEdit.text.toString()
            val qty = itemQuantity.toInt()
            val pr = itemPrice.toInt()

            if(itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty()){
                val items = GroceryItems(itemName, qty, pr)
                groceryViewModel.insert(items)
                Toast.makeText(applicationContext, "Item Inserted...", Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext, "Please enter all the data...", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Item Deleted", Toast.LENGTH_SHORT).show()
    }
}