package com.asimgasimzade.mastermind.presentation.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.GuessHintModel
import com.asimgasimzade.mastermind.presentation.base.BindableAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject

class GuessHintAdapter() :
    RecyclerView.Adapter<GuessHintAdapter.GuessHintHolder>(),
    BindableAdapter<List<GuessHintModel>> {

    private val subscriptions = CompositeDisposable()

    var guessHintsList = mutableListOf<GuessHintModel>()
    val itemClick = PublishSubject.create<Int>()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        subscriptions.clear()
    }

    override fun setData(data: List<GuessHintModel>) {
        guessHintsList = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GuessHintHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.guess_hint_item,
                null,
                false
            )
        )

    override fun getItemCount() = guessHintsList.size + 1

    override fun onBindViewHolder(holder: GuessHintHolder, position: Int) {
        RxView.clicks(holder.itemView)
            .subscribe {
                itemClick.onNext(position)
            }.addTo(subscriptions)

        holder.bind(guessHintsList[position])
    }

    inner class GuessHintHolder(
        private val viewDataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(guessHintModel: GuessHintModel) {
            viewDataBinding.setVariable(BR.guessHintModel, guessHintModel)
        }
    }
}
