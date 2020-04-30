package com.asimgasimzade.mastermind.presentation.game

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.asimgasimzade.mastermind.BR
import com.asimgasimzade.mastermind.R
import com.asimgasimzade.mastermind.data.model.DialogModel
import dagger.Reusable
import kotlinx.android.synthetic.main.dialog_layout.view.*
import javax.inject.Inject

@Reusable
class DialogCreator @Inject constructor() {

    private lateinit var dialog: AlertDialog

    fun create(
        context: Context,
        dialogModel: DialogModel,
        onPlayAgain: () -> Unit = object : () -> Unit {
            override fun invoke() {}
        },
        onLeave: () -> Unit = object : () -> Unit {
            override fun invoke() {}
        },
        onClose: () -> Unit = object : () -> Unit {
            override fun invoke() {}
        }
    ) {
        val inflater = LayoutInflater.from(context)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.dialog_layout,
            null,
            false
        )

        AlertDialog.Builder(context).let { builder ->
            binding.setVariable(BR.dialogModel, dialogModel)
            builder.setView(binding.root)

            binding.root.dialog_button_leave.setOnClickListener {
                onLeave.invoke()
                dialog.dismiss()
            }
            binding.root.dialog_button_play_again.setOnClickListener {
                onPlayAgain.invoke()
                dialog.dismiss()
            }
            binding.root.dialog_button_close.setOnClickListener {
                onClose.invoke()
                dialog.setOnDismissListener(null)
                dialog.dismiss()
            }

            if (!dialogModel.showCloseButton) builder.setCancelable(false)

            dialog = builder.show()
            dialog.setOnDismissListener {
                onClose.invoke()
            }
        }
    }
}
