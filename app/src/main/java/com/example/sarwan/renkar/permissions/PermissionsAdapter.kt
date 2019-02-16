package com.example.sarwan.renkar.permissions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.sarwan.renkar.R
import kotlinx.android.synthetic.main.permissions_item_layout.view.*
import javax.annotation.Nullable
import kotlin.collections.ArrayList

class PermissionsAdapter(private val activity : FragmentActivity?, private var permissions: ArrayList<String>
, val listener : PermissionListener
)
    : androidx.recyclerview.widget.RecyclerView.Adapter<PermissionsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.permissions_item_layout, null)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.loadData(permissions[position],position)
    }

    override fun getItemCount(): Int {
        return permissions.size
    }

    fun swap(records: ArrayList<String>?) {
        records?.let { it ->
            permissions.clear()
            permissions.addAll(it)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun loadData(permission: String, position: Int) {
            Permissions.getIcon(permission)
                ?.let { key -> itemView.permission_icon.background = activity?.resources?.getDrawable(key) }
            Permissions.getName(permission)?.let {
                name-> itemView.permission_name.text = name
            }
            setOnClickListener(position)
        }

        private fun setOnClickListener(position: Int) {
            itemView.allow.tag = position
            itemView.deny.tag = position

            itemView.deny.setOnClickListener {
                showWarning(it.tag as Int)
            }

            itemView.allow.setOnClickListener {
                permissionGrant(it.tag as Int)
            }
        }

        private fun showWarning(i: Int) {
            Toast.makeText(activity, "${permissions[i]} has been denied", Toast.LENGTH_LONG).show()
        }

        private fun permissionGrant(pos: Int) {
            listener.askForPermission(permissions[pos])
            itemView.deny.visibility = View.GONE
        }
    }
        interface PermissionListener{
            fun askForPermission(s: String)
        }
    }