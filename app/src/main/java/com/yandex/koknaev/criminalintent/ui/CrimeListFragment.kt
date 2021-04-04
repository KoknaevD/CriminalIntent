package com.yandex.koknaev.criminalintent.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yandex.koknaev.criminalintent.R
import com.yandex.koknaev.criminalintent.model.Crime
import com.yandex.koknaev.criminalintent.model.CrimeListViewModel

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        recyclerView = view.findViewById(R.id.crime_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        val adapter = CrimeAdapter(crimes)
        recyclerView.adapter = adapter
    }

    companion object {
        fun NewInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        val titleTextView = itemView.findViewById<TextView>(R.id.crime_title)
        val dateTextView = itemView.findViewById<TextView>(R.id.crime_date)

        fun bind(tempCrime: Crime) {
            crime = tempCrime
            dateTextView.text = crime.date.toString()
            titleTextView.text = crime.title
        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${crime.title}", Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimeAdapter(val crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            var layout = R.layout.list_item_crime
            if (viewType == 1) layout = R.layout.list_item_crime_police
            val view = layoutInflater.inflate(layout, parent, false)
            return CrimeHolder(view)
        }

        override fun getItemViewType(position: Int): Int {
            val crime = crimes[position]
            return if (crime.requiresPolice) 1 else 0
        }

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

    }
}