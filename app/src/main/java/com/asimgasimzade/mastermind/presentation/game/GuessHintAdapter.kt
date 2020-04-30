package com.asimgasimzade.mastermind.presentation.game

import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.CodePeg
import com.asimgasimzade.mastermind.data.model.CodePegColor
import com.asimgasimzade.mastermind.data.model.GuessHintModel
import com.asimgasimzade.mastermind.presentation.base.BindableAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.guess_hint_item.view.*

class GuessHintAdapter :
    RecyclerView.Adapter<GuessHintAdapter.GuessHintHolder>(),
    BindableAdapter<List<GuessHintModel>> {

    private val subscriptions = CompositeDisposable()

    private var guessHintsList = mutableListOf<GuessHintModel>()

    val onCodePegAdded = PublishSubject.create<Pair<CodePeg, Int>>()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        subscriptions.clear()
    }

    override fun setData(data: List<GuessHintModel>) {
        guessHintsList = data.toList().reversed().toMutableList()
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

    override fun getItemCount() = guessHintsList.size

    override fun onBindViewHolder(holder: GuessHintHolder, position: Int) {

        val guessPegViewsList = listOf(
            holder.itemView.guess_view_peg_1,
            holder.itemView.guess_view_peg_2,
            holder.itemView.guess_view_peg_3,
            holder.itemView.guess_view_peg_4
        )

        if (guessHintsList[position].isCurrentLevel) {
            val dragListener = View.OnDragListener { view, dragEvent ->
                when (dragEvent.action) {
                    DragEvent.ACTION_DRAG_ENDED -> true
                    DragEvent.ACTION_DRAG_ENTERED -> true
                    DragEvent.ACTION_DRAG_EXITED -> true
                    DragEvent.ACTION_DRAG_LOCATION -> true
                    DragEvent.ACTION_DRAG_STARTED -> true
                    DragEvent.ACTION_DROP -> {
                        if (view is ImageView) {
                            handleDropAction(view, dragEvent)
                            true
                        } else false
                    }
                    else -> false
                }
            }

            guessPegViewsList.forEachIndexed { guessPegPosition, guessPegView ->
                guessPegView.setOnDragListener(dragListener)
                guessPegView.tag = guessPegPosition
            }
        } else {
            guessPegViewsList.forEach { guessPegView ->
                guessPegView.setOnDragListener(null)
            }
        }

        holder.bind(guessHintsList[position])
    }

    private fun handleDropAction(view: View, dragEvent: DragEvent) {
        when (dragEvent.clipData.getItemAt(0).text.toString()) {
            CodePegColor.BLUE.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.BLUE))
            CodePegColor.GREEN.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.GREEN))
            CodePegColor.YELLOW.name -> callOnCodePegAddedCallback(
                view,
                CodePeg(CodePegColor.YELLOW)
            )
            CodePegColor.RED.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.RED))
            CodePegColor.WHITE.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.WHITE))
            CodePegColor.BLACK.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.BLACK))
            CodePegColor.PINK.name -> callOnCodePegAddedCallback(view, CodePeg(CodePegColor.PINK))
        }
    }

    private fun callOnCodePegAddedCallback(view: View, codePeg: CodePeg) {
        onCodePegAdded.onNext(codePeg to view.tag.toString().toInt())
    }

    inner class GuessHintHolder(
        private val viewDataBinding: ViewDataBinding
    ) : RecyclerView.ViewHolder(viewDataBinding.root) {

        fun bind(guessHintModel: GuessHintModel) {
            viewDataBinding.setVariable(BR.guessHintModel, guessHintModel)
        }
    }
}
