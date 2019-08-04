package com.example.travelmantics

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insert.*
import java.util.*

class DealAdapter : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    internal var deals = arrayListOf<TravelDeals>()
    private val mDatabaseReference: DatabaseReference = FirebaseUtil.mDatabaseReference
    private val mChildListener: ChildEventListener

    init {
        deals = FirebaseUtil.mDeals

        mChildListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val td: TravelDeals? = dataSnapshot.getValue(TravelDeals::class.java)
                td?.id = dataSnapshot.key.toString()
                if (td != null) {
                    deals.add(td)
                    Log.e("Deal: ", td.title)
                }
                notifyItemInserted(deals.size - 1)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                notifyDataSetChanged()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                notifyDataSetChanged()
            }
        }
        mDatabaseReference.addChildEventListener(mChildListener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {

        val context = parent.context
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.rv_row, parent, false)
        return DealViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = deals[position]
        holder.bind(deal)
    }

    override fun getItemCount(): Int {
        return if(deals.isEmpty()) {
            0
        } else deals.size
    }

    inner class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var tvTitle: TextView = itemView.findViewById<View>(R.id.tvTitle) as TextView
        private var tvDescription: TextView = itemView.findViewById<View>(R.id.tvDescription) as TextView
        private var tvPrice: TextView = itemView.findViewById<View>(R.id.tvPrice) as TextView
        private var image: ImageView = itemView.findViewById<View>(R.id.imageView) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(deal: TravelDeals) {
            tvTitle.text = deal.title
            tvDescription.text = deal.description
            tvPrice.text = deal.price
            showImage(deal.imageUrl)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            Log.d("Click", position.toString())
            val selectedDeal = deals[position]
            val intent = Intent(view.context,  InsertActivity::class.java)
            intent.putExtra("Deal", selectedDeal)
            view.context.startActivity(intent)
        }


        private fun showImage(url: String) {
            if (!TextUtils.isEmpty(url)) {
                Picasso.get()
                        .load(url)
                        .into(image)
            }
        }

    }


}
