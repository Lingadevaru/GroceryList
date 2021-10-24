package com.dev.groceylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grocery_list.view.*

class GroceryRVAdapter(
    var list: List<GroceryItems>,
    val groceryItemClickInterface: GroceryItemClickInterface
): RecyclerView.Adapter<GroceryRVAdapter.GroceryViewHolder>(){

    inner class GroceryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameGrocery = itemView.idTVItemName
        val quantity = itemView.idTVQuantity
        val rate = itemView.idTVRate
        val amountTotal = itemView.idTVTotalAmount
        val deleteImage = itemView.idIVDelete
    }



    interface GroceryItemClickInterface{
        fun onItemClick(groceryItems: GroceryItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_list, parent, false)
         return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.nameGrocery.text = list.get(position).itemName
        holder.quantity.text = list.get(position).itemQuantity.toString()
        holder.rate.text = "Rs. "+list.get(position).itemPrice.toString()
        val itemTotal: Int = list.get(position).itemPrice * list.get(position).itemQuantity
        holder.amountTotal.text = "Rs. "+ itemTotal.toString()
        holder.deleteImage.setOnClickListener {
            groceryItemClickInterface.onItemClick(list.get(position))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}